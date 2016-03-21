/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.index;

import com.blackrook.commons.math.Tuple2D;
import com.blackrook.commons.math.geometry.Point2D;

/**
 * Model interface for two-dimensional spatial indexing. 
 * @author Matthew Tropiano
 * @since 2.21.0
 */
public interface SpatialIndex2DModel<T extends Object>
{
	/**
	 * Gets an object's centerpoint.
	 * @param object the object to inspect.
	 * @param point the output point for the centerpoint info.
	 */
	public void getCenter(T object, Point2D point);

	/**
	 * Gets an object's half-widths.
	 * @param object the object to inspect.
	 * @param halfwidths the output tuple for the halfwidth info.
	 */
	public void getHalfWidths(T object, Tuple2D halfwidths);

}
