package com.blackrook.commons;

/**
 * Defines the rules and methods of a Set structure.
 * @author Matthew Tropiano
 * @param <P> the object type that this data structure stores.
 * @since 2.19.0
 */
public interface AbstractSet<P extends Object> extends ResettableIterable<P>, Sizable
{
	/**
	 * Adds an object to this structure, but only if it does not exist, according to
	 * {@link #contains(Object)}.
	 * The policy of "put" is that if it an object already in the set, this does nothing. Else,
	 * it is added.
	 * @param object the object to add.
	 */
	public void put(P object);

	/**
	 * Checks if an object (by equality) is present in the structure, according to
	 * {@link #equalityMethod(Object, Object)}.
	 * @param object the object to use for checking presence.
	 * @return true if it is in the structure, false otherwise.
	 */
	public boolean contains(P object);

	/**
	 * Removes an object from this structure.
	 * @param object the object to use for checking presence.
	 * @return true if it was removed from the structure, false otherwise.
	 */
	public boolean remove(P object);
	
	/**
	 * Determines if the objects are equal. This can be implemented differently
	 * in case a data structure has a different concept of what is considered equal.
	 * @param object1 the first object.
	 * @param object2 the second object.
	 * @return true if the keys are considered equal, false otherwise.
	 */
	public boolean equalityMethod(P object1, P object2);
	
}
