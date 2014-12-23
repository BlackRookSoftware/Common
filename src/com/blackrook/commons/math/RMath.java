/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

import java.util.Random;

/**
 * A class with static methods that perform "other" types of mathematics.
 * Also contains convenience methods for pseudorandomly generated numbers.
 * @author Matthew Tropiano
 */
public final class RMath
{
	/** PI over Two. Equivalent to <code> {@link Math#PI} / 2.0</code>. */
	public static final double PI_OVER_TWO = Math.PI / 2.0;
	/** Two PI. Equivalent to <code> 2.0 * {@link Math#PI}</code>. */
	public static final double TWO_PI = 2.0 * Math.PI;
	/** Three PI over Two. Equivalent to <code> 3.0 * {@link Math#PI} / 2.0</code>. */
	public static final double THREE_PI_OVER_TWO = 3.0 * Math.PI / 2.0;

	private RMath() {}
	
	/**
	 * Rotates the bits of a number to the left.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static int rotateLeft(int n, int x)
	{
		if (x < 0) return rotateRight(n, -x);
		x = x % 32;
		int m = ((1 << x) - 1) << (32 - x);
		return (n << x) | ((n & m) >>> (32 - x));
	}
	
	/**
	 * Rotates the bits of a number to the right.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static int rotateRight(int n, int x)
	{
		if (x < 0) return rotateLeft(n, -x);
		x = x % 32;
		int m = ((1 << x) - 1);
		return (n >>> x) | ((n & m) << (32 - x));
	}
	
	/**
	 * Rotates the bits of a number to the left.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static short rotateLeft(short n, int x)
	{
		if (x < 0) return rotateRight(n, -x);
		x = x % 16;
		short m = (short)(((1 << x) - 1) << (16 - x));
		return (short)(((n << x) | ((n & m) >>> (16 - x))) & 0x0000ffff);
	}
	
	/**
	 * Rotates the bits of a number to the right.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static short rotateRight(short n, int x)
	{
		if (x < 0) return rotateLeft(n, -x);
		x = x % 16;
		short m = (short)((1 << x) - 1);
		return (short)(((n >>> x) | ((n & m) << (16 - x))) & 0x0000ffff);
	}
	
	/**
	 * Rotates the bits of a number to the left.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static byte rotateLeft(byte n, int x)
	{
		if (x < 0) return rotateRight(n, -x);
		x = x % 8;
		byte m = (byte)(((1 << x) - 1) << (8 - x));
		return (byte)(((n << x) | ((n & m) >>> (8 - x))) & 0x000000ff);
	}
	
	/**
	 * Rotates the bits of a number to the right.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static byte rotateRight(byte n, int x)
	{
		if (x < 0) return rotateLeft(n, -x);
		x = x % 8;
		byte m = (byte)((1 << x) - 1);
		return (byte)(((n >>> x) | ((n & m) << (8 - x))) & 0x000000ff);
	}
	
	/**
	 * Rotates the bits of a number to the left.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static long rotateLeft(long n, int x)
	{
		if (x < 0) return rotateRight(n, -x);
		x = x % 64;
		long m = ((1 << x) - 1) << (64 - x);
		return (n << x) | ((n & m) >>> (64 - x));
	}
	
	/**
	 * Rotates the bits of a number to the right.
	 * @param n the input number.
	 * @param x the number of positions.
	 * @return the resultant number.
	 */
	public static long rotateRight(long n, int x)
	{
		if (x < 0) return rotateLeft(n, -x);
		x = x % 64;
		long m = (1 << x) - 1;
		return (n >>> x) | ((n & m) << (64 - x));
	}
	
	/**
	 * Returns a random boolean.
	 */
	public static boolean randBoolean(Random rand)
	{
	    return rand.nextBoolean();
	}

	/**
	 * Returns a random byte value.
	 */
	public static byte randByte(Random rand)
	{
		return (byte)randInt(rand,-128,127);
	}

	/**
	 * Returns a random integer.
	 */
	public static int randInt(Random rand)
	{
	    return rand.nextInt();
	}

