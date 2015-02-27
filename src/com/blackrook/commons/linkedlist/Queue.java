/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.linkedlist;

import com.blackrook.commons.AbstractLinkedList;

/**
 * First-In-First-Out data structure implemented as a linked list.
 * Not efficient for iterating or seeking due to its singly-linked nature. 
 * @author Matthew Tropiano
 */
public class Queue<T extends Object> extends AbstractLinkedList<T>
{

	/**
	 * Puts an object at the end of the queue.
	 */
	public void enqueue(T object)
	{
		add(object);
	}

	/**
	 * Removes an object at the front of the queue,
	 * returns NULL if the queue is empty.
	 */
	public T dequeue()
	{
		if (isEmpty())
			return null;
				
		return removeIndex(0);
	}

}
