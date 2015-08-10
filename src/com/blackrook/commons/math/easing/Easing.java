/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math.easing;

import com.blackrook.commons.math.RMath;

/**
 * An enumeration of base easing types for actions. 
 * @author Matthew Tropiano
 * @since 2.21.0
 */
public enum Easing implements EasingType
{
	LINEAR
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			return inputScalar;
		}
	},
	
	SQUARED_EASE_IN
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			return 1f - ((float)Math.pow(1f-inputScalar, 2));
		}
	},
	
	SQUARED_EASE_OUT
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			return (float)Math.pow(inputScalar, 2);
		}
	},
	
	SQUARED_EASE_IN_AND_OUT
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			inputScalar = inputScalar * 2;
            if (inputScalar < 1) {
                return (float)Math.pow(inputScalar, 2) / 2;
            }
            inputScalar -= 2;
            return ((float)Math.pow(inputScalar, 2) + 2) / 2;
		}
	},
	
	CUBIC_EASE_IN
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			return 1f - ((float)Math.pow(1f-inputScalar, 3));
		}
	},
	
	CUBIC_EASE_OUT
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			return (float)Math.pow(inputScalar, 3);
		}
	},
	
	CUBIC_EASE_IN_AND_OUT
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
			inputScalar = inputScalar * 2;
            if (inputScalar < 1) {
                return (float)Math.pow(inputScalar, 3) / 2;
            }
            inputScalar -= 2;
            return ((float)Math.pow(inputScalar, 3) + 2) / 2;
		}
	},
	
	BOUNCE
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
            float s = 7.5625f;
            float p = 2.75f;
            float out = 0f;
	        if (inputScalar < (1 / p))
	        {
	            out = s * inputScalar * inputScalar;
	        } 
	        else
	        {
	            if (inputScalar < (2 / p))
	            {
	                inputScalar -= (1.5 / p);
	                out = s * inputScalar * inputScalar + .75f;
	            } 
	            else
	            {
	                if (inputScalar < (2.5 / p))
	                {
	                    inputScalar -= (2.25 / p);
	                    out = s * inputScalar * inputScalar + .9375f;
	                } 
	                else
	                {
	                    inputScalar -= (2.625 / p);
	                    out = s * inputScalar * inputScalar + .984375f;
	                }
	            }
	        }
	        return out;
		}
	},
	
	ELASTIC
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
            if (inputScalar == 0 || inputScalar == 1)
            {
                return inputScalar;
            }
            float p = 0.3f;
            float s = p / 4;
            return (float)(Math.pow(2, -10 * inputScalar) * Math.sin((inputScalar - s) * (2 * Math.PI) / p) + 1);
		}
	},
	
	BACK_IN
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
            float s = 1.70158f;
            return inputScalar * inputScalar * ((s + 1) * inputScalar - s);
		}
	},
	
	BACK_OUT
	{
		@Override
		public float getScaling(float inputScalar)
		{
			inputScalar = RMath.clampValue(inputScalar, 0f, 1f);
            inputScalar = inputScalar - 1;
            float s = 1.70158f;
            return inputScalar * inputScalar * ((s + 1) * inputScalar + s) + 1;
		}
	},
	;

}
