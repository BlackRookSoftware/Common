/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Abstract, three-dimensional set of coordinates.
 * @author Matthew Tropiano
 */
public abstract class Tuple3F extends Tuple2F
{
	/** Z-coordinate value. */
	public float z;

	/**
	 * Creates a copy of another Vect3D.
	 * @param t the source tuple.
	 */
	public Tuple3F(Tuple3F t)
	{
		this(t.x,t.y,t.z);
	}

	/**
	 * Creates a new three-dimensional tuple, from a Tuple2F.
	 * The missing dimensions are filled with zeroes.
	 * @param t the source tuple.
	 */
	public Tuple3F(Tuple2F t)
	{
		this(t.x,t.y,0);
	}

	/**
	 * Creates a new three-dimensional tuple, from a Tuple1F.
	 * The missing dimensions are filled with zeroes.
	 * @param t the source tuple.
	 */
	public Tuple3F(Tuple1F t)
	{
		this(t.x,0,0);
	}

	/**
	 * Creates a new three-dimensional tuple, with (0,0,0) as its value. 
	 */
	public Tuple3F()
	{
		this(0,0,0);
	}

	/**
	 * Creates a new three-dimensional tuple. 
	 * @param x		the initial x-coordinate value of this tuple.
	 * @param y		the initial y-coordinate value of this tuple.
	 * @param z		the initial z-coordinate value of this tuple.
	 */
	public Tuple3F(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Sets the values of this tuple.
	 * @param x		the x value.
	 * @param y		the y value.
	 * @param z		the z value.
	 */
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Sets the values of this tuple.
	 * @param t the source tuple.
	 */
	public void set(Tuple3F t)
	{
		x = t.x;
		y = t.y;
		z = t.z;
	}

	@Override
	public float length()
	{
		return (float)RMath.getVectorLength(x, y, z);
	}

	@Override
	public float squareLength()
	{
		return (float)RMath.getVectorLengthSquared(x, y, z);
	}

	/**
	 * Yields the dot product of a Tuple3D with this one.
	 * @param v the tuple to use with this one.
	 * @return the dot product.
	 */
	public float dot(Tuple3F v)
	{
		return (float)RMath.getVectorDotProduct(x, y, z, v.x, v.y, v.z);
	}

	@Override
	public boolean isZero()
	{
		return x == 0 && y == 0 && z == 0;
	}

	/**
	 * Projects this tuple onto another tuple. This tuple's data is replaced by the result.
	 * @param v	the target tuple to project this tuple onto.
	 */
	public void projectOnto(Tuple3F v)
	{
		projectOnto(this,v,this);
	}

	/**
	 * Adds another Tuple2D to this one. This tuple's data is replaced by the result.
	 * @param v the tuple to add to this one.
	 */
	public void add(Tuple3F v)
	{
		add(this,v,this);
	}

	@Override
	public void setLength(float len)
	{
		setLength(this,len,this);
	}

	@Override
	public void negate()
	{
		negate(this,this);
	}

	@Override
	public void normalize()
	{
		normalize(this,this);
	}

	@Override
	public void scale(float s)
	{
		scale(s,this,this);
	}

	@Override
	public void rotateX(float radians)
	{
		rotateX(radians,this,this);
	}

	@Override
	public void rotateY(float radians)
	{
		rotateY(radians,this,this);
	}

	@Override
	public void rotateZ(float radians)
	{
		rotateZ(radians,this,this);
	}

	/**
	 * Scales this tuple.
	 * @param sx scalar factor for x-axis. 
	 * @param sy scalar factor for y-axis. 
	 * @param sz scalar factor for z-axis. 
	 */
	public void scale(float sx, float sy, float sz)
	{
		scale(sx,sy,sz,this,this);
	}

	/**
	 * Crosses this tuple with another (cross product).
	 * @param v the input tuple.
	 */
	public void cross(Tuple3F v)
	{
		cross(this,v,this);
	}

	@Override
	public String toString()
	{
		return "["+x+", "+y+", "+z+"]";
	}

	/**
	 * Returns true if the target has the same component values as this one. 
	 * @param v the other tuple.
	 * @return true if so, false if not.
	 */
	public boolean equals(Tuple3F v)
	{
		return x == v.x && y == v.y && z == v.z;
	}

	/**
	 * Gets the distance in units from this tuple to another.
	 * @param point the other tuple.
	 * @return the distance.
	 */
	public float getDistanceTo(Tuple3F point)
	{
		return (float)Math.sqrt(getSquareDistanceTo(point));
	}

	/**
	 * Gets the square distance in units from this tuple to another (no square root step at the end).
	 * @param point the other tuple.
	 * @return the squared distance.
	 */
	public float getSquareDistanceTo(Tuple3F point)
	{
		float dx = point.x - x;
		float dy = point.y - y;
		float dz = point.z - z;
		return dx*dx + dy*dy + dz*dz;
	}

	/**
	 * Projects this tuple onto another tuple.
	 * @param v			the source tuple.
	 * @param target	the target tuple.
	 * @param out		the output tuple.
	 */
	public static void projectOnto(Tuple3F v, Tuple3F target, Tuple3F out)
	{
		float dotp = v.dot(target);
		float fact = target.x*target.x + target.y*target.y + target.z*target.z;
		float dpofact = dotp/fact;
		out.x = dpofact * target.x;
		out.y = dpofact * target.y;
		out.z = dpofact * target.z;
	}

	/**
	 * Adds two tuples and puts the result into a third.
	 * @param v1	the first tuple.
	 * @param v2	the second tuple.
	 * @param out	the output tuple.
	 */
	public static void add(Tuple3F v1, Tuple3F v2, Tuple3F out)
	{
		out.x = v1.x + v2.x;
		out.y = v1.y + v2.y;
		out.z = v1.z + v2.z;
	}

	/**
	 * Turns this tuple into a unit tuple of length 1, while keeping direction intact.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void normalize(Tuple3F in, Tuple3F out)
	{
		float len = in.length();
		out.x = in.x / len;
		out.y = in.y / len;
		out.z = in.z / len;
	}

	/**
	 * Scales a tuple to a certain length.
	 * @param in	the input tuple.
	 * @param len	the new length of the tuple.
	 * @param out	the output tuple.
	 */
	public static void setLength(Tuple3F in, float len, Tuple3F out)
	{
		out.x = in.x;
		out.y = in.y;
		out.z = in.z;
		out.normalize();
		if (len != 1)
			out.scale(len);
	}

	/**
	 * Negates a tuple. Puts result in the output tuple.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void negate(Tuple3F in, Tuple3F out)
	{
		out.x = -in.x;
		out.y = -in.y;
		out.z = -in.z;
	}

	/**
	 * Scales this tuple on all axes.
	 * @param s		scalar factor. 
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void scale(float s, Tuple3F in, Tuple3F out)
	{
		out.x = in.x*s;
		out.y = in.y*s;
		out.z = in.z*s;
	}

	/**	
	 * Scales this tuple.
	 * @param sx	scalar factor for x-axis. 
	 * @param sy	scalar factor for y-axis. 
	 * @param sz	scalar factor for z-axis. 
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void scale(float sx, float sy, float sz, Tuple3F in, Tuple3F out)
	{
		out.x = in.x*sx;
		out.y = in.y*sy;
		out.z = in.z*sz;
	}

	/**
	 * Crosses this tuple with another (cross product).
	 * @param v1	the first tuple.
	 * @param v2	the second tuple.
	 * @param out	the output tuple.
	 */
	public static void cross(Tuple3F v1, Tuple3F v2, Tuple3F out)
	{
		float newx = v1.y*v2.z - v1.z*v2.y;
		float newy = v1.z*v2.x - v1.x*v2.z;
		float newz = v1.x*v2.y - v1.y*v2.x;
		out.x = newx;
		out.y = newy;
		out.z = newz;
	}

	/**
	 * Rotates this tuple around the X axis.
	 * @param radians	 the angle in radians to rotate.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void rotateX(float radians, Tuple3F in, Tuple3F out)
	{
		float newy = (float)(in.y*Math.cos(radians)-in.z*Math.sin(radians));
		float newz = (float)(in.y*Math.sin(radians)+in.z*Math.cos(radians));
		out.x = in.x;
		out.y = newy;
		out.z = newz;
	}

	/**
	 * Rotates this tuple around the Y axis. 
	 * @param radians	 the angle in radians to rotate.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void rotateY(float radians, Tuple3F in, Tuple3F out)
	{
		float newx = (float)(in.x*Math.cos(radians)+in.z*Math.sin(radians));
		float newz = (float)(-in.x*Math.sin(radians)+in.z*Math.cos(radians));
		out.x = newx;
		out.y = in.y;
		out.z = newz;
	}

	/**
	 * Rotates this tuple around the Z axis. 
	 * @param radians	 the angle in radians to rotate.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void rotateZ(float radians, Tuple3F in, Tuple3F out)
	{
		float newx = (float)(in.x*Math.cos(radians)-in.y*Math.sin(radians));
		float newy = (float)(in.x*Math.sin(radians)+in.y*Math.cos(radians));
		out.x = newx;
		out.y = newy;
		out.z = in.z;
	}

}
