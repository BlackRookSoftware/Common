/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.grid;

import java.util.Random;

import com.blackrook.commons.Grid;
import com.blackrook.commons.math.RMath;

/**
 * This is a tile grid that contains a grid of single-precision floating point data generally
 * used for maps and lookups that require contiguous or mosaic-ed data.
 * These maps have a near-constant lookup time due to this grid being
 * a full lookup grid (multidimensional array).
 * @author Matthew Tropiano
 * @since 2.2.0
 */
public class FloatGrid implements Grid
{
	/** List of grid codes. */
	protected float[][] data;

	/** Width wrap type. */
	protected WrapType widthWrapType;
	/** Height wrap type. */
	protected WrapType heightWrapType;
	/** Depth wrap type. */
	protected WrapType depthWrapType;
	
	/**
	 * Creates a new empty data grid of a specified width and height.
	 * @param width the width of the grid in data values.
	 * @param height the height of the grid in data values.
	 */
	public FloatGrid(int width, int height)
	{
		data = new float[width][height];
		widthWrapType = WrapType.NONE;
		heightWrapType = WrapType.NONE;
		depthWrapType = WrapType.NONE;
	}
	
	/**
	 * Returns the full width of the grid.
	 */
	public int getWidth()
	{
		return data.length;
	}

	/**
	 * Returns the full height of the grid.
	 */
	public int getHeight()
	{
		return data[0].length;
	}

	@Override
	public int getDepth()
	{
		return 1;
	}

	/**
	 * Sets one of the three wrapping types describing how this grid
	 * should wrap coordinates on the width axis.
	 */
	public void setWidthWrapType(WrapType type)
	{
		widthWrapType = type;
	}

	/**
	 * Sets one of the three wrapping types describing how this grid
	 * should wrap coordinates on the height axis.
	 */
	public void setHeightWrapType(WrapType type)
	{
		heightWrapType = type;
	}

	/**
	 * Sets one of the three wrapping types describing how this grid
	 * should wrap coordinates on the depth axis.
	 */
	public void setDepthWrapType(WrapType type)
	{
		depthWrapType = type;
	}

	@Override
	public WrapType getWidthWrapType()
	{
		return widthWrapType;
	}

	@Override
	public WrapType getHeightWrapType()
	{
		return heightWrapType;
	}

	@Override
	public WrapType getDepthWrapType()
	{
		return depthWrapType;
	}

	/**
	 * Tests if a certain point is in the defined grid.
	 * @return true if so, false if not.
	 */
	public boolean isInGrid(int x, int y)
	{	
		return !(x < 0 || x >= getWidth() || y < 0 || y >= getHeight());
	}

	/**
	 * Sets a code at a particular part of the grid.
	 * @param x		the grid position x to set info.
	 * @param y		the grid position y to set info.
	 * @param code	the code to set.
	 */
	public void setCode(int x, int y, float code)
	{
		data[correctX(x)][correctY(y)] = code;
	}

	/**
	 * Sets the code at a particular part of the grid.
	 * @param x	the grid position x to set info.
	 * @param y	the grid position y to set info.
	 */
	public float getCode(int x, int y)
	{
		return data[correctX(x)][correctY(y)];
	}
	
	/**
	 * Samples this grid at a particular sampling coordinate point using nearest-neighbor interpolation.
	 * @param x a coordinate point from 0 to 1, 0 being 0, 1 being the width.
	 * @param y a coordinate point from 0 to 1, 0 being 0, 1 being the height.
	 * @return a sampled value.
	 */
	public float getNearestSample(float x, float y)
	{
		float incs = 1f / getWidth();
		float inct = 1f / getHeight();
		int s = (int)(x / incs);
		int t = (int)(y / inct);
		return getCode(s, t);
	}
	
