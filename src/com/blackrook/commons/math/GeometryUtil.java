/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

/**
 * Utility library for geometric stuff.
 * @author Matthew Tropiano
 * @since 2.10.0
 */
public final class GeometryUtil
{
	/**
	 * Tests if two lines, formed by tuples, overlap their bounding areas.
	 * @param lineAPointA the first line's first point.
	 * @param lineAPointB the first line's second point.
	 * @param lineBPointA the second line's first point.
	 * @param lineBPointB the second line's second point.
	 * @param outVect if they overlap, this is filled with the incident vector.
	 * @return true if one line overlap the other, false otherwise.
	 */
	public static boolean lineOverlaps(
		Tuple2D lineAPointA, Tuple2D lineAPointB, Tuple2D lineBPointA, 
		Tuple2D lineBPointB, Tuple2D outVect)
	{
		double minxa = Math.min(lineAPointA.x, lineAPointB.x);
		double minya = Math.min(lineAPointA.y, lineAPointB.y);
		double maxxa = Math.max(lineAPointA.x, lineAPointB.x);
		double maxya = Math.max(lineAPointA.y, lineAPointB.y);

		double minxb = Math.min(lineBPointA.x, lineBPointB.x);
		double minyb = Math.min(lineBPointA.y, lineBPointB.y);
		double maxxb = Math.max(lineBPointA.x, lineBPointB.x);
		double maxyb = Math.max(lineBPointA.y, lineBPointB.y);

		double midxa = (minxa + maxxa) / 2.0;
		double midxb = (minxb + maxxb) / 2.0;

		double midya = (minya + maxya) / 2.0;
		double midyb = (minyb + maxyb) / 2.0;
		
		// left
		if (midxa < midxb)
		{
			if (midya < midyb) // bottom
			{
				if (maxxa >= minxb && maxya >= minyb)
				{
					if (outVect != null) outVect.set(minxb - maxxa , minyb - maxya);
					return true;
				}
				else
					return false;
			}
			else // top
			{
				if (maxxa >= minxb && minya <= maxyb)
				{
					if (outVect != null) outVect.set(minxb - maxxa, maxyb - minya);
					return true;
				}
				else
					return false;
			}
		}
		// right
		else
		{
			if (midya < midyb) // bottom
			{
				if (minxa <= maxxb && maxya >= minyb)
				{
					if (outVect != null) outVect.set(maxxb - minxa, minyb - maxya);
					return true;
				}
				else
					return false;
			}
			else // top
			{
				if (minxa <= maxxb && minya <= maxyb)
				{
					if (outVect != null) outVect.set(maxxb - minxa, maxyb - minya);
					return true;
				}
				else
					return false;
			}
		}
	}
	
}
