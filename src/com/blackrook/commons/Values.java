package com.blackrook.commons;

/**
 * Simple utility functions around values.
 * Split from {@link Common}.
 * @author Matthew Tropiano
 * @since 2.32.0
 */
public final class Values
{
	private static final String PARSE_ARRAY_SEPARATOR_PATTERN = "(\\s|\\,)+";

	private Values() {}
	
	/**
	 * Attempts to parse a boolean from a string.
	 * If the string is null, this returns false.
	 * If the string does not equal "true" (case ignored), this returns false.
	 * @param s the input string.
	 * @return the interpreted boolean.
	 */
	public static boolean parseBoolean(String s)
	{
		if (s == null || !s.equalsIgnoreCase("true"))
			return false;
		else
			return true;
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted byte.
	 */
	public static byte parseByte(String s)
	{
		if (s == null)
			return 0;
		try {
			return Byte.parseByte(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a short from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted short.
	 */
	public static short parseShort(String s)
	{
		if (s == null)
			return 0;
		try {
			return Short.parseShort(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a char from a string.
	 * If the string is null or the empty string, this returns '\0'.
	 * @param s the input string.
	 * @return the first character in the string.
	 */
	public static char parseChar(String s)
	{
		if (Objects.isEmpty(s))
			return '\0';
		else
			return s.charAt(0);
	}

	/**
	 * Attempts to parse an int from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted integer.
	 */
	public static int parseInt(String s)
	{
		if (s == null)
			return 0;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a long from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted long integer.
	 */
	public static long parseLong(String s)
	{
		if (s == null)
			return 0L;
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	/**
	 * Attempts to parse a float from a string.
	 * If the string is null or the empty string, this returns 0.0f.
	 * @param s the input string.
	 * @return the interpreted float.
	 */
	public static float parseFloat(String s)
	{
		if (s == null)
			return 0f;
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	/**
	 * Attempts to parse a double from a string.
	 * If the string is null or the empty string, this returns 0.0.
	 * @param s the input string.
	 * @return the interpreted double.
	 */
	public static double parseDouble(String s)
	{
		if (s == null)
			return 0.0;
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	/**
	 * Attempts to parse a boolean from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * If the string does not equal "true," this returns false.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted boolean or def if the input string is blank.
	 */
	public static boolean parseBoolean(String s, boolean def)
	{
		if (Objects.isEmpty(s))
			return def;
		else if (!s.equalsIgnoreCase("true"))
			return false;
		else
			return true;
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted byte or def if the input string is blank.
	 */
	public static byte parseByte(String s, byte def)
	{
		if (Objects.isEmpty(s))
			return def;
		try {
			return Byte.parseByte(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a short from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted short or def if the input string is blank.
	 */
	public static short parseShort(String s, short def)
	{
		if (Objects.isEmpty(s))
			return def;
		try {
			return Short.parseShort(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the first character in the string or def if the input string is blank.
	 */
	public static char parseChar(String s, char def)
	{
		if (Objects.isEmpty(s))
			return def;
		else
			return s.charAt(0);
	}

	/**
	 * Attempts to parse an int from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted integer or def if the input string is blank.
	 */
	public static int parseInt(String s, int def)
	{
		if (Objects.isEmpty(s))
			return def;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a long from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted long integer or def if the input string is blank.
	 */
	public static long parseLong(String s, long def)
	{
		if (Objects.isEmpty(s))
			return def;
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	/**
	 * Attempts to parse a float from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted float or def if the input string is blank.
	 */
	public static float parseFloat(String s, float def)
	{
		if (Objects.isEmpty(s))
			return def;
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	/**
	 * Attempts to parse a double from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted double or def if the input string is blank.
	 */
	public static double parseDouble(String s, double def)
	{
		if (Objects.isEmpty(s))
			return def;
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	/**
	 * Attempts to parse an array of booleans from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"true, false, apple, false"</code> becomes <code>[true, false, false, false]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of booleans or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseBoolean(String)
	 */
	public static boolean[] parseBooleanArray(String s, boolean[] def)
	{
		return parseBooleanArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of bytes from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of bytes or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseByte(String)
	 */
	public static byte[] parseByteArray(String s, byte[] def)
	{
		return parseByteArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of shorts from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of shorts or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseShort(String)
	 */
	public static short[] parseShortArray(String s, short[] def)
	{
		return parseShortArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of chars from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"apple, pear, b, g"</code> becomes <code>['a', 'p', 'b', 'g']</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of characters or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseChar(String)
	 */
	public static char[] parseCharArray(String s, char[] def)
	{
		return parseCharArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of integers from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2.1, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of integers or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseInt(String)
	 */
	public static int[] parseIntArray(String s, int[] def)
	{
		return parseIntArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of floats from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0.5, -5.4, 2, grape"</code> becomes <code>[0.5f, -5.4f, 2.0f, 0f]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of floats or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseFloat(String)
	 */
	public static float[] parseFloatArray(String s, float[] def)
	{
		return parseFloatArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of longs from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of long integers or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseLong(String)
	 */
	public static long[] parseLongArray(String s, long[] def)
	{
		return parseLongArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of doubles from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0.5, -5.4, 2, grape"</code> becomes <code>[0.5, -5.4, 2.0, 0.0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of doubles or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseDouble(String)
	 */
	public static double[] parseDoubleArray(String s, double[] def)
	{
		return parseDoubleArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}

	/**
	 * Attempts to parse an array of booleans from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of booleans or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseBoolean(String)
	 */
	public static boolean[] parseBooleanArray(String s, String separatorRegex, boolean[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		boolean[] out = new boolean[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseBoolean(token);
		return out;
	}

	/**
	 * Attempts to parse an array of bytes from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of bytes or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseByte(String)
	 */
	public static byte[] parseByteArray(String s, String separatorRegex, byte[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		byte[] out = new byte[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseByte(token);
		return out;
	}

	/**
	 * Attempts to parse an array of shorts from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of shorts or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseShort(String)
	 */
	public static short[] parseShortArray(String s, String separatorRegex, short[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		short[] out = new short[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseShort(token);
		return out;
	}

	/**
	 * Attempts to parse an array of chars from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of characters or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseChar(String)
	 */
	public static char[] parseCharArray(String s, String separatorRegex, char[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		char[] out = new char[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseChar(token);
		return out;
	}

	/**
	 * Attempts to parse an array of integers from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of integers or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseInt(String)
	 */
	public static int[] parseIntArray(String s, String separatorRegex, int[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		int[] out = new int[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseInt(token);
		return out;
	}

	/**
	 * Attempts to parse an array of floats from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of floats or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseFloat(String)
	 */
	public static float[] parseFloatArray(String s, String separatorRegex, float[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		float[] out = new float[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseFloat(token);
		return out;
	}

	/**
	 * Attempts to parse an array of longs from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of long integers or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseLong(String)
	 */
	public static long[] parseLongArray(String s, String separatorRegex, long[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		long[] out = new long[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseLong(token);
		return out;
	}

	/**
	 * Attempts to parse an array of doubles from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of doubles or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseDouble(String)
	 */
	public static double[] parseDoubleArray(String s, String separatorRegex, double[] def)
	{
		if (Objects.isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		double[] out = new double[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseDouble(token);
		return out;
	}

}
