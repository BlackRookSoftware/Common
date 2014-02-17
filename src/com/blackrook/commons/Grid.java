/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
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
public interface Grid
{
	/**
	 * The wrapping types employed by all grids. 
	 */
	public static enum WrapType
	{
		/** No wrapping. Coordinates off of the grid are not altered.  */
		NONE,
		/** Coordinates wrap around to the opposite coordinate.  */
		TILE,
		/** Coordinates are clamped to the nearest edge.  */
		CLAMP
	}
	
	/** Gets the total width of this grid in units. */
	public int getWidth();
	/** Gets the total height of this grid in units. */
	public int getHeight();
	/** Gets the total depth of this grid in units. */
	public int getDepth();
	
	/**
	 * Returns one of the three wrapping types describing how this grid
	 * should wrap coordinates on the width axis.
	 */
	public WrapType getWidthWrapType();
	/**
	 * Returns one of the three wrapping types describing how this grid
	 * should wrap coordinates on the height axis.
	 */
	public WrapType getHeightWrapType();
	/**
	 * Returns one of the three wrapping types describing how this grid
	 * should wrap coordinates on the depth axis.
	 */
	public WrapType getDepthWrapType();
	
}
