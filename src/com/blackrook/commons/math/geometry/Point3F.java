/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple3F;

/**
 * A three dimensional point in space.  
 * @author Matthew Tropiano
 */
public class Point3F extends Tuple3F implements PointF
{
	/**
	 * Creates a new three-dimensional point, from a Point3D.
	 * The missing dimensions are filled with zeroes.
	 */
	public Point3F(Point3F p)
	{
		super(p.x,p.y,p.z);
	}
	
	/**
	 * Creates a new three-dimensional point, from a Point2D.
	 * The missing dimensions are filled with zeroes.
	 */
	public Point3F(Point2F p)
	{
		super(p.x,p.y,0);
	}
	
	/**
	 * Creates a new three-dimensional point, from a Point1D.
	 * The missing dimensions are filled with zeroes.
	 */
	public Point3F(Point1F p)
	{
		super(p.x,0,0);
	}
	
	/**
	 * Creates a new three-dimensional point, with (0,0,0) as its value. 
	 */
	public Point3F()
	{
		super(0,0,0);
	}
	
	/**
	 * Creates a new three-dimensional point. 
	 * @param x		the initial x-coordinate value of this point.
	 * @param y		the initial y-coordinate value of this point.
	 * @param z		the initial z-coordinate value of this point.
	 */
	public Point3F(float x, float y, float z)
	{
		super(x,y,z);
	}

	/**
	 * Returns the closest point in a list to this one.
	 * If the list is empty, null is returned.
	 */
	public Point3F getClosestPoint(Point3F ... points)
	{
		if (points.length == 0)
			return null;
		else if (points.length == 1)
			return points[0];
		else 
		{
			float len = Float.MAX_VALUE; 
			float next = 0; 
			Point3F out = null;
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
