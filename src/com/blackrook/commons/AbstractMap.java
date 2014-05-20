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
	 * Uses {@link #equalityMethodForKey(Object, Object)} to determine key equality.
	 * @param object the object to add.
	 */
	public void put(K key, V value);

	/**
	 * Checks if an object (by equality) is present in the structure, according to
	 * {@link #equalityMethod(Object, Object)}.
	 * @param key the object to use for checking presence.
	 * @return true if it is in the map, false otherwise.
	 */
	public boolean containsKey(K key);

	/**
	 * Removes a value from this map, corresponding to a key.
	 * @param key the key to use for checking presence.
	 * @return the corresponding value if it was removed from the map, null otherwise.
	 */
	public V removeUsingKey(K key);
	
	/**
	 * Determines if two keys are equal. This can be implemented differently
	 * in case a map has a different concept of what keys are is considered equal.
	 * @param key1 the first key.
	 * @param key2 the second key.
	 * @return true if the keys are considered equal, false otherwise.
	 */
	public boolean equalityMethodForKey(K key1, K key2);
	
}
