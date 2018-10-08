/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.index;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.ThreadUtils;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.math.RMath;
import com.blackrook.commons.math.geometry.Point2D;

/**
 * A class to be used for two-dimensional Spatial Hashing in a uniform grid.
 * This is used for finding quick spatial relationships between several
 * objects, or 2D boxes in this case.
 * <p>
 * Objects in this hash, however, are not actively tracked. If the spatial
 * information of objects contained in this hash change in any way, they
 * have to be updated via {@link #updateObject(Object)}.
 * </p>
 * @param <T> the containing type.
 * @author Matthew Tropiano
 * @since 2.21.0
 */
public class SpatialIndex2D<T> extends AbstractSpatialIndex<T>
{
	/** Object map. */
	private SparseQueueGridIndex<T> objectMap;
	/** Object model. */
	private SpatialIndex2DModel<T> model;

	/**
	 * Creates a new IntervalHash.
	 * @param model the model to use for determining object sizes.
	 * @param resolution however many units is one grid space.
	 */
	public SpatialIndex2D(SpatialIndex2DModel<T> model, int resolution)
	{
		super(resolution);
		this.model = model;
		this.objectMap = new SparseQueueGridIndex<T>();
	}
	
	/**
	 * Clears this hash of all of its object references.
	 */
	public synchronized void clear()
	{
		super.clear();
		objectMap.clear();
	}

	/**
	 * Adds an object to the hash. 
	 * @param object the spatial hash object to add.
	 */
	public synchronized void addObject(T object)
	{
		if (containsObject(object))
			return;
		
		Cache cache = getCache();
		model.getCenter(object, cache.tempPoint);
		double centerX = cache.tempPoint.x;
		double centerY = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double halfWidth = cache.tempPoint.x;
		double halfHeight = cache.tempPoint.y;
		
		int startX = AbstractSpatialIndex.getStart(centerX, halfWidth, getResolution());
		int startY = AbstractSpatialIndex.getStart(centerY, halfHeight, getResolution());
		int endX = AbstractSpatialIndex.getEnd(centerX, halfWidth, getResolution());
		int endY = AbstractSpatialIndex.getEnd(centerY, halfHeight, getResolution());
	
		for (int x = startX; x <= endX; x++)
			for (int y = startY; y <= endY; y++)
				objectMap.enqueue(x, y, object);

		super.addObject(object);
	}

	/**
	 * Removes an object from the hash. 
	 * @param object the spatial hash object to remove.
	 * @return true if removed, false if not.
	 */
	public synchronized boolean removeObject(T object)
	{
		if (!containsObject(object))
			return false;
		
		Cache cache = getCache();
		model.getCenter(object, cache.tempPoint);
		double centerX = cache.tempPoint.x;
		double centerY = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double halfWidth = cache.tempPoint.x;
		double halfHeight = cache.tempPoint.y;

		int startX = AbstractSpatialIndex.getStart(centerX, halfWidth, getResolution());
		int startY = AbstractSpatialIndex.getStart(centerY, halfHeight, getResolution());
		int endX = AbstractSpatialIndex.getEnd(centerX, halfWidth, getResolution());
		int endY = AbstractSpatialIndex.getEnd(centerY, halfHeight, getResolution());
	
		for (int x = startX; x <= endX; x++)
			for (int y = startY; y <= endY; y++)
			{
				Queue<T> queue = objectMap.get(x, y);
				if (queue != null)
					queue.remove(object);
			}
		
		return super.removeObject(object);
	}

	/**
	 * Gets the intersections straddling a given point.
	 * @param x the point to test, x-coordinate.
	 * @param y the point to test, y-coordinate.
	 * @param vector the output vector.
	 * @param offset the starting offset into the vector. 
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(double x, double y, AbstractVector<? super T> vector, int offset)
	{
		accumPointIntersections(x, y);
		return accumToVector(vector);
	}

	/**
	 * Gets the intersections of the objects in a bounding box.
	 * @param centerX the centerpoint of the box, x-coordinate.
	 * @param centerY the centerpoint of the box, y-coordinate.
	 * @param halfWidth the half-width of the box.
	 * @param halfHeight the half-height of the box.
	 * @param vector the output vector.
	 * @param offset the starting offset into the vector. 
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(double centerX, double centerY, double halfWidth, double halfHeight, AbstractVector<? super T> vector, int offset)
	{
		accumBoxIntersections(centerX, centerY, halfWidth, halfHeight);
		return accumToVector(vector);
	}

	/**
	 * Gets objects that intersect with an object and adds them to a vector.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * @param object the object to test with.
	 * @param vector the output vector.
	 * @param offset the starting offset into the vector. 
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(T object, AbstractVector<? super T> vector, int offset)
	{
		accumObjectIntersections(object);
		return accumToVector(vector);
	}


	/**
	 * Gets the objects in this index that may intersect the given line.
	 * @param x0 the beginning point of the line, x-coordinate.
	 * @param y0 the beginning point of the line, y-coordinate.
	 * @param x1 the ending point of the line, x-coordinate.
	 * @param y1 the ending point of the line, y-coordinate.
	 * @param vector the output vector.
	 * @param offset the starting offset into the vector. 
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getLineIntersections(double x0, double y0, double x1, double y1, AbstractVector<? super T> vector, int offset)
	{
		accumLineIntersections(x0, y0, x1, y1);
		return accumToVector(vector);
	}

	/**
	 * Tests if a point intersects with an object.
	 * @param x	the point, x-coordinate.
	 * @param y	the point, y-coordinate.
	 * @param object the object to test.
	 * @return true if intersecting, false if not. 
	 */
	protected boolean pointIntersects(double x, double y, T object)
	{
		Cache cache = getCache();
		model.getCenter(object, cache.tempPoint);
		double cx = cache.tempPoint.x;
		double cy = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double hw = cache.tempPoint.x;
		double hh = cache.tempPoint.y;

		return x <= cx + hw && x >= cx - hw && y <= cy + hh && y >= cy - hh;
	}

