package com.blackrook.commons.trie;

import com.blackrook.commons.AbstractTrieMap;

/**
 * An implementation of a Trie that stores strings separated by a set of delimiters.
 * @author Matthew Tropiano
 */
public class TokenTrieMap<V extends Object> extends AbstractTrieMap<String, V, String>
{
	/** RegEx expression for value split. */
	private String splitRegex; 

	public TokenTrieMap(String splitRegex)
	{
		this.splitRegex = splitRegex;
	}
	
	@Override
	protected String[] getSegmentsForKey(String value)
	{
		return value.split(splitRegex);
	}

}
