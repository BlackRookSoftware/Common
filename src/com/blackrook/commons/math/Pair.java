/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Ordered Pair integer object. 
 * @author Matthew Tropiano
 */
public class Pair
{
	/** X-coordinate. */
	public int x;
	/** Y-coordinate. */
	public int y;
	
	/**
	 * Creates a new Pair (0,0).
	 */
	public Pair()
	{
		this(0, 0);
	}
	
	/**
	 * Creates a new Pair.
	 * @param x the x-coordinate value.
	 * @param y the y-coordinate value.
	 */
	public Pair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode()
	{
		final int p = CRC32.POLYNOMIAL_IEEE;
		return RMath.rotateLeft((p ^ x), x) ^ (p ^ y);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Pair)
			return equals((Pair)obj);
		else
			return super.equals(obj);
	}
	
	/**
	 * Sets both components.
	 * @param x the x-coordinate value.
	 * @param y the y-coordinate value.
	 * @since 2.21.0
	 */
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets both components using an existing pair.
	 * @param p the source pair.
	 * @since 2.21.0
	 */
	public void set(Pair p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	/**
	 * Checks if this pair equals another.
	 * @param p the other pair.
	 * @return true if so, false if not.
	 */
	public boolean equals(Pair p)
	{
		return x == p.x && y == p.y;
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
}
