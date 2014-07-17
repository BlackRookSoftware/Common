package com.blackrook.commons;

import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.list.List;

/**
 * A trie is a data structure that contains objects, using a path
 * of objects derived from the stored value. This structure is not thread-safe - wrap calls
 * with synchronized blocks if necessary.
 * @author Matthew Tropiano
 * @param <V> the value type that this holds.
 * @param <S> the type of the split segments used for searching.
 */
public abstract class AbstractTrie<V extends Object, S extends Object> implements AbstractSet<V>, Sizable
{
	/** Root Node. */
	private Node<V, S> root;
	/** Current size. */
	private int size;

	/**
	 * Creates a new trie.
	 */
	public AbstractTrie()
	{
		root = new Node<V, S>();
		size = 0;
	}

	/**
	 * Creates the segments necessary to find/store values.
	 * This should always create the same segments for the same value.
	 * @param value the value to generate significant segments for.
	 * @return the list of segments for the value.
	 */
	protected abstract S[] getSegments(V value);

	/**
	 * Returns all values in the order that they are found on the way through the Trie searching for a
	 * particular value. Result may include the value searched for.
	 * <p>The results are added to the end of the list.
	 * @param value the value to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getPrevious(V value, List<V> out)
	{
		return getPrevious(value, out, out.size());
	}
	
	/**
	 * Returns all values in the order that they are found on the way through the Trie searching for a
	 * particular value. Result may include the value searched for.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param value the value to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the amount of items returned into the list.
	 */
	public int getPrevious(V value, List<V> out, int startOffset)
	{
		Result<V, S> result = search(value, true, false);
		int added = 0;
		for (V obj : result.encounteredValues)
			out.replace(startOffset + (added++), obj);
		return added;
	}
	
	/**
	 * Returns all values descending from the end of a search for a
	 * particular value. Result may include the value searched for. 
	 * <p>The values returned may not be returned in any consistent or stable order.
	 * <p>The results are added to the end of the list.
	 * @param value the value to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getDescendants(V value, List<V> out)
	{
		return getDescendants(value, out, out.size());
	}

	/**
	 * Returns all values descending from the end of a search for a
	 * particular value. Result may include the value searched for. 
	 * <p>The values returned may not be returned in any consistent or stable order.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param value the value to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the amount of items returned into the list.
	 */
	public int getDescendants(V value, List<V> out, int startOffset)
	{
		Result<V, S> result = search(value, false, true);
		int added = 0;
		for (V obj : result.remainderValues)
			out.replace(startOffset + (added++), obj);
		return added;
	}
	
	/**
	 * Returns the last-encountered value down a trie search.
	 * This is the remainder of the segments generated by the key from the last-matched
	 * segment.
	 * @param value the value to search for.
	 * @return the segment remainder.
	 */
	public V getValueWithRemainder(V value, List<S> out, int startOffset)
	{
		Result<V, S> result = search(value, true, false);
		
		if (result.foundValue != null)
		{
			return result.foundValue;
		}
		else
		{
			for (int i = result.moves; i < result.segments.length; i++)
				out.replace(startOffset + (i - result.moves), result.segments[i]);
			
			return result.encounteredValues.getByIndex(result.encounteredValues.size() - 1);
		}
	}

	@Override
	public void put(V value)
	{
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null.");
		
		S[] segments = getSegments(value);
		int segindex = 0;
		
		Node<V, S> current = root;
		Node<V, S> next = null;
		while (segindex < segments.length)
		{
			if ((next = current.edgeMap.get(segments[segindex])) == null)
				current.edgeMap.put(segments[segindex], next = new Node<V, S>());
			
			current = next;
			segindex++;
		}
		
		V prevval = current.value;
		current.value = value;
		if (prevval == null)
			size++;
	}

	@Override
	public boolean contains(V object)
	{
		return equalityMethod(object, search(object, false, false).foundValue);
	}

	@Override
	public boolean remove(V object)
	{
		S[] segments = getSegments(object);
		if (removeRecurse(object, root, segments, 0))
		{
			size--;
			return true;
		}
		return false; 
	}
	
