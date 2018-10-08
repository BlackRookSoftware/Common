package com.blackrook.commons;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Simple utility functions around Arrays.
 * Split from {@link Common}.
 * @author Matthew Tropiano
 * @since 2.32.0
 */
public final class ArrayUtils 
{
	private ArrayUtils() {}

	/**
	 * Returns a valid index of an element in an array if an object is contained in an array. 
	 * Sequentially searches for first match via {@link #equals(Object)}.
	 * Can search for null. 
	 * @param <T> class that extends Object.
	 * @param object the object to search for. Can be null.
	 * @param searchArray the list of objects to search.
	 * @return the index of the object, or -1 if it cannot be found.
	 * @since 2.13.2
	 */
	public static <T> int indexOf(T object, T[] searchArray)
	{
		for (int i = 0; i < searchArray.length; i++)
		{
			if (object == null && searchArray[i] == null)
				return i;
			else if (object.equals(searchArray[i]))
				return i;
		}
		return -1;
	}

	/**
	 * Gets the element at an index in the array, but returns 
	 * null if the index is outside of the array bounds.
	 * @param <T> the array type.
	 * @param array the array to use.
	 * @param index the index to use.
	 * @return <code>array[index]</code> or null if out of bounds.
	 * @since 2.31.2
	 */
	public static <T> T arrayElement(T[] array, int index)
	{
		if (index < 0 || index >= array.length)
			return null;
		else
			return array[index];
	}

