/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

/**
 * A HashMap that maps strings to whatever, but does case-insensitive lookups on
 * the keys, which are strings.
 * @author Matthew Tropiano
 */
public class CaseInsensitiveHashMap<T extends Object> extends HashMap<String, T>
{
	/**
	 * Creates a new CaseInsensitiveHashMap with capacity 10, rehash ratio 0.75.
	 */
	public CaseInsensitiveHashMap()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new CaseInsensitiveHashMap with capacity <i>cap</i> and rehash ratio 0.75. 
	 * @param capacity	the capacity. cannot be negative.
	 */
	public CaseInsensitiveHashMap(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new CaseInsensitiveHashMap.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public CaseInsensitiveHashMap(int capacity, float rehashRatio)
	{
		super(capacity,rehashRatio);
	}
	
	/**
	 * Returns the hashcode for a map key.
	 */
	protected int getHashcodeForKey(String key)
	{
		return key.toLowerCase().hashCode();
	}

	/**
	 * Checks if two keys are equal.
	 * @param key1 the first key.
	 * @param key2 the second key.
	 * @return true if the keys are considered equal, false otherwise.
	 */
	public boolean equalityMethodForKey(String key1, String key2)
	{
		if (key1 == null && key2 != null)
			return false;
		else if (key1 != null && key2 == null)
			return false;
		else if (key1 == null && key2 == null)
			return true;
		return key1.equalsIgnoreCase(key2);
	}

}