	@Override
	public boolean equalityMethod(V object1, V object2)
	{
		if (object1 == null && object2 != null)
			return false;
		else if (object1 != null && object2 == null)
			return false;
		else if (object1 == null && object2 == null)
			return true;
		return object1.equals(object2);
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	@Override
	public ResettableIterator<V> iterator()
	{
		return new TrieIterator(this);
	}

	/**
	 * Dumps a representation of this trie as a string representing an object hierarchy.
	 */
	public String toHierarchyString()
	{
		return null;
	}
	
	/**
	 * Returns a search result generated from walking the edges of the trie looking for
	 * a particular value.
	 * @param value the value to search for.
	 * @param includeEncountered if true, includes encountered (on the way) during the search.
	 * @param includeDescendants if true, includes descendants (remainder) after the search.
	 * @return a trie {@link Result}. The result contents describe matches, encounters, and remainder, plus hops.
	 */
	protected Result<V, S> search(V value, boolean includeEncountered, boolean includeDescendants)
	{
		S[] segments = getSegments(value);
		List<V> encountered = includeEncountered ? new List<V>() : null;
		List<V> descending = includeDescendants ? new List<V>() : null;
		int segindex = 0;
		
		Node<V, S> current = root;
		while (segindex < segments.length && current.hasEdges())
		{
			if (encountered != null && current.value != null)
				encountered.add(current.value);
				
			if (current.edgeMap.containsKey(segments[segindex]))
			{
				current = current.edgeMap.get(segments[segindex]);
				segindex++;
			}
			else
				break;
		}
		
		if (descending != null)
			getDescendantsRecurse(current, descending);
		
		return new Result<V, S>(
			equalityMethod(value, current.value) ? value : null,
			encountered,
			descending,
			segments,
			segindex
		);
	}

	/**
	 * Returns all possible values that can be used based on an input key.
	 */
	private void getDescendantsRecurse(Node<V, S> start, List<V> accum)
	{
		V value = start.getValue();
		if (value != null)
			accum.add(value);
		
		for (ObjectPair<S, Node<V, S>> pair : start.edgeMap)
			getDescendantsRecurse(pair.getValue(), accum);
	}

	/**
	 * Recurses through the trie for an object and removes it, cleaning up empty
	 * nodes in the way back. 
	 * @param object the object to look for.
	 * @param node the starting node.
	 * @return true if any removal occurred down the tree, false if not.
	 */
	private boolean removeRecurse(V object, Node<V, S> node, S[] segments, int sidx)
	{
		if (equalityMethod(node.value, object))
		{
			node.value = null;
			return true;
		}
		
		Node<V, S> next = node.edgeMap.get(segments[sidx]);
		if (next == null)
			return false;
		
		if (removeRecurse(object, next, segments, sidx + 1))
		{
			if (next != root && next.isExpired())
				node.edgeMap.removeUsingKey(segments[sidx]);
			return true;
		}
		else
			return false;
	}

	/**
	 * Iterator for this Trie.
	 */
	protected class TrieIterator implements ResettableIterator<V>
	{
		private AbstractTrie<V, S> self;
		private Queue<Node<V, S>> edgeQueue;
		private V next;
		
		TrieIterator(AbstractTrie<V, S> trie)
		{
			self = trie;
			reset();
		}
	
		@Override
		public boolean hasNext()
		{
			return !edgeQueue.isEmpty();
		}
	
		@Override
		public V next()
		{
			while (!edgeQueue.isEmpty())
			{
				Node<V, S> node = seekForQueue();
				if (node.value != null)
					return next = node.value;
			}
			return null;
		}
	
		@Override
		public void remove()
		{
			self.remove(next);
		}
	
		@Override
		public void reset()
		{
			this.edgeQueue = new Queue<Node<V, S>>();
			this.edgeQueue.enqueue(root);
			seekForQueue();
		}
		
		private Node<V, S> seekForQueue()
		{
			Node<V, S> deq = edgeQueue.dequeue();
			for (ObjectPair<S, Node<V, S>> pair : deq.edgeMap)
				edgeQueue.enqueue(pair.getValue());
			return deq;
		}
		
	}

	/**
	 * A result of a passive search on a trie.
	 */
	protected static class Result<V, S>
	{
		private V foundValue;
		private List<V> encounteredValues;
		private List<V> remainderValues;
		private S[] segments;
		private int moves;
		
		Result(V found, List<V> encountered, List<V> remainder, S[] segments, int moves)
		{
			this.foundValue = found;
			this.encounteredValues = encountered;
			this.remainderValues = remainder;
			this.segments = segments;
			this.moves = moves;
		}
		
		/**
		 * Returns the value on the result, if.
		 */
		public V getFoundValue() 
		{
			return foundValue;
		}
		
		/**
		 * Returns the list of values found along the way of a search.
		 */
		public List<V> getEncounteredValues()
		{
			return encounteredValues;
		}
		
		/**
		 * Returns the list of values descending from the endpoint of a search.
		 */
		public List<V> getDescendantValues()
		{
			return remainderValues;
		}
		
		/**
		 * Returns the segments generated by the input value.
		 */
		public S[] getSegments()
		{
			return segments;
		}
		
		/**
		 * Returns how many edge hops that this performed in order to reach the result.
		 */
		public int getMoveCount() 
		{
			return moves;
		}
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Found: ").append(foundValue).append('\n');
			sb.append("Encountered: ").append(String.valueOf(encounteredValues)).append('\n');
			sb.append("Remainder: ").append(String.valueOf(remainderValues)).append('\n');
			sb.append("Moves: ").append(moves);
			return sb.toString();
		}
		
	}

	/**
	 * A single node in the Trie.
	 */
	private static class Node<V, S>
	{
		/** Edge map. */
		private AbstractMap<S, Node<V, S>> edgeMap;
		/** Value stored at this node. Can be null. */
		private V value;
		
		protected Node()
		{
			edgeMap = new HashMap<S, Node<V, S>>(2, 1f);
			value = null;
		}
		
		/**
		 * Returns this node's value.
		 */
		public V getValue()
		{
			return value;
		}
		
		/**
		 * Returns if this node can be cleaned up.
		 */
		public boolean isExpired()
		{
			return edgeMap.isEmpty() && value == null;
		}
		
		/**
		 * Returns if the node has possible paths.
		 */
		public boolean hasEdges()
		{
			return !edgeMap.isEmpty();
		}
		
	}

}


