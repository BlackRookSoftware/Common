/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.test;

import com.blackrook.commons.Common;
import com.blackrook.commons.spatialhash.SpatialHash2D;
import com.blackrook.commons.spatialhash.SpatialHashable;

public class Scene2DTest
{
	public static void main(String[] args)
	{
		Obj[] ob = new Obj[3];
		SpatialHash2D<Obj> scene = new SpatialHash2D<Obj>(32);
		scene.addObject(new Obj(0,0,2,2));
		scene.addObject(new Obj(3,3,2,2));
		scene.addObject(new Obj(-2,0,2,2));

		int x = 0;
		for (int i = 0; i < 10000; i++)
		{
			long t = System.nanoTime();
			x = scene.getIntersections(.5, .5, 2.5, 2.5, ob, 0);
			System.out.println(System.nanoTime() - t + "ns");
		}
		
		for(int i = 0; i < x; i++)
			System.out.println(ob[i]);
		Common.noop();
}
	
	public static class Obj implements SpatialHashable
	{
		protected float x;
		protected float y;
		protected float width;
		protected float height;
		
		public Obj (float x, float y, float width, float height)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		@Override
		public float getObjectHalfDepth()
		{
			return 0;
		}

		@Override
		public float getObjectHalfHeight()
		{
			return height/2;
		}

		@Override
		public float getObjectHalfWidth()
		{
			return width/2;
		}

		@Override
		public float getObjectCenterX()
		{
			return x;
		}

		@Override
		public float getObjectCenterY()
		{
			return y;
		}

		@Override
		public float getObjectCenterZ()
		{
			return 0;
		}

		@Override
		public float getObjectRadius()
		{
			return 0;
		}

		@Override
		public boolean useObjectRadius()
		{
			return false;
		}
		
		@Override
		public String toString()
		{
			return String.format("(%f, %f) W%f H%f", x, y, width, height);
		}

		@Override
		public float getObjectSweepX()
		{
			return 0;
		}

		@Override
		public float getObjectSweepY()
		{
			return 0;
		}

		@Override
		public float getObjectSweepZ()
		{
			return 0;
		}
}
	
}