	/**
	 * Returns a random long.
	 */
	public static long randLong(Random rand)
	{
	    return rand.nextLong();
	}

	/**
	 * Returns a random float value from [0 to 1) (inclusive/exclusive).
	 */
	public static float randFloat(Random rand)
	{
	    return rand.nextFloat();
	}

	/**
	 * Returns a random double value from [0 to 1) (inclusive/exclusive).
	 */
	public static double randDouble(Random rand)
	{
	    return rand.nextDouble();
	}

	/**
	 * Returns a random Gaussian-distributed double value from -inf to +inf.
	 */
	public static double randGauss(Random rand)
	{
	    return rand.nextGaussian();
	}

	/**
	 * Fills an array with random byte values.
	 */
	public static void randBytes(Random rand, byte[] b)
	{
		rand.nextBytes(b);
	}

	/**
	 * Returns a random integer from 0 (inclusive) to x (exclusive).
	 */
	public static int rand(Random rand, int x)
	{
	    return rand.nextInt(x);
	}

	/**
	 * Returns a random integer from base to base+range.
	 */
	public static int rand(Random rand, int base, int range)
	{
	    return rand(rand,range+1)+base;
	}

	/**
	 * Returns a random float from lo to hi (inclusive).
	 */
	public static float randFloat(Random rand, float lo, float hi)
	{
		return (hi - lo)*randFloat(rand) + lo;
	}

	/**
	 * Returns a random Gaussian float from lo to hi (inclusive).
	 * GAUSS WILL BE MOST PLEASED
	 */
	public static float randGaussFloat(Random rand, float lo, float hi)
	{
		return (hi - lo)*randGaussFloat(rand) + lo;
	}

	/**
	 * Returns a random integer from lo to hi (inclusive).
	 */
	public static int randInt(Random rand, int lo, int hi)
	{
	    return rand(rand,hi-lo+1)+lo;
	}

	/**
	 * Returns a random float value from -1 to 1 (inclusive).
	 */
	public static float randFloatN(Random rand)
	{
	    return randFloat(rand)*(randBoolean(rand)?-1.0f:1.0f);
	}

	/**
	 * Returns a random double from lo to hi (inclusive).
	 */
	public static double randDouble(Random rand, double lo, double hi)
	{
		return (hi - lo)*randDouble(rand) + lo;
	}

	/**
	 * Returns a random double value from -1 to 1 (inclusive).
	 */
	public static double randDoubleN(Random rand)
	{
	    return randDouble(rand)*(randBoolean(rand)?-1:1);
	}

	/**
	 * Returns a random Gaussian-distributed float value from -inf to +inf.
	 */
	public static float randGaussFloat(Random rand)
	{
	    return (float)randGauss(rand);
	}

	/**
	 * Returns a random short from lo to hi (inclusive).
	 */
	public static short randShort(Random rand, int lo, int hi)
	{
	    return (short)(rand(rand,hi-lo+1)+lo);
	}

	/**
	 * Returns a random short from lo to hi (inclusive).
	 */
	public static short randShort(Random rand, short lo, short hi)
	{
	    return (short)(rand(rand,hi-lo+1)+lo);
	}

	/**
	 * Returns a random entry in an array/list.  
	 * @param rand the Random instance to use. 
	 * @param objects the array of objects to select from.
	 * @return a random entry from the array.
	 * @since 2.2.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T randElement(Random rand, T ... objects)
	{
		return objects[rand(rand, objects.length)];
	}
	
	/**
	 * Calculates a percent chance of something occuring.
	 * @param percent the chance
	 * @return true if happening, false otherwise.
	 */
	public static boolean percentChance(Random rand, int percent)
	{
		return randInt(rand,0,99) < percent;
	}

	/**
	 * Rolls a die.
	 * @param die size of the die.
	 * @return the outcome.
	 */
	public static int roll(Random rand, int die)
	{
		if (die <= 1) return 1;
		return randInt(rand,1,die);
	}

