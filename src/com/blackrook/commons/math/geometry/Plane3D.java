/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * A three-dimensional plane.
 * @author Matthew Tropiano
 */
public class Plane3D extends PlaneD<Vect3D>
{
	/**
	 * Creates a plane with a specific normal and distance from the origin. 
	 * @param normal	the surface normal (if the vector is not unit length, it will be normalized).
	 * @param distance	the distance of the plane from the origin.
	 */
	public Plane3D(Vect3D normal, double distance)
	{
		this.normal = normal;
		this.normal.normalize();
		this.distance = distance;
	}

	/**
	 * Creates a plane using three non-collinear points.
	 * It will compute the surface normal and distance based on the triangle created by these points.  
	 * @param p1	the first point on the plane.
	 * @param p2	the second point on the plane.
	 * @param p3	the third point on the plane.
	 */
	public Plane3D(Point3D p1, Point3D p2, Point3D p3)
	{
		normal = new Vect3D();
		Vect3D.cross(new Vect3D(p1,p2), new Vect3D(p1,p3), normal);
		normal.normalize();
		distance = normal.dot(p1);
	}
	
	/**
	 * Creates a plane using another plane (copy).
	 * @param plane	the plane to use.
	 */
	public Plane3D(Plane3D plane)
	{
		this(plane.normal, plane.distance);
	}

	@Override
	public Plane3D copy()
	{
		return new Plane3D(this);
	}

	
}
