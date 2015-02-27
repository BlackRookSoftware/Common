/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import com.blackrook.commons.list.List;

/**
 * A trie is a data structure that maps an object to another object, using a path
 * of objects derived from the key. This structure is not thread-safe - wrap calls
 * with synchronized blocks if necessary.
 * @author Matthew Tropiano
 * @param <V> the value type that this holds.
 * @param <S> the type of the split segments used for searching.
 */
public abstract class AbstractTrieMap<K extends Object, V extends Object, S extends Object> 
	extends AbstractTrie<ObjectPair<K, V>, S> implements AbstractMap<K, V>
{
	/**
	 * Creates a new trie map.
	 */
	public AbstractTrieMap()
	{
		super();
	}

	@Override
	protected final S[] getSegments(ObjectPair<K, V> pair)
	{
		return getSegmentsForKey(pair.getKey());
	}

	/**
	 * Creates the segments necessary to find/store values with keys.
	 * This should always create the same segments for the same key.
	 * @param key the key to generate significant segments for.
	 * @return the list of segments for the key.
	 */
	protected abstract S[] getSegmentsForKey(K key);

	@Override
	public void put(K key, V value)
	{
		super.put(new ObjectPair<K, V>(key, value));
	}

	@Override
	public boolean containsKey(K key)
	{
		Result<ObjectPair<K, V>, S> out = searchByKey(key, false, false);
		return out.getFoundValue() != null;
	}

	/**
	 * Returns a value for the key provided.
	 * @param key the key.
	 * @return the corresponding value, or null if there is no value associated with that key.
	 */
	public V get(K key)
	{
		Result<ObjectPair<K, V>, S> out = searchByKey(key, false, false);
		return out.getFoundValue() != null ? out.getFoundValue().getValue() : null;
	}

	/**
	 * Returns all values in the order that they are found on the way through the Trie searching for a
	 * particular corresponding value. Result may include the value corresponding to the key.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param key the key to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getBeforeKey(K key, List<V> out)
	{
		return getBeforeKey(key, out, out.size());
	}
	
	/**
	 * Returns all values in the order that they are found on the way through the Trie searching for a
	 * particular corresponding value. Result may include the value corresponding to the key.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param key the key to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the amount of items returned into the list.
	 */
	public int getBeforeKey(K key, List<V> out, int startOffset)
	{
		Result<ObjectPair<K, V>, S> result = searchByKey(key, true, false);
		int added = 0;
		for (ObjectPair<K, V> pair : result.getEncounteredValues())
			out.replace(startOffset + (added++), pair.getValue());
		return added;
	}
	
	/**
	 * Returns all values descending from the end of a search for a
	 * particular key. Result may include the value corresponding to the key. 
	 * <p>The values returned may not be returned in any consistent or stable order.
	 * <p>The results are added to the end of the list.
	 * @param key the key to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getAfterKey(K key, List<V> out)
	{
		return getAfterKey(key, out, out.size());
	}

	/**
	 * Returns all values descending from the end of a search for a
	 * particular value. Result may include the value corresponding to the key. 
	 * <p>The values returned may not be returned in any consistent or stable order.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param key the key to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the amount of items returned into the list.
	 */
	public int getAfterKey(K key, List<V> out, int startOffset)
	{
		Result<ObjectPair<K, V>, S> result = searchByKey(key, false, true);
		int added = 0;
		for (ObjectPair<K, V> pair : result.getDescendantValues())
			out.replace(startOffset + (added++), pair.getValue());
		return added;
	}

	/**
	 * Returns the last-encountered value down a trie search.
	 * This is the remainder of the segments generated by the key from the last-matched
	 * segment.
	 * @param key the key to search for.
	 * @param out the output list.
	 * @return the last-encountered value.
	 */
	public V getWithRemainderByKey(K key, List<S> out)
	{
		return getWithRemainderByKey(key, out, 0);
	}

	/**
	 * Returns the last-encountered value down a trie search.
	 * This is the remainder of the segments generated by the key from the last-matched
	 * segment.
	 * @param key the key to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the last-encountered value, or null if none encountered.
	 */
	public V getWithRemainderByKey(K key, List<S> out, int startOffset)
	{
		Result<ObjectPair<K, V>, S> result = searchByKey(key, true, false);
		
		if (result.getFoundValue() != null)
		{
			return result.getFoundValue().getValue();
		}
		else
		{
			for (int i = result.getMovesToLastEncounter(); i < result.getSegments().length; i++)
				out.replace(startOffset + (i - result.getMovesToLastEncounter()), result.getSegments()[i]);
			
			if (!result.getEncounteredValues().isEmpty())
				return result.getEncounteredValues().getByIndex(result.getEncounteredValues().size() - 1).getValue();
			else
				return null;
		}
	}

	/**
	 * Returns all keys in the order that they are found on the way through the Trie searching for a
	 * particular matching key. Result may include the provided key.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param key the key to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getKeysBeforeKey(K key, List<K> out)
	{
		return getKeysBeforeKey(key, out, out.size());
	}

	/**
	 * Returns all keys in the order that they are found on the way through the Trie searching for a
	 * particular matching key. Result may include the the provided key.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param key the key to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set keys.
	 * @return the amount of items returned into the list.
	 */
	public int getKeysBeforeKey(K key, List<K> out, int startOffset)
	{
		Result<ObjectPair<K, V>, S> result = searchByKey(key, true, false);
		int added = 0;
		for (ObjectPair<K, V> pair : result.getEncounteredValues())
			out.replace(startOffset + (added++), pair.getKey());
		return added;
	}

	/**
	 * Returns all keys descending from the end of a search for a
	 * particular key. Result may include the provided key. 
	 * <p>The keys returned may not be returned in any consistent or stable order.
	 * <p>The results are added to the end of the list.
	 * @param key the key to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getKeysAfterKey(K key, List<K> out)
	{
		return getKeysAfterKey(key, out, out.size());
	}

	/**
	 * Returns all keys descending from the end of a search for a
	 * particular key. Result may include the provided key. 
	 * <p>The keys returned may not be returned in any consistent or stable order.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param key the key to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set keys.
	 * @return the amount of items returned into the list.
	 */
	public int getKeysAfterKey(K key, List<K> out, int startOffset)
	{
		Result<ObjectPair<K, V>, S> result = searchByKey(key, false, true);
		int added = 0;
		for (ObjectPair<K, V> pair : result.getDescendantValues())
			out.replace(startOffset + (added++), pair.getKey());
		return added;
	}

	/**
	 * Returns the last-encountered key down a trie search, plus
	 * the remainder of the segments generated by the key from the last-matched segment.
	 * @param key the key to search for.
	 * @param out the output list.
	 * @return the last-encountered value.
	 */
	public K getKeyWithRemainderByKey(K key, List<S> out)
	{
		return getKeyWithRemainderByKey(key, out, 0);
	}

	/**
	 * Returns the last-encountered key down a trie search, plus
	 * the remainder of the segments generated by the key from the last-matched segment.
	 * @param key the key to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set keys.
	 * @return the last-encountered value.
	 */
	public K getKeyWithRemainderByKey(K key, List<S> out, int startOffset)
	{
		Result<ObjectPair<K, V>, S> result = searchByKey(key, true, false);
		
		if (result.getFoundValue() != null)
		{
			return result.getFoundValue().getKey();
		}
		else
		{
			for (int i = result.getMovesToLastEncounter(); i < result.getSegments().length; i++)
				out.replace(startOffset + (i - result.getMovesToLastEncounter()), result.getSegments()[i]);
			
			return result.getEncounteredValues().getByIndex(result.getEncounteredValues().size() - 1).getKey();
		}
	}

	/**
	 * Search using a key.
	 */
	protected Result<ObjectPair<K, V>, S> searchByKey(K key, boolean includeEncountered, boolean includeDescendants)
	{
		return search(new ObjectPair<K, V>(key, null), includeEncountered, includeDescendants);
	}
	

	@Override
	public V removeUsingKey(K key)
	{
		ObjectPair<K, V> p = new ObjectPair<K, V>(key, null);
		S[] segments = getSegments(p);
		if ((p = removeRecurse(p, root, segments, 0)) != null)
		{
			return p.getValue();
		}
		return null; 
	}

	@Override
	public final boolean equalityMethod(ObjectPair<K, V> object1, ObjectPair<K, V> object2)
	{
		if (object1 == null && object2 != null)
			return false;
		else if (object1 != null && object2 == null)
			return false;
		else if (object1 == null && object2 == null)
			return true;
		return equalityMethodForKey(object1.getKey(), object2.getKey());
	}

	@Override
	public boolean equalityMethodForKey(K key1, K key2)
	{
		if (key1 == null && key2 != null)
			return false;
		else if (key1 != null && key2 == null)
			return false;
		else if (key1 == null && key2 == null)
			return true;
		return key1.equals(key2);
	}
	
}


