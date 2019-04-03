/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * A one-dimensional hyperplane.
 * @author Matthew Tropiano
 */
public class Plane1F extends PlaneF<Vect1F>
{
	/**
	 * Creates a plane with a specific normal and distance from the origin. 
	 * @param normal	the surface normal (if the vector is not unit length, it will be normalized).
	 * @param distance	the distance of the plane from the origin.
	 */
	public Plane1F(Vect1F normal, float distance)
	{
		this.normal = new Vect1F(normal);
		this.normal.normalize();
		this.distance = distance;
	}

	/**
	 * Creates a plane using an origin point.
	 * It will compute the surface normal and distance based on this point.  
	 * @param origin		a point on the plane.
	 */
	public Plane1F(Point1F origin)
	{
		this(new Vect1F(origin.x),origin.length());
	}
	
	/**
	 * Creates a plane using another plane (copy).
	 * @param plane	the plane to use.
	 */
	public Plane1F(Plane1F plane)
	{
		this(plane.normal, plane.distance);
	}

	@Override
	public Plane1F copy()
	{
		return new Plane1F(this);
	}
}