	/**
	 * Rolls a die many times. Example: 2d20 = roll(rand,2,20)
	 * @param n times to roll.
	 * @param die size of the die.
	 * @return the outcome total.
	 */
	public static int roll(Random rand, int n, int die)
	{
		int total = 0;
		for (int i = 0; i < n; i++)
			total += roll(rand,die);
		return total;
	}

	/**
	 * Finds the closest power of two to an integer value, larger than the initial value.
	 * <p>Examples:</p>
	 * <ul>
	 * <li>If x is 19, this returns 32.</li>
	 * <li>If x is 4, this returns 4.</li>
	 * <li>If x is 99, this returns 128.</li>
	 * <li>If x is 129, this returns 256.</li>
	 * </ul>
	 * @param x	the input value.
	 */
	public static int closestPowerOfTwo(int x)
	{
		if (x <= 1)
			return 1;
		if (x == 2)
			return x;
		int out = 2;
		while (x > 1)
		{
			out <<= 1;
			x >>= 1;
		}
		return out;
	}

	/**
	 * Returns the greatest common divisor of two integers.
	 * @param a the first integer.
	 * @param b the second integer.
	 * @since 2.9.0
	 */
	public static int gcd(int a, int b)
	{
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}
	
	/**
	 * Checks if an integer is a valid power of two.
	 * @return true if it is, false if not.
	 */
	public static boolean isPowerOfTwo(int x)
	{
		return (x & (x-1)) == 0;
	}
	
	/**
	 * Checks if an integer is a valid power of two.
	 * @return true if it is, false if not.
	 */
	public static boolean isPowerOfTwo(long x)
	{
		return (x & (x-1L)) != 0L;
	}
	
	/**
	 * Returns the percentage of an integer to the nearest complete integer.
	 * Example: getPercent(20,50) returns 10.
	 * Example 2: getPercent(10,25) returns 2.
	 * 
	 * @param x the integer.
	 * @param percentage the percentage.
	 * @return the result equal to floor of x*(percentage/100), mathematically.
	 */
	public static int getPercent(int x, float percentage)
	{
	    return (int)(x*(percentage/100f));
	}

	/**
	 * Converts radians to degrees.
	 * @return the resultant angle in degrees.
	 */
	public static double radToDeg(double radians)
	{
		return radians * (180/Math.PI);
	}

	/**
	 * Converts radians to degrees.
	 * @return the resultant angle in degrees.
	 */
	public static int radToDeg(int radians)
	{
		return new Double(radians * (180/Math.PI)).intValue();
	}

	/**
	 * Converts degrees to radians.
	 * @return the resultant angle in radians.
	 */
	public static double degToRad(double degrees)
	{
		return (degrees * Math.PI)/180;
	}

	/**
	 * Rounds to the nearest increment.
	 * <br>Example: roundToNearest(3.4, 1.0) returns 3.0. 
	 * <br>Example: roundToNearest(3.4, 5.0) returns 5.0. 
	 * <br>Example: roundToNearest(3.4, 0.3) returns 3.3. 
	 * @param value the value to round.
	 * @param increment the incremental value. 
	 * @return the nearest increment using the input value.
	 * @since 2.10.0
	 */
	public static double roundToNearest(double value, double increment)
	{
		increment = Math.abs(increment);
		return Math.round(value / increment) * increment;
	}
	
	/**
	 * Coerces an integer to the range bounded by lo and hi.
	 * <br>Example: clampValue(32,-16,16) returns 16.
	 * <br>Example: clampValue(4,-16,16) returns 4.
	 * <br>Example: clampValue(-1000,-16,16) returns -16.
	 * 
	 * @param val the integer.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "forced" into the range.
	 */
	public static int clampValue(int val, int lo, int hi)
	{
		return Math.min(Math.max(val,lo),hi);
	}

	/**
	 * Coerces a short to the range bounded by lo and hi.
	 * <br>Example: clampValue(32,-16,16) returns 16.
	 * <br>Example: clampValue(4,-16,16) returns 4.
	 * <br>Example: clampValue(-1000,-16,16) returns -16.
	 * 
	 * @param val the short.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "forced" into the range.
	 */
	public static short clampValue(short val, short lo, short hi)
	{
		return (short)Math.min((short)Math.max(val,lo),hi);
	}

