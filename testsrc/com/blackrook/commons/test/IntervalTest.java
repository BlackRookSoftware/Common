/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.test;

import java.util.Random;

import com.blackrook.commons.Common;
import com.blackrook.commons.list.List;
import com.blackrook.commons.math.RMath;
import com.blackrook.commons.spatialhash.IntervalHash;
import com.blackrook.commons.spatialhash.IntervalHashable;

public class IntervalTest
{
	public static void main(String[] args)
	{
		Random r = new Random();
		List<Obj> list = new List<Obj>();
		IntervalHash<Obj> scene = new IntervalHash<Obj>(32);
		for (int i = 0; i < 10000; i++)
		{
			float x = RMath.randFloat(r, 0f, 2000f);
			float y = RMath.randFloat(r, 0f, 100f);
			scene.addObject(new Obj(x, y));
		}

		int x = 0;
		for (int i = 0; i < 10000; i++)
		{
			long t = System.nanoTime();
			x = scene.getIntersections(0f, list, true);
			System.out.println(System.nanoTime() - t + "ns");
		}
		
		System.out.println("***"+x);
		for(Obj obj : list)
			System.out.println(obj);
		Common.noop();
}
	
	public static class Obj implements IntervalHashable
	{
		protected float x;
		protected float width;
		
		public Obj (float x, float width)
		{
			this.x = x;
			this.width = width;
		}
		
		@Override
		public float getObjectHalfWidth()
		{
			return width/2f;
		}

		@Override
		public float getObjectCenterX()
		{
			return x;
		}

		@Override
		public String toString()
		{
			return String.format("[%f, %f]", x - getObjectHalfWidth(), x + getObjectHalfWidth());
		}

		@Override
		public float getObjectSweepX()
		{
			return 0;
		}

}
	
}
