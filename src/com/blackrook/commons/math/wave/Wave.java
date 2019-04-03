/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.wave;

import com.blackrook.commons.math.RMath;

/**
 * Wave object for waveform analysis.
 * @author Matthew Tropiano
 * @since 2.2.1, offset is a double, not float.
 */
public class Wave
{
	/** The waveform type. */
	private WaveFormType waveForm;
	/** The waveform period in milliseconds. */
	private int period;
	/** The offset time for the wave. */
	private double offset;
	
	/**
	 * Creates a new wave with no waveform,
	 * zero period, and no offset.
	 */
	public Wave()
	{
		this(WaveForm.NONE, 0, 0f);
	}

	/**
	 * Creates a new wave.
	 * @param type the waveform type that this wave uses.
	 * @param period the waveform period in milliseconds.
	 * @param offset the offset time for the wave in period lengths.
	 */
	public Wave(WaveFormType type, int period, double offset)
	{
		waveForm = type;
		this.period = period;
		this.offset = offset;
	}

	/**
	 * @return the underlying waveform type.
	 */
	public WaveFormType getWaveForm()
	{
		return waveForm;
	}

	/**
	 * Sets the waveform type.
	 * @param waveForm the waveform type to set.
	 */
	public void setWaveForm(WaveFormType waveForm)
	{
		this.waveForm = waveForm;
	}

	/** 
	 * @return the waveform period in milliseconds.
	 */
	public int getPeriod()
	{
		return period;
	}

	/** 
	 * Sets the waveform period in milliseconds.
	 * @param period the period to set.
	 */
	public void setPeriod(int period)
	{
		this.period = period;
	}

	/** 
	 * Gets the offset time for the wave.
	 * Measured in wavelengths.
	 * @return the offset along the wave.
	 */
	public double getOffset()
	{
		return offset;
	}

	/** 
	 * Sets the offset time for the wave.
	 * @param offset the offset to set.
	 */
	public void setOffset(double offset)
	{
		this.offset = offset;
	}
	
	/**
	 * Gets the proper sampled value along the wave at a particular time.
	 * @param time the current time in milliseconds.
	 * @return the resultant sampled value. 
	 * @see WaveForm#getSample(double)
	 */
	public double getSample(long time)
	{
		double ftime = period != 0 ? (time % period) / (float)period : 0.0f;
		ftime = (ftime + offset) % 1.0f;
		return waveForm.getSample(ftime);
	}

	/**
	 * Gets the proper interpolated value between two input values.
	 * Assumes that the sample returned is between -1 and 1.
	 * @param time	the current time in milliseconds.
	 * @param a	the first sample bound value.
	 * @param b	the second sample bound value.
	 * @return the resultant sampled value. 
	 */
	public float getInterpolatedValue(long time, float a, float b)
	{
		return (float)RMath.linearInterpolate((getSample(time) + 1.0) / 2.0, a, b);
	}

	/**
	 * Gets the proper interpolated value between two input values.
	 * Assumes that the sample returned is between -1 and 1.
	 * @param time	the current time in milliseconds.
	 * @param a		the first set of sample bound value.
	 * @param b		the second set of sample bound value.
	 * @param out	the output array for the interpolated values.
	 */
	public void getInterpolatedValue(long time, float[] a, float[] b, float[] out)
	{
		double sample = getSample(time);
		for (int i = 0; i < a.length; i++)
			out[i] = (float)RMath.linearInterpolate((sample + 1.0) / 2.0, a[i], b[i]);
	}

	/**
	 * Gets the proper interpolated value between two input values.
	 * Assumes that the sample returned is between -1 and 1.
	 * @param time	the current time in milliseconds.
	 * @param a	the first sample bound value.
	 * @param b	the second sample bound value.
	 * @return the resultant sampled value. 
	 */
	public double getInterpolatedValue(long time, double a, double b)
	{
		double sample = getSample(time);
		return RMath.linearInterpolate((sample + 1.0) / 2.0, a, b);
	}
	
	/**
	 * Gets the proper interpolated value between two input values.
	 * Assumes that the sample returned is between -1 and 1.
	 * @param time	the current time in milliseconds.
	 * @param a		the first set of sample bound values.
	 * @param b		the second set of sample bound values.
	 * @param out	the output array for the interpolated values.
	 */
	public void getInterpolatedValue(long time, double[] a, double[] b, double[] out)
	{
		double sample = getSample(time);
		for (int i = 0; i < a.length; i++)
			out[i] = RMath.linearInterpolate((sample + 1.0) / 2.0, a[i], b[i]);
	}

	/**
	 * Gets the proper interpolated value between two input values.
	 * Assumes that the sample returned is between -1 and 1.
	 * @param time	the current time in milliseconds.
	 * @param a	the first sample bound value.
	 * @param b	the second sample bound value.
	 * @return the resultant sampled value. 
	 */
	public int getInterpolatedValue(long time, int a, int b)
	{
		double sample = (getSample(time) + 1) / 2;
		int range = Math.abs(a - b);
		return Math.min(a, b) + (int)RMath.linearInterpolate(sample, 0, range+1);
	}
	
}

