/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.grid;

import com.blackrook.commons.AbstractGrid;
import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.Sizable;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.math.Pair;

/**
 * This is a grid that contains a grid of Object data generally
 * used for maps and lookups that require contiguous or mosaic-ed data.
 * These maps are sparse, which means it uses as little memory as possible, which
 * increases the lookup time in most cases.
 * <p>
 * No wrapping style does not correct the bounds of input coordinates.
 * @since 2.13.0, this implements Sizable.
 * @author Matthew Tropiano
 */
public class SparseGridMap<T extends Object> extends AbstractGrid<T> implements ResettableIterable<ObjectPair<Pair, T>>, Sizable
{
	/** Macro constant for an unspecified size. */
	public static final int SIZE_UNSPECIFIED = 0;

	/** List of grid codes. */
	protected HashMap<Pair, T> data;

	/** Defined width. */
	protected int width;
	/** Defined height. */
	protected int height;
	
	/** Temporary pair object for get. */
	protected Pair TEMP_PAIR;
	
	/**
	 * Creates a new sparse grid of an unspecified width and height.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public SparseGridMap()
	{
		this(SIZE_UNSPECIFIED, SIZE_UNSPECIFIED);
	}
	
	/**
	 * Creates a new sparse grid of a specified width and height.
	 * @param width the width of the grid in data values.
	 * @param height the height of the grid in data values.
	 */
	public SparseGridMap(int width, int height)
	{
		width = Math.max(0, width);
		height = Math.max(0, height);
		TEMP_PAIR = new Pair();
		int capacity = width + height;
		data = new HashMap<Pair, T>(capacity <= 0 ? HashMap.DEFAULT_CAPACITY : capacity);
		this.width = width;
		this.height = height;
		widthWrapType = WrapType.NONE;
		heightWrapType = WrapType.NONE;
		depthWrapType = WrapType.NONE;
	}
	
	/**
	 * Clears everything from the grid map.
	 */
	public void clear()
	{
		data.clear();
	}

	/**
	 * Returns the full width of the grid or SIZE_UNSPECIFIED
	 * if the width is not specified or valid.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Returns the full height of the grid or SIZE_UNSPECIFIED
	 * if the height is not specified or valid.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Returns 1.
	 */
	public int getDepth()
	{
		return 1;
	}

	/**
	 * Tests if a certain point is in the grid.
	 * @return true if so, false if not.
	 */
	public boolean isInGrid(int x, int y)
	{	
		return !(x < 0 || x >= getWidth() || y < 0 || y >= getHeight());
	}

	/**
	 * Sets an object at a particular part of the grid.
	 * @param x		the grid position x to set info.
	 * @param y		the grid position y to set info.
	 * @param object	the object to set.
	 */
	public void set(int x, int y, T object)
	{
		x = correctX(x);
		y = correctY(y);
		if (object == null)
			data.removeUsingKey(new Pair(x, y));
		else
			data.put(new Pair(x, y), object);
	}

	/**
	 * Gets the object at a particular part of the grid.
	 * @param x	the grid position x to set info.
	 * @param y	the grid position y to set info.
	 */
	public T get(int x, int y)
	{
		TEMP_PAIR.x = correctX(x);
		TEMP_PAIR.y = correctY(y);
		return data.get(TEMP_PAIR);
	}
	
	@Override
	public String toString()
	{
		return data.toString();
	}

	@Override
	public ResettableIterator<ObjectPair<Pair, T>> iterator()
	{
		return data.iterator();
	}

	@Override
	public int size()
	{
		return data.size();
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
}
