/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Abstract, two-dimensional set of coordinates.
 * @author Matthew Tropiano
 */
public abstract class Tuple2F extends Tuple1F
{
	/** Y-coordinate value. */
	public float y;

	/**
	 * Creates a new two-dimensional point, with (0, 0) as its value. 
	 */
	public Tuple2F()
	{
		this(0, 0);
	}

	/**
	 * Creates a copy of another Tuple2F.
	 * @param v the source tuple.
	 */
	public Tuple2F(Tuple2F v)
	{
		this(v.x, v.y);
	}

	/**
	 * Creates a new two-dimensional point, from a Tuple1F.
	 * The missing dimensions are filled with zeroes.
	 * @param v the source tuple.
	 */
	public Tuple2F(Tuple1F v)
	{
		this(v.x, 0);
	}

	/**
	 * Creates a new two-dimensional tuple. 
	 * @param x the initial x-coordinate value of this tuple.
	 * @param y the initial y-coordinate value of this tuple.
	 */
	public Tuple2F(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the values of this tuple.
	 * @param x the x value.
	 * @param y the y value.
	 */
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the values of this tuple.
	 * @param t the source tuple.
	 */
	public void set(Tuple2F t)
	{
		x = t.x;
		y = t.y;
	}

	@Override
	public float length()
	{
		return (float)RMath.getVectorLength(x, y);
	}

	/**
	 * @return the squared length of this tuple (no square root step at the end).
	 */
	public float squareLength()
	{
		return (float)RMath.getVectorLengthSquared(x, y);
	}

	/**
	 * Yields the dot product of a Tuple2D with this one.
	 * @param v the tuple to use with this one.
	 * @return the dot product.
	 */
	public float dot(Tuple2F v)
	{
		return (float)RMath.getVectorDotProduct(x, y, v.x, v.y);
	}

	@Override
	public boolean isZero()
	{
		return x == 0 && y == 0;
	}

	/**
	 * Projects this tuple onto another tuple. This tuple's data is replaced by the result.
	 * @param v	the target tuple to project this tuple onto.
	 */
	public void projectOnto(Tuple2F v)
	{
		projectOnto(this,v,this);
	}

	/**
	 * Adds another Tuple2D to this one. This tuple's data is replaced by the result.
	 * @param v	the tuple to add to this one.
	 */
	public void add(Tuple2F v)
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

	/**
	 * Scales this tuple.
	 * @param sx scalar factor for x-axis. 
	 * @param sy scalar factor for y-axis. 
	 */
	public void scale(float sx, float sy)
	{
		scale(sx,sy,this,this);
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
	 * Turns this tuple into its left-hand normal.
	 */
	public void leftNormal()
	{
		leftNormal(this, this);
	}

	/**
	 * Turns this tuple into its right-hand normal.
	 */
	public void rightNormal()
	{
		rightNormal(this, this);
	}

	@Override
	public String toString()
	{
		return "["+x+", "+y+"]";
	}

	/**
	 * Compares tuple components.
	 * @param p the other tuple.
	 * @return a positive number if greater, negative if less, zero if equal.
	 */
	public float compareTo(Tuple2F p)
	{
		return (x - p.x) + (y - p.y);
	}

	/**
	 * Returns true if the target has the same component values as this one. 
	 * @param v the other tuple.
	 * @return true if so, false if not.
	 */
	public boolean equals(Tuple2F v)
	{
		return x == v.x && y == v.y;
	}

	@Override
	public int hashCode()
	{
		return Float.hashCode(x) + Float.hashCode(y);
	}

	/**
	 * Gets the distance in units from this tuple to another.
	 * @param tuple the other tuple.
	 * @return the distance.
	 */
	public float getDistanceTo(Tuple2F tuple)
	{
		return (float)Math.sqrt(getSquareDistanceTo(tuple));
	}

	/**
	 * Gets the square distance in units from this tuple to another (no square root step at the end).
	 * @param tuple the other tuple.
	 * @return the squared distance.
	 */
	public float getSquareDistanceTo(Tuple2F tuple)
	{
		float dx = tuple.x - x;
		float dy = tuple.y - y;
		return dx*dx + dy*dy;
	}

	/**
	 * Projects a tuple onto another tuple.
	 * @param source the source tuple.
	 * @param target the target tuple.
	 * @param out the output tuple.
	 */
	public static void projectOnto(Tuple2F source, Tuple2F target, Tuple2F out)
	{
		float dotp = source.dot(target);
		float tx = target.x;
		float ty = target.y;
		float fact = tx*tx + ty*ty;
		float dpofact = dotp/fact;
		out.x = dpofact * tx;
		out.y = dpofact * ty;
	}

	/**
	 * Adds two tuples and puts the result into a third.
	 * @param v1 the first tuple.
	 * @param v2 the second tuple.
	 * @param out the output tuple.
	 */
	public static void add(Tuple2F v1, Tuple2F v2, Tuple2F out)
	{
		out.x = v1.x + v2.x;
		out.y = v1.y + v2.y;
	}

	/**
	 * Scales a tuple to a certain length.
	 * @param in the input tuple.
	 * @param len the new length of the tuple.
	 * @param out the output tuple.
	 */
	public static void setLength(Tuple2F in, float len, Tuple2F out)
	{
		out.x = in.x;
		out.y = in.y;
		out.normalize();
		if (len != 1)
			out.scale(len);
	}

	/**
	 * Negates a tuple. Puts result in the output tuple.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void negate(Tuple2F in, Tuple2F out)
	{
		out.x = -in.x;
		out.y = -in.y;
	}

	/**
	 * Turns this tuple into a unit tuple of length 1, while keeping direction intact.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void normalize(Tuple2F in, Tuple2F out)
	{
		float len = in.length();
		out.x = in.x / len;
		out.y = in.y / len;
	}

	/**
	 * Scales this tuple on all axes.
	 * @param s scalar factor. 
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void scale(float s, Tuple2F in, Tuple2F out)
	{
		out.x = in.x*s;
		out.y = in.y*s;
	}

	/**	
	 * Scales this tuple.
	 * @param sx scalar factor for x-axis. 
	 * @param sy scalar factor for y-axis. 
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void scale(float sx, float sy, Tuple2F in, Tuple2F out)
	{
		out.x = in.x*sx;
		out.y = in.y*sy;
	}

	/**
	 * Gets the left-hand normal of this tuple.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void leftNormal(Tuple2F in, Tuple2F out)
	{
		float newx = -in.y;
		float newy = in.x;
		out.x = newx;
		out.y = newy;
	}

	/**
	 * Gets the right-hand normal of this tuple.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void rightNormal(Tuple2F in, Tuple2F out)
	{
		float newx = in.y;
		float newy = -in.x;
		out.x = newx;
		out.y = newy;
	}

	/**
	 * Rotates this tuple around the X axis.
	 * @param radians the angle in radians to rotate.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void rotateX(float radians, Tuple2F in, Tuple2F out)
	{
		out.x = in.x;
		out.y = (float)(in.y*Math.cos(radians));
	}

	/**
	 * Rotates this tuple around the Y axis. 
	 * @param radians the angle in radians to rotate.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void rotateY(float radians, Tuple2F in, Tuple2F out)
	{
		out.x = (float)(in.x*Math.cos(radians));
		out.y = in.y;
	}

	/**
	 * Rotates this tuple around the Z axis. 
	 * @param radians the angle in radians to rotate.
	 * @param in the input tuple.
	 * @param out the output tuple.
	 */
	public static void rotateZ(float radians, Tuple2F in, Tuple2F out)
	{
		float newx = (float)(in.x*Math.cos(radians)-in.y*Math.sin(radians));
		float newy = (float)(in.x*Math.sin(radians)+in.y*Math.cos(radians));
		out.x = newx;
		out.y = newy;
	}

}