	/**
	 * Samples this grid at a particular sampling coordinate point using linear interpolation.
	 * @param x a coordinate point from 0 to 1, 0 being 0, 1 being the width.
	 * @param y a coordinate point from 0 to 1, 0 being 0, 1 being the height.
	 * @return a sampled value.
	 */
	public float getLinearSample(float x, float y)
	{
		float s = x * getWidth();
		float t = y * getHeight();
		int ix = (int)(s);
		int iy = (int)(t);
		float interpS = s - ix;
		float interpT = t - iy;
		return ((getCode(ix, iy) * (1f - interpS) + getCode(ix+1, iy) * interpS) * (1f - interpT) + 
				(getCode(ix, iy+1) * (1f - interpS) + getCode(ix+1, iy+1) * interpS) * interpT);
	}
	
	/**
	 * Samples this grid at a particular sampling coordinate point using bilinear interpolation.
	 * @param x a coordinate point from 0 to 1, 0 being 0, 1 being the width.
	 * @param y a coordinate point from 0 to 1, 0 being 0, 1 being the height.
	 * @return a sampled value.
	 */
	public float getBilinearSample(float x, float y)
	{
		float s = x * getWidth();
		float t = y * getHeight();
		int ix = (int)(s);
		int iy = (int)(t);

		float q11 = getCode(ix, iy);
		float q12 = getCode(ix, iy+1);
		float q21 = getCode(ix+1, iy);
		float q22 = getCode(ix+1, iy+1);
		float xmx1 = s - ix;
		float x2mx = ix+1 - s;
		float ymy1 = t - iy;
		float y2my = iy+1 - t;
		
		return (q11*x2mx*y2my)+(q21*xmx1*y2my)+(q12*x2mx*ymy1)+(q22*xmx1*ymy1);
	}
		
	/**
	 * "Corrects" the input for the X axis.
	 */
	protected int correctX(int x)
	{
		int w = getWidth();
		switch (widthWrapType)
		{
			default:
				return x;
			case TILE:
				return x < 0 ? w - ((-x) % w) : x % w;
			case CLAMP:
				return Math.max(Math.min(x, w - 1), 0);
		}
	}

	/**
	 * "Corrects" the input for the Y axis.
	 */
	protected int correctY(int y)
	{
		int h = getHeight();
		switch (heightWrapType)
		{
			default:
				return y;
			case TILE:
				return y < 0 ? h - ((-y) % h) : y % h;
			case CLAMP:
				return Math.max(Math.min(y, h - 1), 0);
		}
	}

	/**
	 * Creates a grid of two-dimensional noise. 
	 * @param random the random number generator.
	 * @param startSize the starting dimensions.
	 * @param startMagnitude the starting output value magnitude.
	 * @param persistence change in magnitude.
	 * @param passes how many times to expand the grid.
	 */
	public static FloatGrid createNoise(Random random, int startSize, float startMagnitude, float persistence, int passes)
	{
		float amplitude = startMagnitude;
		int size = startSize;
		FloatGrid out = null;

		for (int i = 0; i <= passes; i++)
		{
			if (out != null)
			{
				FloatGrid f = new FloatGrid(size, size);
				f.setWidthWrapType(WrapType.TILE);
				f.setHeightWrapType(WrapType.TILE);
				for (int s = 0; s < size; s++)
					for (int t = 0; t < size; t++)
						f.setCode(s, t, out.getBilinearSample((float)s/size, (float)t/size));
				out = f;
			}

			FloatGrid next = new FloatGrid(size, size);
			next.setWidthWrapType(WrapType.TILE);
			next.setHeightWrapType(WrapType.TILE);
			for (int s = 0; s < size; s++)
				for (int t = 0; t < size; t++)
					next.setCode(s, t, random.nextFloat() * amplitude + ((1f - amplitude) / 2));
			
			if (out != null)
			{
				for (int s = 0; s < size; s++)
					for (int t = 0; t < size; t++)
						next.setCode(s, t, (RMath.clampValue((((out.getCode(s, t)*2f) - 1f) + ((next.getCode(s, t)*2f) - 1f)), -1f, 1f) + 1f) / 2f);
			}

			out = next;
			size <<= 1;
			amplitude = startMagnitude * (float)Math.pow(persistence, i+1);
		}
		
		return out;
	}
	
}
