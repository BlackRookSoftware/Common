/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.scene;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.grid.SparseGridMap;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.list.List;
import com.blackrook.commons.math.Pair;
import com.blackrook.commons.math.RMath;
import com.blackrook.commons.spatialhash.SpatialHash2D;

/**
 * Scene data structure for setting up an expansive, 2D world. 
 * <p>
 * To be perfectly honest, this is merely a stripped down collision detector between
 * a series of rectangular viewpoints and rectangular areas in order to figure
 * out what to draw/collide with/whatever.
 * <p><b>NOTE:</b> Currently, this class is having some issues with thread safety or weird behavior.
 * Should work fine transactionally, but not ready for real-time just yet.
 * @author Matthew Tropiano
 * @deprecated Since 2.10.0. Use {@link SpatialHash2D} instead.
 */
@Deprecated
public class Scene2D<T extends SceneObject> implements ResettableIterable<T>
{
	/** List of all objects. */
	private Hash<T> allObjects;

	/** Resolution of the collision grid. */
	private int gridResolution;

	/** Grid map of objects. */
	private SparseGridMap<Hash<T>> objectGrid;
	/** Grid map of iterators for each. */
	private SparseGridMap<ResettableIterator<T>> objectGridIterators;
	/** Table of lists of grid coordinates that an object is tied to. */
	private HashMap<SceneObject, Pair[]> objectMap;
	
	/** Cache list for generating new grid pairs. */
	private List<Pair> gridPairsCache;
	/** Grid pairs length. */
	private int gridPairsCacheLength;

	/** Search accumulator iterator. */
	private ResettableIterator<T> intersectionAccumIterator;
	/** Search accumulator hash. */
	private Hash<T> intersectionAccum;
	
	/**
	 * Creates a new Scene2D.
	 * @param gridResolution however many units is one grid space.
	 */
	public Scene2D(int gridResolution)
	{
		if (gridResolution <= 0)
			throw new IllegalArgumentException("Grid resolution cannot be 0 or less.");
		this.gridResolution = gridResolution;
		
		allObjects = new Hash<T>();

		objectMap = new HashMap<SceneObject, Pair[]>();
		objectGrid = new SparseGridMap<Hash<T>>();
		objectGridIterators = new SparseGridMap<ResettableIterator<T>>();
		
		gridPairsCache = new List<Pair>(20);
		gridPairsCacheLength = 0;
		
		intersectionAccum = new Hash<T>();
		intersectionAccumIterator = intersectionAccum.iterator();
	}
	
	/**
	 * Adds an object to the scene. 
	 * @param object the scene object to add.
	 */
	public synchronized void addObject(T object)
	{
		if (allObjects.contains(object))
			return;
		
		createGridPairsForObject(object);
		Pair[] pa = new Pair[gridPairsCacheLength];
		for (int i = 0; i < gridPairsCacheLength; i++)
		{
			Pair p = gridPairsCache.getByIndex(i);
			Hash<T> h = getHashAt(p.x, p.y);
			h.put(object);
			pa[i] = new Pair(p.x, p.y);
		}

		objectMap.put(object, pa);
		allObjects.put(object);
	}
	
	/**
	 * Removes an object from the scene. 
	 * @param object the scene object to add.
	 */
	public synchronized boolean removeObject(T object)
	{
		if (!allObjects.contains(object))
			return false;
		
		allObjects.remove(object);
		Pair[] pa = objectMap.removeUsingKey(object);
		for (int i = 0; i < pa.length; i++)
		{
			Pair p = pa[i];
			Hash<T> h = getHashAt(p.x, p.y);
			h.remove(object);
		}
		
		return true;
	}
	
	/**
	 * Updates the grid layout of an object in the Scene.
	 * Checks which coordinates that the object is in, then changes it. 
	 */
	public synchronized void updateObject(T scene2DObject)
	{
		removeObject(scene2DObject);
		addObject(scene2DObject);
	}

	/**
	 * Gets objects that intersect with a point and puts them into an array,
	 * starting from the beginning of it.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param out the output array.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersectingObjects(double x, double y, T[] out)
	{
		return getIntersectingObjects(x, y, out, 0);
	}

	/**
	 * Gets objects that intersect with an object and puts them into an array,
	 * starting from the beginning of it.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param object the object to test with.
	 * @param out the output array.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersectingObjects(SceneObject object, T[] out)
	{
		return getIntersectingObjects(object, out, 0);
	}

	/**
	 * Gets objects that intersect with a point and puts them into an array,
	 * starting from the specified offset.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param out the output array.
	 * @param offset the starting offset into the array.
	 * @return the amount of objects put in the array.
	 * @since 2.2.0
	 */
	public synchronized int getIntersectingObjects(double x, double y, T[] out, int offset)
	{
		accumPointIntersections(x, y);
		return accumToArray(out, offset);
	}

