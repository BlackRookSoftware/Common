/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.wave;

import java.util.Arrays;

/**
 * Polynomial structure for defining polynomials.
 * @author Matthew Tropiano
 * @since 2.2.0
 */
public class Polynomial implements WaveFormType
{
	/** Starting exponent for polynomial. */
	protected int startingExponent;
	/** Polynomial coefficients. */
	protected double[] coefficients;
	
	/**
	 * Creates a new polynomial.
	 * @param startingExponent the starting exponent.
	 * @param coefficients the polynomial coefficients.
	 */
	public Polynomial(int startingExponent, double ... coefficients)
	{
		this.startingExponent = startingExponent;
		this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
	}

	/**
	 * Always returns 1.
	 */
	public double getAmplitude()
	{
		return 1;
	}

	@Override
	public double getSample(double time)
	{
		double out = 0;
		for (int i = 0; i < coefficients.length; i++)
			out += coefficients[i] * Math.pow(time, startingExponent + i);
		return out;
	}

	/**
	 * Gets the starting exponent. 
	 */
	public int getStartingExponent()
	{
		return startingExponent;
	}

	/**
	 * Gets the coefficients.
	 */
	public double[] getCoefficients()
	{
		return coefficients;
	}

}
