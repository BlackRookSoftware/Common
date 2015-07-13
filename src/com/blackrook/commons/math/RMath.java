/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

import java.util.Random;

import com.blackrook.commons.math.geometry.Point2D;

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
	 * Returns the length of a vector by its components.
	 * @param x the x-component.
	 * @param y the y-component.
	 * @return the length of the vector.
	 * @since 2.21.0
	 */
	public static double getVectorLength(double x, double y)
	{
		return Math.sqrt(getVectorLengthSquared(x, y));
	}

	/**
	 * Returns the squared length of a vector by its components.
	 * @param x the x-component.
	 * @param y the y-component.
	 * @return the length of the vector.
	 * @since 2.21.0
	 */
	public static double getVectorLengthSquared(double x, double y)
	{
		return x*x + y*y;
	}

	/**
	 * Returns the length of a vector by its components.
	 * @param x the x-component.
	 * @param y the y-component.
	 * @param z the z-component.
	 * @return the length of the vector.
	 * @since 2.21.0
	 */
	public static double getVectorLength(double x, double y, double z)
	{
		return Math.sqrt(getVectorLengthSquared(x, y, z));
	}

	/**
	 * Returns the squared length of a vector by its components.
	 * @param x the x-component.
	 * @param y the y-component.
	 * @param z the z-component.
	 * @return the length of the vector.
	 * @since 2.21.0
	 */
	public static double getVectorLengthSquared(double x, double y, double z)
	{
		return x*x + y*y + z*z;
	}

	/**
	 * Returns the dot product of two vectors.
	 * @param v1x the first vector's x-component.
	 * @param v1y the first vector's y-component.
	 * @param v2x the second point's x-component.
	 * @param v2y the second point's y-component.
	 * @return the dot product of both vectors.
	 * @since 2.21.0
	 */
	public static double getVectorDotProduct(double v1x, double v1y, double v2x, double v2y)
	{
		return v1x * v2x + v1y * v2y;
	}

	/**
	 * Returns the dot product of two vectors.
	 * @param v1x the first vector's x-component.
	 * @param v1y the first vector's y-component.
	 * @param v1z the first vector's z-component.
	 * @param v2x the second point's x-component.
	 * @param v2y the second point's y-component.
	 * @param v2z the second point's z-component.
	 * @return the dot product of both vectors.
	 * @since 2.21.0
	 */
	public static double getVectorDotProduct(double v1x, double v1y, double v1z, double v2x, double v2y, double v2z)
	{
		return v1x * v2x + v1y * v2y + v1z * v2z;
	}

	/**
	 * Returns the dot product of two vectors, converted to unit vectors first.
	 * NOTE: Zero vectors will cause a <b>divide by zero</b>!
	 * @param v1x the first vector's x-component.
	 * @param v1y the first vector's y-component.
	 * @param v2x the second point's x-component.
	 * @param v2y the second point's y-component.
	 * @return the dot product of both vectors.
	 * @since 2.21.0
	 */
	public static double getVectorUnitDotProduct(double v1x, double v1y, double v2x, double v2y)
	{
		double v1d = getVectorLength(v1x, v1y); 
		double v2d = getVectorLength(v2x, v2y);
		v1x = v1x / v1d;
		v1y = v1y / v1d;
		v2x = v2x / v2d;
		v2y = v2y / v2d;
		return getVectorDotProduct(v1x, v1y, v2x, v2y);
	}

	/**
	 * Returns the dot product of two vectors, converted to unit vectors first.
	 * NOTE: Zero vectors will cause a <b>divide by zero</b>!
	 * @param v1x the first vector's x-component.
	 * @param v1y the first vector's y-component.
	 * @param v1z the first vector's z-component.
	 * @param v2x the second point's x-component.
	 * @param v2y the second point's y-component.
	 * @param v2z the second point's z-component.
	 * @return the dot product of both vectors.
	 * @since 2.21.0
	 */
	public static double getVectorUnitDotProduct(double v1x, double v1y, double v1z, double v2x, double v2y, double v2z)
	{
		double v1d = getVectorLength(v1x, v1y, v1z); 
		double v2d = getVectorLength(v2x, v2y, v2z);
		v1x = v1x / v1d;
		v1y = v1y / v1d;
		v1z = v1z / v1d;
		v2x = v2x / v2d;
		v2y = v2y / v2d;
		v2z = v2z / v2d;
		return getVectorDotProduct(v1x, v1y, v1z, v2x, v2y, v2z);
	}

	/**
	 * Returns the length of a line by 
	 * the coordinates of the two points that comprise it.
	 * @param x0 the first point's x-component.
	 * @param y0 the first point's y-component.
	 * @param x1 the second point's x-component.
	 * @param y1 the second point's y-component.
	 * @return the length of the line.
	 * @since 2.21.0
	 */
	public static double getLineLength(double x0, double y0, double x1, double y1)
	{
		return Math.sqrt(getLineLengthSquared(x0, y0, x1, y1));
	}

	/**
	 * Returns the squared length of a line by 
	 * the coordinates of the two points that comprise it.
	 * @param x0 the first point's x-component.
	 * @param y0 the first point's y-component.
	 * @param x1 the second point's x-component.
	 * @param y1 the second point's y-component.
	 * @return the length of the line.
	 * @since 2.21.0
	 */
	public static double getLineLengthSquared(double x0, double y0, double x1, double y1)
	{
		return getVectorLengthSquared(x1 - x0, y1 - y0);
	}

	/**
	 * Returns the length of a line by 
	 * the coordinates of the two points that comprise it.
	 * @param x0 the first point's x-component.
	 * @param y0 the first point's y-component.
	 * @param z0 the first point's z-component.
	 * @param x1 the second point's x-component.
	 * @param y1 the second point's y-component.
	 * @param z1 the second point's z-component.
	 * @return the length of the line.
	 * @since 2.21.0
	 */
	public static double getLineLength(double x0, double y0, double z0, double x1, double y1, double z1)
	{
		return Math.sqrt(getLineLengthSquared(x0, y0, z0, x1, y1, z1));
	}

	/**
	 * Returns the squared length of a line by 
	 * the coordinates of the two points that comprise it.
	 * @param x0 the first point's x-component.
	 * @param y0 the first point's y-component.
	 * @param z0 the first point's z-component.
	 * @param x1 the second point's x-component.
	 * @param y1 the second point's y-component.
	 * @param z1 the second point's z-component.
	 * @return the length of the line.
	 * @since 2.21.0
	 */
	public static double getLineLengthSquared(double x0, double y0, double z0, double x1, double y1, double z1)
	{
		return getVectorLengthSquared(x1 - x0, y1 - y0, z1 - z0);
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
	
	/**
	 * Returns the signed area of a triangular area made up of 3 points.
	 * @param ax the first point, x-coordinate.
	 * @param ay the first point, y-coordinate.
	 * @param bx the second point, x-coordinate.
	 * @param by the second point, y-coordinate.
	 * @param cx the third point, x-coordinate.
	 * @param cy the third point, y-coordinate.
	 * @since 2.21.0
	 */
	public static double getTriangleArea(double ax, double ay, double bx, double by, double cx, double cy)
	{
		return Math.abs(getTriangleAreaSigned(ax, ay, bx, by, cx, cy));
	}

	/**
	 * Returns the signed area of a triangular area made up of 3 points.
	 * @param ax the first point, x-coordinate.
	 * @param ay the first point, y-coordinate.
	 * @param bx the second point, x-coordinate.
	 * @param by the second point, y-coordinate.
	 * @param cx the third point, x-coordinate.
	 * @param cy the third point, y-coordinate.
	 * @since 2.21.0
	 */
	public static double getTriangleAreaSigned(double ax, double ay, double bx, double by, double cx, double cy)
	{
		return getTriangleAreaDoubleSigned(ax, ay, bx, by, cx, cy) / 2.0;
	}

	/**
	 * Returns the doubled signed area of a triangular area made up of 3 points.  
	 * @param ax the first point, x-coordinate.
	 * @param ay the first point, y-coordinate.
	 * @param bx the second point, x-coordinate.
	 * @param by the second point, y-coordinate.
	 * @param cx the third point, x-coordinate.
	 * @param cy the third point, y-coordinate.
	 * @since 2.21.0
	 */
	public static double getTriangleAreaDoubleSigned(double ax, double ay, double bx, double by, double cx, double cy)
	{
		return (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
	}

	/**
	 * Tests if an intersection occurs between two line segments.
	 * @param ax the first line segment, first point, x-coordinate.
	 * @param ay the first line segment, first point, y-coordinate.
	 * @param bx the first line segment, second point, x-coordinate.
	 * @param by the first line segment, second point, y-coordinate.
	 * @param cx the second line segment, first point, x-coordinate.
	 * @param cy the second line segment, first point, y-coordinate.
	 * @param dx the second line segment, second point, x-coordinate.
	 * @param dy the second line segment, second point, y-coordinate.
	 * @return a scalar value representing how far along the first line segment the intersection occurred, or {@link Double#NaN} if no intersection.
	 */
	public static double getLineSegmentIntersection(double ax, double ay, double bx, double by, double cx, double cy, double dx, double dy)
	{
		double a1 = RMath.getTriangleAreaDoubleSigned(ax, ay, bx, by, dx, dy);
		double a2 = RMath.getTriangleAreaDoubleSigned(ax, ay, bx, by, cx, cy);
		
		// If the triangle areas have opposite signs. 
		if (a1 != 0.0 && a2 != 0.0 && a1 * a2 < 0.0)
		{
			double a3 = RMath.getTriangleAreaDoubleSigned(cx, cy, dx, dy, ax, ay);
			double a4 = a3 + a2 - a1;
			
			if (a3 * a4 < 0.0)
			{
				return a3 / (a3 - a4);
			}
		}
		
		return Double.NaN;
	}

	/**
	 * Tests if a line segment intersects with a circle.
	 * @param ax the line segment, first point, x-coordinate.
	 * @param ay the line segment, first point, y-coordinate.
	 * @param bx the line segment, second point, x-coordinate.
	 * @param by the line segment, second point, y-coordinate.
	 * @param ccx the circle center, x-coordinate.
	 * @param ccy the circle center, y-coordinate.
	 * @param crad the circle radius.
	 * @return if an intersection occurred.
	 * @since 2.21.0
	 */
	public static boolean getLineCircleIntersection(double ax, double ay, double bx, double by, double ccx, double ccy, double crad)
	{
		// cull short lines.
		if (RMath.getVectorLength(bx - ax, by - ay) < RMath.getVectorLength(ccx - ax, ccy - ay) - crad)
			return false;

		// cull outside of possible vectors (dot product of line and line start to center).
		if (RMath.getVectorUnitDotProduct(ccx - ax, ccy - ay, bx - ax, by - ay) <= 0)
			return false;

		double dotp = RMath.getVectorUnitDotProduct(bx - ax, by - ay, bx - ccx, by - ccy);

		// line ends at circle center.
		if (Double.isNaN(dotp))
			return true;
		// line ends before circle center.
		else if (dotp < 0)
		{
			if (RMath.getVectorLength(ccx - bx, ccy - by) >= crad)
				return false;
			else
				return true;
		}
		// line ends after circle center.
		else
		{
			// project point
			double tx = bx - ax;
			double ty = by - ay;
			double dot = ccx * tx + ccy * ty;
			
			double fact = tx * tx + ty * ty;
			double dpofact = dot / fact;
			double ppx = dpofact * tx;
			double ppy = dpofact * ty;

			// no collision if distance to projected less than radius
			if (RMath.getLineLength(ccx, ccy, ppx, ppy) > crad)
				return false;
			else
				return true;
		}
	}

	/**
	 * Returns if two described circles intersect.  
	 * @param spx the first circle center, x-coordinate.
	 * @param spy the first circle center, y-coordinate.
	 * @param srad the first circle radius.
	 * @param tpx the second circle center, x-coordinate.
	 * @param tpy the second circle center, y-coordinate.
	 * @param trad the second circle radius.
	 * @since 2.21.0
	 */
	public static boolean getCircleIntersection(double spx, double spy, double srad, double tpx, double tpy, double trad)
	{
		return RMath.getLineLength(spx, spy, tpx, tpy) < srad + trad;
	}

	/**
	 * Returns if a circle and box intersect.  
	 * @param ccx the circle center, x-coordinate.
	 * @param ccy the circle center, y-coordinate.
	 * @param crad the circle radius.
	 * @param bcx the box center, x-coordinate.
	 * @param bcy the box center, y-coordinate.
	 * @param bhw the box half width.
	 * @param bhh the box half height.
	 * @return if an intersection occurred.
	 * @since 2.21.0
	 */
	public static boolean getCircleBoxIntersection(double ccx, double ccy, double crad, double bcx, double bcy, double bhw, double bhh)
	{
		double tx0 = bcx - bhw;
		double tx1 = bcx + bhw;
		double ty0 = bcy - bhh;
		double ty1 = bcy + bhh;

		// Voronoi Region Test.
		if (ccx < tx0)
		{
			if (ccy < ty0)
				return RMath.getLineLength(ccx, ccy, tx0, ty0) < crad;
			else if (ccy > ty1)
				return RMath.getLineLength(ccx, ccy, tx0, ty1) < crad;
			else
				return RMath.getLineLength(ccx, ccy, tx0, ccy) < crad;
		}
		else if (ccx > tx1)
		{
			if (ccy < ty0)
				return RMath.getLineLength(ccx, ccy, tx1, ty0) < crad;
			else if (ccy > ty1)
				return RMath.getLineLength(ccx, ccy, tx1, ty1) < crad;
			else
				return RMath.getLineLength(ccx, ccy, tx1, ccy) < crad;
		}
		else
		{
			if (ccy < ty0)
				return RMath.getLineLength(ccx, ccy, ccx, ty0) < crad;
			else if (ccy > ty1)
				return RMath.getLineLength(ccx, ccy, ccx, ty1) < crad;
			else // circle center is inside box
				return true;
		}

	}

	/**
	 * Returns the doubled signed area of a triangular area made up of 3 points.  
	 * @param spx the first box center, x-coordinate.
	 * @param spy the first box center, y-coordinate.
	 * @param shw the first box half width.
	 * @param shh the first box half height.
	 * @param tpx the second box center, x-coordinate.
	 * @param tpy the second box center, y-coordinate.
	 * @param thw the second box half width.
	 * @param thh the second box half height.
	 * @return if an intersection occurred.
	 * @since 2.21.0
	 */
	public static boolean getBoxIntersection(double spx, double spy, double shw, double shh, double tpx, double tpy, double thw, double thh)
	{
		if (spx < tpx) // box to the left.
		{
			if (spx + shw < tpx - thw)
				return false;
			
			if (spy < tpy) // box to the bottom.
			{
				if (spy + shh < tpy - thh)
					return false;
				
				return true;
			}
			else // box to the top.
			{
				if (spy - shh > tpy + thh)
					return false;
				
				return true;
			}
		}
		else // box to the right
		{
			if (spx - shw > tpx + thw)
				return false;
	
			if (spy < tpy) // box to the bottom.
			{
				if (spy + shh < tpy - thh)
					return false;
				
				return true;
			}
			else // box to the top.
			{
				if (spy - shh > tpy + thh)
					return false;
				
				return true;
			}
		}
	}
	
	/**
	 * Calculates the intersection point as the result of a <code>getLineSegmentIntersection</code> call.
	 * @param out the point to write the information to.
	 * @param ax the first line segment, first point, x-coordinate.
	 * @param ay the first line segment, first point, y-coordinate.
	 * @param bx the first line segment, second point, x-coordinate.
	 * @param by the first line segment, second point, y-coordinate.
	 * @param intersectionScalar the scalar along the line.
	 * @see #getLineSegmentIntersection(double, double, double, double, double, double, double, double)
	 */
	public static void getIntersectionPoint(Point2D out, double ax, double ay, double bx, double by, double intersectionScalar)
	{
		out.x = ax + intersectionScalar * (bx - ax);
		out.y = ay + intersectionScalar * (by - ay);
	}
	
}
