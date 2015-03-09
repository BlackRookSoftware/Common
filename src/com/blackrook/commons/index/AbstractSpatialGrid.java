/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.index;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.hash.Hash;

/**
 * The basis for all Spatial Indexes of all dimensions.
 * @author Matthew Tropiano
 * @since 2.21.0
 */
abstract class AbstractSpatialGrid<T extends Object> implements ResettableIterable<T>
{
	/** List of all objects. */
	private Hash<T> allObjects;
	/** Resolution of the collision grid. */
	private int resolution;

	/**
	 * SpatialHash with resolution 1.
	 */
	protected AbstractSpatialGrid()
	{
		this(1);
	}

	protected AbstractSpatialGrid(int resolution)
	{
		if (resolution <= 0)
			throw new IllegalArgumentException("Grid resolution cannot be 0 or less.");
		
		allObjects = new Hash<T>();
		this.resolution = resolution;
	}
	
	@Override
	public ResettableIterator<T> iterator()
	{
		return allObjects.iterator();
	}

	/**
	 * Returns the spatial resolution of this grid. 
	 */
	public int getResolution()
	{
		return resolution;
	}

	/**
	 * Checks if this contains an object. 
	 * @param object the object to check.
	 * @return true if this hash contains this object, false otherwise.
	 */
	public boolean containsObject(T object)
	{
		return allObjects.contains(object);
	}

	/**
	 * Adds an object to the hash. 
	 * @param object the spatial hash object to add.
	 */
	public void addObject(T object)
	{
		allObjects.put(object);
	}

	/**
	 * Removes an object from the hash. 
	 * @param object the spatial hash object to remove.
	 * @return true if removed, false if not.
	 */
	public boolean removeObject(T object)
	{
		return allObjects.remove(object);
	}

	/**
	 * Updates the hashing of an object in the hash.
	 * Equivalent to calling:
	 * <p>
	 * <code>
	 * if (removeObject(object)) addObject(object);
	 * </code>
	 * </p>
	 * @param object the spatial hash object to update.
	 */
	public void updateObject(T object)
	{
		if (removeObject(object))
			addObject(object);
	}

	/**
	 * Clears this hash of all of its object references.
	 */
	public void clear()
	{
		allObjects.clear();
	}

	/**
	 * Returns the number of objects in this hash.
	 */
	public int size()
	{
		return allObjects.size();
	}

	/**
	 * Gets objects that intersect with an object and adds them to a vector.
	 * This is a check of bounding volumes, according to the provided model.
	 * @param object the object to test with.
	 * @param output the output vector.
	 * @param offset the starting index to replace objects in the vector. 
	 * 		If it is greater or equal to the vector's length, the found objects are appended to it.
	 * @return the amount of objects added to the vector.
	 */
	public abstract int getIntersections(T object, AbstractVector<? super T> output, int offset);

	/**
	 * Gets the start grid coordinate for the center, sweep, and width of an object's dimensions.
	 * @param center the center of the object on an axis. 
	 * @param halfbreadth the breadth of the object on an axis.
	 * @param resolution the resolution of the grid.
	 * @return the grid coordinate to use based on the dimensions.
	 */
	public static int getStart(double center, double halfbreadth, int resolution)
	{
		halfbreadth = Math.abs(halfbreadth);
		return (int)Math.floor((center - halfbreadth) / (double)resolution);
	}

	/**
	 * Gets the end grid coordinate for the center, sweep, and width of an object's dimensions.
	 * @param center the center of the object on an axis. 
	 * @param halfbreadth the breadth of the object on an axis.
	 * @param resolution the resolution of the grid.
	 * @return the grid coordinate to use based on the dimensions.
	 */
	public static int getEnd(double center, double halfbreadth, int resolution)
	{
		halfbreadth = Math.abs(halfbreadth);
		return (int)Math.ceil((center + halfbreadth) / (double)resolution);
	}

}
