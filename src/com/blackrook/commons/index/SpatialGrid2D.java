/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.index;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.grid.SparseQueueGridMap;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.math.geometry.Point2D;

/**
 * A class to be used for two-dimensional Spatial Hashing in a uniform grid.
 * This is used for finding quick spatial relationships between several
 * objects, or intervals in this case.
 * <p>
 * Objects in this hash, however, are not actively tracked. If the spatial
 * information of objects contained in this hash change in any way, they
 * have to be updated via {@link #updateObject(Object)}.
 * </p>
 * @author Matthew Tropiano
 * @since 2.21.0
 */
public class SpatialGrid2D<T> extends AbstractSpatialGrid<T>
{
	/** Object map. */
	private SparseQueueGridMap<T> objectMap;
	/** Object model. */
	private SpatialIndex2DModel<T> model;

	/** Search accumulator iterator. */
	private ResettableIterator<T> intersectionAccumIterator;
	/** Search accumulator hash. */
	private Hash<T> intersectionAccum;
	/** Temporary point. */
	private Point2D tempPoint;

	/**
	 * Creates a new IntervalHash.
	 * @param resolution however many units is one grid space.
	 */
	public SpatialGrid2D(SpatialIndex2DModel<T> model, int resolution)
	{
		super(resolution);
		this.model = model;
		this.objectMap = new SparseQueueGridMap<T>();
		
		this.intersectionAccum = new Hash<T>();
		this.intersectionAccumIterator = intersectionAccum.iterator();
		this.tempPoint = new Point2D();
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
		
		model.getCenter(object, tempPoint);
		double centerX = tempPoint.x;
		double centerY = tempPoint.y;
		model.getHalfWidths(object, tempPoint);
		double halfWidth = tempPoint.x;
		double halfHeight = tempPoint.y;
		
		int startX = AbstractSpatialGrid.getStart(centerX, halfWidth, getResolution());
		int startY = AbstractSpatialGrid.getStart(centerY, halfHeight, getResolution());
		int endX = AbstractSpatialGrid.getEnd(centerX, halfWidth, getResolution());
		int endY = AbstractSpatialGrid.getEnd(centerY, halfHeight, getResolution());
	
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
		
		model.getCenter(object, tempPoint);
		double centerX = tempPoint.x;
		double centerY = tempPoint.y;
		model.getHalfWidths(object, tempPoint);
		double halfWidth = tempPoint.x;
		double halfHeight = tempPoint.y;
		
		int startX = AbstractSpatialGrid.getStart(centerX, halfWidth, getResolution());
		int startY = AbstractSpatialGrid.getStart(centerY, halfHeight, getResolution());
		int endX = AbstractSpatialGrid.getEnd(centerX, halfWidth, getResolution());
		int endY = AbstractSpatialGrid.getEnd(centerY, halfHeight, getResolution());
	
		for (int x = startX; x <= endX; x++)
			for (int y = startY; y <= endY; y++)
				objectMap.enqueue(x, y, object);
		
		return super.removeObject(object);
	}

	/**
	 * Gets the intersections straddling a given point.
	 * @param x the point to test.
	 * @param vector the output vector.
	 * @param offset 
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(double x, double y, AbstractVector<? super T> vector, int offset)
	{
		accumPointIntersections(x, y);
		return accumToVector(vector);
	}

	/**
	 * Gets the intersections straddling a given interval.
	 * @param x0 the beginning of the interval to test.
	 * @param x1 the ending of the interval to test.
	 * @param vector the output vector.
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(double x0, double y0, double x1, double y1, AbstractVector<? super T> vector, int offset)
	{
		accumLineIntersections(x0, y0, x1, y1);
		return accumToVector(vector);
	}

	/**
	 * Gets objects that intersect with an object and adds them to a vector.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * @param object the object to test with.
	 * @param vector the output vector.
	 * @return the amount of objects added to the vector.
	 */
	public synchronized int getIntersections(T object, AbstractVector<? super T> vector, int offset)
	{
		accumObjectIntersections(object);
		return accumToVector(vector);
	}

