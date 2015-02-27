/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Interface that is used by all "grid" types.
 * @author Matthew Tropiano
 */
public abstract class AbstractGrid<T extends Object> implements Grid
{

	/** Width wrap type. */
	protected WrapType widthWrapType;
	/** Height wrap type. */
	protected WrapType heightWrapType;
	/** Depth wrap type. */
	protected WrapType depthWrapType;
	
	/**
	 * Sets one of the three wrapping types describing how this grid
	 * should wrap coordinates on the width axis.
	 */
	public void setWidthWrapType(WrapType type)
	{
		widthWrapType = type;
	}

	/**
	 * Sets one of the three wrapping types describing how this grid
	 * should wrap coordinates on the height axis.
	 */
	public void setHeightWrapType(WrapType type)
	{
		heightWrapType = type;
	}

	/**
	 * Sets one of the three wrapping types describing how this grid
	 * should wrap coordinates on the depth axis.
	 */
	public void setDepthWrapType(WrapType type)
	{
		depthWrapType = type;
	}

	@Override
	public WrapType getWidthWrapType()
	{
		return widthWrapType;
	}

	@Override
	public WrapType getHeightWrapType()
	{
		return heightWrapType;
	}

	@Override
	public WrapType getDepthWrapType()
	{
		return depthWrapType;
	}

	/**
	 * "Corrects" the input for the X axis based on the wrapping type
	 * and whether the length of the axis is defined (greater than 0).
	 * If not, this acts as though there is no wrapping type specified.
	 */
	protected int correctX(int x)
	{
		int w = getWidth();
		if (w > 0) switch (widthWrapType)
		{
			default:
				return x;
			case TILE:
				return x < 0 ? w - ((-x) % w) : x % w;
			case CLAMP:
				return Math.max(Math.min(x, w - 1), 0);
		}
		else
			return x;
	}

	/**
	 * "Corrects" the input for the Y axis based on the wrapping type
	 * and whether the length of the axis is defined (greater than 0).
	 * If not, this acts as though there is no wrapping type specified.
	 */
	protected int correctY(int y)
	{
		int h = getHeight();
		if (h > 0) switch (heightWrapType)
		{
			default:
				return y;
			case TILE:
				return y < 0 ? h - ((-y) % h) : y % h;
			case CLAMP:
				return Math.max(Math.min(y, h - 1), 0);
		}
		else
			return y;
	}

	/**
	 * "Corrects" the input for the Z axis based on the wrapping type
	 * and whether the length of the axis is defined (greater than 0).
	 * If not, this acts as though there is no wrapping type specified.
	 */
	protected int correctZ(int z)
	{
		int d = getDepth();
		if (d > 0) switch (depthWrapType)
		{
			default:
				return z;
			case TILE:
				return z < 0 ? d - ((-z) % d) : z % d;
			case CLAMP:
				return Math.max(Math.min(z, d - 1), 0);
		}
		else
			return z;
	}

	/**
	 * Sets an object at a particular part of the grid.
	 * @param x		the grid position x to set info.
	 * @param y		the grid position y to set info.
	 * @param code	the code to set.
	 */
	public abstract void set(int x, int y, T code);

	/**
	 * Gets the object at a particular part of the grid.
	 * @param x	the grid position x to set info.
	 * @param y	the grid position y to set info.
	 */
	public abstract T get(int x, int y);
}
