/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

import com.blackrook.commons.AbstractChainedHashMap;

/**
 * Hashed data structure that maps keys to values.
 * @author Matthew Tropiano
 */
public class HashMap<K extends Object, V extends Object> extends AbstractChainedHashMap<K, V>
{
	/**
	 * Creates a new hash map with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public HashMap()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hash map with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 * @throws IllegalArgumentException if capacity is negative.
	 */
	public HashMap(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hash map.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public HashMap(int capacity, float rehashRatio)
	{
		super(capacity, rehashRatio);
	}
}
