/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Interface that is used by all "grid" types.
 * @author Matthew Tropiano
 * @deprecated Since 2.21.0. Structure is too rigid.
 */
@Deprecated
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
	
	/** @return the total width of this grid in units. */
	public int getWidth();
	/** @return the total height of this grid in units. */
	public int getHeight();
	/** @return the total depth of this grid in units. */
	public int getDepth();
	
	/**
	 * @return one of the three wrapping types describing how this grid
	 * should wrap coordinates on the width axis.
	 */
	public WrapType getWidthWrapType();
	/**
	 * @return one of the three wrapping types describing how this grid
	 * should wrap coordinates on the height axis.
	 */
	public WrapType getHeightWrapType();
	/**
	 * @return one of the three wrapping types describing how this grid
	 * should wrap coordinates on the depth axis.
	 */
	public WrapType getDepthWrapType();
	
}
