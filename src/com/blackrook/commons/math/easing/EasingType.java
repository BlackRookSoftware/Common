/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.easing;

/**
 * Easing type interface for custom easings.
 * @author Matthew Tropiano
 * @since 2.21.0
 */
public interface EasingType
{
	/**
	 * Samples this easing to get the final output value for interpolation.
	 * An input time of 0f and less should return 0f. An input time of 1f or greater should return 1f.  
	 * @param inputScalar the input scalar (between 0 and 1, inclusively).
	 */
	public float getScaling(float inputScalar);
	
}
