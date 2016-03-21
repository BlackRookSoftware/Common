/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Defines the rules and methods of a Key-Value Map structure.
 * @author Matthew Tropiano
 * @param <K> the object type that serves as the Key in the map.
 * @param <V> the object type that serves as the Value in the map.
 * @since 2.19.0
 */
public interface AbstractMap<K extends Object, V extends Object> extends AbstractSet<ObjectPair<K,V>>
{
	/**
	 * Associates a key to a value in this map.
	 * The policy of "put" is that if it an object already in the set, its value is replaced with the new value. 
	 * @param key the map key.
	 * @param value the corresponding value.
	 */
	public void put(K key, V value);

	/**
	 * Checks if an object (by equality) is present in the structure.
	 * @param key the object to use for checking presence.
	 * @return true if it is in the map, false otherwise.
	 */
	public boolean containsKey(K key);

	/**
	 * Gets the value attached to the provided key.
	 * @param key the desired key.
	 * @return the value associated with the key or 
	 * null if no value associated with the provided key.
	 */
	public V get(K key);
	
	/**
	 * Removes a value from this map, corresponding to a key.
	 * @param key the key to use for checking presence.
	 * @return the corresponding value if it was removed from the map, null otherwise.
	 */
	public V removeUsingKey(K key);
	
}
