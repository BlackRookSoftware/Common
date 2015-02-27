/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple2F;

/**
 * Object that describes a rectangular area (single-precision coordinates).
 * @author Matthew Tropiano
 */
public class Rectangle2F
{
	/** Starting X-coordinate. */
	public float x;
	/** Starting Y-coordinate. */
	public float y;
	/** Rectangle width. */
	public float width;
	/** Rectangle height. */
	public float height;
	
	/**
	 * Creates a rectangle at (0,0) with width and height of 0.
	 */
	public Rectangle2F()
	{
		this(0, 0, 0, 0);
	}
	
	/**
	 * Creates a copy of a rectangle.
	 * @param r the source rectangle to copy.
	 */
	public Rectangle2F(Rectangle2F r)
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
	public Rectangle2F(float x, float y, float width, float height)
	{
		set(x, y, width, height);
	}

	/**
	 * Sets the values in this rectangle using another rectangle.
	 * @param r the other rectangle.
	 * @since 2.8.0
	 */
	public void set(Rectangle2F r)
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
	public void set(float x, float y, float width, float height)
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
	public void projectOnto(Tuple2F target, Line2F out)
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
				out.pointA.x = (float)(ox - dx);
				out.pointA.y = (float)(oy + dy);
				out.pointB.x = (float)(ox + dx);
				out.pointB.y = (float)(oy - dy);
			}
			else
			{
				out.pointA.x = (float)(ox - dx);
				out.pointA.y = (float)(oy - dy);
				out.pointB.x = (float)(ox + dx);
				out.pointB.y = (float)(oy + dy);
			}
		}
		else
		{
			out.pointA.x = (float)ox;
			out.pointA.y = (float)(oy - (height / 2.0));
			out.pointB.x = (float)ox;
			out.pointB.y = (float)(oy + (height / 2.0));
		}
	}

	@Override
	public String toString()
	{
		return String.format("R[X%f, Y%f, W%f, H%f]", x, y, width, height);
	}
	
}
