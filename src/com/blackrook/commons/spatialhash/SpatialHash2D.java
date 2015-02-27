/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.spatialhash;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.Common;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.grid.SparseGridMap;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.list.List;
import com.blackrook.commons.math.GeometryUtil;
import com.blackrook.commons.math.Pair;
import com.blackrook.commons.math.RMath;
import com.blackrook.commons.math.geometry.Line2D;
import com.blackrook.commons.math.geometry.Rectangle2D;
import com.blackrook.commons.math.geometry.Vect2D;

/**
 * A class to be used for two-dimensional Spatial Hashing.
 * This is used for finding quick spacial relationships between several
 * objects.
 * <p>
 * Objects in this hash, however, are not actively tracked. If the spacial
 * information of objects contained in this hash change in any way, they
 * have to be updated via {@link #updateObject(SpatialHashable)}.
 * </p>
 * @author Matthew Tropiano
 * @since 2.10.0
 * @deprecated Since 2.21.0. Use the spatial indexing packages instead.
 */
@Deprecated
public class SpatialHash2D<T extends SpatialHashable> extends AbstractSpatialHash<T>
{
	private static final String LOCAL_CACHE_NAME = Common.getPackagePathForClass(SpatialHash2D.class) + ".Cache";
	
	/** Object types for collision hints. */
	private static enum ObjectType
	{
		POINT,
		LINE_SEGMENT,
		CIRCLE,
		SWEPT_CIRCLE,
		BOX,
		SWEPT_BOX;
	}

	private static final class Cache<T extends SpatialHashable>
	{
		/** Cache list for generating new grid pairs. */
		private List<Pair> gridPairsCache;
		/** Grid pairs length. */
		private int gridPairsCacheLength;
		/** Search accumulator iterator. */
		private ResettableIterator<T> intersectionPreTestAccumIterator;
		/** Search pre-test accumulator hash. */
		private Hash<T> intersectionPreTestAccum;
		/** Search accumulator iterator. */
		private ResettableIterator<T> intersectionAccumIterator;
		/** Search accumulator hash. */
		private Hash<T> intersectionAccum;
		/** Rectangle for testing intersecting shapes. */
		private Rectangle2D intersectionRectangle;
		/** Line for testing intersecting shapes. */
		private Line2D intersectionLine;
		/** Line for testing intersecting shapes. */
		private Line2D intersectionLine2;
		/** Collection of separating axes. */
		private Vect2D[] intersectionAxes;
		/** Length of separating axes. */
		private int intersectionAxesCount;
		
		private Cache()
		{
			gridPairsCache = new List<Pair>(20);
			gridPairsCacheLength = 0;
			
			intersectionAccum = new Hash<T>();
			intersectionAccumIterator = intersectionAccum.iterator();
			intersectionPreTestAccum = new Hash<T>();
			intersectionPreTestAccumIterator = intersectionPreTestAccum.iterator();
			intersectionRectangle = new Rectangle2D();
			intersectionLine = new Line2D(0,0,0,0);
			intersectionLine2 = new Line2D(0,0,0,0);
			
			final int AXES_COUNT = 6;
			
			intersectionAxes = new Vect2D[AXES_COUNT];
			for (int i = 0; i < AXES_COUNT; i++)
				intersectionAxes[i] = new Vect2D(); 
			intersectionAxesCount = 0;
		}

		/**
		 * Figures out the type of object that you are dealing with in order to figure out
		 * potential collisions.
		 * @param object the input object.
		 * @return the object type.
		 */
		protected ObjectType getCollisionTypeForObject(T object)
		{
			boolean sweep = object.getObjectSweepX() != 0.0 || object.getObjectSweepY() != 0.0;
			boolean area = object.getObjectHalfWidth() != 0.0 || object.getObjectHalfHeight() != 0.0;
			
			// moving?
			if (sweep)
			{
				if (area)
				{
					if (object.useObjectRadius())
						return ObjectType.SWEPT_CIRCLE;
					else
						return ObjectType.SWEPT_BOX;
				}
				else
					return ObjectType.LINE_SEGMENT;
			}
			// not moving
			else
			{
				if (area)
				{
					if (object.useObjectRadius())
						return ObjectType.CIRCLE;
					else
						return ObjectType.BOX;
				}
				else
					return ObjectType.POINT;
			}
		}

