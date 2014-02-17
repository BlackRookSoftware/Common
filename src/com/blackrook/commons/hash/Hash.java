/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

import com.blackrook.commons.AbstractChainedHash;

/**
 * Hashed data structure. It stores several objects
 * in a chained hash table, used primarily for testing for
 * existence of archive-able, searchable objects.
 * @author Matthew Tropiano
 */
public class Hash<T extends Object> extends AbstractChainedHash<T>
{
	/**
	 * Creates a new hash with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public Hash()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hash with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 */
	public Hash(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hash.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public Hash(int capacity, float rehashRatio)
	{
		super(capacity, rehashRatio);
	}
}
