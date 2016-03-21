/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

import com.blackrook.commons.Common;

/**
 * This is a 4x4 Matrix object that stores doubles.
 * 
 * Indices are in column-major order.
 * 
 * <br> 0  4  8  12
 * <br> 1  5  9  13
 * <br> 2  6  10 14
 * <br> 3  7  11 15
 * 
 * @author Matthew Tropiano
 */
public class Matrix4D
{
	/** Identity Matrix in column-major form. */
	protected static double[] IDENTITY = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};

	/** Matrix coordinates. */
	private double[] mCoord;
	
	/**
	 * Constructs a new, blank 4x4 matrix.
	 */
	public Matrix4D()
	{
		mCoord = new double[16];
	}
	
	/**
	 * @return a new 4x4 Identity Matrix.
	 */
	public static Matrix4D newIdentity()
	{
		Matrix4D out = new Matrix4D();
		out.setIdentity();
		return out;
	}

	/**
	 * Sets this matrix to the identity matrix.
	 */
	public void setIdentity()
	{
		System.arraycopy(IDENTITY, 0, mCoord, 0, 16);
	}
	
	/**
	 * Sets this matrix to a translation matrix.
	 * @param x the x-axis translation.
	 * @param y the y-axis translation.
	 * @param z the z-axis translation.
	 */
	public void setTranslation(double x, double y, double z)
	{
		setIdentity();
		mCoord[12] = x;
		mCoord[13] = y;
		mCoord[14] = z;
	}

	/**
	 * Sets this matrix to a x-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateX(double degrees)
	{
		double angle = (degrees*Math.PI)/180;
		setIdentity();
		mCoord[5] = mCoord[10] = Math.cos(angle);
		mCoord[6] = Math.sin(angle);
		mCoord[9] = -mCoord[6];
	}

	/**
	 * Sets this matrix to a y-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateY(double degrees)
	{
		double angle = (degrees*Math.PI)/180;
		setIdentity();
		mCoord[0] = mCoord[10] = Math.cos(angle);
		mCoord[8] = Math.sin(angle);
		mCoord[2] = -mCoord[8];
	}
	
	/**
	 * Sets this matrix to a z-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateZ(double degrees)
	{
		double angle = (degrees*Math.PI)/180;
		setIdentity();
		mCoord[0] = mCoord[5] = Math.cos(angle);
		mCoord[1] = Math.sin(angle);
		mCoord[4] = -mCoord[1];
	}
	
	/**
	 * Sets this matrix to a rotation matrix.
	 * @param degX degrees to rotate around the X axis.
	 * @param degY degrees to rotate around the Y axis.
	 * @param degZ degrees to rotate around the Z axis.
	 */
	public void setRotation(double degX, double degY, double degZ)
	{
		Cache c = Common.getLocal(Cache.class);
		setIdentity();
		c.rotwork.setRotateX(degX);
		multiplyRight(c.rotwork);
		c.rotwork.setRotateY(degY);
		multiplyRight(c.rotwork);
		c.rotwork.setRotateZ(degZ);
		multiplyRight(c.rotwork);
	}

	/**
	 * Sets this matrix to a scaling matrix.
	 * @param scaleX amount to scale along the X axis.
	 * @param scaleY amount to scale along the Y axis.
	 * @param scaleZ amount to scale along the Z axis.
	 */
	public void setScale(double scaleX, double scaleY, double scaleZ)
	{
		setIdentity();
		mCoord[0] = scaleX;
		mCoord[5] = scaleY;
		mCoord[10] = scaleZ;
	}

	/**
	 * Sets this matrix to a scaling matrix, scaling all axes equally.
	 * @param scalar amount to scale along the all axes.
	 */
	public void setScale(double scalar)
	{
		setScale(scalar, scalar, scalar);
	}

	/**
	 * Sets this matrix to a shearing matrix.
	 * @param shear amount to shear.
	 */
	public void setShear(double shear)
	{
		setIdentity();
		mCoord[4] = shear;
	}

	/**
	 * Sets a position in this matrix to a value.
	 * @param row the desired matrix row.
	 * @param col the desired matrix column.
	 * @param val the new value to set.
	 */
	public void set(int row, int col, double val)
	{
		mCoord[row+(col*4)] = val;
	}
	
	/**
	 * Sets all positions in this matrix to a set of values.
	 * Please note that the values must be in column-major order.
	 * The amount of values copied is values.length or 16, whichever's smaller.
	 * @param values new array of values.
	 */
	public void set(double[] values)
	{
		System.arraycopy(values,0,mCoord,0,Math.min(values.length, mCoord.length));
	}
	
	/**
	 * Sets a matrix index (column major index) to a value.
	 * @param index the column-major-wise index.
	 * @param value the value to set.
	 */
	public void set(int index, double value)
	{
		mCoord[index] = value;
	}

	/**
	 * Multiplies this matrix with another.
	 * <pre>this x m</pre>
	 * @param matrix the multiplicand matrix.
	 */
	public void multiplyRight(Matrix4D matrix)
	{
		Cache c = Common.getLocal(Cache.class);
		getDoubles(c.SCRATCH_A);
		matrix.getDoubles(c.SCRATCH_B);
		multMatrices(c.SCRATCH_A, c.SCRATCH_B, mCoord);
	}
	
	/**
	 * Multiplies this matrix with another.
	 * <pre>m x this</pre>
	 * @param matrix the multiplicand matrix.
	 */
	public void multiplyLeft(Matrix4D matrix)
	{
		Cache c = Common.getLocal(Cache.class);
		matrix.getDoubles(c.SCRATCH_A);
		getDoubles(c.SCRATCH_B);
		multMatrices(c.SCRATCH_A, c.SCRATCH_B, mCoord);
	}
	
	/**
	 * Sets this matrix's values up as a projection matrix, perspective arguments.
	 * @param degFOV front of view angle in degrees.
	 * @param aspectRatio the aspect ratio, usually view width over view height.
	 * @param zNear the near clipping plane on the Z-Axis.
	 * @param zFar the far clipping plane on the Z-Axis.
	 */
	public void setPerspective(double degFOV, double aspectRatio, double zNear, double zFar)
	{
		double halfangle = ((degFOV*Math.PI)/180)/2;
		double fpn = zFar+zNear;
		double nmf = zNear-zFar;
		double cothalffov = Math.cos(halfangle)/Math.sin(halfangle);
		
		setIdentity();
		mCoord[0] = cothalffov / aspectRatio;
		mCoord[5] = cothalffov;
		mCoord[10] = fpn / nmf;
		mCoord[11] = -1;
		mCoord[14] = (2*zFar*zNear) / nmf;
		mCoord[15] = 0;
	}
	
	/**
	 * Sets this matrix's values up as a projection matrix, frustum projection.
	 * @param left the left clipping plane on the X axis.
	 * @param right	 the right clipping plane on the X axis.
	 * @param bottom the bottom clipping plane on the Y axis.
	 * @param top the upper clipping plane on the Y axis.
	 * @param zNear	the near clipping plane on the Z axis.
	 * @param zFar the far clipping plane on the Z axis.
	 */
	public void setFrustum(double left, double right, double bottom, double top, double zNear, double zFar)
	{
		double rml = right - left;
		double tmb = top - bottom;
		double fmn = zFar - zNear;
		double n2 = zNear + zNear;
		setIdentity();

		mCoord[0] = n2 / rml;
		mCoord[5] = n2 / tmb;
		mCoord[8] = (right+left) / rml;
		mCoord[9] = (top+bottom) / tmb;
		mCoord[10] = (zFar+zNear) / fmn;
		mCoord[11] = -1;
		mCoord[14] = (2*zNear*zFar) / fmn;
		mCoord[15] = 0;
	}
	
	/**
	 * Sets this matrix's values up as a projection matrix, orthographic projection.
	 * @param left the left clipping plane on the X axis.
	 * @param right	 the right clipping plane on the X axis.
	 * @param bottom the bottom clipping plane on the Y axis.
	 * @param top the upper clipping plane on the Y axis.
	 * @param zNear	the near clipping plane on the Z axis.
	 * @param zFar the far clipping plane on the Z axis.
	 */
	public void setOrtho(double left, double right, double bottom, double top, double zNear, double zFar)
	{
		double rml = right - left;
		double tmb = top - bottom;
		double fmn = zFar - zNear;
		setIdentity();
		
		mCoord[0] = 2 / rml;
		mCoord[5] = 2 / tmb;
		mCoord[10] = -2 / fmn;
		mCoord[12] = (right+left) / rml;
		mCoord[13] = (top+bottom) / tmb;
		mCoord[14] = (zFar+zNear) / fmn;
	}
	
	private void multMatrices(double[] a, double[] b, double[] dest)
	{
		for(int i = 0; i < 4; i++)
		{
			dest[i]    = a[i]*b[0]  + a[i+4]*b[1]  + a[i+8]*b[2]  + a[i+12]*b[3];
			dest[i+4]  = a[i]*b[4]  + a[i+4]*b[5]  + a[i+8]*b[6]  + a[i+12]*b[7];
			dest[i+8]  = a[i]*b[8]  + a[i+4]*b[9]  + a[i+8]*b[10] + a[i+12]*b[11];
			dest[i+12] = a[i]*b[12] + a[i+4]*b[13] + a[i+8]*b[14] + a[i+12]*b[15];
		}
	}
	
	/**
	 * @return a reference to the double array that makes up this matrix.
	 * @since 2.0.1
	 */
	public double[] getArray()
	{
		return mCoord;
	}
	
	/**
	 * Returns the doubles that make up this matrix into a double array.
	 * If the output array is shorter than 16, up to that amount of values are copied. 
	 * @param out the output array.
	 */
	public void getDoubles(double[] out)
	{
		System.arraycopy(mCoord, 0, out, 0, Math.min(out.length, mCoord.length));
	}
	
	/**
	 * @return a copy of this Matrix.
	 */
	public Matrix4D copy()
	{
		Matrix4D out = new Matrix4D();
		System.arraycopy(mCoord,0,out.mCoord,0,16);
		return out;
	}
	
	/**
	 * Copies this Matrix into another.
	 * @param target the target matrix to copy into.
	 */
	public void copyTo(Matrix4D target)
	{
		System.arraycopy(mCoord,0,target.mCoord,0,16);
	}
	
	public static final class Cache
	{
		private Matrix4D rotwork;
		private double[] SCRATCH_A;
		private double[] SCRATCH_B;

		public Cache()
		{
			rotwork = new Matrix4D();
			SCRATCH_A = new double[16];
			SCRATCH_B = new double[16];
		}
		
	}
	
}