	/**
	 * Coerces a float to the range bounded by lo and hi.
	 * <br>Example: clampValue(32,-16,16) returns 16.
	 * <br>Example: clampValue(4,-16,16) returns 4.
	 * <br>Example: clampValue(-1000,-16,16) returns -16.
	 * 
	 * @param val the float.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "forced" into the range.
	 */
	public static float clampValue(float val, float lo, float hi)
	{
		return Math.min(Math.max(val,lo),hi);
	}

	/**
	 * Coerces a double to the range bounded by lo and hi.
	 * <br>Example: clampValue(32,-16,16) returns 16.
	 * <br>Example: clampValue(4,-16,16) returns 4.
	 * <br>Example: clampValue(-1000,-16,16) returns -16.
	 * 
	 * @param val the double.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "forced" into the range.
	 */
	public static double clampValue(double val, double lo, double hi)
	{
		return Math.min(Math.max(val,lo),hi);
	}
	
	
	/**
	 * Coerces an integer to the range bounded by lo and hi, by "wrapping"
	 * the value. This is equal to <i>lo + (val % (hi - lo))</i> if lo > 0.
	 * <br>Example: wrapValue(32,-16,16) returns 0.
	 * <br>Example: wrapValue(4,-16,16) returns 4.
	 * <br>Example: wrapValue(-1000,-16,16) returns 8.
	 * 
	 * @param val the integer.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "wrapped" into the range.
	 */
	public static int wrapValue(int val, int lo, int hi)
	{
		val = val - (int)(val - lo) / (hi - lo) * (hi - lo);
	   	if (val < 0)
	   		val = val + hi - lo;
	   	return val;
	}

	/**
	 * Coerces a float to the range bounded by lo and hi, by "wrapping"
	 * the value. This is equal to <i>lo + (val % (hi - lo))</i> if lo > 0.
	 * <br>Example: wrapValue(32,-16,16) returns 0.
	 * <br>Example: wrapValue(4,-16,16) returns 4.
	 * <br>Example: wrapValue(-1000,-16,16) returns 8.
	 * 
	 * @param val the float.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "wrapped" into the range.
	 */
	public static float wrapValue(float val, float lo, float hi)
	{
		val = val - (int)((val - lo) / (hi - lo) * (hi - lo));
	   	if (val < 0)
	   		val = val + hi - lo;
	   	return val;
	}

	/**
	 * Coerces a short to the range bounded by lo and hi, by "wrapping"
	 * the value. This is equal to <i>lo + (val % (hi - lo))</i> if lo > 0.
	 * <br>Example: wrapValue(32,-16,16) returns 0.
	 * <br>Example: wrapValue(4,-16,16) returns 4.
	 * <br>Example: wrapValue(-1000,-16,16) returns 8.
	 * 
	 * @param val the short.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "wrapped" into the range.
	 */
	public static short wrapValue(short val, short lo, short hi)
	{
		val = (short)(val - (val - lo) / (hi - lo) * (hi - lo));
	   	if (val < 0)
	   		val = (short)(val + hi - lo);
	   	return val;
	}

	/**
	 * Coerces a double to the range bounded by lo and hi, by "wrapping"
	 * the value. This is equal to <i>lo + (val % (hi - lo))</i> if lo > 0.
	 * <br>Example: wrapValue(32,-16,16) returns 0.
	 * <br>Example: wrapValue(4,-16,16) returns 4.
	 * <br>Example: wrapValue(-1000,-16,16) returns 8.
	 * 
	 * @param val the double.
	 * @param lo the lower bound.
	 * @param hi the upper bound.
	 * @return the value after being "wrapped" into the range.
	 */
	public static double wrapValue(double val, double lo, double hi)
	{
		val = val - (int)((val - lo) / (hi - lo) * (hi - lo));
	   	if (val < 0)
	   		val = val + hi - lo;
	   	return val;
	}

