/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
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
public class Line2D extends LineD<Point2D>
{
	/**
	 * Creates a line segment with two points, both (0, 0).
	 * @since 2.10.0
	 */
	public Line2D()
	{
		this(0, 0, 0, 0);
	}
	
	/**
	 * Creates a line segment from two points.
	 */
	public Line2D(Point2D a, Point2D b)
	{
		this(a.x, a.y, b.x, b.y);
	}
	
	/**
	 * Creates a line segment from a set from 
	 * coordinates making up two points.
	 * @param ax	start point x-coordinate.
	 * @param bx	end point x-coordinate.
	 */
	public Line2D(double ax, double ay, double bx, double by)
	{
		this.pointA = new Point2D(ax, ay);
		this.pointB = new Point2D(bx, by);
	}
	
	/**
	 * Creates a line by copying another.
	 */
	public Line2D(Line2D line)
	{
		this.pointA = new Point2D(line.pointA);
		this.pointB = new Point2D(line.pointB);
	}
	
	/**
	 * Returns the length of this line in units.
	 */
	public double getLength()
	{
		return pointA.getDistanceTo(pointB);
	}
	
	@Override
	public Line2D copy()
	{
		return new Line2D(this);
	}
	
}