		/**
		 * Create grid pairs.
		 */
		private void createGridPairsForObject(T object, int resolution)
		{
			switch (getCollisionTypeForObject(object))
			{
				case POINT:
					createGridPairsForPoint(
						object.getObjectCenterX(), object.getObjectCenterY(), resolution
						);
					break;
				case LINE_SEGMENT:
					createGridPairsForLine(
						object.getObjectCenterX(), object.getObjectCenterY(),
						object.getObjectSweepX(), object.getObjectSweepY(), resolution
						);
					break;
				case CIRCLE:
				case SWEPT_CIRCLE:
					createGridPairsForCircle(
						object.getObjectCenterX(), object.getObjectCenterY(),
						object.getObjectRadius(),
						object.getObjectSweepX(), object.getObjectSweepY(), 
						resolution
						);
					break;
				default:
				case BOX:
				case SWEPT_BOX:
					createGridPairsForBox(
						object.getObjectCenterX(), object.getObjectCenterY(),
						object.getObjectHalfWidth(), object.getObjectHalfHeight(),
						object.getObjectSweepX(), object.getObjectSweepY(),
						resolution
						);
					break;
			}
		}

		/**
		 * Gets the grid pairs for a point.
		 */
		private void createGridPairsForPoint(double x, double y, int res)
		{
			int startX = AbstractSpatialHash.getStartGrid(x, 0, 0, res);
			int startY = AbstractSpatialHash.getStartGrid(y, 0, 0, res);
		    
			cachePairReset();
		    cachePair(startX, startY);
		}

		/**
		 * Gets the grid positions that a line would be added to in the block grid.
		 * @param x first point x-coordinate.
		 * @param y first point y-coordinate.
		 * @param sx the sweep, x, from the first coordinate. 
		 * @param sy the sweep, y, from the first coordinate. 
		 */
		private void createGridPairsForLine(double x, double y, double sx, double sy, int res)
		{
			int x0 = AbstractSpatialHash.getStartGrid(x, 0, sx, res);
			int y0 = AbstractSpatialHash.getStartGrid(y, 0, sy, res);
			int x1 = AbstractSpatialHash.getEndGrid(x, 0, sx, res);
			int y1 = AbstractSpatialHash.getEndGrid(y, 0, sy, res);

		    int dy = y1 - y0;
		    int dx = x1 - x0;
		    int stepx, stepy;
		
		    if (dy < 0) { dy = -dy;  stepy = -1; } else { stepy = 1; }
		    if (dx < 0) { dx = -dx;  stepx = -1; } else { stepx = 1; }
		    dy <<= 1;
		    dx <<= 1;
		
		    cachePairReset();
		    cachePair(x0,y0);
		    if (dx > dy) {
		        int fraction = dy - (dx >> 1);
		        while (x0 != x1)
		        {
		            if (fraction >= 0)
		            {
		                y0 += stepy;
		                fraction -= dx;
		                cachePair(x0,y0);
		                }
		            x0 += stepx;
		            fraction += dy;
		            cachePair(x0,y0);
		            }
		        }
		    else
		    {
		        int fraction = dx - (dy >> 1);
		        while (y0 != y1)
		        {
		            if (fraction >= 0)
		            {
		                x0 += stepx;
		                fraction -= dy;
		                cachePair(x0,y0);
		                }
		            y0 += stepy;
		            fraction += dx;
		            cachePair(x0,y0);
		            }
		        }
		}

