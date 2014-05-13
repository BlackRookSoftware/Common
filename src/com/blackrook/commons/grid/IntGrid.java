/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.grid;

import com.blackrook.commons.Grid;

/**
 * This is a tile grid that contains a grid of integer data generally
 * used for maps and lookups that require contiguous or mosaic-ed data.
 * These maps have a near-constant lookup time due to this grid being
 * a full lookup grid (multidimensional array).
 * @author Matthew Tropiano
 */
public class IntGrid implements Grid
{
	/** List of grid codes. */
	protected int[][] data;

	/** Using text area bounds for printing text? */
	protected boolean useArea;
	/** Using wrapping by word? */
	protected boolean wrapWord;
	/** Current text area bounds for printing text, start X. */
	protected int textStartX;
	/** Current text area bounds for printing text, start Y. */
	protected int textStartY;
	/** Current text area bounds for printing text, end X. */
	protected int textEndX;
	/** Current text area bounds for printing text, end Y. */
	protected int textEndY;
	/** Current carat position, X. */
	protected int cursorX;
	/** Current carat position, Y. */
	protected int cursorY;
	
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
	public IntGrid(int width, int height)
	{
		data = new int[width][height];
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
	public void setCode(int x, int y, int code)
	{
		data[correctX(x)][correctY(y)] = code;
	}

	/**
	 * Sets the code at a particular part of the grid.
	 * @param x	the grid position x to set info.
	 * @param y	the grid position y to set info.
	 */
	public int getCode(int x, int y)
	{
		return data[correctX(x)][correctY(y)];
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
	 * Sets the text cursor position for the next writing of text data.
	 * This also clears the flag that says that we are writing in a text area.
	 * Coordinates, in this case, start in the lower-left. 
	 * @param x the x coordinate of the cursor.
	 * @param y the y coordinate of the cursor.
	 */
	public void setCursor(int x, int y)
	{
		useArea = false;
		cursorX = x;
		cursorY = y;
	}

	/**
	 * Prints a message to the text grid at the current cursor position. 
	 * The grid codes set in the grid correspond immediately to Unicode 
	 * character codes.
	 * <p>
	 * It will wrap to the next line if there is no more room on the line to print.
	 * It will wrap by word if it is set.
	 * @param seq the character sequence.
	 */
	public void print(CharSequence seq)
	{
		int i = 0;
		int areaEndY = textEndY;
	
		// non-word wrap
		while (i < seq.length() && ((!useArea && cursorY >= 0) || (useArea && cursorY >= areaEndY)))
		{
			char c = seq.charAt(i);
			setChar(c);
			nextColumn();
			if (cursorX == getWidth() || (useArea && cursorX > textEndX))
			{
				cursorX = useArea ? textStartX : 0; 
				nextLine();
			}
			i++;
		}
	}

	/**
	 * Sets the character at the current cursor position.
	 */
	public void setChar(char c)
	{
		setCode(cursorX, cursorY, c);
	}

	/**
	 * Sets the cursor to the next line.
	 * This essentially decrements cursorY by 1.
	 */
	public void nextLine()
	{
		cursorY--;
	}

	/**
	 * Advances the cursor once to the right.
	 * This essentially increments cursorX by 1.
	 */
	public void nextColumn()
	{
		cursorX++;
	}

	/**
	 * Resets the cursor to the home column.
	 * This essentially sets cursorX to 0.
	 */
	public void returnCursor()
	{
		cursorX = 0;
	}

	/**
	 * Resets the cursor to the home column and row.
	 * This essentially sets cursorX and Y to the maximum height.
	 * This also clears the flag that says that we are writing in a text area.
	 */
	public void homeCursor()
	{
		setCursor(0, 0);
	}

	/**
	 * Sets a writable text area, and sets the cursor position 
	 * to the beginning of it.
	 * @param startX the starting grid coordinate, X.
	 * @param startY the starting grid coordinate, Y.
	 * @param width the width of the area in characters.
	 * @param height the height of the area in characters.
	 */
	public void setArea(int startX, int startY, int width, int height)
	{
		cursorX = startX;
		cursorY = startY;
		useArea = true;
		textStartX = startX;
		textStartY = startY;
		textEndX = startX + width;
		textEndY = startY - height;
	}

}