	/**
	 * Checks if an object intersects a line.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param x0 the first bound, x-axis. 
	 * @param y0 the first bound, y-axis. 
	 * @param x1 the second bound, x-axis. 
	 * @param y1 the second bound, y-axis.
	 * @param object the object to test. 
	 * @return true if their bounding areas overlap, false otherwise.
	 */
	protected boolean lineIntersects(double x0, double y0, double x1, double y1, T object)
	{
		Cache cache = getCache();
		model.getCenter(object, cache.tempPoint);
		double cx = cache.tempPoint.x;
		double cy = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double hw = cache.tempPoint.x;
		double hh = cache.tempPoint.y;

		for (int i = 0; i < 4; i++)
		{
			double sx, sy, tx, ty;
			
			switch (i)
			{
				default:
					throw new RuntimeException("YOU SHOULDN'T SEE THIS!");
				case 0:
					sx = cx - hw;
					sy = cy - hh;
					tx = cx + hw;
					ty = cy - hh;
					break;
				case 1:
					sx = cx - hw;
					sy = cy - hh;
					tx = cx - hw;
					ty = cy + hh;
					break;
				case 2:
					sx = cx - hw;
					sy = cy + hh;
					tx = cx + hw;
					ty = cy + hh;
					break;
				case 3:
					sx = cx + hw;
					sy = cy - hh;
					tx = cx + hw;
					ty = cy + hh;
					break;
			}
			
			if (!Double.isNaN(RMath.getIntersectionLine(x0, y0, x1, y1, sx, sy, tx, ty)))
				return true;
		}
		
		return false; 
	}
	
