/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Common interface for tuples, or associated sets 
 * of numeric single-precision coordinates.
 * @author Matthew Tropiano
 */
public interface TupleF
{

	/**
	 * Returns the distance of this tuple from the origin.
	 */
	public float length();

	/**
	 * Returns true if this tuple is zero, false otherwise.
	 */
	public boolean isZero();

	/**
	 * Turns this tuple into a unit tuple of length 1, while keeping direction intact.
	 */
	public void normalize();

	/**
	 * Negates this vector.
	 */
	public void negate();

	/**
	 * Scales this tuple.
	 * @param s		scalar factor. 
	 */
	public void scale(float s);

	/**
	 * Rotates this tuple around the zero origin's X axis.
	 * @param radians	 the angle in radians to rotate.
	 */
	public void rotateX(float radians);

	/**
	 * Rotates this tuple around the zero origin's Y axis.
	 * @param radians	 the angle in radians to rotate.
	 */
	public void rotateY(float radians);

	/**
	 * Rotates this tuple around the zero origin's Z axis.
	 * @param radians	 the angle in radians to rotate.
	 */
	public void rotateZ(float radians);

	/**
	 * Sets the distance of this tuple from the origin.
	 * @param d	the new distance.
	 */
	public void setLength(float d);

}
