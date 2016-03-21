/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.grid;

import com.blackrook.commons.AbstractGrid;

/**
 * This is a tile grid that contains a grid of Object data generally
 * used for maps and lookups that require contiguous or mosaic-ed data.
 * These maps have a near-constant lookup time due to this grid being
 * a full lookup grid (multidimensional array).
 * @author Matthew Tropiano
 */
public class GridMap<T extends Object> extends AbstractGrid<T>
{
	/** List of grid codes. */
	protected Object[][] data;

	/**
	 * Creates a new empty data grid of a specified width and height.
	 * @param width the width of the grid in data values.
	 * @param height the height of the grid in data values.
	 */
	public GridMap(int width, int height)
	{
		data = new Object[width][height];
		widthWrapType = WrapType.NONE;
		heightWrapType = WrapType.NONE;
		depthWrapType = WrapType.NONE;
	}

	/**
	 * Clears everything from the grid.
	 */
	public void clear()
	{
		data = new Object[getWidth()][getHeight()];
	}
	
	/**
	 * @return the full width of the grid.
	 */
	public int getWidth()
	{
		return data.length;
	}

	/**
	 * @return the full height of the grid.
	 */
	public int getHeight()
	{
		return data[0].length;
	}

	/**
	 * @return <code>1</code>.
	 */
	public int getDepth()
	{
		return 1;
	}

	/**
	 * Tests if a certain point is in the grid.
	 * Takes grid wrapping/clamping into consideration.
	 * @param x the grid position x.
	 * @param y the grid position y.
	 * @return true if so, false if not.
	 */
	public boolean isInGrid(int x, int y)
	{	
		x = correctX(x);
		y = correctY(y);
		return !(x < 0 || x >= getWidth() || y < 0 || y >= getHeight());
	}

	/**
	 * Sets an object at a particular part of the grid.
	 * Takes grid wrapping/clamping into consideration.
	 * @param x the grid position x to set info.
	 * @param y the grid position y to set info.
	 * @param object the object to set.
	 */
	public void set(int x, int y, T object)
	{
		data[correctX(x)][correctY(y)] = object;
	}

	/**
	 * Gets the object at a particular part of the grid.
	 * @param x	the grid position x to get info.
	 * @param y	the grid position y to get info.
	 * @return the object or null if no object is at that position or off the grid.
	 */
	@SuppressWarnings("unchecked")
	public T get(int x, int y)
	{
		if (!isInGrid(x,y))
			return null;
		return (T)data[correctX(x)][correctY(y)];
	}

}