	/**
	 * Checks if an object intersects another box area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param centerX the box centerpoint, x-axis. 
	 * @param centerY the box centerpoint, y-axis. 
	 * @param halfWidth the box halfwidth. 
	 * @param halfHeight the box halfheight.
	 * @param object the object to test. 
	 * @return true if their bounding areas overlap, false otherwise.
	 */
	protected boolean boxIntersects(double centerX, double centerY, double halfWidth, double halfHeight, T object)
	{
		Cache cache = getCache();
		model.getCenter(object, cache.tempPoint);
		double cx = cache.tempPoint.x;
		double cy = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double hw = cache.tempPoint.x;
		double hh = cache.tempPoint.y;

		return RMath.getIntersectionBox(centerX, centerY, halfWidth, halfHeight, cx, cy, hw, hh);
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param object first object.
	 * @param object2 another object.
	 * @return true if their bounding areas overlap, false otherwise.
	 */
	protected boolean objectIntersects(T object, T object2)
	{
		Cache cache = getCache();

		model.getCenter(object, cache.tempPoint);
		double spx = cache.tempPoint.x;
		double spy = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double shw = cache.tempPoint.x;
		double shh = cache.tempPoint.y;

		model.getCenter(object2, cache.tempPoint);
		double tpx = cache.tempPoint.x;
		double tpy = cache.tempPoint.y;
		model.getHalfWidths(object2, cache.tempPoint);
		double thw = cache.tempPoint.x;
		double thh = cache.tempPoint.y;
		
		return RMath.getIntersectionBox(spx, spy, shw, shh, tpx, tpy, thw, thh);
	}

	// Throws all object intersections into the accumulation hash.
	private void accumObjectIntersections(T object)
	{
		Cache cache = getCache();
		model.getCenter(object, cache.tempPoint);
		double centerX = cache.tempPoint.x;
		double centerY = cache.tempPoint.y;
		model.getHalfWidths(object, cache.tempPoint);
		double halfWidth = cache.tempPoint.x;
		double halfHeight = cache.tempPoint.y;
		accumBoxIntersections(centerX, centerY, halfWidth, halfHeight);
	}
	
	// Throws all object intersections into the accumulation hash.
	private void accumBoxIntersections(double centerX, double centerY, double halfWidth, double halfHeight)
	{
		Cache cache = getCache();
		cache.intersectionAccum.clear();
		int startX = AbstractSpatialIndex.getStart(centerX, halfWidth, getResolution());
		int startY = AbstractSpatialIndex.getStart(centerY, halfHeight, getResolution());
		int endX = AbstractSpatialIndex.getEnd(centerX, halfWidth, getResolution());
		int endY = AbstractSpatialIndex.getEnd(centerY, halfHeight, getResolution());
	
		for (int x = startX; x <= endX; x++)
			for (int y = startY; y <= endY; y++)
			{
				Queue<T> queue = objectMap.get(x, y);
				if (queue != null) for (T obj : queue)
				{
					if (cache.intersectionAccum.contains(obj))
						continue;
					
					if (boxIntersects(centerX, centerY, halfWidth, halfHeight, obj))
						cache.intersectionAccum.put(obj);
				}
			}
	}


	// Throws all line intersections into the accumulation hash.
	private void accumLineIntersections(double x0, double y0, double x1, double y1)
	{
		if (x0 == x1 && y0 == y1)
		{
			accumPointIntersections(x0, y0);
			return;
		}
		
		Cache cache = getCache();
		cache.intersectionAccum.clear();
		int startX = AbstractSpatialIndex.getStart(x0, 0, getResolution());
		int startY = AbstractSpatialIndex.getStart(y0, 0, getResolution());
		int endX = AbstractSpatialIndex.getEnd(x1, 0, getResolution());
		int endY = AbstractSpatialIndex.getEnd(y1, 0, getResolution());

		int x = startX;
		int y = startY;
		int dy = endY - startY;
		int dx = endX - startX;
		int stepx, stepy;
		
		if (dy < 0) { dy = -dy;  stepy = -1; } else { stepy = 1; }
		if (dx < 0) { dx = -dx;  stepx = -1; } else { stepx = 1; }
		dy <<= 1;
		dx <<= 1;
		
		accumLineIntersectionGrid(x, y, x0, y0, x1, y1);
		if (dx > dy)
		{
			int fraction = dy - (dx >> 1);
			while (x != endX)
			{
				if (fraction >= 0)
				{
					y += stepy;
					fraction -= dx;
					accumLineIntersectionGrid(x, y, x0, y0, x1, y1);
				}
				x += stepx;
				fraction += dy;
				accumLineIntersectionGrid(x, y, x0, y0, x1, y1);
			}
		} 
		else 
		{
			int fraction = dx - (dy >> 1);
			while (y != endY) 
			{
				if (fraction >= 0)
				{
					x += stepx;
					fraction -= dy;
					accumLineIntersectionGrid(x, y, x0, y0, x1, y1);
				}
				y += stepy;
				fraction += dx;
				accumLineIntersectionGrid(x, y, x0, y0, x1, y1);
			}
		}
	}

	// Throws all line intersections into the accumulation hash.
	private void accumLineIntersectionGrid(int x, int y, double x0, double y0, double x1, double y1)
	{
		Cache cache = getCache();
		Queue<T> queue = objectMap.get(x, y);
		if (queue != null) for (T obj : queue)
		{
			if (cache.intersectionAccum.contains(obj))
				continue;
			
			if (lineIntersects(x0, y0, x1, y1, obj))
				cache.intersectionAccum.put(obj);
		}
	}
	
	// Throws all point intersections into the accumulation hash.
	private void accumPointIntersections(double x, double y)
	{
		Cache cache = getCache();
		cache.intersectionAccum.clear();

		int mapX = (int)(x / getResolution());
		int mapY = (int)(y / getResolution());
		
		Queue<T> queue = objectMap.get(mapX, mapY);
		if (queue != null) for (T object : queue)
		{
			if (cache.intersectionAccum.contains(object))
				continue;
			
			if (pointIntersects(x, y, object))
				cache.intersectionAccum.put(object);
		}
	}

	// Dumps the contents of the accum hash to a vector.
	@SuppressWarnings("unchecked")
	private int accumToVector(AbstractVector<? super T> vector)
	{
		Cache cache = getCache();
		cache.intersectionAccumIterator.reset();
		int i = 0;
		while (cache.intersectionAccumIterator.hasNext())
		{
			vector.replace(i, (T)cache.intersectionAccumIterator.next());
			cache.intersectionAccumIterator.remove();
			i++;
		}
		return i;
	}

	private static final String CACHE_NAME = "$$"+Cache.class.getCanonicalName();

	// Get the cache.
	private Cache getCache()
	{
		Cache out;
		if ((out = (Cache)ThreadUtils.getLocal(CACHE_NAME)) == null)
			ThreadUtils.setLocal(CACHE_NAME, out = new Cache());
		return out;
	}

	/** Internal Cache. */
	private static final class Cache
	{
		/** Temporary point. */
		private Point2D tempPoint;
		/** Search accumulator hash. */
		private Hash<Object> intersectionAccum;
		/** Search accumulator iterator. */
		private ResettableIterator<Object> intersectionAccumIterator;
		
		private Cache()
		{
			this.intersectionAccum = new Hash<Object>();
			this.intersectionAccumIterator = intersectionAccum.iterator();
			this.tempPoint = new Point2D();
		}
		
	}
	
}
