/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.trie;

import com.blackrook.commons.AbstractTrie;

/**
 * An implementation of a Trie that stores strings.
 * @author Matthew Tropiano
 * @since 2.20.0
 */
public class StringTrie extends AbstractTrie<String, Character>
{
	public StringTrie()
	{
		super();
	}
	
	@Override
	protected Character[] getSegments(String value)
	{
		Character[] out = new Character[value.length()];
		for (int i = 0; i < value.length(); i++)
			out[i] = value.charAt(i);
		return out;
	}

}
