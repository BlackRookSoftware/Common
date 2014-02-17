/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.linkedlist;

import com.blackrook.commons.AbstractLinkedList;

/**
 * Last-In-First-Out data structure implemented as a linked list.
 * Not efficient for iterating or seeking due to its singly-linked nature. 
 * @author Matthew Tropiano
 */
public class Stack<T extends Object> extends AbstractLinkedList<T>
{

	/**
	 * Adds an object to the front of the list, similar
	 * to pushing an object onto a stack.
	 * Runs at O(1) time.
	 * @param object the object to add.
	 */
	public void push(T object)
	{
	    if (head == null)
	    {
	        head = new Node<T>(object, null);
	        tail = head;
	    }
	    else
	        head = new Node<T>(object, head);

	    size++;
	}

	/**
	 * Removes the object at the front of the list, like popping
	 * an object off a stack.
	 * Runs at O(1) time.
	 * @return	the object removed, or null if the list is empty.
	 */
	public T pop()
	{
	    return removeIndex(0);
	}

	/**
	 * Returns the topmost element in the stack or null if
	 * the list is empty.
	 */
	public T peek()
	{
	    if (head == null) 
	    	return null;
	    
	    return head.getData();
	}

}
