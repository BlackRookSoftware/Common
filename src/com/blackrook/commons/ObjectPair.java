/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Object pairs for a bunch of data structures. 
 * @author Matthew Tropiano
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class ObjectPair<K extends Object, V extends Object>
{
	/** The key object in the pair. */
	protected K key;
	/** The value object in the pair. */
	protected V value;
	
	/**
	 * Creates a new object pair.
	 * @param key the key object.
	 * @param value the value object.
	 */
	public ObjectPair(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	/** Returns the key. */
	public K getKey()
	{
		return key;
	}

	/** Returns the value. */
	public V getValue()
	{
		return value;
	}

	/** Sets the value to a new value. */
	public void setValue(V value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return '(' + key.toString() + ", " + value.toString() + ')';
	}
}