	/**
	 * Gets objects that intersect with an object and puts them into an array,
	 * starting from the specified offset.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param object the object to test with.
	 * @param out the output array.
	 * @param offset the starting offset into the array.
	 * @return the amount of objects put in the array.
	 * @since 2.2.0
	 */
	public synchronized int getIntersectingObjects(SceneObject object, T[] out, int offset)
	{
		accumObjectIntersections(object);
		return accumToArray(out, offset);
	}

	/**
	 * Gets objects that intersect with a point and adds them to a vector.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param vector the output vector.
	 * @return the amount of objects added to the vector.
	 * @since 2.2.0
	 */
	public synchronized int getIntersectingObjects(double x, double y, AbstractVector<T> vector)
	{
		accumPointIntersections(x, y);
		return accumToVector(vector);
	}

	/**
	 * Gets objects that intersect with an object and adds them to a vector.
	 * @param object the object to test with.
	 * @param vector the output vector.
	 * @return the amount of objects added to the vector.
	 * @since 2.2.0
	 */
	public synchronized int getIntersectingObjects(SceneObject object, AbstractVector<T> vector)
	{
		accumObjectIntersections(object);
		return accumToVector(vector);
	}

	/**
	 * Gets objects that intersect with a point and adds them to a vector, replacing contents from the beginning.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param vector the output vector.
	 * @return the amount of objects replaced/added to the vector.
	 * @since 2.2.0
	 */
	public synchronized int getIntersectingObjectsReplace(double x, double y, AbstractVector<T> vector)
	{
		accumPointIntersections(x, y);
		return accumToVectorReplace(vector);
	}

	/**
	 * Gets objects that intersect with an object and adds them to a vector, replacing contents from the beginning.
	 * @param object the object to test with.
	 * @param vector the output vector.
	 * @return the amount of objects replaced/added to the vector.
	 * @since 2.2.0
	 */
	public synchronized int getIntersectingObjectsReplace(SceneObject object, AbstractVector<T> vector)
	{
		accumObjectIntersections(object);
		return accumToVectorReplace(vector);
	}

	/**
	 * Clears this scene of all of its object references.
	 */
	public synchronized void clear()
	{
		objectMap.clear();
		objectGrid.clear();
		objectGridIterators.clear();
		allObjects.clear();
	}

	/**
	 * Returns the number of objects in this scene.
	 * @since 2.2.0
	 */
	public int size()
	{
		return allObjects.size();
	}

	@Override
	public ResettableIterator<T> iterator()
	{
		return allObjects.iterator();
	}

	/**
	 * Throws all point intersections into the accumulation hash.
	 */
	private void accumPointIntersections(double x, double y)
	{
		int gx = (int)x / gridResolution; 
		int gy = (int)y / gridResolution; 
		
		ResettableIterator<T> rit = getIteratorAt(gx, gy);
		while (rit.hasNext())
		{
			T object = rit.next();
			if (pointIntersect(x, y, object))
				intersectionAccum.put(object);
		}
	}
	
	/**
	 * Throws all object intersections into the accumulation hash.
	 */
	private void accumObjectIntersections(SceneObject object)
	{
		createGridPairsForObject(object);
		for (int i = 0; i < gridPairsCacheLength; i++)
		{
			Pair p = gridPairsCache.getByIndex(i);
			ResettableIterator<T> rit = getIteratorAt(p.x, p.y);
			while (rit.hasNext())
			{
				T obj = rit.next();
				if (object != obj && elementIntersects(object, obj))
					intersectionAccum.put(obj);
			}
		}
	}

	/**
	 * Dumps the contents of the accum hash to an array.
	 */
	private int accumToArray(T[] out, int offset)
	{
		intersectionAccumIterator.reset();
		int i = 0;
		while (i+offset < out.length && intersectionAccumIterator.hasNext())
		{
			out[i+offset] = intersectionAccumIterator.next();
			intersectionAccumIterator.remove();
			i++;
		}
		return i;
	}
	
