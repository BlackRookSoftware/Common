/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.util.Iterator;

/**
 * An iterator interface that allows for resetting the iterator to the beginning. 
 * @author Matthew Tropiano
 * @since 2.2.0
 */
public interface ResettableIterator<T extends Object> extends Iterator<T>
{
	/**
	 * Resets this iterator's cursor to the beginning, as though it were just created.
	 */
	public void reset();
}
