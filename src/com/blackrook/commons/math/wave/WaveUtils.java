/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.wave;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.blackrook.commons.math.wave.CustomWaveForm.InterpolationType;
import com.blackrook.commons.math.RMath;

/**
 * Utility methods for manipulating or displaying waves.
 * @author Matthew Tropiano
 */
public final class WaveUtils
{
	private WaveUtils() {}
	
	/**
	 * Draws a wave to a Graphics2D context.
	 * This does so by plotting the entire wave along the length of 
	 * @param g2d the Graphics2D context to use.
	 * @param area the rectangular area with which to plot the wave. 
	 * @param sampleColor the color of each discrete sample point in the wave.
	 * @param lineColor the color of the interpolated data along the wave.
	 */
	public static void drawWave(WaveFormType wft, Graphics2D g2d, Rectangle area, Color sampleColor, Color lineColor)
	{
		double ampScale = area.getHeight()/2.0;
		double waveAmp = wft.getAmplitude();
		
		if (waveAmp == 0.0)
			return;
		
		int x = area.x;
		int midy = area.height / 2;
		Color lastColor = g2d.getColor();
		g2d.setColor(lineColor);
		
		int lastx = x;
		int lasty = midy;
		for (int i = 0; i < area.width; i++)
		{
			int px = x+i;
			double smp = (wft.getSample(i /(double)area.width)/waveAmp)*ampScale;
			int py = area.height - (int)(midy + smp); // invert so it appears correct.
			if (i != 0)
				g2d.drawLine(lastx, lasty, px, py);
			else
				g2d.drawLine(px, py, px, py);
			lastx = px;
			lasty = py;
		}
		
		if (wft instanceof CustomWaveForm)
		{
			g2d.setColor(sampleColor);
			CustomWaveForm cwf = (CustomWaveForm)wft;
			int scount = cwf.getSampleCount();
			double xinc = (double)area.width / scount;
			for (int i = 0; i < scount; i++)
			{
				g2d.setColor(sampleColor);
				int sx = (int)((xinc * i)) - 3;
				int sy = area.height - (int)(area.height * ((cwf.getNormalizedSample((1.0 / scount) * i) + 1.0) / 2)) - 3;
				g2d.fillOval(sx, sy, 6, 6);
				g2d.setColor(Color.BLACK);
				g2d.drawOval(sx, sy, 6, 6);
			}
		}
		
		g2d.setColor(lastColor);
	}

	/**
	 * Creates a Perlin Noise Wave.
	 * @param random the random seeder to use for generating the random data.
	 * @param startingSamples the starting number of discrete samples. 
	 * @param persistance the change in amplitude in each iteration.
	 * @param steps how many iterations to use to create the noise function. 
	 * @param interpolationType the type of interpolation to use for sampling values between the discrete samples.
	 * @return the resulting noise wave. the total amplitude will not exceed the starting one.
	 */
	public static CustomWaveForm createNoiseWave(Random random, int startingSamples, 
			double persistance, int steps, InterpolationType interpolationType)
	{
		double amp = 1.0;
		int samples = startingSamples;
		CustomWaveForm out = null;
		for (int i = 0; i < steps; i++)
		{
			CustomWaveForm next = new CustomWaveForm(random, amp, samples, interpolationType);
			if (out != null) for (int x = 0; x < samples; x++)
			{
				next.amplitude = 1.0;
				next.setSampleValue(x, RMath.clampValue(next.getSampleValue(x) + out.getSample(x * (1.0 / samples)), -1.0, 1.0));
			}
			out = next;
			samples *= 2;
			amp = Math.pow(persistance, i+1);
		}
		return out;
	}
	
}
