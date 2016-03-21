/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.wave;

/**
 * Describes all types of sampleable waveforms.
 * @author Matthew Tropiano
 */
public interface WaveFormType
{
	/**
	 * Returns a value on the wave at a particular part of the wave's period.
	 * @param time a value from 0 to 1, describing a position along the period (0 = beginning, 1 = end).
	 * Depending on the WaveForm, this may wrap the input value around the interval [0,1] or clamp it.
	 * @return a value within the wave's amplitude.
	 */
	public double getSample(double time);
	
	/**
	 * @return this wave's amplitude.
	 */
	public double getAmplitude();

}
