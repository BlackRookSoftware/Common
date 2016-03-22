/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.spatialhash;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.Common;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.hash.HashedHashMap;

/**
 * A class to be used for one-dimensional Spatial Hashing.
 * This is used for finding quick spatial relationships between several
 * objects, or intervals in this case.
 * <p>
 * Objects in this hash, however, are not actively tracked. If the spatial
 * information of objects contained in this hash change in any way, they
 * have to be updated via {@link #updateObject(AbstractSpatialHashable)}.
 * </p>
 * @author Matthew Tropiano
 * @since 2.10.0
 * @deprecated Since 2.21.0. Use the spatial indexing packages instead.
 */
@Deprecated
public class IntervalHash<T extends IntervalHashable> extends AbstractSpatialHash<T> implements ResettableIterable<T>
{
	private static final String LOCAL_CACHE_NAME = Common.getPackagePathForClass(IntervalHash.class) + "/Cache";

	/** Object map. */
	private HashedHashMap<Integer, T> objectMap;

	private static final class Cache<T extends IntervalHashable>
	{
		/** Search accumulator iterator. */
		private ResettableIterator<T> intersectionAccumIterator;
		/** Search accumulator hash. */
		private Hash<T> intersectionAccum;
		
		private Cache()
		{			
			intersectionAccum = new Hash<T>();
			intersectionAccumIterator = intersectionAccum.iterator();
		}
	}
	
	/**
	 * Creates a new IntervalHash.
	 * @param resolution however many units is one grid space.
	 */
	public IntervalHash(int resolution)
	{
		super(resolution);
		objectMap = new HashedHashMap<Integer, T>();
	}

	
	/**
	 * Adds an object to the hash. 
	 * @param object the spatial hash object to add.
	 */
	public void addObject(T object)
	{
		if (containsObject(object))
			return;
		
		int startX = getStartGrid(object.getObjectCenterX(), object.getObjectHalfWidth(), object.getObjectSweepX());
		int endX = getEndGrid(object.getObjectCenterX(), object.getObjectHalfWidth(), object.getObjectSweepX());
	
		for (int i = startX; i <= endX; i++)
			objectMap.add(i, object);

		super.addObject(object);
	}

	/**
	 * Removes an object from the hash. 
	 * @param object the spatial hash object to remove.
	 * @return true if removed, false if not.
	 */
	public boolean removeObject(T object)
	{
		if (!containsObject(object))
			return false;
		
		int startX = getStartGrid(object.getObjectCenterX(), object.getObjectHalfWidth(), object.getObjectSweepX());
		int endX = getEndGrid(object.getObjectCenterX(), object.getObjectHalfWidth(), object.getObjectSweepX());
	
		for (int i = startX; i <= endX; i++)
			objectMap.removeValue(i, object);
		
		return super.removeObject(object);
	}

	/**
	 * Clears this hash of all of its object references.
	 */
	public void clear()
	{
		super.clear();
		objectMap.clear();
	}

	/**
	 * Gets the intersections straddling a given point.
	 * @param x the point to test.
	 * @param out the output array.
	 * @param offset the starting offset into the output array.
	 * @return the amount of objects returned.
	 */
	public synchronized int getIntersections(double x, T[] out, int offset)
	{
		Cache<T> cache = getCache();
		accumPointIntersections(x, cache);
		return accumToArray(out, offset, cache);
	}

	/**
	 * Gets the intersections straddling a given point.
	 * @param x the point to test.
	 * @param vector the output vector.
	 * @param replace if true, this replaces contents starting from the beginning of the vector.
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(double x, AbstractVector<T> vector, boolean replace)
	{
		Cache<T> cache = getCache();
		accumPointIntersections(x, cache);
		if (replace)
			return accumToVectorReplace(vector, cache);
		else
			return accumToVector(vector, cache);
	}

	/**
	 * Gets the intersections straddling a given interval.
	 * @param x0 the beginning of the interval to test.
	 * @param x1 the ending of the interval to test.
	 * @param out the output array.
	 * @param offset the starting offset into the output array.
	 * @return the amount of objects returned.
	 */
	public synchronized int getIntersections(double x0, double x1, T[] out, int offset)
	{
		Cache<T> cache = getCache();
		accumLineIntersections(x0, x1, cache);
		return accumToArray(out, offset, cache);
	}

	/**
	 * Gets the intersections straddling a given interval.
	 * @param x0 the beginning of the interval to test.
	 * @param x1 the ending of the interval to test.
	 * @param vector the output vector.
	 * @param replace if true, this replaces contents starting from the beginning of the vector.
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(double x0, double x1, AbstractVector<T> vector, boolean replace)
	{
		Cache<T> cache = getCache();
		accumLineIntersections(x0, x1, cache);
		if (replace)
			return accumToVectorReplace(vector, cache);
		else
			return accumToVector(vector, cache);
	}

	/**
	 * Gets objects that intersect with an object and puts them into an array,
	 * starting from the specified offset.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * This will stop putting objects into the array if it reaches the end.
	 * @param object the object to test with.
	 * @param out the output array.
	 * @param offset the starting offset into the array.
	 * @return the amount of objects put in the array.
	 */
	public synchronized int getIntersections(T object, T[] out, int offset)
	{
		Cache<T> cache = getCache();
		accumObjectIntersections(object, cache);
		return accumToArray(out, offset, cache);
	}

