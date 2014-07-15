package com.blackrook.commons.trie;

import com.blackrook.commons.AbstractTrie;

/**
 * An implementation of a Trie that stores strings, case-insensitively.
 * @author Matthew Tropiano
 */
public class CaseInsensitiveTrie extends AbstractTrie<String, Character>
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

}
