/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Interface for objects that return a resettable iterator, not
 * just a plain old iterator.
 * @author Matthew Tropiano
 * @since 2.4.0
 */
public interface ResettableIterable<T extends Object> extends Iterable<T>
{
	/**
	 * Returns a resettable iterator over a set of elements of type T.
	 */
	public ResettableIterator<T> iterator();
}
