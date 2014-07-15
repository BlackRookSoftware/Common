package com.blackrook.commons.trie;

import com.blackrook.commons.AbstractTrie;

/**
 * An implementation of a Trie that stores strings separated by a set of delimiters.
 * @author Matthew Tropiano
 */
public class TokenTrie extends AbstractTrie<String, String>
{
	/** RegEx expression for value split. */
	private String splitRegex; 

	public TokenTrie(String splitRegex)
	{
		this.splitRegex = splitRegex;
	}
	
	@Override
	protected String[] getSegments(String value)
	{
		return value.split(splitRegex);
	}

}
