/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.wave;

import com.blackrook.commons.math.RMath;

/** 
 * Waveform type enumeration.
 * @author Matthew Tropiano
 */
public enum WaveForm implements WaveFormType
{
	NONE
	{
		@Override
		public double getSample(double time)
		{
			return -1.0; // always get "first" value.
		}
	},

	SINE
	{
		@Override
		public double getSample(double time)
		{
			return Math.sin(time*2.0*Math.PI);
		}
	},

	TRIANGLE
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			if (time < 0.25)
				return time/0.25;
			else if (time >= 0.25 && time < 0.75)
				return 1.0 - 2.0 * RMath.getInterpolationFactor(time, 0.25, 0.75);
			else
				return RMath.getInterpolationFactor(time, 0.75, 1.0) - 1.0;
		}
	},
		
	SQUARE
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			if (time < 0.5)
				return -1.0;
			else
				return 1.0;
		}
	},
		
	SAWTOOTH
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			return 2.0*time - 1.0;
		}
	},
		
	INVERSE_SAWTOOTH
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			return -((2.0*time) - 1.0);
		}
	},
	
	SQUARED_FUNC
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			return 2.0*time*time - 1.0;
		}
	},
		
	INVERSE_SQUARED_FUNC
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			return -(2.0*time*time - 1.0);			
		}
	},
		
	REVERSE_SQUARED_FUNC
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			return 2.0*(1.0-time)*(1.0-time) - 1.0;
		}
	},
		
	INVERSE_REVERSE_SQUARED_FUNC
	{
		@Override
		public double getSample(double time)
		{
			time = RMath.wrapValue(time, 0.0, 1.0);
			return -(2.0*(1.0-time)*(1.0-time) - 1.0);
		}
	},
		
	RANDOM
	{
		@Override
		public double getSample(double time)
		{
			return Math.random()*2.0 - 1.0;
		}
	};
	
	/**
	 * Interpolates between two values via waveform analysis.
	 * @param time	the time. 0 = period start, 1 = period end.
	 * @param v1	first sample bound of the wave.
	 * @param v2	second sample bound of the wave.
	 * @return the resultant value.
	 */
	public double interpolateValue(double time, double v1, double v2)
	{
		return RMath.linearInterpolate((getSample(time) + 1.0)/2.0, v1, v2);
	}
	
	/**
	 * Interpolates an integer range for this waveform.
	 * @param time	the time. 0 = period start, 1 = period end.
	 * @param v1	first sample bound of the wave.
	 * @param v2	second sample bound of the wave.
	 * @return the resultant value.
	 */
	public int interpolateValue(double time, int v1, int v2)
	{
		return (int)interpolateValue(time, (double)v1, (double)v2);
	}
	
	/**
	 * Interpolates an array of values for this waveform.
	 * @param time	the time. 0 = period start, 1 = period end.
	 * @param v1	first bounds of the wave.
	 * @param v2	second bounds of the wave.
	 * @param out	the out array. values are saved here.
	 */
	public void interpolateValue(double time, double[] v1, double[] v2, double[] out)
	{
		for (int i = 0; i < out.length; i++)
			out[i] = interpolateValue(time, v1[i], v2[i]);
	}
	
	/**
	 * Interpolates an array of float values for this waveform.
	 * @param time	the time. 0 = period start, 1 = period end.
	 * @param v1	first bounds of the wave.
	 * @param v2	second bounds of the wave.
	 * @param out	the out array. values are saved here.
	 */
	public void interpolateValue(double time, float[] v1, float[] v2, float[] out)
	{
		for (int i = 0; i < out.length; i++)
			out[i] = (float)interpolateValue(time, v1[i], v2[i]);
	}

	@Override
	public double getAmplitude()
	{
		return 1.0;
	}
	
}

