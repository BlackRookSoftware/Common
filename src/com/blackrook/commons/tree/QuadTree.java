package com.blackrook.commons.tree;

import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.Sizable;

/**
 * Generic quadtree for storing data in a two-dimensional manner.
 * Like other collections in the Black Rook Commons, this structure is not thread-safe. 
 * You will have to enforce it on your own.
 * @param <X> first object key.
 * @param <Y> second object key.
 * @param <V> object type to store.
 * @author Matthew Tropiano
 * @since 2.21.0
 */
public class QuadTree<X extends Comparable<X>, Y extends Comparable<Y>, V extends Object> implements Sizable, ResettableIterable<V>
{
	/** Root tree node. */
	private Node root;
	/** Current amount of objects in the tree. */
	private int size;
	
	public QuadTree()
	{
		this.root = null;
		this.size = 0;
	}
	
	public void set(X x, Y y, V value)
	{
		// TODO: Finish.
	}
	
	public V get(X x, Y y)
	{
		// TODO: Finish.
		return null;
	}
	
	public V remove(X x, Y y)
	{
		// TODO: Finish.
		return null;
	}
	
	public boolean contains(X x, Y y)
	{
		return get(x, y) != null;
	}

	@Override
	public ResettableIterator<V> iterator()
	{
		// TODO: Finish.
		return null;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	/** A quadtree node.*/
	private class Node
	{
		/** First key. */
		X x;
		/** Second key. */
		Y y;
		/** Stored value (can be null). */
		V value;
		
		Node nw;
		Node sw; 
		Node ne; 
		Node se;
		
		private Node(X x, Y y, V value)
		{
			this.x = x;
			this.y = y;
			this.value = value;
		}
		
	}
	
}
