/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.scene;

import com.blackrook.commons.math.geometry.Rectangle2F;

/**
 * Generic scene object that defines a viewable area.
 * @author Matthew Tropiano
 * @deprecated Since 2.10.0.
 */
@Deprecated
public class Scene2DCamera implements SceneObject
{
	/** Internal rectangle. */
	protected Rectangle2F bounds;

	/**
	 * Creates a new camera with 0,0,0,0 bounds.
	 */
	public Scene2DCamera()
	{
		this(0,0,0,0);
	}

	/**
	 * Creates a new camera.
	 * @param x starting X-coordinate.
	 * @param y starting Y-coordinate.
	 * @param width rectangle width.
	 * @param height rectangle height.
	 */
	public Scene2DCamera(float x, float y, float width, float height)
	{
		this.bounds = new Rectangle2F(x, y, width, height);
	}
	
	/**
	 * Sets the bounds of the camera.
	 * @param x starting X-coordinate.
	 * @param y starting Y-coordinate.
	 * @param width rectangle width.
	 * @param height rectangle height.
	 */
	public void setBounds(float x, float y, float width, float height)
	{
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
	}
	
	/**
	 * Translates the bounds of the camera.
	 * @param x starting X-coordinate.
	 * @param y starting Y-coordinate.
	 */
	public void translate(float x, float y)
	{
		bounds.x += x;
		bounds.y += y;
	}
	
	/**
	 * Adjusts the bounds of the camera by an amount.
	 * @param x starting X-coordinate.
	 * @param y starting Y-coordinate.
	 */
	public void zoom(float x, float y)
	{
		float newWidth = bounds.width - x;
		float newHeight = bounds.height - y;
		float newX = x - (bounds.width - newWidth) / 2;
		float newY = y - (bounds.height - newHeight) / 2;
		
		bounds.x = newX;
		bounds.y = newY;
		bounds.width = newWidth;
		bounds.height = newHeight;
	}
	
	@Override
	public float getHalfWidth()
	{
		return bounds.width / 2;
	}

	@Override
	public float getHalfHeight()
	{
		return bounds.height / 2;
	}

	@Override
	public float getHalfDepth()
	{
		return 0;
	}

	@Override
	public float getPositionX()
	{
		return bounds.x + (bounds.width / 2);
	}

	@Override
	public float getPositionY()
	{
		return bounds.y + (bounds.height / 2);
	}

	@Override
	public float getPositionZ()
	{
		return 0;
	}

	@Override
	public float getRadius()
	{
		return 0;
	}

	@Override
	public boolean useRadius()
	{
		return false;
	}

}
