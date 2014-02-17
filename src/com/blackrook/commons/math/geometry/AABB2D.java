/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import java.awt.Rectangle;

import com.blackrook.commons.math.Tuple2D;

/**
 * Object that describes an origin-less axis-aligned bounding box.
 * @author Matthew Tropiano
 */
public class AABB2D
{
	/** Rectangle half-width. */
	public double halfWidth;
	/** Rectangle half-height. */
	public double halfHeight;
	
	/**
	 * Creates a box with.
	 */
	public AABB2D()
	{
		this(0, 0);
	}
	
	/**
	 * Creates a two-dimensional AABB using a rectangle.
	 * @param r the source rectangle to copy.
	 */
	public AABB2D(Rectangle r)
	{
		this(r.width / 2, r.height / 2);
	}
	
	/**
	 * Creates a new two-dimensional AABB.
	 * @param halfWidth box half-width.
	 * @param halfHeight box half-height.
	 */
	public AABB2D(double halfWidth, double halfHeight)
	{
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
	}
	
	/**
	 * Projects this bounding box onto a line, <code>out</code>.
	 * @param target the target tuple, treated as a vector.
	 * @param out the output line.
	 * @since 2.10.0
	 */
	public void projectOnto(Tuple2D target, Line2D out)
	{
		if (target.x != 0.0)
		{
			double slope = target.y / target.x;
			double theta = Math.atan(slope);
			if (theta < 0)
				theta = -theta;
			double hl = (halfHeight * Math.sin(theta)) + (halfWidth * Math.cos(theta));
			double dx = (hl * Math.cos(theta));
			double dy = (hl * Math.sin(theta));
			
			boolean swap = target.x < 0 ^ target.y < 0;

			if (swap)
			{
				out.pointA.x = -dx;
				out.pointA.y = dy;
				out.pointB.x = dx;
				out.pointB.y = -dy;
			}
			else
			{
				out.pointA.x = -dx;
				out.pointA.y = -dy;
				out.pointB.x = dx;
				out.pointB.y = dy;
			}
		}
		else
		{
			out.pointA.x = 0.0;
			out.pointA.y = -halfHeight;
			out.pointB.x = 0.0;
			out.pointB.y = halfHeight;
		}
	}

}
