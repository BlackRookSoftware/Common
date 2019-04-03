/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple3D;

/**
 * A three dimensional point in space.  
 * @author Matthew Tropiano
 */
public class Point3D extends Tuple3D implements PointD
{
	/**
	 * Creates a new three-dimensional point, from a Point3D.
	 * The missing dimensions are filled with zeroes.
	 * @param p the source point.
	 */
	public Point3D(Point3D p)
	{
		super(p.x,p.y,p.z);
	}
	
	/**
	 * Creates a new three-dimensional point, from a Point2D.
	 * The missing dimensions are filled with zeroes.
	 * @param p the source point.
	 */
	public Point3D(Point2D p)
	{
		super(p.x,p.y,0);
	}
	
	/**
	 * Creates a new three-dimensional point, from a Point1D.
	 * The missing dimensions are filled with zeroes.
	 * @param p the source point.
	 */
	public Point3D(Point1D p)
	{
		super(p.x,0,0);
	}
	
	/**
	 * Creates a new three-dimensional point, with (0,0,0) as its value. 
	 */
	public Point3D()
	{
		super(0,0,0);
	}
	
	/**
	 * Creates a new three-dimensional point. 
	 * @param x	the initial x-coordinate value of this point.
	 * @param y	the initial y-coordinate value of this point.
	 * @param z	the initial z-coordinate value of this point.
	 */
	public Point3D(double x, double y, double z)
	{
		super(x,y,z);
	}

	/**
	 * Gets the closest point in a list to this one.
	 * If the list is empty, null is returned.
	 * @param points the list of points.
	 * @return the closest point or null if no points provided.
	 */
	public Point3D getClosestPoint(Point3D ... points)
	{
		if (points.length == 0)
			return null;
		else if (points.length == 1)
			return points[0];
		else 
		{
			double len = Double.MAX_VALUE; 
			double next = 0; 
			Point3D out = null;
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
