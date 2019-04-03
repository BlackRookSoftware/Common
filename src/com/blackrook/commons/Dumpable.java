/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * Defines a class that has a method for dumping the contents of a data structure to an array.
 * @author Matthew Tropiano
 * @param <T> target type.
 */
public interface Dumpable<T> 
{
	/**
	 * Copies the contents of this class into an array.
	 * The order of the contents are not guaranteed unless otherwise noted.
	 * @param out the target array to copy the objects into.
	 * @throws ArrayIndexOutOfBoundsException if the target array is too small to contain the objects.
	 * @since 2.31.0
	 */
	public void toArray(T[] out);

}
