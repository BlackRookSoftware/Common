package com.blackrook.commons.trie;

import com.blackrook.commons.AbstractTrie;

/**
 * An implementation of a Trie that stores strings.
 * @author Matthew Tropiano
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
