/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.comparators;

import java.util.Comparator;

/**
 * A Comparator that compares two Strings as though they were the same case.
 * @author Matthew Tropiano
 * @since 2.12.0
 */
public class CaseInsensitiveComparator implements Comparator<String>
{
	/** The class instance. */
	private static CaseInsensitiveComparator INSTANCE = new CaseInsensitiveComparator();
	
	/** Returns the only instance of this class. */
	public static CaseInsensitiveComparator getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Protected Constructor.
	 */
	protected CaseInsensitiveComparator()
	{
		// Do nothing.
	}
	
	/**
	 * Compares two chars as though they were the same case.
	 * @param c1 the first char.
	 * @param c2 the second char.
	 * @return <code>Character.toUpperCase(c1) - Character.toUpperCase(c2)</code>
	 */
	protected int compareChars(char c1, char c2)
	{
		return Character.toUpperCase(c1) - Character.toUpperCase(c2);
	}
	
	@Override
	public int compare(String s1, String s2)
	{
		int len = Math.min(s1.length(), s2.length());
		int i = 0;
		while (i < len)
		{
			int c = compareChars(s1.charAt(i), s2.charAt(i));
			if (c == 0)
				i++;
			else
				return c;
		}
		
		// if we're here, strings are equivalent up to this.
		// longer is later.
		return s1.length() - s2.length();
	}

}
