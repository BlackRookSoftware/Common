package com.blackrook.commons;

/**
 * Defines a class that has a method for dumping the contents of a data structure to an array.
 * @author Matthew Tropiano
 * @param <T> target type.
 */
public interface Dumpable<T> 
{
	/**
	 * Copies the contents of this class into an array.
	 * The order of the contents are not guaranteed unless otherwise noted.
	 * @param out the target array to copy the objects into.
	 * @throws ArrayIndexOutOfBoundsException if the target array is too small to contain the objects.
	 * @since 2.31.0
	 */
	public void toArray(T[] out);

}
