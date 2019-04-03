/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Custom Linked List data structure.
 * Encompasses behavior for stacks and queues. 
 * @author Matthew Tropiano
 */
public abstract class AbstractLinkedList<T extends Object> implements ResettableIterable<T>, Sizable
{
	/** Head of the Linked List. */
    protected Node<T> head;
	/** Tail of the Linked List. */
    protected Node<T> tail;
	/** The amount of elements in the list. */
    protected int size;
    
    /**
     * Creates a new Linked List.
     */
    public AbstractLinkedList()
    {
    	clear();
    }
    
    /**
     * Clears the contents of this list.
     */
    public void clear()
    {
		if (isEmpty()) return;
        head = null;
        tail = null;
        size = 0;
    }
    
    /**
	 * Checks if an object is present in the list via testing
	 * if it is equal to the others. If c is null, this returns false.
	 * Runs at O(n) time.
	 * @param object the object reference.
	 * @return true if it is in the list, false otherwise.
	 */
	public boolean contains(T object)
	{
		if (object == null)
			return false;
		
	    Node<T> curr = head;
	    while (curr != null)
	    {
	        if (object.equals(curr.data))
	            return true;
	        curr = curr.next;
	    }
	    return false;
	}

	/**
     * Adds an object to the list.
     * Time is O(1).
     * If object is null, nothing happens.
	 * @param object the object to add.
     */
    public void add(T object)
    {
    	if (object == null)
    		return;
    	
        if (head == null)
        {
        	Node<T> n = new Node<T>(object,null);
        	head = n;
            tail = n;
        }
        else
        {
        	tail.next = new Node<T>(object, null);
            tail = tail.next;
        }
        
        size++;
    }

    /**
     * Adds an object at a specific place (index) in the list.
     * If n is greater or equal to the size, it is added to the end.
     * If object is null, nothing happens.
     * Runs at O(n) time, where n is the minimum of the index. and the list size.
     * @param index the target index.
	 * @param object the object to add.
     */
    public void addAt(int index, T object)
    {
    	if (object == null || index < 0)
    		return;
        
        int i = 0;
        Node<T> curr = head;
        Node<T> prev = null;
        while (curr != null && i < index)
        {
        	prev = curr;
            curr = curr.next;
            i++;
        }

        Node<T> newNode = new Node<T>(object,curr);
        if (curr == head)
        	head = newNode;
        else
        {
        	prev.next = newNode;
        	if (prev == tail)
        		tail = newNode;
        }
        
        size++;
    }

    /**
     * Adds a series of objects to the list in the order that they are listed.
     * Does not add null objects.
	 * @param objects the objects to add.
	 * @see #add(Object)
     */
    @SuppressWarnings("unchecked")
	public void addAll(T ... objects)
    {
    	for (int i = 0; i < objects.length; i++)
    		add(objects[i]);
    }

	/**
     * Replaces an object at a place in the list with another.
     * Does nothing if n refers to an index outside of list bounds.
     * Runs at O(n) time. If object is null, nothing happens.
     * @param index the desired index.
	 * @param object the object to add.
     */
    public void set(int index, T object)
    {
        if (index < 0 || index >=size || object == null) return;
        
        int i = 0;
        Node<T> curr = head;
        while (curr != null && i < index)
        {
            curr = curr.next;
            i++;
        }

        curr.data = object;
    }
    
    /**
     * Retrieves an object at a specific index in the list.
     * Runs in O(n) time.
     * @param index the desired index.
     * @return null if index refers to an index outside of list bounds, or the object at that index.
     */
    public T get(int index)
    {
        if (index < 0 || index >= size) return null;
        
        int i = 0;
        Node<T> curr = head;
        while (curr != null && i < index)
        {
            curr = curr.next;
            i++;
        }
        return curr.data;
    }
    
    /**
     * Swaps an object in the list for another object, namely obj2.
     * Checks if an object is present in the list via testing
     * if it is equal to the obj1.
	 * Runs at O(n) time. If obj1 or obj2 is null, nothing happens, and this returns false.
	 * @param object1 the first object.
	 * @param object2 the second object.
     * @return true if the switch was made, false if object1 wasn't in the list.
     */
    public boolean change(T object1, T object2)
    {
    	if (object1 == null || object2 == null)
    		return false;
    	
        boolean ret = false;
        Node<T> curr = head;
        while (curr != null && !curr.data.equals(object1))
            curr = curr.next;
        if (curr != null)
        {
        	curr.data = object2;
        	ret = true;
        }
        return ret;
    }

