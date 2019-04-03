/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Abstract, one-dimensional set of coordinates.
 * @author Matthew Tropiano
 */
public abstract class Tuple1F implements TupleF
{
	/** X-coordinate value. */
	public float x;

	/**
	 * Creates a new one-dimensional tuple, with 0 as its value. 
	 */
	protected Tuple1F()
	{
		this(0);
	}
	
	/**
	 * Creates a new one-dimensional tuple. 
	 * @param x	the initial x-coordinate value of this tuple.
	 */
	protected Tuple1F(float x)
	{
		set(x);
	}

	/**
	 * Sets the values of this tuple using another.
	 * @param t the source tuple.
	 */
	public void set(Tuple1F t)
	{
		x = t.x;
	}

	/**
	 * Sets the values of this tuple.
	 * @param x the new x value.
	 */
	public void set(float x)
	{
		this.x = x;
	}

	/**
	 * Compares tuple components.
	 * @param t the other tuple.
	 * @return a positive number if greater, negative if less, zero if equal.
	 */
	public float compareTo(Tuple1F t)
	{
		return x - t.x;
	}

	@Override
	public int hashCode()
	{
		return Float.hashCode(x);
	}

	/**
	 * Returns true if the target has the same component values as this one. 
	 * @param t the other tuple.
	 * @return true if so, false if not.
	 */
	public boolean equals(Tuple1F t)
	{
		return x == t.x;
	}

	@Override
	public float length()
	{
		return x < 0 ? -x : x;
	}

	/**
	 * Yields the dot product of a Vect1D with this one.
	 * @param v the vector to use with this one.
	 * @return the dot product.
	 */
	public float dot(Tuple1F v)
	{
		return x*v.x;
	}

	/**
	 * @return true if this tuple is zero, false otherwise.
	 */
	public boolean isZero()
	{
		return x == 0;
	}

	/**
	 * Gets the distance in units from this tuple to another.
	 * @param tuple the other tuple.
	 * @return the distance.
	 */
	public float getDistanceTo(Tuple1F tuple)
	{
		return Math.abs(tuple.x - x);
	}

	/**
	 * Adds another Tuple1D to this one. This tuple's data is replaced by the result.
	 * @param t the tuple to add to this one.
	 */
	public void add(Tuple1F t)
	{
		add(this,t,this);
	}

	@Override
	public void normalize()
	{
		normalize(this,this);
	}

	@Override
	public void setLength(float len)
	{
		setLength(this,(float)len,this);
	}

	@Override
	public void negate()
	{
		negate(this,this);
	}

	@Override
	public void scale(float sx)
	{
		scale((float)sx,this,this);
	}

	@Override
	public void rotateX(float radians)
	{
		rotateX((float)radians,this,this);
	}

	@Override
	public void rotateY(float radians)
	{
		rotateY((float)radians,this,this);
	}

	@Override
	public void rotateZ(float radians)
	{
		rotateZ((float)radians,this,this);
	}

	@Override
	public String toString()
	{
		return "["+x+"]";
	}

	/**
	 * Adds two tuples and puts the result into a third.
	 * @param t1	the first tuple.
	 * @param t2	the second tuple.
	 * @param out	the output tuple.
	 */
	public static void add(Tuple1F t1, Tuple1F t2, Tuple1F out)
	{
		out.x = t1.x + t2.x;
	}

	/**
	 * Turns this tuple into a unit tuple of length 1, while keeping direction intact.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void normalize(Tuple1F in, Tuple1F out)
	{
		out.x = 1 * (in.x < 0 ? -1 : 1);
	}

	/**
	 * Scales a tuple to a certain length.
	 * @param in	the input tuple.
	 * @param len	the new length of the tuple.
	 * @param out	the output tuple.
	 */
	public static void setLength(Tuple1F in, float len, Tuple1F out)
	{
		out.x = in.x;
		out.normalize();
		if (len != 1)
			out.scale(len);
	}

	/**
	 * Negates a tuple. Puts result in the output tuple.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void negate(Tuple1F in, Tuple1F out)
	{
		out.x = -in.x;
	}

	/**
	 * Scales this tuple.
	 * @param sx	scalar factor for x-axis. 
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void scale(float sx, Tuple1F in, Tuple1F out)
	{
		out.x = in.x*sx;
	}

	/**
	 * Rotates this tuple around the X axis.
	 * @param radians	 the angle in radians to rotate.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void rotateX(float radians, Tuple1F in, Tuple1F out)
	{
		// do nothing.
	}

	/**
	 * Rotates this tuple around the Y axis. 
	 * @param radians	 the angle in radians to rotate.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void rotateY(float radians, Tuple1F in, Tuple1F out)
	{
		out.x = (float)(in.x*Math.cos(radians));
	}

	/**
	 * Rotates this tuple around the Z axis. 
	 * @param radians	 the angle in radians to rotate.
	 * @param in	the input tuple.
	 * @param out	the output tuple.
	 */
	public static void rotateZ(float radians, Tuple1F in, Tuple1F out)
	{
		out.x = (float)(in.x*Math.cos(radians));
	}

}
