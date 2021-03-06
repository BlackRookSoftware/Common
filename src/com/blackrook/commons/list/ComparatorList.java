/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.list;

import java.util.Comparator;

import com.blackrook.commons.AbstractVector;

/**
 * Maintains an expandable list of objects, but is always sorted.
 * @author Matthew Tropiano
 * @param <T> any Object.
 */
public class ComparatorList<T extends Object> extends AbstractVector<T>
{
	public static final int DEFAULT_CAPACITY = 10;

	/** The object comparator. */
	protected Comparator<? super T> comparator;
	
	/**
	 * Constructs a new ComparatorVector with capacity DEFAULT_CAPACITY.
	 * @param comparator	the comparator to use for sorting and searching.
	 */
	public ComparatorList(Comparator<? super T> comparator)
	{
		this(comparator,DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs a new ComparatorVector with defined capacity.
	 * Capacity incrementor = capacity.
	 * @param comparator	the comparator to use for sorting and searching.
	 * @param capacity		the initial capacity of the vector.
	 */
	public ComparatorList(Comparator<? super T> comparator, int capacity)
	{
		this(comparator,capacity,0);
	}
	
	/**
	 * Constructs a new ComparatorVector with defined capacity and capacity increment.
	 * @param comparator	the comparator to use for sorting and searching.
	 * @param capacity		the initial capacity of the vector.
	 * @param inc			the capacity incrementor.
	 */
	public ComparatorList(Comparator<? super T> comparator, int capacity, int inc)
	{
		super(capacity, inc);
		this.comparator = comparator;
	}
	
	/**
	 * Adds an object to the end of the vector and sorts it
	 * to the correct position in the vector.
	 * @param object the object to add.
	 */
	@Override
	public void add(T object)
	{
		int index = size;
		add(size, object);
		while (index > 0 && comparator.compare(getByIndex(index), getByIndex(index - 1)) < 0)
		{
			swap(index, index - 1);
			index--;
		}
	}
	
	@Override
	public int getIndexOf(T object)
	{
		int out = search(object, comparator);
		return out < 0 ? -1 : out;
	}

}
