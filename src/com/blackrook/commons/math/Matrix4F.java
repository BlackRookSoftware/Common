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
 * This is a 4x4 Matrix object that stores floats.
 * 
 * Indices are in column-major order.
 * 
 * <br> 0  4  8  12
 * <br> 1  5  9  13
 * <br> 2  6  10 14
 * <br> 3  7  11 15
 * 
 * @author Matthew Tropiano
 * @since 2.0.1
 */
public class Matrix4F
{
	/** Identity Matrix in column-major form. */
	protected static float[] IDENTITY = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};

	/** Matrix coordinates. */
	private float[] matrixArray;
	
	/**
	 * Constructs a new, blank 4x4 matrix.
	 */
	public Matrix4F()
	{
		matrixArray = new float[16];
	}
	
	/**
	 * @return a new 4x4 Identity Matrix.
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
		identityArray(matrixArray);
	}
	
	/**
	 * Sets this matrix to a translation matrix.
	 * @param x the x-axis translation.
	 * @param y the y-axis translation.
	 * @param z the z-axis translation.
	 */
	public void setTranslation(float x, float y, float z)
	{
		translateArray(matrixArray, x, y, z);
	}

	/**
	 * Sets this matrix to a x-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateX(float degrees)
	{
		rotationXArray(matrixArray, degrees);
	}

	/**
	 * Sets this matrix to a y-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateY(float degrees)
	{
		rotationYArray(matrixArray, degrees);
	}
	
	/**
	 * Sets this matrix to a z-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateZ(float degrees)
	{
		rotationZArray(matrixArray, degrees);
	}
	
	/**
	 * Sets this matrix to a rotation matrix.
	 * @param degX degrees to rotate around the X axis.
	 * @param degY degrees to rotate around the Y axis.
	 * @param degZ degrees to rotate around the Z axis.
	 */
	public void setRotation(float degX, float degY, float degZ)
	{
		reset().rotateX(degX).rotateY(degY).rotateZ(degZ);
	}

	/**
	 * Sets this matrix to a scaling matrix.
	 * @param scaleX amount to scale along the X axis.
	 * @param scaleY amount to scale along the Y axis.
	 * @param scaleZ amount to scale along the Z axis.
	 */
	public void setScale(float scaleX, float scaleY, float scaleZ)
	{
		scaleArray(matrixArray, scaleX, scaleY, scaleZ);
	}

	/**
	 * Sets this matrix to a scaling matrix, scaling all axes equally.
	 * @param scalar amount to scale along the all axes.
	 */
	public void setScale(float scalar)
	{
		setScale(scalar, scalar, scalar);
	}

	/**
	 * Sets this matrix to a shearing matrix.
	 * @param shear amount to shear.
	 */
	public void setShear(float shear)
	{
		shearArray(matrixArray, shear);
	}

	/**
	 * Sets this matrix's values up as a "look at" perspective matrix.
	 * @param eyeX the eye vector, x-component.
	 * @param eyeY the eye vector, y-component.
	 * @param eyeZ the eye vector, z-component.
	 * @param centerX the centerpoint, x-component.
	 * @param centerY the centerpoint, y-component.
	 * @param centerZ the centerpoint, z-component.
	 * @param upX the upward vector, x-component.
	 * @param upY the upward vector, y-component.
	 * @param upZ the upward vector, z-component.
	 */
	public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
	{
		lookAtArray(matrixArray, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	/**
	 * Sets this matrix's values up as a projection matrix, perspective arguments.
	 * @param fov front of view angle in degrees.
	 * @param aspect the aspect ratio, usually view width over view height.
	 * @param zNear the near clipping plane on the Z-Axis.
	 * @param zFar the far clipping plane on the Z-Axis.
	 */
	public void setPerspective(float fov, float aspect, float zNear, float zFar)
	{
		perspectiveArray(matrixArray, fov, aspect, zNear, zFar);
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
	public void setFrustum(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		frustumArray(matrixArray, left, right, bottom, top, zNear, zFar);
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
	public void setOrtho(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		orthoArray(matrixArray, left, right, bottom, top, zNear, zFar);
	}

	/**
	 * Sets this matrix's values up as an aspect-corrected projection matrix, orthographic projection.
	 * @param aspect the target aspect.
	 * @param left the left clipping plane on the X axis.
	 * @param right	 the right clipping plane on the X axis.
	 * @param bottom the bottom clipping plane on the Y axis.
	 * @param top the upper clipping plane on the Y axis.
	 * @param zNear	the near clipping plane on the Z axis.
	 * @param zFar the far clipping plane on the Z axis.
	 */
	public void setAspectOrtho(float aspect, float left, float right, float bottom, float top, float zNear, float zFar)
	{
		aspectOrthoArray(matrixArray, aspect, left, right, bottom, top, zNear, zFar);
	}

	/**
	 * Sets a position in this matrix to a value.
	 * @param row the desired matrix row.
	 * @param col the desired matrix column.
	 * @param val the new value to set.
	 */
	public void set(int row, int col, float val)
	{
		matrixArray[row+(col*4)] = val;
	}
	
	/**
	 * Sets all positions in this matrix to a set of values.
	 * Please note that the values must be in column-major order.
	 * The amount of values copied is values.length or 16, whichever's smaller.
	 * @param values new array of values.
	 */
	public void set(float[] values)
	{
		System.arraycopy(values,0,matrixArray,0,Math.min(values.length, matrixArray.length));
	}
	
	/**
	 * Sets a matrix index (column major index) to a value.
	 * @param index the column-major-wise index.
	 * @param value the value to set.
	 */
	public void set(int index, float value)
	{
		matrixArray[index] = value;
	}

	/**
	 * Resets this matrix to the identity matrix. 
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F reset()
	{
		setIdentity();
		return this;
	}
	
	/**
	 * Multiplies a translation matrix into this one. 
	 * @param x the x-axis translation.
	 * @param y the y-axis translation.
	 * @param z the z-axis translation.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F translate(float x, float y, float z)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		translateArray(c.scratchB, x, y, z);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a X-axis rotation matrix into this one. 
	 * @param degrees degrees to rotate.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F rotateX(float degrees)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		rotationXArray(c.scratchB, degrees);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a Y-axis rotation matrix into this one. 
	 * @param degrees degrees to rotate.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F rotateY(float degrees)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		rotationYArray(c.scratchB, degrees);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a Z-axis rotation matrix into this one. 
	 * @param degrees degrees to rotate.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F rotateZ(float degrees)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		rotationZArray(c.scratchB, degrees);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a scaling matrix into this one. 
	 * @param scaleX amount to scale along the X axis.
	 * @param scaleY amount to scale along the Y axis.
	 * @param scaleZ amount to scale along the Z axis.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F scale(float scaleX, float scaleY, float scaleZ)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		scaleArray(c.scratchB, scaleX, scaleY, scaleZ);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a scaling matrix into this one. 
	 * @param scalar amount to scale along the all axes.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F scale(float scalar)
	{
		return scale(scalar, scalar, scalar);
	}
	
	/**
	 * Multiplies a shearing matrix into this one. 
	 * @param shear amount to shear.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F shear(float shear)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		shearArray(c.scratchB, shear);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a "look at" perspective matrix into this one.
	 * @param eyeX the eye vector, x-component.
	 * @param eyeY the eye vector, y-component.
	 * @param eyeZ the eye vector, z-component.
	 * @param centerX the centerpoint, x-component.
	 * @param centerY the centerpoint, y-component.
	 * @param centerZ the centerpoint, z-component.
	 * @param upX the upward vector, x-component.
	 * @param upY the upward vector, y-component.
	 * @param upZ the upward vector, z-component.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		lookAtArray(c.scratchB, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}

	/**
	 * Multiplies a perspective projection matrix into this one. 
	 * @param fov front of view angle in degrees.
	 * @param aspect the aspect ratio, usually view width over view height.
	 * @param zNear the near clipping plane on the Z-Axis.
	 * @param zFar the far clipping plane on the Z-Axis.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F perspective(float fov, float aspect, float zNear, float zFar)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		perspectiveArray(c.scratchB, fov, aspect, zNear, zFar);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a frustum projection matrix into this one. 
	 * @param left the left clipping plane on the X axis.
	 * @param right	the right clipping plane on the X axis.
	 * @param bottom the bottom clipping plane on the Y axis.
	 * @param top the upper clipping plane on the Y axis.
	 * @param zNear	the near clipping plane on the Z axis.
	 * @param zFar the far clipping plane on the Z axis.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F frustum(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		frustumArray(c.scratchB, left, right, bottom, top, zNear, zFar);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies a orthographic projection matrix into this one. 
	 * @param left the left clipping plane on the X axis.
	 * @param right	the right clipping plane on the X axis.
	 * @param bottom the bottom clipping plane on the Y axis.
	 * @param top the upper clipping plane on the Y axis.
	 * @param zNear	the near clipping plane on the Z axis.
	 * @param zFar the far clipping plane on the Z axis.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F ortho(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		orthoArray(c.scratchB, left, right, bottom, top, zNear, zFar);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies an aspect-corrected orthographic projection matrix into this one. 
	 * @param aspect the target aspect.
	 * @param left the left clipping plane on the X axis.
	 * @param right	the right clipping plane on the X axis.
	 * @param bottom the bottom clipping plane on the Y axis.
	 * @param top the upper clipping plane on the Y axis.
	 * @param zNear	the near clipping plane on the Z axis.
	 * @param zFar the far clipping plane on the Z axis.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4F aspectOrtho(float aspect, float left, float right, float bottom, float top, float zNear, float zFar)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		aspectOrthoArray(c.scratchB, aspect, left, right, bottom, top, zNear, zFar);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}
	
	/**
	 * Multiplies this matrix with another.
	 * <pre>this x m</pre>
	 * @param matrix the multiplicand matrix.
	 */
	public void multiplyRight(Matrix4F matrix)
	{
		Cache c = getCache();
		getFloats(c.scratchA);
		matrix.getFloats(c.scratchB);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
	}
	
	/**
	 * Multiplies this matrix with another.
	 * <pre>m x this</pre>
	 * @param matrix the multiplicand matrix.
	 */
	public void multiplyLeft(Matrix4F matrix)
	{
		Cache c = getCache();
		matrix.getFloats(c.scratchA);
		getFloats(c.scratchB);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
	}
	
	/**
	 * @return a reference to the float array that makes up this matrix.
	 * @since 2.0.1
	 */
	public float[] getArray()
	{
		return matrixArray;
	}

	/**
	 * Returns the floats that make up this matrix into a float array.
	 * Equivalent to <code>getFloats(out, 0)</code>
	 * Values are in column-major order.
	 * @param out the output array.
	 * @throws ArrayIndexOutOfBoundsException if <code>out.length < 16</code>.
	 */
	public void getFloats(float[] out)
	{
		getFloats(out, 0);
	}

	/**
	 * Returns the floats that make up this matrix into a float array.
	 * Values are in column-major order.
	 * @param out the output array.
	 * @param offset the starting offset array.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset + 16 >= out.length</code>.
	 * @since 2.31.0
	 */
	public void getFloats(float[] out, int offset)
	{
		System.arraycopy(matrixArray, 0, out, offset, matrixArray.length);
	}

	/**
	 * Copies this Matrix into another.
	 * @param target the target matrix to copy into.
	 */
	public void copyTo(Matrix4F target)
	{
		System.arraycopy(matrixArray, 0, target.matrixArray, 0, 16);
	}

	/**
	 * @return a new copy of this Matrix.
	 */
	public Matrix4F copy()
	{
		Matrix4F out = new Matrix4F();
		System.arraycopy(matrixArray, 0, out.matrixArray, 0, 16);
		return out;
	}

	// Set identity.
	private static void identityArray(float[] out)
	{
		System.arraycopy(IDENTITY, 0, out, 0, 16);
	}
	
	// Set translation.
	private static void translateArray(float[] out, float x, float y, float z)
	{
		identityArray(out);
		out[12] = x;
		out[13] = y;
		out[14] = z;
	}

	// Rotate X.
	private static void rotationXArray(float[] out, float degrees)
	{
		double rads = RMath.degToRad(degrees);
		identityArray(out);
		out[5] = out[10] = (float)Math.cos(rads);
		out[6] = (float)Math.sin(rads);
		out[9] = -out[6];
	}
	
	// Rotate Y.
	private static void rotationYArray(float[] out, float degrees)
	{
		double rads = RMath.degToRad(degrees);
		identityArray(out);
		out[0] = out[10] = (float)Math.cos(rads);
		out[8] = (float)Math.sin(rads);
		out[2] = -out[8];
	}
	
	// Rotate Z.
	private static void rotationZArray(float[] out, float degrees)
	{
		double rads = RMath.degToRad(degrees);
		identityArray(out);
		out[0] = out[5] = (float)Math.cos(rads);
		out[1] = (float)Math.sin(rads);
		out[4] = -out[1];
	}
	
	// Set scale.
	private static void scaleArray(float[] out, float x, float y, float z)
	{
		identityArray(out);
		out[0] = x;
		out[5] = y;
		out[10] = z;
	}

	// Set shear.
	private static void shearArray(float[] out, float shear)
	{
		identityArray(out);
		out[4] = shear;
	}

	// Set "look at."
	private static void lookAtArray(float[] out, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
	{
		double fx = centerX - eyeX;
		double fy = centerY - eyeY;
		double fz = centerZ - eyeZ;
		double flen = RMath.getVectorLength(fx, fy, fz);
		fx = fx / flen;
		fy = fy / flen;
		fz = fz / flen;
	
		double ulen = RMath.getVectorLength(upX, upY, upZ);
		double ux = upX / ulen;
		double uy = upY / ulen;
		double uz = upZ / ulen;
	
		double sx = fy*uz - fz*uy;
		double sy = fz*ux - fx*uz;
		double sz = fx*uy - fy*ux;
		double slen = RMath.getVectorLength(sx, sy, sz);
		sx = sx / slen;
		sy = sy / slen;
		sz = sz / slen;
	
		ux = sy*fz - sz*fy;
		uy = sz*fx - sx*fz;
		uz = sx*fy - sy*fx;
		
		out[0] = (float)sx;
		out[1] = (float)sy;
		out[2] = (float)sz;
		out[3] = (float)-RMath.getVectorDotProduct(eyeX, eyeY, eyeZ, sx, sy, sz);
		out[4] = (float)ux;
		out[5] = (float)uy;
		out[6] = (float)uz;
		out[7] = (float)-RMath.getVectorDotProduct(eyeX, eyeY, eyeZ, ux, uy, uz);
		out[8] = (float)-fx;
		out[9] = (float)-fy;
		out[10] = (float)-fz;
		out[11] = (float)-RMath.getVectorDotProduct(eyeX, eyeY, eyeZ, fx, fy, fz);
		out[12] = out[13] = out[14] = 0.0f;
		out[15] = 1.0f;
	}

	// Set perspective.
	private static void perspectiveArray(float[] out, float fov, float aspectRatio, float zNear, float zFar)
	{
		double halfangle = RMath.degToRad(fov) / 2;
		float fpn = zFar+zNear;
		float nmf = zNear-zFar;
		double cothalffov = Math.cos(halfangle)/Math.sin(halfangle);
		
		identityArray(out);
		out[0] = (float)(cothalffov / aspectRatio);
		out[5] = (float)cothalffov;
		out[10] = fpn / nmf;
		out[11] = -1;
		out[14] = (2*zFar*zNear) / nmf;
		out[15] = 0;
	}

	// Set frustum.
	private static void frustumArray(float[] out, float left, float right, float bottom, float top, float zNear, float zFar)
	{
		float rml = right - left;
		float tmb = top - bottom;
		float fmn = zFar - zNear;
		float n2 = zNear + zNear;
	
		identityArray(out);
		out[0] = n2 / rml;
		out[5] = n2 / tmb;
		out[8] = (right+left) / rml;
		out[9] = (top+bottom) / tmb;
		out[10] = (zFar+zNear) / fmn;
		out[11] = -1f;
		out[14] = (2f*zNear*zFar) / fmn;
		out[15] = 0f;
	}

	// Set ortho.
	private static void orthoArray(float[] out, float left, float right, float bottom, float top, float zNear, float zFar)
	{
		float rml = right - left;
		float tmb = top - bottom;
		float fmn = zFar - zNear;
		
		identityArray(out);
		out[0] = 2f / rml;
		out[5] = 2f / tmb;
		out[10] = -2f / fmn;
		out[12] = (right+left) / rml;
		out[13] = (top+bottom) / tmb;
		out[14] = (zFar+zNear) / fmn;
	}

	// Set aspect ortho.
	private static void aspectOrthoArray(float[] out, float targetAspect, float left, float right, float bottom, float top, float near, float far)
	{
		float viewWidth = Math.max(left, right) - Math.min(left, right);
		float viewHeight = Math.max(bottom, top) - Math.min(bottom, top);
		float viewAspect = viewWidth / viewHeight;
        
        if (targetAspect >= viewAspect)
        {
            float axis = targetAspect * viewHeight;
            float widthDiff = (axis - viewWidth) / 2f;
            right = left + viewWidth + widthDiff;
            left = left - widthDiff;
        }
        else
        {
            float axis = (1.0f / targetAspect) * viewWidth;
            float heightDiff = (axis - viewHeight) / 2f;
            top = bottom + viewHeight + heightDiff;
        	bottom = bottom - heightDiff;
        }
		
        orthoArray(out, left, right, bottom, top, near, far);	
	}
	
	// Multiplies two matrices.
	private static void multiplyArray(float[] a, float[] b, float[] out)
	{
		for(int i = 0; i < 4; i++)
		{
			out[i]    = a[i]*b[0]  + a[i+4]*b[1]  + a[i+8]*b[2]  + a[i+12]*b[3];
			out[i+4]  = a[i]*b[4]  + a[i+4]*b[5]  + a[i+8]*b[6]  + a[i+12]*b[7];
			out[i+8]  = a[i]*b[8]  + a[i+4]*b[9]  + a[i+8]*b[10] + a[i+12]*b[11];
			out[i+12] = a[i]*b[12] + a[i+4]*b[13] + a[i+8]*b[14] + a[i+12]*b[15];
		}
	}
	
	private static final String CACHE_NAME = "$$"+Cache.class.getCanonicalName();

	// Get the cache.
	private Cache getCache()
	{
		Cache out;
		if ((out = (Cache)Common.getLocal(CACHE_NAME)) == null)
			Common.setLocal(CACHE_NAME, out = new Cache());
		return out;
	}

	private static final class Cache
	{
		private float[] scratchA;
		private float[] scratchB;

		private Cache()
		{
			scratchA = new float[16];
			scratchB = new float[16];
		}
		
	}
	
}
