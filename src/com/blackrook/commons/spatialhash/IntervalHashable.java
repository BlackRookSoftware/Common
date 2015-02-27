/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.spatialhash;

/**
 * An object that can be added to an implementor of {@link IntervalHash}.
 * The methods defined here are used by the map to approximate how to hash an object in the map for comparisons later.
 * The map does not update automatically if any of the implementing functions change their values - the object
 * must be re-added/updated in order for that to take effect in the map.
 * @author Matthew Tropiano
 * @since 2.10.0
 * @deprecated Since 2.21.0. Use the spatial indexing packages instead.
 */
@Deprecated
public interface IntervalHashable extends AbstractSpatialHashable
{
	/**
	 * Gets the object's absolute X coordinate in the spatial hash (center).
	 */
	public double getObjectCenterX();

	/**
	 * Gets the object's half-width in the spatial hash.
	 */
	public double getObjectHalfWidth();

	/**
	 * Gets the object's sweep along the X-axis (from its center) in the spatial hash.
	 * This is useful for detecting objects that are in the middle of, 
	 * or anticipated to change between hash queries.
	 */
	public double getObjectSweepX();

}
