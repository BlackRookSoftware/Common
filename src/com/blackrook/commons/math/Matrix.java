/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * A matrix class for linear algebra computations.
 * Matrix cells are stored in column-major order, as double-precision numbers.
 * @author Matthew Tropiano
 */
public class Matrix
{
	/** Width of the matrix. */
	private int width;
	/** Height of the matrix. */
	private int height;
	
	/** The Matrix data. */
	protected double[] mCoord;
	
	/**
	 * Creates a new Matrix of arbitrary width and height.
	 * @param width		the width of the Matrix in cells.
	 * @param height	the height of the Matrix in cells.
	 * @throws IllegalArgumentException if width or height is less than 1.
	 */
	public Matrix(int width, int height)
	{
		this.width = width;
		this.height = height;
		mCoord = new double[width*height];
	}
	
	/**
	 * Creates a copy of a matrix.
	 * @param m the Matrix to copy.
	 */
	public Matrix(Matrix m)
	{
		this(m.width, m.height);
		System.arraycopy(m.mCoord, 0, mCoord, 0, mCoord.length);
	}
	
	/**
	 * Sets all cells in the Matrix to 0.
	 */
	public void clear()
	{
		for (int i = 0; i < mCoord.length; i++)
			mCoord[i] = 0.0;
	}

	/**
	 * Returns the width of this Matrix in cells.
	 */
	public final int getWidth()
	{
		return width;
	}

	/**
	 * Returns the height of this Matrix in cells.
	 */
	public final int getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the value of cell in a particular place in the Matrix.
	 * @param row	the row (0 is first).
	 * @param col	the column (0 is first).
	 * @return the value in the desired Matrix cell.
	 * @throws ArrayIndexOutOfBoundsException if (x + height*y) lies outside the bounds of the Matrix.
	 */
	public double get(int row, int col)
	{
		return mCoord[getDataIndex(row,col)];
	}
	
	/**
	 * Sets the value of cell in a particular place in the Matrix.
	 * @param row	the row (0 is first).
	 * @param col	the column (0 is first).
	 * @param val	the value to set. 
	 */
	public void set(int row, int col, double val)
	{
		mCoord[getDataIndex(row,col)] = val;
	}

	/**
	 * Returns the index of the cell in a particular place in a Matrix.
	 * @param row	the row (0 is first).
	 * @param col	the column (0 is first).
	 * @return the correct index of the data.
	 */
	protected int getDataIndex(int row, int col)
	{
		return row + (height*col);
	}
	
	/**
	 * Returns true if the dimensions of this Matrix are equal, false otherwise.
	 */
	public boolean isSquare()
	{
		return width == height;
	}

	/**
	 * Returns a copy of this Matrix.
	 */
	public Matrix copy()
	{
		return new Matrix(this);
	}
	
	@Override
	public String toString()
	{
		int longestdouble = 0;
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				longestdouble = Math.max(longestdouble, Double.toString(get(x,y)).length());
		for (int y = 0; y < height; y++)
		{
			sb.append('[');
			sb.append(' ');
			for (int x = 0; x < width; x++)
			{
				sb.append(String.format("%-"+longestdouble+"s", Double.toString(get(x,y))));
				sb.append(' ');
			}
			sb.append(']');
			sb.append('\n');
		}
		return sb.toString();
	}
	
}
