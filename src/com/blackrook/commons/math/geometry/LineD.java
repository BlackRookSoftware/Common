/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * Describes a line segment.
 * @author Matthew Tropiano
 */
public abstract class LineD<T extends PointD>
{
	/** Starting point. */
	public T pointA;
	/** Ending point. */
	public T pointB;

	/**
	 * Returns the reference to the starting point.
	 * @return a reference to starting point.
	 */
	public T getPointA()
	{
		return pointA;
	}
	
	/**
	 * Returns the reference to the ending point.
	 * @return a reference to ending point.
	 */
	public T getPointB()
	{
		return pointB;
	}
	
	/** @return a copy of this line. */
	public abstract LineD<T> copy();

	@Override
	public String toString()
	{
		return pointA + " to " + pointB; 
	}
	
}
