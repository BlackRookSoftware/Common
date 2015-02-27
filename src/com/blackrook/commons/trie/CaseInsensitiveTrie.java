/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.trie;

/**
 * An implementation of a Trie that stores strings, case-insensitively.
 * @author Matthew Tropiano
 */
public class CaseInsensitiveTrie extends StringTrie
{
	public CaseInsensitiveTrie()
	{
		super();
	}
	
	@Override
	protected Character[] getSegments(String value)
	{
		Character[] out = new Character[value.length()];
		for (int i = 0; i < value.length(); i++)
			out[i] = Character.toLowerCase(value.charAt(i));
		return out;
	}

	@Override
	public boolean equalityMethod(String object1, String object2)
	{
		if (object1 == null && object2 != null)
			return false;
		else if (object1 != null && object2 == null)
			return false;
		else if (object1 == null && object2 == null)
			return true;
		return object1.equalsIgnoreCase(object2);
	}
	
}
