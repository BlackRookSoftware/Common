/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.bank;

import java.util.List;

import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.linkedlist.Queue;

/**
 * A data structure that holds a series of data structures
 * using a combination of a sorted list of keys 
 * that correspond to a hashed list of integer keys.
 * @author Matthew Tropiano
 */
public class Bank<T extends Object, U extends Object>
{
	/** Last id to use for added objects. */
	private int lastId; 
	
	/** Hash Map of names. */
	private HashMap<T,Integer> keyHash; 
	/** Hash Map of ids. */
	private HashMap<Integer, BankPair<T,U>> idHash;
	
	/**
	 * Creates a new Bank with capacity 10, rehash ratio 0.75.
	 */
	public Bank()
	{
		this(HashMap.DEFAULT_CAPACITY, HashMap.DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new Bank with capacity <i>cap</i> and rehash ratio 0.75. 
	 * @param cap	the capacity. cannot be negative.
	 */
	public Bank(int cap)
	{
		this(cap, HashMap.DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new Bank.
	 * @param cap	the capacity. cannot be negative.
	 * @param ratio	the ratio of capacity/tablesize. if this ratio is exceeded, the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public Bank(int cap, float ratio)
	{
		lastId = 0;
		keyHash = new HashMap<T,Integer>(cap, ratio); 
		idHash = new HashMap<Integer,BankPair<T,U>>(cap, ratio); 
	}

	/**
	 * Flags all of the records in this Bank.
	 */
	public void flagAll()
	{
		for (ObjectPair<Integer,BankPair<T,U>> hp : idHash)
			hp.getValue().flagged = true;
	}
	
	/**
	 * Removes all of the records in the list that are flagged.
	 */
	public void removeAllFlagged(List<U> removed)
	{
		Queue<T> list = new Queue<T>();
		for (ObjectPair<Integer,BankPair<T,U>> hp : idHash)
			if (hp.getValue().flagged)
				list.add(hp.getValue().name);
		
		while (!list.isEmpty())
			removed.add(removeByKey(list.dequeue()));
	}
	
	/**
	 * Adds an object to the list using a name.
	 * If the object is already in this structure (by name), its flag is cleared.
	 * @param name 	the name of the resource.
	 * @param obj 	the resource itself.
	 */
	public void add(T name, U obj)
	{
		if (keyHash.containsKey(name))
			idHash.get(keyHash.get(name)).flagged = false;
		else
		{
			int id = lastId++;
			keyHash.put(name, id);
			idHash.put(id, new BankPair<T,U>(name,obj));
		}
	}

	/**
	 * Clears the flag of an object if it is in the Bank.
	 * @param name the name of the resource.
	 */
	public void clearFlag(T name)
	{
		if (keyHash.containsKey(name))
			idHash.get(keyHash.get(name)).flagged = false;
	}
	
	/**
	 * Removes an object from the bank by name.
	 * @param name	the name of the object.
	 * @return null if it is not in the Bank, or the object removed.
	 */
	public U removeByKey(T name)
	{
		Integer id = keyHash.removeUsingKey(name);
		if (id != null)
			return removeById(id);
		return null;
	}
	
	/**
	 * Removes an object from the bank by id.
	 * @param id	the id of the object.
	 * @return null if it is not in the Bank, or the object removed.
	 */
	public U removeById(int id)
	{
		BankPair<T,U> bp = idHash.removeUsingKey(id);
		if (bp != null)
		{
			keyHash.removeUsingKey(bp.name);
			return bp.data;
		}
		return null;
	}
	
	/**
	 * Clears all objects from this table and resets the current
	 * id to 0.
	 */
	public void clear()
	{
		keyHash.clear();
		idHash.clear();
		lastId = 0;
	}
	
	/**
	 * Gets a resource from the list using an id number.
	 * Returns null if not found.
	 */
	public U getById(int id)
	{
		BankPair<T,U> bp = idHash.get(id);
		return bp != null ? bp.data : null;
	}
	
	/**
	 * Gets a resource's id by its name.
	 * Returns -1 if not found.
	 */
	public int getIdByKey(T name)
	{
		Integer in = keyHash.get(name);
		return in == null ? -1 : in;
	}

	/**
	 * Return true of an object by a certain name exists in this bank,
	 * false otherwise.
	 */
	public boolean containsKey(T name)
	{
		return getByKey(name) != null;
	}
	
	/**
	 * Gets a resource from the list using a name, case-insensitive.
	 */
	public U getByKey(T name)
	{
		int id = getIdByKey(name);
		if (id < 0) return null;
		return getById(id);
	}
	
	/**
	 * Returns of all of the names associated with objects (all of them) in the bank.
	 */
	public void getAllKeys(T[] out)
	{
		int i = 0;
		for (ObjectPair<T,Integer> hp : keyHash)
			out[i++] = hp.getKey();
	}
	
	/**
	 * Returns of all of the flagged names associated with objects in the bank.
	 */
	public void getAllFlaggedKeys(List<T> out)
	{
		for (ObjectPair<T,Integer> hp : keyHash)
			out.add(hp.getKey());
	}
	
	/**
	 * Returns the amount of objects in the bank.
	 */
	public int size()
	{
		return idHash.size();
	}
	
	/**
	 * The pair that is stored in the bank. 
	 */
	public static class BankPair<T extends Object, U extends Object>
	{
		/** Is this flagged for any reason? */
		boolean flagged;
		/** Name of object. */
		T name;
		/** The object. */
		U data;
		
		/**
		 * Makes a new bank pair.
		 */
		public BankPair(T name, U data)
		{
			this.flagged = false;
			this.name = name;
			this.data = data;
		}
	}
	
}
