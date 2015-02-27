/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.map;

import com.blackrook.commons.hash.CaseInsensitiveHashMap;

/**
 * A variant of {@link AbstractMappedVector} in which the keys are
 * Strings that are case-insensitively mapped to the underlying object indices.
 * @author Matthew Tropiano
 * @param <T> the Object type stored in this Vector. 
 */
public abstract class CaseInsensitiveMappedVector<T extends Object> extends AbstractMappedVector<T, String>
{
	/**
	 * Makes a new mapped vector.
	 */
	public CaseInsensitiveMappedVector()
	{
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Makes a new mapped vector that doubles every resize.
	 * @param capacity the initial capacity of this vector. If 0 or less, it is 1. 
	 */
	public CaseInsensitiveMappedVector(int capacity)
	{
		this(capacity, 0);
	}
	
	/**
	 * Makes a new mapped vector.
	 * @param capacity the initial capacity of this vector.
	 * @param capacityIncrement what to increase the capacity of this vector by 
	 * if this reaches the max. if 0 or less, it will double.
	 */
	public CaseInsensitiveMappedVector(int capacity, int capacityIncrement)
	{
		super(capacity, capacityIncrement);
		indexMap = new CaseInsensitiveHashMap<Integer>();
	}

}
