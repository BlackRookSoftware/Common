/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.search;

import java.util.Iterator;

import com.blackrook.commons.Common;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.Sizable;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.hash.HashedHashMap;
import com.blackrook.commons.linkedlist.Queue;

/**
 * A search set is a special data structure that stores
 * a large set of objects that can be queried using a phrase, both
 * full or partial.
 * <p>
 * By default, this set uses the output from the object's <code>toString()</code>
 * method to find data that it can aggregate into lookups.
 * <p>
 * This is a heavyweight class - depending on what is stored and how much,
 * this can eat up a lot of memory. Adds are slow. Reads are quick.
 * <p>
 * If an object is added to this set, another add of the same object does nothing.
 * @author Matthew Tropiano
 * @since 2.16.0
 * @deprecated Slated for deprecation on 2.21.0. Better served through tries.
 */
@Deprecated
public class SearchSet<T extends Object> implements Sizable, ResettableIterable<T>
{
	public static boolean DEBUG = Common.parseBoolean(System.getProperty(SearchSet.class.getName()+".debug"), false);
	
	/** An object used as a mutex node for critical-section tasks. */
	private Integer SET_MUTEX;
	
	/** 
	 * A map that holds partial sets - maps string part to full string.
	 * This is not null if and only if partials are requested. 
	 */
	private HashedHashMap<String, String> partialsMap;
	/** String to object map. */
	private HashedHashMap<String, T> objectMap;
	/** Hash of all objects. */
	private Hash<T> allObjects;
	/** Partials length. */
	private int partialLength;
	
	/**
	 * Creates a new, empty SearchSet.
	 * @param partialLength if greater than 0, this describes the minimum partial length
	 * of each partial string.
	 */
	public SearchSet(int partialLength)
	{
		this.partialLength = partialLength;
		
		SET_MUTEX = new Integer(0);
		
		partialsMap = partialLength > 0 
				? new HashedHashMap<String, String>() 
				: null; 
		objectMap = new HashedHashMap<String, T>();
		allObjects = new Hash<T>();
	}

	/**
	 * Checks if this set contains a particular object.
	 * @param object the object to search for.
	 * @return true if this set contains a particular object, false otherwise.
	 */
	public boolean contains(T object)
	{
		return allObjects.contains(object);
	}
	
	/**
	 * Adds an object to this SearchSet.
	 * If the object is already in this SearchSet, this does nothing.
	 * The object's search string is obtained via {@link #getSearchString(Object)}.
	 * @param object the object to add.
	 * @throws NullPointerException if object is null.
	 */
	public void add(T object)
	{
		if (contains(object))
			return;
		
		synchronized (SET_MUTEX)
		{
			String search = getSanitizedString(getSearchString(object));
			int splitIndex = 0;
			
			allObjects.put(object);

			while (splitIndex < search.length())
			{
				int next = search.indexOf(' ', splitIndex);
				next = next < 0 ? search.length() : next;
				
				String token = search.substring(splitIndex, next);
				if (next < search.length())
					next = next + 1;
				
				objectMap.add(token, object);
				
				if (partialLength > 0 && partialLength < token.length())
				{
					int tokenIndex = 0;
					while (tokenIndex + partialLength <= token.length())
					{
						String part = token.substring(tokenIndex, tokenIndex + partialLength);
						partialsMap.add(part, token);
						tokenIndex = tokenIndex + 1;
					}
				}
				
				splitIndex = next;
			}
		}
		
	}

	/**
	 * Outputs a list of all possible objects that match a
	 * search string. The string is broken up into tokens and all results
	 * are merged together into one list. This is also influenced by this set's
	 * partial string contents. 
	 * @param searchPhrase the search phrase to use for searching. 
	 * @param outArray the array to put the results into. If the amount returned
	 * would exceed the length boundary of this array, only that amount of objects are returned.
	 * @param offset the starting offset in the array to return the found objects into. 
	 * @return the number of objects returned.
	 */
	public int search(String searchPhrase, T[] outArray, int offset)
	{
		Hash<T> out = null;

		synchronized (SET_MUTEX)
		{
			Queue<String> tokenQueue = new Queue<String>();
	
			for (String token : searchPhrase.split("\\s"))
			{
				if (partialLength > 0)
				{
					Hash<String> partq = partialsMap.get(token);
					if (partq != null)
					{
						for (String tq : partq)
							tokenQueue.enqueue(tq);
					}
					else
						tokenQueue.enqueue(token);
				}
			}
			
			
			while (!tokenQueue.isEmpty() && (out == null || !out.isEmpty()))
			{
				String token = tokenQueue.dequeue();
				Hash<T> tokenObjects = objectMap.get(token);
				if (tokenObjects != null)
				{
					if (out == null)
					{
						out = new Hash<T>();
						for (T objt : tokenObjects)
							out.put(objt);
					}
					else
					{
						Hash<T> intersection = new Hash<T>(out.size());
						for (T outObject : out)
							if (tokenObjects.contains(outObject))
								intersection.put(outObject);
	
						// speed up collection maybe.
						out = null;
						
						out = intersection;
					}
				}
			}
		}
		
		int x = 0;
		Iterator<T> it = out.iterator();
		while (it.hasNext() && offset + x < outArray.length)
		{
			 outArray[offset + x] = it.next();
			 x++;
	}
		
		return x;
	}
	
	/**
	 * Returns a string that this SearchSet uses to generate search terms from.
	 * SearchSet takes this string, turns special characters and punctuation in it into whitespace, 
	 * splits it into tokens using the whitespace, and puts it in the search map.
	 * <p>
	 * For example, if this returns <code>"purple monkey dishwasher"</code>, the
	 * tokens <code>"purple"</code>, <code>"monkey"</code>, and <code>"dishwasher"</code>
	 * are used to map to this object, and partials may be generated from each token to
	 * map to those tokens.
	 * <p>
	 * This returns <code>object.toString()</code> unless overridden.
	 * @param object the source object. 
	 * @return a storable search string.
	 * @throws NullPointerException if object is null.
	 */
	public String getSearchString(T object)
	{
		return object.toString();
	}
	
	/**
	 * Returns the string that SearchSet uses for storing an object.
	 * @param searchString the string returned by {@link #getSearchString(Object)}.
	 * @return a sanitized string suitable for storing objects.
	 * @throws NullPointerException if object is null.
	 */
	public static String getSanitizedString(String searchString)
	{
		StringBuilder sb = new StringBuilder();
		
		boolean lastWasSpecial = false;
		for (char c : searchString.toCharArray())
		{
			if (Character.isLetter(c) || Character.isDigit(c))
			{
				sb.append(c);
				lastWasSpecial = false;
			}
			else if (!lastWasSpecial)
			{
				sb.append(' ');
				lastWasSpecial = true;
			}
		}
		
		return sb.toString().trim();
	}
	
	@Override
	public int size()
	{
		return allObjects.size();
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}

	@Override
	public ResettableIterator<T> iterator()
	{
		return allObjects.iterator();
	}
	
}
