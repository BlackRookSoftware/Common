/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.list;

import com.blackrook.commons.AbstractVector;

/**
 * A completely unsynchronized vector implementation.
 * @author Matthew Tropiano
 */
public class List<T extends Object> extends AbstractVector<T>
{
	/**
	 * Makes a new volatile list.
	 */
	public List()
	{
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Makes a new volatile list that doubles every resize.
	 * @param capacity		the initial capacity of this list. If 0 or less, it is 1. 
	 */
	public List(int capacity)
	{
		this(capacity, 0);
	}
	
	/**
	 * Makes a new volatile list.
	 * @param capacity		the initial capacity of this list.
	 * @param capacityIncrement	what to increase the capacity of this list by 
	 * 						if this reaches the max. if 0 or less, it will double.
	 */
	public List(int capacity, int capacityIncrement)
	{
		super(capacity, capacityIncrement);
	}
}
