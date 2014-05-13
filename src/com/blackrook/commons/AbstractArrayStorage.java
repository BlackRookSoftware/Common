/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * This class contains the base for all data structures 
 * that make use of a contiguous memory structure.
 * @author Matthew Tropiano
 */
public abstract class AbstractArrayStorage<T extends Object>
{
	/** Default capacity for a new array. */
	public static final int DEFAULT_CAPACITY = 8;
	
	/** Underlying object array. */
	protected Object[] storageArray;

	/**
	 * Initializes the array with the default storage capacity.
	 */
	protected AbstractArrayStorage()
	{
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Initializes the array with a particular storage capacity.
	 */
	protected AbstractArrayStorage(int capacity)
	{
		storageArray = new Object[capacity];
	}
	
	/**
	 * Returns the data at a particular index in the array.
	 */
	@SuppressWarnings("unchecked")
	protected T getByIndex(int index)
	{
		return (T)storageArray[index];
	}
	
	/**
	 * Sets the data at a particular index in the array.
	 */
	protected void setByIndex(int index, T object)
	{
		storageArray[index] = object;
	}
	
}
