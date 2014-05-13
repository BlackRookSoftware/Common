/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * One-dimensional line segment.
 * @author Matthew Tropiano
 */
public class Line1D extends LineD<Point1D>
{
	/**
	 * Creates a line segment with two points, both (0).
	 * @since 2.10.0
	 */
	public Line1D()
	{
		this(0.0, 0.0);
	}
	
	/**
	 * Creates a line segment from two points.
	 */
	public Line1D(Point1D a, Point1D b)
	{
		this(a.x, b.x);
	}
	
	/**
	 * Creates a line segment from a set from 
	 * coordinates making up two points.
	 * @param ax	start point x-coordinate.
	 * @param bx	end point x-coordinate.
	 */
	public Line1D(double ax, double bx)
	{
		this.pointA = new Point1D(ax);
		this.pointB = new Point1D(bx);
	}
	
	/**
	 * Creates a line by copying another.
	 */
	public Line1D(Line1D line)
	{
		this.pointA = new Point1D(line.pointA);
		this.pointB = new Point1D(line.pointB);
	}
	
	/**
	 * Returns the length of this line in units.
	 */
	public double getLength()
	{
		return pointA.getDistanceTo(pointB);
	}
	
	@Override
	public Line1D copy()
	{
		return new Line1D(this);
	}
	
}
