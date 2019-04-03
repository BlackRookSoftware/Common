/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple2D;

/**
 * Object that describes a rectangular area.
 * @author Matthew Tropiano
 */
public class Rectangle2D
{
	/** Starting X-coordinate. */
	public double x;
	/** Starting Y-coordinate. */
	public double y;
	/** Rectangle width. */
	public double width;
	/** Rectangle height. */
	public double height;
	
	/**
	 * Creates a rectangle at (0,0) with width and height of 0.
	 */
	public Rectangle2D()
	{
		this(0.0, 0.0, 0.0, 0.0);
	}
	
	/**
	 * Creates a copy of a rectangle.
	 * @param r the source rectangle to copy.
	 */
	public Rectangle2D(Rectangle2D r)
	{
		this(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Creates a new rectangle.
	 * @param x Starting X-coordinate.
	 * @param y Starting Y-coordinate.
	 * @param width Rectangle width.
	 * @param height Rectangle height.
	 */
	public Rectangle2D(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Sets the values in this rectangle using another rectangle.
	 * @param r the other rectangle.
	 * @since 2.8.0
	 */
	public void set(Rectangle2D r)
	{
		set(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Sets the values in this rectangle.
	 * @param x Starting X-coordinate.
	 * @param y Starting Y-coordinate.
	 * @param width Rectangle width.
	 * @param height Rectangle height.
	 * @since 2.8.0
	 */
	public void set(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Projects this rectangle onto a line, <code>out</code>.
	 * @param target the target tuple, treated as a vector.
	 * @param out the output line.
	 * @since 2.10.0
	 */
	public void projectOnto(Tuple2D target, Line2D out)
	{
		double ox = x + (width / 2.0);
		double oy = y + (height / 2.0);
		
		if (target.x != 0.0)
		{
			double slope = target.y / target.x;
			double theta = Math.atan(slope);
			if (theta < 0)
				theta = -theta;
			double hl = ((height * Math.sin(theta)) + (width * Math.cos(theta))) / 2.0;
			double dx = (hl * Math.cos(theta));
			double dy = (hl * Math.sin(theta));
			
			boolean swap = target.x < 0 ^ target.y < 0;

			if (swap)
			{
				out.pointA.x = ox - dx;
				out.pointA.y = oy + dy;
				out.pointB.x = ox + dx;
				out.pointB.y = oy - dy;
			}
			else
			{
				out.pointA.x = ox - dx;
				out.pointA.y = oy - dy;
				out.pointB.x = ox + dx;
				out.pointB.y = oy + dy;
			}
		}
		else
		{
			out.pointA.x = ox;
			out.pointA.y = oy - (height / 2.0);
			out.pointB.x = ox;
			out.pointB.y = oy + (height / 2.0);
		}
		
		out.pointA.projectOnto(target);
		out.pointB.projectOnto(target);
	}
	
	@Override
	public String toString()
	{
		return String.format("R[X%f, Y%f, W%f, H%f]", x, y, width, height);
	}
}
