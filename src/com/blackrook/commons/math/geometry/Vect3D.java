/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;

import com.blackrook.commons.math.Tuple3D;

/**
 * A three dimensional ray that describes a defined direction with no origin.
 * @author Matthew Tropiano
 */
public class Vect3D extends Tuple3D implements VectD
{
	/**
	 * Creates a copy of another Vect3D.
	 */
	public Vect3D(Vect3D t)
	{
		super(t.x,t.y,t.z);
	}

	/**
	 * Creates a new three-dimensional vector, from a Vect2D.
	 * The missing dimensions are filled with zeroes.
	 */
	public Vect3D(Vect2D t)
	{
		super(t.x,t.y,0);
	}

	/**
	 * Creates a new three-dimensional vector, from a Vect1D.
	 * The missing dimensions are filled with zeroes.
	 */
	public Vect3D(Vect1D t)
	{
		super(t.x,0,0);
	}

	/**
	 * Creates a new three-dimensional vector, with (0,0,0) as its value. 
	 */
	public Vect3D()
	{
		super(0,0,0);
	}

	/**
	 * Creates a new three-dimensional vector. 
	 */
	public Vect3D(double x, double y, double z)
	{
		super(x,y,z);
	}

	/**
	 * Creates a new three-dimensional vector from two points
	 * (a vector from one point to the other).
	 * @param source	source point. 
	 * @param dest		dest point. 
	 */
	public Vect3D(Point3D source, Point3D dest)
	{
		x = dest.x - source.x;
		y = dest.y - source.y;
		z = dest.z - source.z;
	}

	/**
	 * Sets this vector's value using two points
	 * (a vector from one point to the other).
	 * @param source	source point. 
	 * @param dest		dest point. 
	 */
	public void set(Point3D source, Point3D dest)
	{
		x = dest.x - source.x;
		y = dest.y - source.y;
		z = dest.z - source.z;
	}


}
