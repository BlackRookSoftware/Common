package com.blackrook.commons;

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
	protected final S[] getSegments(ObjectPair<K, V> value)
	{
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
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
	 * Search using a key.
	 * @see #search(Object, boolean, boolean);
	 */
	protected Result<ObjectPair<K, V>, S> searchByKey(K key, boolean includeEncountered, boolean includeDescendants)
	{
		return search(new ObjectPair<K, V>(key, null), false, false);
	}
	

	@Override
	public V removeUsingKey(K key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final boolean equalityMethod(ObjectPair<K, V> object1, ObjectPair<K, V> object2)
	{
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


