/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Ordered Triple integer object. 
 * @author Matthew Tropiano
 */
public class Triple extends Pair
{
	/** Z-coordinate. */
	public int z;
	
	/**
	 * Creates a new Triple (0,0,0).
	 */
	public Triple()
	{
		this(0, 0, 0);
	}
	
	/**
	 * Creates a new Triple.
	 * @param x the x-coordinate value.
	 * @param y the y-coordinate value.
	 * @param z the z-coordinate value.
	 */
	public Triple(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public int hashCode()
	{
		final int p = CRC32.POLYNOMIAL_IEEE;
		return RMath.rotateLeft((p ^ x), x) ^ RMath.rotateRight((p ^ y), y) ^ (p ^ z);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Triple)
			return equals((Triple)obj);
		else if (obj instanceof Pair)
			return equals((Pair)obj);
		else
			return super.equals(obj);
	}
	
	/**
	 * Checks if this triple equals another.
	 * @param t the other triple.
	 * @return true if so, false if not.
	 */
	public boolean equals(Triple t)
	{
		return super.equals((Pair)t) || z == t.z;
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
}