		/**
		 * Gets the grid positions that an object would be added to in the block grid.
		 * @param object	the object.
		 */
		private void createGridPairsForBox(
				double centerX, double centerY, double halfWidth, 
				double halfHeight, double sweepX, double sweepY, int res)
		{
			int startX = AbstractSpatialHash.getStartGrid(centerX, halfWidth, sweepX, res);
			int endX = AbstractSpatialHash.getEndGrid(centerX, halfWidth, sweepX, res);
			int startY = AbstractSpatialHash.getStartGrid(centerY, halfHeight, sweepY, res);
			int endY = AbstractSpatialHash.getEndGrid(centerY, halfHeight, sweepY, res);

			cachePairReset();
			for (int x = startX; x <= endX; x += 1)
				for (int y = startY; y <= endY; y += 1)
					cachePair(x, y);
		}

		/**
		 * Gets the grid positions that an object would be added to in the block grid.
		 * @param object	the object.
		 */
		private void createGridPairsForCircle(double centerX, double centerY, double radius, double sweepX, double sweepY, int res)
		{
			int startX = AbstractSpatialHash.getStartGrid(centerX, radius, sweepX, res);
			int endX = AbstractSpatialHash.getEndGrid(centerX, radius, sweepX, res);
			int startY = AbstractSpatialHash.getStartGrid(centerY, radius, sweepY, res);
			int endY = AbstractSpatialHash.getEndGrid(centerY, radius, sweepY, res);
		
			cachePairReset();
			for (int x = startX; x <= endX; x += 1)
				for (int y = startY; y <= endY; y += 1)
					cachePair(x, y);
		}

		/**
		 * Generates separating axes to test for a line segment.
		 */
		private void cacheSeparatingAxesForLine(double x0, double y0, double x1, double y1)
		{
			intersectionAxes[0].set(x1 - x0, y1 - y0);
			intersectionAxes[1].set(-(y1 - y0), x1 - x0);
			intersectionAxesCount = 2;
		}

		/**
		 * Resets the cache pair.
		 */
		private void cachePairReset()
		{
			gridPairsCacheLength = 0;
		}

		/**
		 * Caches a pair to be used later.
		 * @param x x-coordinate.
		 * @param y y-coordinate.
		 */
		private void cachePair(int x, int y)
		{
			if (gridPairsCacheLength < gridPairsCache.size())
			{
				Pair p = gridPairsCache.getByIndex(gridPairsCacheLength);
				p.x = x;
				p.y = y;
			}
			else
				gridPairsCache.add(new Pair(x,y));
			
			gridPairsCacheLength++;
		}
	}
	
	/** Grid map of objects. */
	private SparseGridMap<Hash<T>> objectGrid;
	/** Grid map of iterators for each. */
	private SparseGridMap<ResettableIterator<T>> objectGridIterators;
	/** Table of lists of grid coordinates that an object is tied to. */
	private HashMap<T, Pair[]> objectMap;

	
	
	/**
	 * Creates a new Scene2D.
	 * @param gridResolution however many units is one grid space.
	 */
	public SpatialHash2D(int gridResolution)
	{
		super(gridResolution);

		objectMap = new HashMap<T, Pair[]>();
		objectGrid = new SparseGridMap<Hash<T>>();
		objectGridIterators = new SparseGridMap<ResettableIterator<T>>();
	}

	@Override
	public synchronized void addObject(T object)
	{
		if (containsObject(object))
			return;
		
		Cache<T> cache = getCache();
		
		cache.createGridPairsForObject(object, getResolution());
		Pair[] pa = new Pair[cache.gridPairsCacheLength];
		for (int i = 0; i < cache.gridPairsCacheLength; i++)
		{
			Pair p = cache.gridPairsCache.getByIndex(i);
			Hash<T> h = getHashAt(p.x, p.y);
			h.put(object);
			pa[i] = new Pair(p.x, p.y);
		}

		objectMap.put(object, pa);
		super.addObject(object);
	}
	
	@Override
	public synchronized boolean removeObject(T object)
	{
		if (!containsObject(object))
			return false;
		
		super.removeObject(object);
		Pair[] pa = objectMap.removeUsingKey(object);
		for (int i = 0; i < pa.length; i++)
		{
			Pair p = pa[i];
			Hash<T> h = getHashAt(p.x, p.y);
			h.remove(object);
		}
		
		return true;
	}
	
	@Override
	public synchronized void updateObject(T object)
	{
		super.updateObject(object);
	}

