/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * This is a 4x4 Matrix object.
 * 
 * Indices are in column-major order.
 * 
 * <br> 0  4  8  12
 * <br> 1  5  9  13
 * <br> 2  6  10 14
 * <br> 3  7  11 15
 * @since 2.0.1
 * @author Matthew Tropiano
 */
public class Matrix4F
{
	/** Identity Matrix in column-major form. */
	protected static float[] IDENTITY = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	/** Matrix for rotation work so that memory does not need to be reallocated. */
	protected Matrix4F rotwork = new Matrix4F();
	
	private float[] SCRATCH_A = new float[16];
	private float[] SCRATCH_B = new float[16];
	
	/** Matrix coordinate */
	float[] mCoord;
	
	/**
	 * Constructs a new, blank 4x4 matrix.
	 */
	public Matrix4F()
	{
		mCoord = new float[16];
	}
	
	/**
	 * Returns a new 4x4 Identity Matrix.
	 */
	public static Matrix4F newIdentity()
	{
		Matrix4F out = new Matrix4F();
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
	 */
	public void setTranslation(float x, float y, float z)
	{
		setIdentity();
		mCoord[12] = x;
		mCoord[13] = y;
		mCoord[14] = z;
	}

	/**
	 * Sets this matrix to a x-rotation matrix.
	 */
	public void setRotateX(float deg_x)
	{
		float angle = (float)((deg_x*Math.PI)/180);
		setIdentity();
		mCoord[5] = mCoord[10] = (float)Math.cos(angle);
		mCoord[6] = (float)Math.sin(angle);
		mCoord[9] = -mCoord[6];
	}

	/**
	 * Sets this matrix to a y-rotation matrix.
	 */
	public void setRotateY(float deg_y)
	{
		float angle = (float)((deg_y*Math.PI)/180);
		setIdentity();
		mCoord[0] = mCoord[10] = (float)Math.cos(angle);
		mCoord[8] = (float)Math.sin(angle);
		mCoord[2] = -mCoord[8];
	}
	
	/**
	 * Sets this matrix to a z-rotation matrix.
	 */
	public void setRotateZ(float deg_z)
	{
		float angle = (float)((deg_z*Math.PI)/180);
		setIdentity();
		mCoord[0] = mCoord[5] = (float)Math.cos(angle);
		mCoord[1] = (float)Math.sin(angle);
		mCoord[4] = -mCoord[1];
	}
	
	/**
	 * Sets this matrix to a rotation matrix.
	 */
	public void setRotation(float deg_x, float deg_y, float deg_z)
	{
		setIdentity();
		rotwork.setRotateX(deg_x);
		multiplyRight(rotwork);
		rotwork.setRotateY(deg_y);
		multiplyRight(rotwork);
		rotwork.setRotateZ(deg_z);
		multiplyRight(rotwork);
	}

	/**
	 * Sets this matrix to a scaling matrix.
	 */
	public void setScale(float scale_x, float scale_y, float scale_z)
	{
		setIdentity();
		mCoord[0] = scale_x;
		mCoord[5] = scale_y;
		mCoord[10] = scale_z;
	}

	/**
	 * Sets this matrix to a scaling matrix, scaling all axes equally.
	 */
	public void setScale(float scalar)
	{
		setScale(scalar,scalar,scalar);
	}

	/**
	 * Sets this matrix to a shearing matrix.
	 */
	public void setShear(float shear)
	{
		setIdentity();
		mCoord[4] = shear;
	}

	/**
	 * Sets a position in this matrix to a value.
	 * @param row	matrix row
	 * @param col	matrix column
	 * @param val	new value
	 */
	public void set(int row, int col, float val)
	{
		mCoord[row+(col*4)] = val;
	}
	
	/**
	 * Sets all positions in this matrix to a set of values.
	 * Please note that the values must be in column-major order.
	 * @param values	new array of values.
	 */
	public void set(float[] values)
	{
		System.arraycopy(values,0,mCoord,0,Math.min(values.length, mCoord.length));
	}
	
	/**
	 * Sets a matrix index (column major index) to a value.
	 * @param index		the column major wise index.
	 * @param val		 new value
	 */
	public void set(int index, float val)
	{
		mCoord[index] = val;
	}

	/**
	 * Multiplies this matrix with another.
	 * <pre>this x m</pre>
	 */
	public void multiplyRight(Matrix4F m)
	{
		getFloats(SCRATCH_A);
		m.getFloats(SCRATCH_B);
		multMatrices(SCRATCH_A,SCRATCH_B,mCoord);
	}
	
	/**
	 * Multiplies this matrix with another.
	 * <pre>m x this</pre>
	 */
	public void multiplyLeft(Matrix4F m)
	{
		m.getFloats(SCRATCH_A);
		getFloats(SCRATCH_B);
		multMatrices(SCRATCH_A,SCRATCH_B,mCoord);
	}
	
	/**
	 * Sets this matrix's values up as a projection matrix, perspective arguments.
	 * @param deg_fov		front of view angle in degrees.
	 * @param aspectRatio	the aspect ratio, usually view width over view height.
	 * @param zNear			the near clipping plane on the Z-Axis.
	 * @param zFar			the far clipping plane on the Z-Axis.
	 */
	public void setPerspective(float deg_fov, float aspectRatio, float zNear, float zFar)
	{
		float halfangle = (float)((deg_fov*Math.PI)/180)/2;
		float fpn = zFar+zNear;
		float nmf = zNear-zFar;
		float cothalffov = (float)(Math.cos(halfangle)/Math.sin(halfangle));
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
	 * @param left			the left clipping plane on the X-Axis.
	 * @param right			the right clipping plane on the X-Axis.
	 * @param bottom		the bottom clipping plane on the Y-Axis.
	 * @param top			the upper clipping plane on the Y-Axis.
	 * @param zNear			the near clipping plane on the Z-Axis.
	 * @param zFar			the far clipping plane on the Z-Axis.
	 */
	public void setFrustum(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		float rml = right - left;
		float tmb = top - bottom;
		float fmn = zFar - zNear;
		float n2 = zNear + zNear;
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
	 * @param left			the left clipping plane on the X-Axis.
	 * @param right			the right clipping plane on the X-Axis.
	 * @param bottom		the bottom clipping plane on the Y-Axis.
	 * @param top			the upper clipping plane on the Y-Axis.
	 * @param zNear			the near clipping plane on the Z-Axis.
	 * @param zFar			the far clipping plane on the Z-Axis.
	 */
	public void setOrtho(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		float rml = right - left;
		float tmb = top - bottom;
		float fmn = zFar - zNear;
		setIdentity();
		
		mCoord[0] = 2 / rml;
		mCoord[5] = 2 / tmb;
		mCoord[10] = -2 / fmn;
		mCoord[12] = (right+left) / rml;
		mCoord[13] = (top+bottom) / tmb;
		mCoord[14] = (zFar+zNear) / fmn;
	}
	
	private void multMatrices(float[] a, float[] b, float[] dest)
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
	 * Returns a reference to the float array that makes up this matrix.
	 */
	public float[] getArray()
	{
		return mCoord;
	}
	
	/**
	 * Returns the floats that make up this matrix into a float array.
	 */
	public void getFloats(float[] out)
	{
		System.arraycopy(mCoord, 0, out, 0, Math.min(out.length, mCoord.length));
	}
	
	/**
	 * Returns a copy of this Matrix.
	 */
	public Matrix4D copy()
	{
		Matrix4D out = new Matrix4D();
		System.arraycopy(mCoord,0,out.mCoord,0,16);
		return out;
	}
	
	/**
	 * Copies this Matrix into another.
	 */
	public void copyTo(Matrix4D target)
	{
		System.arraycopy(mCoord,0,target.mCoord,0,16);
	}
	
}