	/**
	 * Gets objects that intersect with an object and adds them to a vector.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * @param object the object to test with.
	 * @param vector the output vector.
	 * @param replace if true, this replaces contents starting from the beginning of the vector.
	 * @return the amount of objects added to the vector.
	 */
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
	 * Tests if a point intersects with an object.
	 * @param x	the point, x-coordinate.
	 * @param object the object.
	 * @return true if so, false if not.
	 */
	protected boolean pointIntersects(double x, T object)
	{
		return 
			x <= object.getObjectCenterX() + object.getObjectHalfWidth() && 
			x >= object.getObjectCenterX() - object.getObjectHalfWidth();
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param x0 the first bound.
	 * @param x1 the second bound.
	 * @param obj the object to test.
	 * @return	true if their bounding areas overlap, false otherwise.
	 */
	protected boolean lineIntersects(double x0, double x1, T obj)
	{
		double min = x0 < x1 ? x0 : x1;
		double max = x0 < x1 ? x1 : x0;
		return 
			min < obj.getObjectCenterX() + obj.getObjectHalfWidth() &&
			max > obj.getObjectCenterX() - obj.getObjectHalfWidth(); 
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param obj	first object.
	 * @param obj2	another object.
	 * @return	true if their bounding areas overlap, false otherwise.
	 */
	protected boolean objectIntersects(SpatialHashable obj, T obj2)
	{
		return 
			obj.getObjectCenterX() - obj.getObjectHalfWidth() < obj2.getObjectCenterX() + obj2.getObjectHalfWidth() &&
			obj.getObjectCenterX() + obj.getObjectHalfWidth() > obj2.getObjectCenterX() - obj2.getObjectHalfWidth(); 
	}

	/**
	 * Throws all point intersections into the accumulation hash.
	 */
	private void accumPointIntersections(double x, Cache<T> cache)
	{
		cache.intersectionAccum.clear();

		int mapX = (int)(x / getResolution());
		
		Hash<T> hash = objectMap.get(mapX);
		if (hash != null) for (T object : hash)
		{
			if (cache.intersectionAccum.contains(object))
				continue;
			
			if (pointIntersects(x, object))
				cache.intersectionAccum.put(object);
		}
	}

	/**
	 * Throws all line intersections into the accumulation hash.
	 */
	private void accumLineIntersections(double x0, double x1, Cache<T> cache)
	{
		if (x0 == x1)
		{
			accumPointIntersections(x0, cache);
			return;
		}
		
		cache.intersectionAccum.clear();
		
		int startX = getStartGrid((float)x0, 0f, 0f);
		int endX = getEndGrid((float)x1, 0f, 0f);

		for (int i = startX; i <= endX; i++)
		{
			Hash<T> hash = objectMap.get(i);
			if (hash != null) for (T object : hash)
			{
				if (cache.intersectionAccum.contains(object))
					continue;
				
				if (lineIntersects(
						object.getObjectCenterX() - object.getObjectHalfWidth(), 
						object.getObjectCenterX() + object.getObjectHalfWidth(), 
						object))
					cache.intersectionAccum.put(object);
			}
		}
	}

	/**
	 * Throws all object intersections into the accumulation hash.
	 */
	private void accumObjectIntersections(T object, Cache<T> cache)
	{
		cache.intersectionAccum.clear();
		
		int startX = getStartGrid(object.getObjectCenterX(), object.getObjectHalfWidth(), object.getObjectSweepX());
		int endX = getEndGrid(object.getObjectCenterX(), object.getObjectHalfWidth(), object.getObjectSweepX());
	
		for (int i = startX; i <= endX; i++)
		{
			Hash<T> hash = objectMap.get(i);
			if (hash != null) for (T obj : hash)
			{
				if (cache.intersectionAccum.contains(obj))
					continue;
				
				if (lineIntersects(
						obj.getObjectCenterX() - obj.getObjectHalfWidth(), 
						obj.getObjectCenterX() + obj.getObjectHalfWidth(), 
						obj))
					cache.intersectionAccum.put(obj);
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

	@SuppressWarnings("unchecked")
	private Cache<T> getCache()
	{
		Cache<T> out = null;
		if ((out = (Cache<T>)Common.getLocal(LOCAL_CACHE_NAME)) == null)
			Common.setLocal(LOCAL_CACHE_NAME, out = new Cache<T>());
		return out;
	}

}