    /**
     * Removes an object in the list at a specific index.
     * Runs in O(n) time.
     * @param index the desired index.
     * @return the object, or null if index refers to an index outside of list bounds.
     */
    public T removeIndex(int index)
    {
        T ret = null;
        int curri = 0;
        Node<T> curr = head;
        Node<T> prev = null;
        
        while (curr != null && curri < index)
        {
            prev = curr;
            curr = curr.next;
            curri++;
        }
        
        if (curr != null)
        {
            ret = curr.data;
            removeNode(curr, prev);
            size--;
        }
        
        return ret;
    }

    /**
     * Removes a specific object in the list.
     * Runs in O(n) time. If c is null, this returns false.
     * @param object the object to search for.
     * @return true if removed, false otherwise.
     */
    public boolean remove(T object)
    {
    	if (object == null)
    		return false;
    	
        boolean ret = false;
        Node<T> curr = head;
        Node<T> prev = null;
        
        while (curr != null && !curr.data.equals(object))
        {
            prev = curr;
            curr = curr.next;
        }
        
        if (curr != null)
        {
            ret = true;
            removeNode(curr, prev);
            size--;
        }

        return ret;
    }

    /**
     * Gets the object at the beginning of the list.
     * If empty, this returns null.
     * Runs in O(1) time.
     * @return the object at the beginning of the list, or null if the list is empty.
     */
    public T head()
    {
    	return head != null ? head.data : null;
    }
    
    /**
     * Gets the object at the end of the list.
     * If empty, this returns null.
     * Runs in O(1) time.
     * @return the object at the end of the list, or null if the list is empty.
     */
    public T tail()
    {
    	return tail != null ? tail.data : null;
    }

    @Override
	public boolean isEmpty()
	{
		return size == 0;
	}

    @Override
    public int size()
    {
        return size;
    }
    
    /**
	 * Returns all of the objects in this list into an array.
	 * @param out the output array.
	 * @throws ArrayIndexOutOfBoundsException if the target array is smaller than the size of the list.
	 */
	public void toArray(T[] out)
	{
	    int i = 0;
	    Node<T> curr = head;
	    while (curr != null)
	    {
	        out[i++] = curr.data;
	        curr = curr.next;
	    }
	}

	@Override
	public String toString()
	{
	    StringBuilder out = new StringBuilder();
	    out.append("[");
	    for (Node<T> curr = head; curr != null; curr = curr.next)
	    {
	        out.append(curr.data.toString());
	        if (curr.next != null)
	        	out.append(", ");
	    }
	    out.append("]");
	    return out.toString();
	}

	@Override
	public ResettableIterator<T> iterator()
	{
		return new LLIterator();
	}

	// performs removal logic.
	private void removeNode(Node<T> curr, Node<T> prev)
	{
        if (curr == head && curr == tail)
        {
            head = null;
            tail = null;
        }
        else if (curr == head)
            head = head.next;
        else if (curr == tail)
        {
        	prev.next = null;
        	tail = prev;
        }
        else
            prev.next = curr.next;
	}
	
	/**
	 * Encapsulating node class.
	 */
	protected static class Node<T>
	{
		/** The node's data. */
	    protected T data;

		/** Reference to next list. */
	    protected Node<T> next;
	    
	    /**
	     * Creates a new node.
	     * @param data the data object.
	     * @param next the reference to the next node.
	     */
	    public Node(T data, Node<T> next)
	    {
	        this.data = data;
	        this.next = next;
	    }
	
	    /**
	     * @return the encapsulated data at this node.
	     */
	    public T getData()
	    {
			return data;
		}

	    /**
	     * @return the reference to the next node.
	     */
		public Node<T> getNext()
		{
			return next;
		}

		@Override
		public String toString()
		{
			return data.toString();
		} 
	    
	}

	/**
	 * Base iterator class for all LinkedListAbstracts.
	 */
	protected class LLIterator implements ResettableIterator<T>
	{
		protected Node<T> previous;
		protected Node<T> current;
		protected Node<T> next;
		protected boolean removeCalled;
		
		public LLIterator()
		{
			reset();
		}
		
		@Override
		public boolean hasNext()
		{
			return next != null;
		}
	
		@Override
		public T next()
		{
			previous = current;
			if (current != null)
				current = current.next;
			current = next;
			if (next != null)
				next = next.next;
			removeCalled = false;
			return current.data;
		}
	
		@Override
		public void remove()
		{
			if (removeCalled) 
				throw new IllegalStateException("remove() called before next()");
			removeNode(current, previous);
			current = previous;
			removeCalled = true;
			size--;
		}

		@Override
		public void reset()
		{
			previous = null;
			current = null;
			next = head;
			removeCalled = true;
		}
		
	}
	
}
