/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * Three-dimensional line segment.
 * @author Matthew Tropiano
 */
public class Line3D extends LineD<Point3D>
{
	/**
	 * Creates a line segment with two points, both (0, 0, 0).
	 * @since 2.10.0
	 */
	public Line3D()
	{
		this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}
	
	/**
	 * Creates a line segment from two points.
	 */
	public Line3D(Point3D a, Point3D b)
	{
		this(a.x, a.y, a.z, b.x, b.y, b.z);
	}
	
	/**
	 * Creates a line segment from a set from 
	 * coordinates making up two points.
	 * @param ax	start point x-coordinate.
	 * @param bx	end point x-coordinate.
	 */
	public Line3D(double ax, double ay, double az, double bx, double by, double bz)
	{
		this.pointA = new Point3D(ax, ay, az);
		this.pointB = new Point3D(bx, by, bz);
	}
	
	/**
	 * Creates a line by copying another.
	 */
	public Line3D(Line3D line)
	{
		this.pointA = new Point3D(line.pointA);
		this.pointB = new Point3D(line.pointB);
	}
	
	/**
	 * Returns the length of this line in units.
	 */
	public double getLength()
	{
		return pointA.getDistanceTo(pointB);
	}
	
	@Override
	public Line3D copy()
	{
		return new Line3D(this);
	}
	
}
