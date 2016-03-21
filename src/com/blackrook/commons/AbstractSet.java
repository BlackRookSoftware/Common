/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Defines the rules and methods of a Set structure.
 * @author Matthew Tropiano
 * @param <P> the object type that this data structure stores.
 * @since 2.19.0
 */
public interface AbstractSet<P extends Object> extends ResettableIterable<P>, Sizable
{
	/**
	 * Adds an object to this structure, but only if it does not exist, according to
	 * {@link #contains(Object)}.
	 * The policy of "put" is that if it an object already in the set, this does nothing. Else,
	 * it is added.
	 * @param object the object to add.
	 */
	public void put(P object);

	/**
	 * Checks if an object (by equality) is present in the structure.
	 * @param object the object to use for checking presence.
	 * @return true if it is in the structure, false otherwise.
	 */
	public boolean contains(P object);

	/**
	 * Removes an object from this structure.
	 * @param object the object to use for checking presence.
	 * @return true if it was removed from the structure, false otherwise.
	 */
	public boolean remove(P object);
	
}
