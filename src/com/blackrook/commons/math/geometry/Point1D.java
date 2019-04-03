/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple1D;

/**
 * A one dimensional point in space.  
 * @author Matthew Tropiano
 */
public class Point1D extends Tuple1D implements PointD
{
	/**
	 * Creates a new one-dimensional point, with 0 as its value. 
	 */
	public Point1D()
	{
		super(0);
	}
	
	/**
	 * Creates a copy of another Point1D.
	 * @param p the source point.
	 */
	public Point1D(Point1D p)
	{
		super(p.x);
	}

	/**
	 * Creates a new one-dimensional point. 
	 * @param x the initial x-coordinate value of this point.
	 */
	public Point1D(double x)
	{
		super(x);
	}
	
	/**
	 * Gets the closest point in a list to this one.
	 * If the list is empty, null is returned.
	 * @param points the list of points.
	 * @return the closest point or null if no points provided.
	 */
	public Point1D getClosestPoint(Point1D ... points)
	{
		if (points.length == 0)
			return null;
		else if (points.length == 1)
			return points[0];
		else 
		{
			double len = Double.MAX_VALUE; 
			double next = 0; 
			Point1D out = null;
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
