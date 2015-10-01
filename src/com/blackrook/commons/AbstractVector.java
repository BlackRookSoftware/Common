/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import com.blackrook.commons.math.RMath;

/**
 * An abstract class for performing unsynchronized Vector (data structure)
 * operatons and concepts. None of the methods are synchronized, so be careful
 * when using this in a multithreaded setting or extend this class with synchronized
 * methods.
 * @param <T> the underlying object type.
 * @author Matthew Tropiano
 */
public abstract class AbstractVector<T extends Object> 
	extends AbstractArrayStorage<T> implements ResettableIterable<T>, Sizable
{
	/** Capacity increment. */
	protected int capacityIncrement;
	/** Amount of objects in the storageArray. */
	protected int size;

	/**
	 * Makes a new vector.
	 */
	public AbstractVector()
	{
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Makes a new vector that doubles every resize.
	 * @param capacity the initial capacity of this vector. If 0 or less, it is 1. 
	 */
	public AbstractVector(int capacity)
	{
		this(capacity,0);
	}
	
	/**
	 * Makes a new vector.
	 * @param capacity the initial capacity of this vector.
	 * @param capacityIncrement what to increase the capacity of this vector by 
	 * if this reaches the max. if 0 or less, it will double.
	 */
	public AbstractVector(int capacity, int capacityIncrement)
	{
		if (capacity <= 0)
			capacity = 1;
		setCapacityIncrement(capacityIncrement);
		setCapacity(capacity);
	}

	/**
	 * Clears the vector.
	 * As of 2.21.0, this fills this vector with nulls instead of using allocating a new array.
	 */
	public void clear()
	{
		if (isEmpty()) 
			return;
		shallowClear();
		Arrays.fill(storageArray, null);
	}

	/**
	 * Clears the vector shallowly - removes no references and just sets the current size to 0.
	 * This can be prone to holding references in memory longer than necessary, so know what
	 * you are doing if you care about garbage collecting. 
	 * @since 2.21.0
	 */
	public void shallowClear()
	{
		if (isEmpty()) 
			return;
		size = 0;
	}

	/**
	 * Gets the capacity of this vector (size before it resizes itself).
	 */
	public int getCapacity()
	{
		return storageArray.length;
	}

	/**
	 * Sets this storageArray's capacity to some value. If this vector is set to a capacity
	 * that is less than the current one, it will cut the vector short. If the
	 * capacity argument is 0 or less, it is set to 1.
	 * @param capacity the new capacity of this vector.
	 */
	public void setCapacity(int capacity)
	{
		if (capacity < 1)
			capacity = 1;
		Object[] newList = new Object[capacity];
		if (storageArray != null)
			System.arraycopy(storageArray, 0, newList, 0, Math.min(newList.length, storageArray.length));
		if (newList.length < storageArray.length && newList.length < size)
			size = newList.length;
		storageArray = newList;
	}

	/**
	 * Returns the capacity increment value.
	 */
	public int getCapacityIncrement()
	{
		return capacityIncrement;
	}

	/**
	 * Sets the capacity increment value.
	 * @param capacityIncrement	what to increase the capacity of this vector by 
	 * if this reaches the max. if 0 or less, it will double.
	 */
	public void setCapacityIncrement(int capacityIncrement)
	{
		this.capacityIncrement = capacityIncrement;
	}

	/**
	 * Gets an object at an index in the vector.
	 * @param index the desired index.
	 * @return null if index is out of vector bounds.
	 */
	public T getByIndex(int index)
	{
		if (index < 0 || index >= size)
			return null;
		return super.getByIndex(index);
	}

	/**
	 * Adds an object to the end of the vector.
	 * @param object the object to add.
	 */
	public void add(T object)
	{
		add(size,object);
	}

	/**
	 * Adds an object at an index. 
	 * If index is greater than or equal to the size, it will add it at the end.
	 * If index is less than 0, it won't add it.
	 * @param index the index to add this at.
	 * @param object the object to add.
	 */
	public void add(int index, T object)
	{
		if (index < 0)
			return;
		if (index > size)
			index = size;
		if (size == storageArray.length)
			setCapacity(getCapacity()+(capacityIncrement <= 0 ? getCapacity() : capacityIncrement));
		if (storageArray[index] != null)
			System.arraycopy(storageArray, index, storageArray, index+1, size - index);
		setByIndex(index, object);
		size++;
	}

	/**
	 * Adds an object to the end of the vector, and attempts to sort it down to a sorted position.
	 * @param object the object to add.
	 * @param comparator the comparator to use.
	 * @since 2.21.0
	 * @throws NullPointerException if object or comparator is null.
	 */
	public void addAndSort(T object, Comparator<? super T> comparator)
	{
		int index = size;
		add(size, object);
		while (index > 0 && comparator.compare(getByIndex(index), getByIndex(index - 1)) < 0)
		{
			swap(index, index - 1);
			index--;
		}
	}
	
	/**
	 * Sets an object at an index. Used for replacing contents.
	 * If index is greater than or equal to the size, it will add it at the end.
	 * If index is less than 0, this does nothing.
	 * @param index the index to set this at.
	 * @param object the object to add.
	 * @since 2.2.0
	 */
	public void replace(int index, T object)
	{
		if (index < 0)
			return;
		if (index >= size)
			add(object);
		else
			setByIndex(index, object);
	}

	/**
	 * Removes an object from the vector, if it exists in the vector.
	 * Sequential search.
	 * @param object the object to search for and remove.
	 * @return true if removed, false if not in the vector.
	 * @throws NullPointerException if object is null.
	 */
	public boolean remove(T object)
	{
		int i = getIndexOf(object);
		if (i < 0)
			return false;
		return removeIndex(i) != null;
	}

	/**
	 * Removes an object from a spot in the vector and shifts 
	 * everything after it down an index position.
	 * @return	null if the index is out of bounds or the object at that index.
	 */
	@SuppressWarnings("unchecked")
	public T removeIndex(int index)
	{
		if (index < 0 || index >= size)
			return null;
		T out = (T)storageArray[index];
		if (index+1 < size)
			System.arraycopy(storageArray, index+1, storageArray, index, size-index-1);
		size--;
		storageArray[size] = null;
		return out;
	}

	/**
	 * Checks if an object exists in this vector via iterative comparison.
	 */
	public boolean contains(T obj)
	{
		return getIndexOf(obj) != -1;
	}
	
	/**
	 * Gets the index of an object, presumably in the vector.
	 * Sequential search.
	 * @param object the object to search for.
	 * @return the index of the object if it is in the vector, or -1 if it is not present.
	 * @throws NullPointerException if object is null.
	 */
	public int getIndexOf(T object)
	{
		for (int i = 0; i < size; i++)
			if (object.equals(storageArray[i]))
				return i;
		return -1;
	}

	/**
	 * Gets the index of an object, presumably in the vector via binary search.
	 * Expects the contents of this vector to be sorted.
	 * @param object the object to search for.
	 * @param comparator the comparator to use for comparison or equivalence.
	 * @return the index of the object if it is in the vector, or -1 if it is not present.
	 * @since 2.21.0
	 * @throws NullPointerException if object or comparator is null.
	 */
	public int getIndexOf(T object, Comparator<? super T> comparator)
	{
		int hi = size, lo = 0;
		int i = (hi + lo) / 2;
		int prev = hi;
		
		while (i != prev)
		{
			if (getByIndex(i).equals((T)object))
				return i;
			
			int c = comparator.compare(getByIndex(i),(T)object);
			
			if (c < 0)
				lo = i;
			else if (c == 0)
				return i;
			else
				hi = i;
			
			prev = i;
			i = (hi + lo) / 2;
		}
		
		return lo - 1;
	}

	/**
	 * Returns an iterator for this vector.
	 */
	public ResettableIterator<T> iterator()
	{
		return new VectorIterator();
	}

	/**
	 * Sorts this vector using NATURAL ORDERING.
	 * Calls {@link Arrays#sort(Object[], int, int)} on the internal storage array.
	 */
	@SuppressWarnings("unchecked")
	public void sort()
	{
		Arrays.sort((T[])storageArray, 0, size);
	}

	/**
	 * Sorts this vector using a comparator.
	 * Calls {@link Arrays#sort(Object[], int, int, Comparator)} on the internal storage array, using the specified comparator.
	 */
	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super T> comp)
	{
		Arrays.sort((T[])storageArray, 0, size, comp);
	}

	/**
	 * Sorts this vector using NATURAL ORDERING.
	 * Calls {@link Arrays#sort(Object[], int, int)} on the internal storage array.
	 * @param startIndex the starting index of the sort.
	 * @param endIndex the ending index of the sort, exclusive.
	 */
	@SuppressWarnings("unchecked")
	public void sort(int startIndex, int endIndex)
	{
		Arrays.sort((T[])storageArray, startIndex, endIndex);
	}

	/**
	 * Sorts this vector using a comparator.
	 * Calls {@link Arrays#sort(Object[], int, int, Comparator)} on the internal storage array, using the specified comparator.
	 * @param startIndex the starting index of the sort.
	 * @param endIndex the ending index of the sort, exclusive.
	 */
	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super T> comp, int startIndex, int endIndex)
	{
		Arrays.sort((T[])storageArray, startIndex, endIndex, comp);
	}

	/**
	 * Swaps the contents of two indices in the vector.
	 * <p>If index0 is equal to index1, this does nothing.
	 * <p>If one index is outside the bounds of this vector 
	 * (less than 0 or greater than or equal to {@link #size()}),
	 * this throws an exception. 
	 * @param index0 the first index.
	 * @param index1 the second index.
	 * @throws IllegalArgumentException if one index is outside the bounds of this vector 
	 * (less than 0 or greater than or equal to {@link #size()}).
	 * @since 2.10.0
	 */
	public void swap(int index0, int index1)
	{
		if (index0 < 0 || index0 >= size)
			throw new IllegalArgumentException("index0 cannot be outside the range of this vector.");
		if (index1 < 0 || index1 >= size)
			throw new IllegalArgumentException("index1 cannot be outside the range of this vector.");
		
		if (index0 == index1)
			return;
		
		T obj = getByIndex(index0);
		setByIndex(index0, getByIndex(index1));
		setByIndex(index1, obj);
	}
	
	/**
	 * Moves the object at an index in this vector to another index,
	 * shifting the contents between the two selected indices in this vector back or forward.
	 * <p>If sourceIndex is equal to targetIndex, this does nothing.
	 * <p>If one index is outside the bounds of this vector 
	 * (less than 0 or greater than or equal to {@link #size()}),
	 * this throws an exception. 
	 * @param sourceIndex the first index.
	 * @param targetIndex the second index.
	 * @throws IllegalArgumentException if one index is outside the bounds of this vector 
	 * (less than 0 or greater than or equal to {@link #size()}).
	 * @since 2.10.0
	 */
	public void shift(int sourceIndex, int targetIndex)
	{
		if (sourceIndex < 0 || sourceIndex >= size)
			throw new IllegalArgumentException("sourceIndex cannot be outside the range of this vector.");
		if (targetIndex < 0 || targetIndex >= size)
			throw new IllegalArgumentException("index1 cannot be outside the range of this vector.");
		
		if (sourceIndex == targetIndex)
			return;

		T obj = getByIndex(sourceIndex);
		if (targetIndex < sourceIndex)
			System.arraycopy(storageArray, targetIndex, storageArray, targetIndex + 1, sourceIndex - targetIndex);
		else if (targetIndex > sourceIndex)
			System.arraycopy(storageArray, sourceIndex + 1, storageArray, sourceIndex, targetIndex - sourceIndex);
		setByIndex(targetIndex, obj);
	}
	
	/**
	 * Randomizes the order of the objects in this vector,
	 * using a random number generator.
	 * @param random the random number generator to use.
	 * @since 2.10.0
	 */
	public void shuffle(Random random)
	{
		for (int i = size(); i > 1; i--)
			swap(i-1, RMath.rand(random, i));
	}

	/**
	 * Returns the amount of objects in the vector.
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns true if there is nothing in this vector, false otherwise.
	 * Equivalent to <code>size() == 0</code>.
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	/**
	 * Copies the contents of this class into an array.
	 * @param out the target array to copy the objects into.
	 * @throws ArrayIndexOutOfBoundsException if the target array is too small.
	 */
	@SuppressWarnings("unchecked")
	public void toArray(T[] out)
	{
		System.arraycopy((T[])storageArray, 0, out, 0, size);
	}
	
	/**
	 * Returns this storageArray as a string.
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < size; i++)
		{
			sb.append(storageArray[i].toString());
			if (i < size-1)
				sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Iterator class for this vector.
	 */
	protected class VectorIterator implements ResettableIterator<T>
	{
		private int currIndex;
		private boolean removeCalled;
		
		public VectorIterator()
		{
			reset();
		}
		
		public boolean hasNext()
		{
			while (currIndex < storageArray.length && getByIndex(currIndex) == null)
				currIndex++;
				
			return currIndex != storageArray.length;
		}
	
		public T next()
		{
			T out = getByIndex(currIndex);
			do {currIndex++;} while (currIndex < storageArray.length && getByIndex(currIndex) == null);
			removeCalled = false;
			return out;
		}
	
		public void remove()
		{
			if (!removeCalled)
			{
				removeIndex(currIndex-1);
				removeCalled = true;
			}
		}

		@Override
		public void reset()
		{
			currIndex = 0;
			removeCalled = true;
		}
		
	}
	

}
