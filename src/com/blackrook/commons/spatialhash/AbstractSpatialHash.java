/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.spatialhash;

import com.blackrook.commons.AbstractVector;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.hash.Hash;

/**
 * The basis for all Spatial Hashes of all dimensions.
 * @author Matthew Tropiano
 * @param <T> an object of type {@link SpatialHashable}.
 * @since 2.10.0
 * @deprecated Since 2.21.0. Use the spatial indexing packages instead.
 */
@Deprecated
abstract class AbstractSpatialHash<T extends AbstractSpatialHashable> implements ResettableIterable<T>
{
	/** List of all objects. */
	private Hash<T> allObjects;
	/** Resolution of the collision grid. */
	private int resolution;

	/**
	 * SpatialHash with resolution 1.
	 */
	protected AbstractSpatialHash()
	{
		this(1);
	}

	/**
	 * @param resolution the grid resolution in units.
	 */
	protected AbstractSpatialHash(int resolution)
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
	 * @return the number of objects in this hash.
	 */
	public int size()
	{
		return allObjects.size();
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
	public abstract int getIntersections(T object, T[] out, int offset);

	/**
	 * Gets objects that intersect with an object and adds them to a vector.
	 * This is not a comprehensive check - just a check of bounding volumes.
	 * @param object the object to test with.
	 * @param vector the output vector.
	 * @param replace if true, this replaces contents starting from the beginning of the vector.
	 * @return the amount of objects added to the vector.
	 */
	public abstract int getIntersections(T object, AbstractVector<T> vector, boolean replace);

	/**
	 * @return the spatial resolution of this grid. 
	 */
	public int getResolution()
	{
		return resolution;
	}
	
	/**
	 * Gets the start grid coordinate for the center, sweep, and width of an object's dimensions.
	 * @param center the center of the object on an axis. 
	 * @param halfbreadth the breadth of the object on an axis.
	 * @param sweep the sweep of the object across an axis.
	 * @return the grid coordinate to use based on the dimensions.
	 */
	public int getStartGrid(double center, double halfbreadth, double sweep)
	{
		return getStartGrid(center, halfbreadth, sweep, resolution);
	}

	/**
	 * Gets the end grid coordinate for the center, sweep, and width of an object's dimensions.
	 * @param center the center of the object on an axis. 
	 * @param halfbreadth the breadth of the object on an axis.
	 * @param sweep the sweep of the object across an axis.
	 * @return the grid coordinate to use based on the dimensions.
	 */
	public int getEndGrid(double center, double halfbreadth, double sweep)
	{
		return getEndGrid(center, halfbreadth, sweep, resolution);
	}

	/**
	 * Gets the start grid coordinate for the center, sweep, and width of an object's dimensions.
	 * @param center the center of the object on an axis. 
	 * @param halfbreadth the breadth of the object on an axis.
	 * @param sweep the sweep of the object across an axis.
	 * @param resolution the resolution of the grid.
	 * @return the grid coordinate to use based on the dimensions.
	 */
	public static int getStartGrid(double center, double halfbreadth, double sweep, int resolution)
	{
		halfbreadth = Math.abs(halfbreadth);

		double halfsweep = sweep / 2f;
		
		center += halfsweep;
		halfbreadth += halfsweep;
		
		return (int)Math.floor((center - halfbreadth) / (double)resolution);
	}

	/**
	 * Gets the end grid coordinate for the center, sweep, and width of an object's dimensions.
	 * @param center the center of the object on an axis. 
	 * @param halfbreadth the breadth of the object on an axis.
	 * @param sweep the sweep of the object across an axis.
	 * @param resolution the resolution of the grid.
	 * @return the grid coordinate to use based on the dimensions.
	 */
	public static int getEndGrid(double center, double halfbreadth, double sweep, int resolution)
	{
		halfbreadth = Math.abs(halfbreadth);
		
		double halfsweep = sweep / 2f;
		
		center += halfsweep;
		halfbreadth += halfsweep;
		
		return (int)Math.ceil((center + halfbreadth) / (double)resolution);
	}

}