	/**
	 * Dumps the contents of the accum hash to a vector.
	 */
	private int accumToVector(AbstractVector<T> vector)
	{
		intersectionAccumIterator.reset();
		int i = 0;
		while (intersectionAccumIterator.hasNext())
		{
			vector.add(intersectionAccumIterator.next());
			intersectionAccumIterator.remove();
			i++;
		}
		return i;
	}
	
	/**
	 * Dumps the contents of the accum hash to a vector.
	 */
	private int accumToVectorReplace(AbstractVector<T> vector)
	{
		intersectionAccumIterator.reset();
		int i = 0;
		while (intersectionAccumIterator.hasNext())
		{
			vector.replace(i, intersectionAccumIterator.next());
			intersectionAccumIterator.remove();
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
	
	/**
	 * Gets the grid positions that an object would be added to in the block grid.
	 * @param object	the object.
	 */
	private void createGridPairsForObject(SceneObject object)
	{
		float halfWidth = 0f;
		float halfHeight = 0f;

		if (object.useRadius())
		{
			float radius = object.getRadius();
			halfWidth = radius;
			halfHeight = radius;
		}
		else
		{
			halfWidth = object.getHalfWidth();
			halfHeight = object.getHalfHeight();
		}
		
		int startX = (int)Math.floor((object.getPositionX() - halfWidth) / gridResolution); 
		int endX = (int)Math.ceil((object.getPositionX() + halfWidth) / gridResolution); 
		int startY = (int)Math.floor((object.getPositionY() - halfHeight) / gridResolution); 
		int endY = (int)Math.ceil((object.getPositionY() + halfHeight) / gridResolution); 
		
		int i = 0;
		for (int x = startX; x <= endX; x += 1)
			for (int y = startY; y <= endY; y += 1)
			{
				if (i < gridPairsCache.size())
				{
					Pair p = gridPairsCache.getByIndex(i);
					p.x = x;
					p.y = y;
				}
				else
					gridPairsCache.add(new Pair(x,y));
				i++;
			}
		gridPairsCacheLength = i;
	}

	/**
	 * Tests if a point intersects with an object.
	 * @param x		the point, x-coordinate.
	 * @param y		the point, y-coordinate.
	 * @param object	the object.
	 */
	protected boolean pointIntersect(double x, double y, T object)
	{
		return 
			x <= object.getPositionX() + object.getHalfWidth() && 
			x >= object.getPositionX() - object.getHalfWidth() &&
			y <= object.getPositionY() + object.getHalfHeight() && 
			y >= object.getPositionY() - object.getHalfHeight();
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param obj2	another object.
	 * @return	true if their bounding areas overlap, false otherwise.
	 */
	protected boolean elementIntersects(SceneObject obj, SceneObject obj2)
	{
		// both are "circles."
		if (obj.useRadius() && obj2.useRadius())
		{
			float dx = obj.getPositionX() - obj2.getPositionX();
			float dy = obj.getPositionY() - obj2.getPositionY();
			return Math.sqrt(dx*dx + dy*dy) < (obj.getRadius() + obj2.getRadius());
		}
		// one is a "circle."
		else if (obj2.useRadius() ^ obj.useRadius())
		{
			SceneObject circ = obj2.useRadius() ? obj2 : obj;
			SceneObject rect = circ == obj2 ? obj : obj2; 
			
			float minrectX = rect.getPositionX() - rect.getHalfWidth();
			float maxrectX = rect.getPositionX() + rect.getHalfWidth();
			float minrectY = rect.getPositionY() - rect.getHalfHeight();
			float maxrectY = rect.getPositionY() + rect.getHalfHeight();
			
			// get closest rectangle region point.
			float vx = RMath.clampValue(circ.getPositionX(), minrectX, maxrectX);
			float vy = RMath.clampValue(circ.getPositionY(), minrectY, maxrectY);
	
			float dx = circ.getPositionX() - vx;
			float dy = circ.getPositionY() - vy;
			
			return Math.sqrt(dx*dx + dy*dy) < (circ.getRadius());
		}
		else
		{
			return 
				obj.getPositionX() - obj.getHalfWidth() < obj2.getPositionX() + obj2.getHalfWidth() &&
				obj.getPositionX() + obj.getHalfWidth() > obj2.getPositionX() - obj2.getHalfWidth() && 
				obj.getPositionY() - obj.getHalfHeight() < obj2.getPositionY() + obj2.getHalfHeight() &&
				obj.getPositionY() + obj.getHalfHeight() > obj2.getPositionY() - obj2.getHalfHeight();
		}
	}
	
}