	/**
	 * Logically "and"-s two boolean arrays together.
	 * If both arrays are not the same size, an array of length <i>max(b1.length, b2.length)</i>
	 * is returned and the longer array is "and"-ed against falses past <i>min(b1.length, b2.length)</i>.
	 * @param b1	the first array.
	 * @param b2	the second array.
	 * @return		A new boolean array that is the logical "and" of both arrays.
	 */
	public static boolean[] andBooleanArrays(boolean[] b1, boolean[] b2)
	{
		boolean[] longer = b1.length > b2.length ? b1 : b2;
		boolean[] shorter = b1 == longer ? b2 : b1;
		boolean[] out = new boolean[longer.length];
		if (longer.length != shorter.length)
		{
			boolean[] b = new boolean[longer.length];
			System.arraycopy(shorter, 0, b, 0, shorter.length);
			shorter = b;
		}
		
		for (int i = 0; i < longer.length; i++)
			out[i] = longer[i] && shorter[i];
	
		return out;
	}

	/**
	 * Logically "or"-s two boolean arrays together.
	 * If both arrays are not the same size, an array of length <i>max(b1.length, b2.length)</i>
	 * is returned and the longer array is "or"-ed against falses past <i>min(b1.length, b2.length)</i>.
	 * @param b1	the first array.
	 * @param b2	the second array.
	 * @return		A new boolean array that is the logical "or" of both arrays.
	 */
	public static boolean[] orBooleanArrays(boolean[] b1, boolean[] b2)
	{
		boolean[] longer = b1.length > b2.length ? b1 : b2;
		boolean[] shorter = b1 == longer ? b2 : b1;
		boolean[] out = new boolean[longer.length];
		if (longer.length != shorter.length)
		{
			boolean[] b = new boolean[longer.length];
			System.arraycopy(shorter, 0, b, 0, shorter.length);
			shorter = b;
		}
		
		for (int i = 0; i < longer.length; i++)
			out[i] = longer[i] || shorter[i];
	
		return out;
	}

	/**
	 * Logically "xor"-s two boolean arrays together.
	 * If both arrays are not the same size, an array of length <i>max(b1.length, b2.length)</i>
	 * is returned and the longer array is "xor"-ed against falses past <i>min(b1.length, b2.length)</i>.
	 * @param b1	the first array.
	 * @param b2	the second array.
	 * @return		A new boolean array that is the logical "xor" of both arrays.
	 */
	public static boolean[] xorBooleanArrays(boolean[] b1, boolean[] b2)
	{
		boolean[] longer = b1.length > b2.length ? b1 : b2;
		boolean[] shorter = b1 == longer ? b2 : b1;
		boolean[] out = new boolean[longer.length];
		if (longer.length != shorter.length)
		{
			boolean[] b = new boolean[longer.length];
			System.arraycopy(shorter, 0, b, 0, shorter.length);
			shorter = b;
		}
		
		for (int i = 0; i < longer.length; i++)
			out[i] = longer[i] ^ shorter[i];
	
		return out;
	}

	/**
	 * Returns the value that "value" is closest to.
	 * @param value		the input value
	 * @param v1		first evaluating value
	 * @param v2		second evaluating value
	 * @return			either v1 or v2, whichever's closer.
	 */
	public static int closer(double value, int v1, int v2)
	{
		return (Math.abs(value-v1) <= Math.abs(value-v2)) ? v1 : v2;
	}

	/**
	 * Returns the value that "value" is farthest from.
	 * @param value		the input value
	 * @param v1		first evaluating value
	 * @param v2		second evaluating value
	 * @return			either v1 or v2, whichever's farther.
	 */
	public static int farther(double value, int v1, int v2)
	{
		return (Math.abs(value-v1) >= Math.abs(value-v2)) ? v1 : v2;
	}

	/**
	 * Gives a value that is the result of a linear interpolation between two values.
	 * @param factor	the interpolation factor.
	 * @param x			the first value.
	 * @param y			the second value.
	 */
	public static double linearInterpolate(double factor, double x, double y)
	{
		return factor * (y - x) + x;
	}
	
