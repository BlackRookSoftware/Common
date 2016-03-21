/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple2D;

/**
 * A two dimensional point in space.  
 * @author Matthew Tropiano
 */
public class Point2D extends Tuple2D implements PointD
{
	/**
	 * Creates a copy of a Point2D.
	 * @param p the source point.
	 */
	public Point2D(Point2D p)
	{
		super(p.x,p.y);
	}
	
	/**
	 * Creates a new two-dimensional point, from a Point1D.
	 * The missing dimensions are filled with zeroes.
	 * @param p the source point.
	 */
	public Point2D(Point1D p)
	{
		super(p.x,0);
	}
	
	/**
	 * Creates a new two-dimensional point, with (0,0) as its value. 
	 */
	public Point2D()
	{
		super(0,0);
	}
	
	/**
	 * Creates a new two-dimensional point. 
	 * @param x	the initial x-coordinate value of this point.
	 * @param y	the initial y-coordinate value of this point.
	 */
	public Point2D(double x, double y)
	{
		super(x,y);
	}

	/**
	 * Gets the closest point in a list to this one.
	 * If the list is empty, null is returned.
	 * @param points the list of points.
	 * @return the closest point or null if no points provided.
	 */
	public Point2D getClosestPoint(Point2D ... points)
	{
		if (points.length == 0)
			return null;
		else if (points.length == 1)
			return points[0];
		else 
		{
			double len = Double.MAX_VALUE; 
			double next = 0; 
			Point2D out = null;
			for (int i = 0; i < points.length; i++)
			{
				next = getDistanceTo(points[i]);
				if (next < len)
				{
					out = points[i];
					len = next;
				}
			}
			return out;
		}
	}

}
