/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.scene;

import com.blackrook.commons.spatialhash.SpatialHashable;

/**
 * An object that can be added to a scene layer.
 * @author Matthew Tropiano
 * @deprecated Since 2.10.0. Use {@link SpatialHashable} instead.
 */
@Deprecated
public interface SceneObject
{
	/**
	 * Gets the object's absolute X coordinate in the scene (center).
	 */
	public float getPositionX();

	/**
	 * Gets the object's absolute Y coordinate in the scene (center).
	 */
	public float getPositionY();

	/**
	 * Gets the object's absolute Z coordinate in the scene (center).
	 */
	public float getPositionZ();

	/**
	 * Gets the object's half-width.
	 */
	public float getHalfWidth();

	/**
	 * Gets the object's half-height.
	 */
	public float getHalfHeight();

	/**
	 * Gets the object's half-depth.
	 */
	public float getHalfDepth();

	/**
	 * Gets the object's radius.
	 * <p>
	 * Since this could be an expensive call, this 
	 * is not always used - it is used if the object's useRadius() 
	 * function returns true, which leaves it in the hands of the implementor.
	 */
	public float getRadius();

	/**
	 * Should the object's radius value be used for collision, rather
	 * than its half-height or half-width?
	 */
	public boolean useRadius();
	
}
