/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.spatialhash;

/**
 * An object that can be added to an implementor of {@link SpatialHash2D}.
 * The methods defined here are used by the map to approximate how to hash an object in the map for comparisons later.
 * The map does not update automatically if any of the implementing functions change their values - the object
 * must be re-added/updated in order for that to take effect in the map.
 * @author Matthew Tropiano
 * @since 2.10.0
 * @deprecated Since 2.21.0. Use the spatial indexing packages instead.
 */
@Deprecated
public interface SpatialHashable extends IntervalHashable
{
	/**
	 * Gets the object's absolute Y coordinate in the spatial hash (center).
	 */
	public double getObjectCenterY();

	/**
	 * Gets the object's absolute Z coordinate in the spatial hash (center).
	 */
	public double getObjectCenterZ();

	/**
	 * Gets the object's half-height in the spatial hash.
	 */
	public double getObjectHalfHeight();

	/**
	 * Gets the object's half-depth in the spatial hash.
	 */
	public double getObjectHalfDepth();

	/**
	 * Gets the object's sweep along the Y-axis (from its center) in the spatial hash.
	 * This is useful for detecting objects that are in the middle of, 
	 * or anticipated to change between hash queries.
	 */
	public double getObjectSweepY();

	/**
	 * Gets the object's sweep along the Z-axis (from its center) in the spatial hash.
	 * This is useful for detecting objects that are in the middle of, 
	 * or anticipated to change between hash queries.
	 */
	public double getObjectSweepZ();

	/**
	 * Gets the object's radius in the spatial hash.
	 * <p>
	 * Since this could be an expensive call, this 
	 * is not always used - it is used if the object's {@link #useObjectRadius()} 
	 * function returns true, which leaves it in the hands of the implementor.
	 */
	public double getObjectRadius();

	/**
	 * Should the object's radius value in the spatial hash be used for collision, rather
	 * than its orthogonal measurements?
	 */
	public boolean useObjectRadius();
	
}