	/**
	 * Swaps the contents of two indices of an array.
	 * @param <T> the object type stored in the array.
	 * @param array the input array.
	 * @param a the first index.
	 * @param b the second index.
	 * @since 2.21.0
	 */
	public static <T> void arraySwap(T[] array, int a, int b)
	{
		T temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

	/**
	 * Concatenates a set of arrays together, such that the contents of each
	 * array are joined into one array. Null arrays are skipped.
	 * @param <T> the object type stored in the arrays.
	 * @param arrays the list of arrays.
	 * @return a new array with all objects in each provided array added 
	 * to the resultant one in the order in which they appear.
	 * @since 2.17.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] joinArrays(T[]...  arrays)
	{
		int totalLen = 0;
		for (T[] a : arrays)
			if (a != null)
				totalLen += a.length;
		
		Class<?> type = Reflect.getArrayType(arrays);
		T[] out = (T[])Array.newInstance(type, totalLen);
		
		int offs = 0;
		for (T[] a : arrays)
		{
			System.arraycopy(a, 0, out, offs, a.length);
			offs += a.length;
		}
		
		return out;
	}

	/**
	 * Adds an object to an array that presumably contains sorted elements.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @return the final index in the array of the added object.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #sortFrom(Comparable[], int)
	 * @since 2.21.0 
	 */
	public static <T extends Comparable<T>> int addSorted(T[] array, T object, int start)
	{
		array[start] = object;
		return sortFrom(array, start);
	}

	/**
	 * Adds an object to an array that presumably contains sorted elements.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the arrays.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @param comparator the comparator to use for comparisons.
	 * @return the final index in the array of the added object.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #sortFrom(Object[], int, Comparator)
	 * @since 2.21.0 
	 */
	public static <T> int addSorted(T[] array, T object, int start, Comparator<T> comparator)
	{
		array[start] = object;
		return sortFrom(array, start, comparator);
	}

	/**
	 * Adds an object to an array that presumably contains sorted elements, but only if it isn't found via binary search.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @return the final index in the array of the added object, or -1 if not added.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #addSorted(Comparable[], Comparable, int)
	 * @since 2.21.0 
	 */
	public static <T extends Comparable<T>> int addSortedUnique(T[] array, T object, int start)
	{
		if (Arrays.binarySearch(array, 0, start, object) < 0)
			return addSorted(array, object, start);
		else
			return -1;
	}

	/**
	 * Adds an object to an array that presumably contains sorted elements, but only if it isn't found via binary search.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the arrays.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @param comparator the comparator to use for comparisons.
	 * @return the final index in the array of the added object, or -1 if not added.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #addSorted(Object[], Object, int, Comparator)
	 * @since 2.21.0 
	 */
	public static <T> int addSortedUnique(T[] array, T object, int start, Comparator<T> comparator)
	{
		if (Arrays.binarySearch(array, 0, start, object, comparator) < 0)
			return addSorted(array, object, start, comparator);
		else
			return -1;
	}

	/**
	 * Shifts an object to an appropriate position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the array to shift the contents of.
	 * @param index the index to add it to (the contents are replaced).
	 * @return the final index in the array of the sorted object.
	 * @since 2.21.0 
	 */
	public static <T extends Comparable<T>> int sortFrom(T[] array, int index)
	{
		while (index > 0 && array[index].compareTo(array[index - 1]) < 0)
		{
			arraySwap(array, index, index - 1);
			index--;
		}
		return index;
	}

	/**
	 * Shifts an object to an appropriate position according to the provided <code>comparator</code> function.
	 * @param <T> the object type stored in the arrays.
	 * @param array the array to shift the contents of.
	 * @param index the index to add it to (the contents are replaced).
	 * @param comparator the comparator to use.
	 * @return the final index in the array of the sorted object.
	 * @since 2.21.0 
	 */
	public static <T> int sortFrom(T[] array, int index, Comparator<? super T> comparator)
	{
		while (index > 0 && comparator.compare(array[index], array[index - 1]) < 0)
		{
			arraySwap(array, index, index - 1);
			index--;
		}
		return index;
	}

	/**
	 * Performs an in-place QuickSort on the provided array.
	 * The array's contents will change upon completion.
	 * Convenience method for <code>quicksort(array, 0, array.length - 1);</code>
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the input array.
	 * @since 2.21.0
	 */
	public static <T extends Comparable<T>> void quicksort(T[] array)
	{
		quicksort(array, 0, array.length - 1);
	}

	/**
	 * Performs an in-place QuickSort on the provided array using a compatible Comparator.
	 * The array's contents will change upon completion.
	 * Convenience method for <code>quicksort(array, 0, array.length - 1, comparator);</code>
	 * @param <T> the object type stored in the array.
	 * @param array the input array.
	 * @param comparator the comparator to use for comparing.
	 * @since 2.21.0
	 */
	public static <T> void quicksort(T[] array, Comparator<? super T> comparator)
	{
		quicksort(array, 0, array.length - 1, comparator);
	}

	/**
	 * Performs an in-place QuickSort on the provided array within an interval of indices.
	 * The array's contents will change upon completion.
	 * If <code>lo</code> is greater than <code>hi</code>, this does nothing. 
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the input array.
	 * @param lo the low index to start the sort (inclusive).
	 * @param hi the high index to start the sort (inclusive).
	 * @since 2.21.0
	 */
	public static <T extends Comparable<T>> void quicksort(T[] array, int lo, int hi)
	{
		if (lo >= hi)
			return;
	    int p = quicksortPartition(array, lo, hi);
	    quicksort(array, lo, p - 1);
	    quicksort(array, p + 1, hi);
	}

	/**
	 * Performs an in-place QuickSort on the provided array within an interval of indices.
	 * The array's contents will change upon completion.
	 * If <code>lo</code> is greater than <code>hi</code>, this does nothing. 
	 * @param <T> the object type stored in the array.
	 * @param array the input array.
	 * @param lo the low index to start the sort (inclusive).
	 * @param hi the high index to start the sort (inclusive).
	 * @param comparator the comparator to use for comparing.
	 * @since 2.21.0
	 */
	public static <T> void quicksort(T[] array, int lo, int hi, Comparator<? super T> comparator)
	{
		if (lo >= hi)
			return;
	    int p = quicksortPartition(array, lo, hi, comparator);
	    quicksort(array, lo, p - 1, comparator);
	    quicksort(array, p + 1, hi, comparator);
	}

	// Do quicksort partition - pivot sort.
	private static <T extends Comparable<T>> int quicksortPartition(T[] array, int lo, int hi)
	{
		T pivot = array[hi];
	    int i = lo;
	    for (int j = lo; j <= hi - 1; j++)
	    {
	        if (array[j].compareTo(pivot) <= 0)
	        {
	        	arraySwap(array, i, j);
	            i++;
	        }
	    }
		arraySwap(array, i, hi);
	    return i;
	}

	// Do quicksort partition - pivot sort.
	private static <T> int quicksortPartition(T[] array, int lo, int hi, Comparator<? super T> comparator)
	{
		T pivot = array[hi];
	    int i = lo;
	    for (int j = lo; j <= hi - 1; j++)
	    {
	        if (comparator.compare(array[j], pivot) <= 0)
	        {
	        	arraySwap(array, i, j);
	            i++;
	        }
	    }
		arraySwap(array, i, hi);
	    return i;
	}

	/**
	 * Copies references from one array to another until 
	 * it hits a null sentinel reference or the end of the source array.
	 * @param <T> the object type stored in the arrays.
	 * @param source the source array.
	 * @param sourceOffset the source offset.
	 * @param destination the destination array.
	 * @param destinationOffset the starting destination offset.
	 * @return how many references were copied.
	 * @throws ArrayIndexOutOfBoundsException if this tries to resolve a destination that is out of bounds.
	 * @since 2.21.0
	 */
	public static <T> int arrayCopyToNull(T[] source, int sourceOffset, T[] destination, int destinationOffset)
	{
		int s;
		for (s = 0; s + sourceOffset < source.length && source[s + sourceOffset] != null; s++)
			destination[s + destinationOffset] = source[s + sourceOffset];
		return s;
	}
}
