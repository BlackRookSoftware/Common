/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
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
public class Line1F extends LineF<Point1F>
{
	/**
	 * Creates a line segment with two points, both (0).
	 * @since 2.10.0
	 */
	public Line1F()
	{
		this(0.0f, 0.0f);
	}
	
	/**
	 * Creates a line segment from two points.
	 * @param a the first point.
	 * @param b the second point.
	 */
	public Line1F(Point1F a, Point1F b)
	{
		this(a.x, b.x);
	}
	
	/**
	 * Creates a line segment from a set from 
	 * coordinates making up two points.
	 * @param ax start point x-coordinate.
	 * @param bx end point x-coordinate.
	 */
	public Line1F(float ax, float bx)
	{
		this.pointA = new Point1F(ax);
		this.pointB = new Point1F(bx);
	}
	
	/**
	 * Creates a line by copying another.
	 * @param line the source line
	 */
	public Line1F(Line1F line)
	{
		this.pointA = new Point1F(line.pointA);
		this.pointB = new Point1F(line.pointB);
	}
	
	/**
	 * @return the length of this line in units.
	 */
	public float getLength()
	{
		return pointA.getDistanceTo(pointB);
	}
	
	@Override
	public Line1F copy()
	{
		return new Line1F(this);
	}
	
}
