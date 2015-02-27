/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * Two-dimensional line segment.
 * @author Matthew Tropiano
 */
public class Line2F extends LineF<Point2F>
{
	/**
	 * Creates a line segment with two points, both (0, 0).
	 * @since 2.10.0
	 */
	public Line2F()
	{
		this(0f, 0f, 0f, 0f);
	}
	
	/**
	 * Creates a line segment from two points.
	 */
	public Line2F(Point2F a, Point2F b)
	{
		this(a.x, a.y, b.x, b.y);
	}
	
	/**
	 * Creates a line segment from a set from 
	 * coordinates making up two points.
	 * @param ax	start point x-coordinate.
	 * @param bx	end point x-coordinate.
	 */
	public Line2F(float ax, float ay, float bx, float by)
	{
		this.pointA = new Point2F(ax, ay);
		this.pointB = new Point2F(bx, by);
	}
	
	/**
	 * Creates a line by copying another.
	 */
	public Line2F(Line2F line)
	{
		this.pointA = new Point2F(line.pointA);
		this.pointB = new Point2F(line.pointB);
	}
	
	/**
	 * Returns the length of this line in units.
	 */
	public float getLength()
	{
		return pointA.getDistanceTo(pointB);
	}
	
	@Override
	public Line2F copy()
	{
		return new Line2F(this);
	}
	
}