	@Override
	public synchronized void clear()
	{
		super.clear();
		objectMap.clear();
		objectGrid.clear();
		objectGridIterators.clear();
	}

	@Override
	public synchronized int getIntersections(T object, T[] out, int offset)
	{
		Cache<T> cache = getCache();
		accumObjectIntersections(object, cache);
		return accumToArray(out, offset, cache);
	}

	@Override
	public synchronized int getIntersections(T object, AbstractVector<T> vector, boolean replace)
	{
		Cache<T> cache = getCache();
		accumObjectIntersections(object, cache);
		if (replace)
			return accumToVectorReplace(vector, cache);
		else
			return accumToVector(vector, cache);
	}

	/**
	 * Gets objects that intersect with a point and puts them into an array,
	 * starting from the specified offset.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param out the output array.
	 * @param offset the starting offset into the array.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersections(double x, double y, T[] out, int offset)
	{
		Cache<T> cache = getCache();
		accumPointIntersections(x, y, cache);
		return accumToArray(out, offset, cache);
	}

	/**
	 * Gets objects that intersect with a point and puts them into a vector,
	 * starting from the specified offset.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param vector the output vector.
	 * @param replace if true, this replaces contents starting from the beginning of the vector.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersections(double x, double y, AbstractVector<T> vector, boolean replace)
	{
		Cache<T> cache = getCache();
		accumPointIntersections(x, y, cache);
		if (replace)
			return accumToVectorReplace(vector, cache);
		else
			return accumToVector(vector, cache);
	}

	/**
	 * Gets objects that intersect with a line segment and puts them into an array,
	 * starting from the specified offset.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param x0 the first x-coordinate of the line segment.
	 * @param y0 the first y-coordinate of the line segment.
	 * @param x1 the second x-coordinate of the line segment.
	 * @param y1 the second y-coordinate of the line segment.
	 * @param out the output array.
	 * @param offset the starting offset into the array.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersections(double x0, double y0, double x1, double y1, T[] out, int offset)
	{
		Cache<T> cache = getCache();
		accumLineIntersections(x0, y0, x1, y1, getCache());
		return accumToArray(out, offset, cache);
	}

	/**
	 * Gets objects that intersect with a line segment and puts them into a vector.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param x0 the first x-coordinate of the line segment.
	 * @param y0 the first y-coordinate of the line segment.
	 * @param x1 the second x-coordinate of the line segment.
	 * @param y1 the second y-coordinate of the line segment.
	 * @param vector the output vector.
	 * @param replace if true, this replaces contents starting from the beginning of the vector.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersections(double x0, double y0, double x1, double y1, AbstractVector<T> vector, boolean replace)
	{
		Cache<T> cache = getCache();
		accumLineIntersections(x0, y0, x1, y1, cache);
		if (replace)
			return accumToVectorReplace(vector, cache);
		else
			return accumToVector(vector, cache);
	}

	/**
	 * Tests if a point intersects with an object.
	 * @param x	the point, x-coordinate.
	 * @param y	the point, y-coordinate.
	 * @param object the object.
	 */
	protected boolean pointIntersect(double x, double y, T object)
	{
		return 
			x <= object.getObjectCenterX() + object.getObjectHalfWidth() && 
			x >= object.getObjectCenterX() - object.getObjectHalfWidth() &&
			y <= object.getObjectCenterY() + object.getObjectHalfHeight() && 
			y >= object.getObjectCenterY() - object.getObjectHalfHeight();
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param obj	first object.
	 * @param obj2	another object.
	 * @return	true if their bounding areas overlap, false otherwise.
	 */
	protected boolean objectIntersects(T obj, T obj2)
	{
		// both are "circles."
		if (obj.useObjectRadius() && obj2.useObjectRadius())
		{
			double dx = obj.getObjectCenterX() - obj2.getObjectCenterX();
			double dy = obj.getObjectCenterY() - obj2.getObjectCenterY();
			return Math.sqrt(dx*dx + dy*dy) < (obj.getObjectRadius() + obj2.getObjectRadius());
		}
		// one is a "circle."
		else if (obj2.useObjectRadius() ^ obj.useObjectRadius())
		{
			T circ = obj2.useObjectRadius() ? obj2 : obj;
			T rect = circ == obj2 ? obj : obj2; 
			
			double minrectX = rect.getObjectCenterX() - rect.getObjectHalfWidth();
			double maxrectX = rect.getObjectCenterX() + rect.getObjectHalfWidth();
			double minrectY = rect.getObjectCenterY() - rect.getObjectHalfHeight();
			double maxrectY = rect.getObjectCenterY() + rect.getObjectHalfHeight();
			
			// get closest rectangle region point.
			double vx = RMath.clampValue(circ.getObjectCenterX(), minrectX, maxrectX);
			double vy = RMath.clampValue(circ.getObjectCenterY(), minrectY, maxrectY);
	
			double dx = circ.getObjectCenterX() - vx;
			double dy = circ.getObjectCenterY() - vy;
			
			return Math.sqrt(dx*dx + dy*dy) < (circ.getObjectRadius());
		}
		else
		{
			return 
				obj.getObjectCenterX() - obj.getObjectHalfWidth() < obj2.getObjectCenterX() + obj2.getObjectHalfWidth() &&
				obj.getObjectCenterX() + obj.getObjectHalfWidth() > obj2.getObjectCenterX() - obj2.getObjectHalfWidth() && 
				obj.getObjectCenterY() - obj.getObjectHalfHeight() < obj2.getObjectCenterY() + obj2.getObjectHalfHeight() &&
				obj.getObjectCenterY() + obj.getObjectHalfHeight() > obj2.getObjectCenterY() - obj2.getObjectHalfHeight();
		}
	}

	/**
	 * Throws all point intersections into the accumulation hash.
	 */
	private void accumPointIntersections(double x, double y, Cache<T> cache)
	{
		cache.intersectionAccum.clear();

		int startX = getStartGrid(x, 0, 0);
		int startY = getStartGrid(y, 0, 0);

		ResettableIterator<T> rit = getIteratorAt(startX, startY);
		while (rit.hasNext())
		{
			T object = rit.next();
			if (cache.intersectionAccum.contains(object))
				continue;
			
			if (pointIntersect(x, y, object))
				cache.intersectionAccum.put(object);
		}
	}
	
	/**
	 * Throws all object intersections into the accumulation hash.
	 */
	private void accumObjectIntersections(T object, Cache<T> cache)
	{
		cache.intersectionAccum.clear();
		
		cache.createGridPairsForObject(object, getResolution());
		for (int i = 0; i < cache.gridPairsCacheLength; i++)
		{
			Pair p = cache.gridPairsCache.getByIndex(i);
			ResettableIterator<T> rit = getIteratorAt(p.x, p.y);
			while (rit.hasNext())
			{
				T obj = rit.next();
				if (cache.intersectionAccum.contains(obj))
					continue;
	
				if (object != obj && objectIntersects(object, obj))
					cache.intersectionAccum.put(obj);
			}
		}
	}

	/**
	 * Throws all line intersections into the accumulation hash.
	 */
	private void accumLineIntersections(double x0, double y0, double x1, double y1, Cache<T> cache)
	{
		if (x0 == x1 && y0 == y1)
		{
			accumPointIntersections(x0, y0, cache);
			return;
		}
		
		cache.intersectionAccum.clear();
		
		cache.intersectionPreTestAccum.clear();
		ResettableIterator<T> rit = null;
		cache.createGridPairsForLine(x0, y0, x1, y1, getResolution());
		for (int i = 0; i < cache.gridPairsCacheLength; i++)
		{
			Pair p = cache.gridPairsCache.getByIndex(i);
			rit = getIteratorAt(p.x, p.y);
			while (rit.hasNext())
				cache.intersectionPreTestAccum.put(rit.next());
		}

		rit = cache.intersectionPreTestAccumIterator;
		
		// test separating axes.
		cache.cacheSeparatingAxesForLine(x0, y0, x1, y1);
		for (int axis = 0; axis < cache.intersectionAxesCount; axis++)
		{
			Vect2D separatingAxis = cache.intersectionAxes[axis];
			
			cache.intersectionLine2.pointA.set(x0, y0);
			cache.intersectionLine2.pointB.set(x1, y1);
			cache.intersectionLine2.pointA.projectOnto(separatingAxis);
			cache.intersectionLine2.pointB.projectOnto(separatingAxis);
			
			rit.reset();
			while (rit.hasNext())
			{
				T obj = rit.next();
				cache.intersectionRectangle.x = obj.getObjectCenterX() - obj.getObjectHalfWidth();
				cache.intersectionRectangle.y = obj.getObjectCenterY() - obj.getObjectHalfHeight();
				cache.intersectionRectangle.width = obj.getObjectHalfWidth() * 2.0;
				cache.intersectionRectangle.height = obj.getObjectHalfHeight() * 2.0;
				cache.intersectionRectangle.projectOnto(separatingAxis, cache.intersectionLine);
			
				if (axis < cache.intersectionAxesCount - 1)
				{
					if (!GeometryUtil.lineOverlaps(
							cache.intersectionLine.pointA, cache.intersectionLine.pointB,
							cache.intersectionLine2.pointA, cache.intersectionLine2.pointB,
							null
							))
						rit.remove();
				}
				else
				{
					if (GeometryUtil.lineOverlaps(
							cache.intersectionLine.pointA, cache.intersectionLine.pointB,
							cache.intersectionLine2.pointA, cache.intersectionLine2.pointB,
							null
							))
						cache.intersectionAccum.put(obj);
				}
			}
		}
	}

	/**
	 * Dumps the contents of the accum hash to an array.
	 */
	private int accumToArray(T[] out, int offset, Cache<T> cache)
	{
		cache.intersectionAccumIterator.reset();
		int i = 0;
		while (i+offset < out.length && cache.intersectionAccumIterator.hasNext())
		{
			out[i+offset] = cache.intersectionAccumIterator.next();
			cache.intersectionAccumIterator.remove();
			i++;
		}
		return i;
	}
	
	/**
	 * Dumps the contents of the accum hash to a vector.
	 */
	private int accumToVector(AbstractVector<T> vector, Cache<T> cache)
	{
		cache.intersectionAccumIterator.reset();
		int i = 0;
		while (cache.intersectionAccumIterator.hasNext())
		{
			vector.add(cache.intersectionAccumIterator.next());
			cache.intersectionAccumIterator.remove();
			i++;
		}
		return i;
	}
	
	/**
	 * Dumps the contents of the accum hash to a vector.
	 */
	private int accumToVectorReplace(AbstractVector<T> vector, Cache<T> cache)
	{
		cache.intersectionAccumIterator.reset();
		int i = 0;
		while (cache.intersectionAccumIterator.hasNext())
		{
			vector.replace(i, cache.intersectionAccumIterator.next());
			cache.intersectionAccumIterator.remove();
			i++;
		}
		return i;
	}
	
	/**
	 * Returns the hash at a particular grid coordinate.
	 * If it is null, one is created.
	 */
	private Hash<T> getHashAt(int x, int y)
	{
		Hash<T> out = objectGrid.get(x, y);
		if (out == null)
		{
			out = new Hash<T>(4);
			objectGrid.set(x, y, out);
		}
		return out;
	}
	
	/**
	 * Returns the hash at a particular grid coordinate.
	 * If it is null, one is created.
	 */
	private ResettableIterator<T> getIteratorAt(int x, int y)
	{
		ResettableIterator<T> out = objectGridIterators.get(x, y);
		if (out == null)
		{
			out = getHashAt(x,y).iterator();
			objectGridIterators.set(x, y, out);
		}
		else
			out.reset();
		return out;
	}

	@SuppressWarnings("unchecked")
	private Cache<T> getCache()
	{
		Cache<T> out = null;
		if ((out = (Cache<T>)Common.getLocal(LOCAL_CACHE_NAME)) == null)
			Common.setLocal(LOCAL_CACHE_NAME, out = new Cache<T>());
		return out;
	}
	
}