	/**
	 * Tests if a point intersects with an object.
	 * @param x	the point, x-coordinate.
	 * @param object the object.
	 */
	protected boolean pointIntersects(double x, double y, T object)
	{
		model.getCenter(object, tempPoint);
		double centerX = tempPoint.x;
		double centerY = tempPoint.y;
		model.getHalfWidths(object, tempPoint);
		double halfWidth = tempPoint.x;
		double halfHeight = tempPoint.y;

		return x <= centerX + halfWidth && x >= centerX - halfWidth && y <= centerY + halfHeight && y >= centerY - halfHeight;
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @return true if their bounding areas overlap, false otherwise.
	 */
	protected boolean lineIntersects(double x0, double y0, double x1, double y1, T object)
	{
		//TODO: Write me.
		return false; 
	}

	/**
	 * Checks if an object intersects another bounding area.
	 * This is NOT a comprehensive collision test, as two object's
	 * BOUNDING volumes may intersect, but NOT their ACTUAL boundaries.
	 * @param object first object.
	 * @param object2 another object.
	 * @return	true if their bounding areas overlap, false otherwise.
	 */
	protected boolean objectIntersects(T object, T object2)
	{
		model.getCenter(object, tempPoint);
		double centerX = tempPoint.x;
		model.getHalfWidths(object, tempPoint);
		double halfWidth = tempPoint.x;

		model.getCenter(object2, tempPoint);
		double centerX2 = tempPoint.x;
		model.getHalfWidths(object2, tempPoint);
		double halfWidth2 = tempPoint.x;

		return centerX - halfWidth < centerX2 + halfWidth2 && centerX + halfWidth > centerX2 - halfWidth2; 
	}

	/**
	 * Throws all object intersections into the accumulation hash.
	 */
	private void accumObjectIntersections(T object)
	{
		model.getCenter(object, tempPoint);
		double centerX = tempPoint.x;
		double centerY = tempPoint.y;
		model.getHalfWidths(object, tempPoint);
		double halfWidth = tempPoint.x;
		double halfHeight = tempPoint.y;
		
		intersectionAccum.clear();
		int startX = AbstractSpatialGrid.getStart(centerX, halfWidth, getResolution());
		int startY = AbstractSpatialGrid.getStart(centerY, halfHeight, getResolution());
		int endX = AbstractSpatialGrid.getEnd(centerX, halfWidth, getResolution());
		int endY = AbstractSpatialGrid.getEnd(centerY, halfHeight, getResolution());
	
		for (int x = startX; x <= endX; x++)
			for (int y = startY; y <= endY; y++)
			{
				Queue<T> queue = objectMap.get(x, y);
				if (queue != null) for (T obj : queue)
				{
					if (intersectionAccum.contains(obj))
						continue;
					
					if (lineIntersects(centerX - halfWidth,	centerY - halfHeight, centerX + halfWidth, centerY + halfHeight, obj))
						intersectionAccum.put(obj);
				}
			}
	}


	/**
	 * Throws all line intersections into the accumulation hash.
	 */
	private void accumLineIntersections(double x0, double y0, double x1, double y1)
	{
		if (x0 == x1 && y0 == y1)
		{
			accumPointIntersections(x0, y0);
			return;
		}
		
		intersectionAccum.clear();
		
		// TODO: Finish. Draw Bresenham line with corners across the grid, accum all intersections.
		
	}


	/**
	 * Throws all point intersections into the accumulation hash.
	 */
	private void accumPointIntersections(double x, double y)
	{
		intersectionAccum.clear();

		int mapX = (int)(x / getResolution());
		int mapY = (int)(y / getResolution());
		
		Queue<T> queue = objectMap.get(mapX, mapY);
		if (queue != null) for (T object : queue)
		{
			if (intersectionAccum.contains(object))
				continue;
			
			if (pointIntersects(x, y, object))
				intersectionAccum.put(object);
		}
	}

	/**
	 * Dumps the contents of the accum hash to a vector.
	 */
	private int accumToVector(AbstractVector<? super T> vector)
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

}
