/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.geometry;


/**
 * Data that represents a plane, consisting of a normal vector
 * and distance from the origin. 
 * @author Matthew Tropiano
 */
public abstract class PlaneD<T extends VectD>
{
	/** Normal vector. */
	public T normal;
	/** Distance from the origin. */
	public double distance;
	
	/**
	 * @return this plane's normal vector.
	 */
	public T getNormal()
	{
		return normal;
	}
	
	/**
	 * @return this plane's distance from the origin.
	 */
	public double getDistance()
	{
		return distance;
	}
	
	/**
	 * Sets this plane's distance from the origin.
	 * @param d the new distance.
	 */
	public void setDistance(double d)
	{
		distance = d;
	}
	
	/**
	 * Rotates this plane around the X axis.
	 * Basically just rotates the normal (shhh! don't tell!).
	 * @param radians	 the angle in radians to rotate.
	 */
	public void rotateX(double radians)
	{
		normal.rotateX(radians);
	}

	/**
	 * Rotates this plane around the Y axis.
	 * Basically just rotates the normal (shhh! don't tell!).
	 * @param radians	 the angle in radians to rotate.
	 */
	public void rotateY(double radians)
	{
		normal.rotateY(radians);
	}

	/**
	 * Rotates this plane around the Z axis.
	 * Basically just rotates the normal (shhh! don't tell!).
	 * @param radians	 the angle in radians to rotate.
	 */
	public void rotateZ(double radians)
	{
		normal.rotateZ(radians);
	}

	/**
	 * Scales this plane's distance from the origin.
	 * @param s		scalar factor. 
	 */
	public void scale(double s)
	{
		distance *= s;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(normal.toString());
		sb.append(' ');
		sb.append(distance);
		return sb.toString();
	}
	
	/** @return an exact copy of this Plane. */
	public abstract PlaneD<T> copy();
	
}
