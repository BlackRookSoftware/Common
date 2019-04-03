/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

import com.blackrook.commons.util.ThreadUtils;

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
 * @since 2.0.1
 */
public class Matrix4D
{
	/** Identity Matrix in column-major form. */
	protected static double[] IDENTITY = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};

	/** Matrix coordinates. */
	private double[] matrixArray;
	
	/**
	 * Constructs a new, blank 4x4 matrix.
	 */
	public Matrix4D()
	{
		matrixArray = new double[16];
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
		identityArray(matrixArray);
	}
	
	/**
	 * Sets this matrix to a translation matrix.
	 * @param x the x-axis translation.
	 * @param y the y-axis translation.
	 * @param z the z-axis translation.
	 */
	public void setTranslation(double x, double y, double z)
	{
		translateArray(matrixArray, x, y, z);
	}

	/**
	 * Sets this matrix to a x-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateX(double degrees)
	{
		rotationXArray(matrixArray, degrees);
	}

	/**
	 * Sets this matrix to a y-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateY(double degrees)
	{
		rotationYArray(matrixArray, degrees);
	}
	
	/**
	 * Sets this matrix to a z-rotation matrix.
	 * @param degrees degrees to rotate.
	 */
	public void setRotateZ(double degrees)
	{
		rotationZArray(matrixArray, degrees);
	}
	
	/**
	 * Sets this matrix to a rotation matrix.
	 * @param degX degrees to rotate around the X axis.
	 * @param degY degrees to rotate around the Y axis.
	 * @param degZ degrees to rotate around the Z axis.
	 */
	public void setRotation(double degX, double degY, double degZ)
	{
		reset().rotateX(degX).rotateY(degY).rotateZ(degZ);
	}

	/**
	 * Sets this matrix to a scaling matrix.
	 * @param scaleX amount to scale along the X axis.
	 * @param scaleY amount to scale along the Y axis.
	 * @param scaleZ amount to scale along the Z axis.
	 */
	public void setScale(double scaleX, double scaleY, double scaleZ)
	{
		scaleArray(matrixArray, scaleX, scaleY, scaleZ);
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
	public void setLookAt(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ)
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
	public void setPerspective(double fov, double aspect, double zNear, double zFar)
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
	public void setFrustum(double left, double right, double bottom, double top, double zNear, double zFar)
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
	public void setOrtho(double left, double right, double bottom, double top, double zNear, double zFar)
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
	public void setAspectOrtho(double aspect, double left, double right, double bottom, double top, double zNear, double zFar)
	{
		aspectOrthoArray(matrixArray, aspect, left, right, bottom, top, zNear, zFar);
	}

	/**
	 * Sets a position in this matrix to a value.
	 * @param row the desired matrix row.
	 * @param col the desired matrix column.
	 * @param val the new value to set.
	 */
	public void set(int row, int col, double val)
	{
		matrixArray[row+(col*4)] = val;
	}
	
	/**
	 * Sets all positions in this matrix to a set of values.
	 * Please note that the values must be in column-major order.
	 * The amount of values copied is values.length or 16, whichever's smaller.
	 * @param values new array of values.
	 */
	public void set(double[] values)
	{
		System.arraycopy(values,0,matrixArray,0,Math.min(values.length, matrixArray.length));
	}
	
	/**
	 * Sets a matrix index (column major index) to a value.
	 * @param index the column-major-wise index.
	 * @param value the value to set.
	 */
	public void set(int index, double value)
	{
		matrixArray[index] = value;
	}

	/**
	 * Resets this matrix to the identity matrix. 
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4D reset()
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
	public Matrix4D translate(double x, double y, double z)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D rotateX(double degrees)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D rotateY(double degrees)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D rotateZ(double degrees)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D scale(double scaleX, double scaleY, double scaleZ)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D scale(double scalar)
	{
		return scale(scalar, scalar, scalar);
	}
	
	/**
	 * Multiplies a shearing matrix into this one. 
	 * @param shear amount to shear.
	 * @return itself, to chain commands.
	 * @since 2.31.0
	 */
	public Matrix4D shear(double shear)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D lookAt(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D perspective(double fov, double aspect, double zNear, double zFar)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D frustum(double left, double right, double bottom, double top, double zNear, double zFar)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D ortho(double left, double right, double bottom, double top, double zNear, double zFar)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
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
	public Matrix4D aspectOrtho(double aspect, double left, double right, double bottom, double top, double zNear, double zFar)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
		aspectOrthoArray(c.scratchB, aspect, left, right, bottom, top, zNear, zFar);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
		return this;
	}

	/**
	 * Multiplies this matrix with another.
	 * <pre>this x m</pre>
	 * @param matrix the multiplicand matrix.
	 */
	public void multiplyRight(Matrix4D matrix)
	{
		Cache c = getCache();
		getDoubles(c.scratchA);
		matrix.getDoubles(c.scratchB);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
	}
	
	/**
	 * Multiplies this matrix with another.
	 * <pre>m x this</pre>
	 * @param matrix the multiplicand matrix.
	 */
	public void multiplyLeft(Matrix4D matrix)
	{
		Cache c = getCache();
		matrix.getDoubles(c.scratchA);
		getDoubles(c.scratchB);
		multiplyArray(c.scratchA, c.scratchB, matrixArray);
	}
	
	/**
	 * @return a reference to the double array that makes up this matrix.
	 * @since 2.0.1
	 */
	public double[] getArray()
	{
		return matrixArray;
	}

	/**
	 * Returns the doubles that make up this matrix into a float array.
	 * Equivalent to <code>getFloats(out, 0)</code>
	 * @param out the output array.
	 * @throws ArrayIndexOutOfBoundsException if <code>out.length &lt; 16</code>.
	 */
	public void getDoubles(double[] out)
	{
		getDoubles(out, 0);
	}

	/**
	 * Returns the doubles that make up this matrix into a float array.
	 * @param out the output array.
	 * @param offset the starting offset array.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset + 16 &gt;= out.length</code>.
	 * @since 2.31.0
	 */
	public void getDoubles(double[] out, int offset)
	{
		System.arraycopy(matrixArray, 0, out, offset, matrixArray.length);
	}


	/**
	 * Copies this Matrix into another.
	 * @param target the target matrix to copy into.
	 */
	public void copyTo(Matrix4D target)
	{
		System.arraycopy(matrixArray, 0, target.matrixArray, 0, 16);
	}

	/**
	 * @return a new copy of this Matrix.
	 */
	public Matrix4D copy()
	{
		Matrix4D out = new Matrix4D();
		System.arraycopy(matrixArray, 0, out.matrixArray, 0, 16);
		return out;
	}

	// Set identity.
	private static void identityArray(double[] out)
	{
		System.arraycopy(IDENTITY, 0, out, 0, 16);
	}
	
	// Set translation.
	private static void translateArray(double[] out, double x, double y, double z)
	{
		identityArray(out);
		out[12] = x;
		out[13] = y;
		out[14] = z;
	}

	// Rotate X.
	private static void rotationXArray(double[] out, double degrees)
	{
		double rads = RMath.degToRad(degrees);
		identityArray(out);
		out[5] = out[10] = Math.cos(rads);
		out[6] = Math.sin(rads);
		out[9] = -out[6];
	}
	
	// Rotate Y.
	private static void rotationYArray(double[] out, double degrees)
	{
		double rads = RMath.degToRad(degrees);
		identityArray(out);
		out[0] = out[10] = Math.cos(rads);
		out[8] = Math.sin(rads);
		out[2] = -out[8];
	}
	
	// Rotate Z.
	private static void rotationZArray(double[] out, double degrees)
	{
		double rads = RMath.degToRad(degrees);
		identityArray(out);
		out[0] = out[5] = Math.cos(rads);
		out[1] = Math.sin(rads);
		out[4] = -out[1];
	}
	
	// Set scale.
	private static void scaleArray(double[] out, double x, double y, double z)
	{
		identityArray(out);
		out[0] = x;
		out[5] = y;
		out[10] = z;
	}

	// Set shear.
	private static void shearArray(double[] out, double shear)
	{
		identityArray(out);
		out[4] = shear;
	}

	// Set "look at."
	private static void lookAtArray(double[] out, double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ)
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
		
		out[0] = sx;
		out[1] = sy;
		out[2] = sz;
		out[3] = -RMath.getVectorDotProduct(eyeX, eyeY, eyeZ, sx, sy, sz);
		out[4] = ux;
		out[5] = uy;
		out[6] = uz;
		out[7] = -RMath.getVectorDotProduct(eyeX, eyeY, eyeZ, ux, uy, uz);
		out[8] = -fx;
		out[9] = -fy;
		out[10] = -fz;
		out[11] = -RMath.getVectorDotProduct(eyeX, eyeY, eyeZ, fx, fy, fz);
		out[12] = out[13] = out[14] = 0.0;
		out[15] = 1.0;
	}

	// Set perspective.
	private static void perspectiveArray(double[] out, double fov, double aspectRatio, double zNear, double zFar)
	{
		double halfangle = RMath.degToRad(fov) / 2;
		double fpn = zFar+zNear;
		double nmf = zNear-zFar;
		double cothalffov = Math.cos(halfangle)/Math.sin(halfangle);
		
		identityArray(out);
		out[0] = (cothalffov / aspectRatio);
		out[5] = cothalffov;
		out[10] = fpn / nmf;
		out[11] = -1;
		out[14] = (2*zFar*zNear) / nmf;
		out[15] = 0;
	}

	// Set frustum.
	private static void frustumArray(double[] out, double left, double right, double bottom, double top, double zNear, double zFar)
	{
		double rml = right - left;
		double tmb = top - bottom;
		double fmn = zFar - zNear;
		double n2 = zNear + zNear;
	
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
	private static void orthoArray(double[] out, double left, double right, double bottom, double top, double zNear, double zFar)
	{
		double rml = right - left;
		double tmb = top - bottom;
		double fmn = zFar - zNear;
		
		identityArray(out);
		out[0] = 2f / rml;
		out[5] = 2f / tmb;
		out[10] = -2f / fmn;
		out[12] = (right+left) / rml;
		out[13] = (top+bottom) / tmb;
		out[14] = (zFar+zNear) / fmn;
	}

	// Set aspect ortho.
	private static void aspectOrthoArray(double[] out, double targetAspect, double left, double right, double bottom, double top, double near, double far)
	{
		double viewWidth = Math.max(left, right) - Math.min(left, right);
		double viewHeight = Math.max(bottom, top) - Math.min(bottom, top);
		double viewAspect = viewWidth / viewHeight;
        
        if (targetAspect >= viewAspect)
        {
        	double axis = targetAspect * viewHeight;
        	double widthDiff = (axis - viewWidth) / 2f;
            right = left + viewWidth + widthDiff;
            left = left - widthDiff;
        }
        else
        {
        	double axis = (1.0f / targetAspect) * viewWidth;
        	double heightDiff = (axis - viewHeight) / 2f;
            top = bottom + viewHeight + heightDiff;
        	bottom = bottom - heightDiff;
        }
		
        orthoArray(out, left, right, bottom, top, near, far);	
	}
	
	// Multiplies two matrices.
	private static void multiplyArray(double[] a, double[] b, double[] out)
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
		if ((out = (Cache)ThreadUtils.getLocal(CACHE_NAME)) == null)
			ThreadUtils.setLocal(CACHE_NAME, out = new Cache());
		return out;
	}

	private static final class Cache
	{
		private double[] scratchA;
		private double[] scratchB;

		private Cache()
		{
			scratchA = new double[16];
			scratchB = new double[16];
		}
		
	}
	
}