	/**
	 * Gives a value that is the result of a cosine interpolation between two values.
	 * @param factor	the interpolation factor.
	 * @param x			the first value.
	 * @param y			the second value.
	 */
	public static double cosineInterpolate(double factor, double x, double y)
	{
		double ft = factor * Math.PI;
		double f = (1 - Math.cos(ft)) * .5;
		return f * (y - x) + x;
	}
	
	/**
	 * Gives a value that is the result of a cublic interpolation between two values.
	 * Requires two outside values to predict a curve more accurately.
	 * @param factor	the interpolation factor between x and y.
	 * @param w			the value before the first.
	 * @param x			the first value.
	 * @param y			the second value.
	 * @param z			the value after the second.
	 */
	public static double cubicInterpolate(double factor, double w, double x, double y, double z)
	{
		double p = (z - y) - (w - x);
		double q = (w - x) - p;
		double r = y - w;
		double s = x;
		return (p*factor*factor*factor) + (q*factor*factor) + (r*factor) + s;
	}
	
	/**
	 * Gets a scalar factor that equals how "far along" a value is along an interval.
	 * @param value	the value to test.
	 * @param lo	the lower value of the interval.
	 * @param hi	the higher value of the interval.
	 * @return		a value between 0 and 1 describing this distance 
	 * 		(0 = beginning or less, 1 = end or greater), or 0 if lo and hi are equal.
	 */
	public static double getInterpolationFactor(double value, double lo, double hi)
	{
		if (lo == hi)
			return 0.0;
		return clampValue((value - lo) / (hi - lo), 0, 1);
	}
	
	/**
	 * Returns the angular rotation of a vector described in two dimensions.
	 * Result is in degrees.
	 * @return a number in the range [0, 360). 0 is considered to be EAST. 
	 * @since 2.3.1
	 * @deprecated Since 2.10.0. Use {@link #getVectorAngleDegrees(double, double)} instead.
	 */
	public static double getVectorAngle(double x, double y)
	{
		return getVectorAngleDegrees(x, y);
	}
	
	/**
	 * Returns the angular rotation of a vector described in two dimensions.
	 * Result is in degrees.
	 * @return a number in the range [0, 360). 0 is considered to be EAST. 
	 * @since 2.3.1
	 */
	public static double getVectorAngleDegrees(double x, double y)
	{
		return x != 0.0 
			? RMath.radToDeg(Math.atan(y / x)) + (x < 0 ? 180 : 0) 
			: (y < 0 ? 270d : 90d);
	}
	
	/**
	 * Returns the angular rotation of a vector described in two dimensions.
	 * Result is in degrees.
	 * @return a number in the range [0, 360). 0 is considered to be EAST. 
	 * @since 2.10.0
	 */
	public static double getVectorAngleRadians(double x, double y)
	{
		return x != 0.0 
			? Math.atan(y / x) + (x < 0 ? Math.PI : 0) 
			: (y < 0 ? THREE_PI_OVER_TWO : PI_OVER_TWO);
	}
	
	/**
	 * Returns the difference in degrees between two angles.
	 * @param angle1 the first angle in DEGREES.
	 * @param angle2 the second angle in DEGREES.
	 * @return a number in the range [0, 180]. 0 is an EXACT match. 
	 * @since 2.10.0
	 */
	public static double getAngularDistanceDegrees(double angle1, double angle2)
	{
		double diff = Math.abs((angle2 % 360.0) - (angle1 % 360.0));
		return diff > 180.0 ? 360.0 - diff : diff; 
	}
	
	/**
	 * Returns the difference in radians between two angles.
	 * @param angle1 the first angle in RADIANS.
	 * @param angle2 the second angle in RADIANS.
	 * @return a number in the range [0, {@link Math#PI}]. 0 is an EXACT match. 
	 * @since 2.10.0
	 */
	public static double getAngularDistanceRadians(double angle1, double angle2)
	{
		double diff = Math.abs((angle2 % TWO_PI) - (angle1 % TWO_PI));
		return diff > Math.PI ? TWO_PI - diff : diff; 
	}
	
}
