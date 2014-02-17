/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.math;

import java.util.Comparator;
import java.util.Random;

import com.blackrook.commons.Common;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.Sizable;
import com.blackrook.commons.list.ComparatorList;
import com.blackrook.commons.math.Pair;

/**
 * An object for holding a set of ordered pairs.
 * @author Matthew Tropiano
 * @since 2.13.0
 */
public class PairGroup implements ResettableIterable<Pair>, Sizable
{
	private static final String LOCAL_CACHE_NAME = Common.getPackagePathForClass(PairGroup.class)+"/Cache";
	private static final Comparator<Pair> PAIR_COMPARATOR = new Comparator<Pair>()
	{
		@Override
		public int compare(Pair p1, Pair p2)
		{
			return p1.x == p2.x ? (p1.y - p2.y) : (p1.x - p2.x);
		}
	}; 
	
	/**
	 * Local cache for multiple operations.
	 */
	private static class Cache
	{
		Pair pair;
		int[] orderingSource;
		int[] randomOrdering;
		
		Cache()
		{
			pair = new Pair();
		}
		
		void doRandom(Random random, int length, int amount)
		{
			if (orderingSource == null)
			{
				orderingSource = new int[length];
				randomOrdering = new int[length];
				fillOrdering(0);
			}
			else if (orderingSource.length < length)
			{
				int[] newOrdering = new int[length];
				randomOrdering = new int[length];
				int oldLen = orderingSource.length;
				System.arraycopy(orderingSource, 0, newOrdering, 0, oldLen);
				orderingSource = newOrdering;
				fillOrdering(oldLen);
			}
			
			System.arraycopy(orderingSource, 0, randomOrdering, 0, orderingSource.length);
			for (int i = 0; i < amount; i++)
			{
				int index = RMath.randInt(random, i, length - 1);
				int temp = randomOrdering[i];
				randomOrdering[i] = randomOrdering[index];
				randomOrdering[index] = temp;
			}
		}
		
		void fillOrdering(int start)
		{
			for (int i = start; i < orderingSource.length; i++)
				orderingSource[i] = i;
		}
		
	}
		
	/** A list of the contained pairs. */
	private ComparatorList<Pair> pairList;
	
	/**
	 * Created a Pair group with no Pairs in it.
	 */
	public PairGroup()
	{
		pairList = new ComparatorList<Pair>(PAIR_COMPARATOR, 50);
	}
	
	private Cache getCache()
	{
		Cache out = null;
		if ((out = (Cache)Common.getLocal(LOCAL_CACHE_NAME)) == null)
			Common.setLocal(LOCAL_CACHE_NAME, out = new Cache());
		return out;
	}

	/**
	 * Returns a new, empty PairGroup.
	 */
	public static PairGroup empty()
	{
		return new PairGroup();
	}
	
	/**
	 * Creates a new group by adding a set of existing pairs.
	 * @param pairs the list of Pairs.
	 */
	public static PairGroup wrap(Pair ... pairs)
	{
		PairGroup out = new PairGroup();
		for (Pair p : pairs)
			out.add(p.x, p.y);
		return out;
	}
	
	/**
	 * Creates a new group by adding a set of existing pairs.
	 * @param pairs the iterable list of Pairs.
	 */
	public static PairGroup wrap(Iterable<Pair> pairs)
	{
		PairGroup out = new PairGroup();
		for (Pair p : pairs)
			out.add(p.x, p.y);
		return out;
	}
	
	/**
	 * Creates a new group that contains a single pair.
	 * @param x the point, x-coordinate.
	 * @param y the point, y-coordinate.
	 */
	public static PairGroup point(int x, int y)
	{
		PairGroup out = new PairGroup();
		out.add(x, y);
		return out;
	}
	
	/**
	 * Creates a new group that contains points in a line.
	 * Uses Bresenham's Line Algorithm.
	 * @param x0 the line start point, x-coordinate.
	 * @param y0 the line start point, y-coordinate.
	 * @param x1 the line end point, x-coordinate.
	 * @param y1 the line end point, y-coordinate.
	 */
	public static PairGroup line(int x0, int y0, int x1, int y1)
	{
		return line(x0, y0, x1, y1, false);
	}
	
	/**
	 * Creates a new group that contains points in a line.
	 * Uses Bresenham's Line Algorithm.
	 * @param x0 the line start point, x-coordinate.
	 * @param y0 the line start point, y-coordinate.
	 * @param x1 the line end point, x-coordinate.
	 * @param y1 the line end point, y-coordinate.
	 * @param solid if true, this will add Pairs that "complete" the line,
	 * and leaves no diagonal gaps. 
	 */
	public static PairGroup line(int x0, int y0, int x1, int y1, boolean solid)
	{
		PairGroup out = new PairGroup();
		out.addLine(x0, y0, x1, y1, solid);
		return out;
	}

	/**
	 * Creates a new group that contains pairs that make up a circle.
	 * Uses Bresenham's Circle Algorithm.
	 * @param cx the center of the circle, x-coordinate.
	 * @param cy the center of the circle, y-coordinate.
	 * @param radius the circle radius.
	 * @return itself, to chain method calls.
	 */
	public static PairGroup circle(int cx, int cy, int radius)
	{
		PairGroup out = new PairGroup();
		out.addCircle(cx, cy, radius);
		return out;
	}
	
	/**
	 * Creates a new group that contains pairs that make up a filled circle.
	 * Uses Bresenham's Circle Algorithm.
	 * @param cx the center of the circle, x-coordinate.
	 * @param cy the center of the circle, y-coordinate.
	 * @param radius the circle radius.
	 * @return itself, to chain method calls.
	 */
	public static PairGroup circleFilled(int cx, int cy, int radius)
	{
		PairGroup out = new PairGroup();
		out.addCircleFilled(cx, cy, radius);
		return out;
	}

	/**
	 * Creates a new group that contains pairs that make up a box.
	 * @param x0 origin x-coordinate.
	 * @param y0 origin y-coordinate.
	 * @param x1 end x-coordinate.
	 * @param y1 end y-coordinate.
	 */
	public static PairGroup box(int x0, int y0, int x1, int y1)
	{
		PairGroup out = new PairGroup();
		out.addBox(x0, y0, x1, y1);
		return out;
	}
	
	/**
	 * Creates a new group that contains pairs that make up a filled box.
	 * @param x0 origin x-coordinate.
	 * @param y0 origin y-coordinate.
	 * @param x1 end x-coordinate.
	 * @param y1 end y-coordinate.
	 */
	public static PairGroup boxFilled(int x0, int y0, int x1, int y1)
	{
		PairGroup out = new PairGroup();
		out.addBoxFilled(x0, y0, x1, y1);
		return out;
	}
	
	/**
	 * Adds a pair.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @return itself, to chain method calls.
	 */
	public PairGroup add(int x, int y)
	{
		Cache c = getCache();
		c.pair.x = x;
		c.pair.y = y;
		if (!pairList.contains(c.pair))
			pairList.add(new Pair(c.pair.x, c.pair.y));
		
		return this;
	}

	/**
	 * Adds a group of pairs to this group.
	 * Compare with {@link #union(PairGroup)}, which returns a NEW PairGroup.
	 * @param group the group to add.
	 * @return itself, to chain method calls.
	 */
	public PairGroup add(PairGroup group)
	{
		for (Pair p : group)
			add(p.x, p.y);
		return this;
	}

	/**
	 * Adds a set of pairs that make up a line.
	 * Uses Bresenham's Line Algorithm.
	 * <p>Convenience method for: <code>addLine(x0, y0, x1, y1, false)</code>
	 * @param x0 the line start point, x-coordinate.
	 * @param y0 the line start point, y-coordinate.
	 * @param x1 the line end point, x-coordinate.
	 * @param y1 the line end point, y-coordinate.
	 * @return itself, to chain method calls.
	 */
	public PairGroup addLine(int x0, int y0, int x1, int y1)
	{
		return addLine(x0, y0, x1, y1, false);
	}

	/**
	 * Adds a set of pairs that make up a line.
	 * Uses Bresenham's Line Algorithm.
	 * @param x0 the line start point, x-coordinate.
	 * @param y0 the line start point, y-coordinate.
	 * @param x1 the line end point, x-coordinate.
	 * @param y1 the line end point, y-coordinate.
	 * @param solid if true, this will add Pairs that "complete" the line,
	 * and leaves no diagonal gaps. 
	 * @return itself, to chain method calls.
	 */
	public PairGroup addLine(int x0, int y0, int x1, int y1, boolean solid)
	{
		int dy = y1 - y0;
		int dx = x1 - x0;
		int stepx, stepy;
		
		if (dy < 0) { dy = -dy;  stepy = -1; } else { stepy = 1; }
		if (dx < 0) { dx = -dx;  stepx = -1; } else { stepx = 1; }
		dy <<= 1;
		dx <<= 1;
		
		add(x0, y0);
		if (dx > dy)
		{
			int fraction = dy - (dx >> 1);
			while (x0 != x1)
			{
				if (fraction >= 0)
				{
					y0 += stepy;
					fraction -= dx;
					if (solid)
						add(x0, y0);
				}
				x0 += stepx;
				fraction += dy;
				add(x0, y0);
			}
		} 
		else 
		{
			int fraction = dx - (dy >> 1);
			while (y0 != y1) 
			{
				if (fraction >= 0)
				{
					x0 += stepx;
					fraction -= dy;
					if (solid)
						add(x0, y0);
				}
				y0 += stepy;
				fraction += dx;
				add(x0, y0);
			}
		}
		
		return this;
	}

	/**
	 * Adds a set of pairs that make up a circle.
	 * Uses Bresenham's Circle Algorithm.
	 * @param cx the center of the circle, x-coordinate.
	 * @param cy the center of the circle, y-coordinate.
	 * @param radius the circle radius.
	 * @return itself, to chain method calls.
	 */
	public PairGroup addCircle(int cx, int cy, int radius)
	{
		int x = 0;
		int y = radius;
		int p = (5 - radius*4)/4;
		
		setCirclePoints(cx, cy, x, y);
		
		while (x < y)
		{
			x++;
			if (p < 0)
				p += 2*x+1;
			else 
			{
				y--;
				p += 2*(x-y)+1;
			}
			setCirclePoints(cx, cy, x, y);
		}
		
		return this;
	}

	/**
	 * Adds a set of pairs that make up a filled circle.
	 * Uses Bresenham's Circle Algorithm.
	 * @param cx the center of the circle, x-coordinate.
	 * @param cy the center of the circle, y-coordinate.
	 * @param radius the circle radius.
	 * @return itself, to chain method calls.
	 */
	public PairGroup addCircleFilled(int cx, int cy, int radius)
	{
		int x = 0;
		int y = radius;
		int p = (5 - radius*4)/4;
		
		connectCirclePoints(cx, cy, x, y);
		
		while (x < y)
		{
			x++;
			if (p < 0)
				p += 2*x+1;
			else 
			{
				y--;
				p += 2*(x-y)+1;
			}
			connectCirclePoints(cx, cy, x, y);
		}
		
		return this;
	}

	// Set circle points.
	private void setCirclePoints(int cx, int cy, int x, int y)
	{
		if (x == 0)
		{
			add(cx, cy + y);
			add(cx, cy - y);
			add(cx + y, cy);
			add(cx - y, cy);
		}
		else if (x == y)
		{
			add(cx + x, cy + y);
			add(cx - x, cy + y);
			add(cx + x, cy - y);
			add(cx - x, cy - y);
		}
		else if (x < y) 
		{
			add(cx + x, cy + y);
			add(cx - x, cy + y);
			add(cx + x, cy - y);
			add(cx - x, cy - y);
			add(cx + y, cy + x);
			add(cx - y, cy + x);
			add(cx + y, cy - x);
			add(cx - y, cy - x);
		}
	}

	// Set filled circle points.
	private void connectCirclePoints(int cx, int cy, int x, int y)
	{
		if (x == 0)
		{
			addLine(cx, cy + y, cx, cy - y);
			addLine(cx + y, cy, cx - y, cy);
		}
		else if (x == y)
		{
			addLine(cx + x, cy + y, cx - x, cy + y);
			addLine(cx + x, cy - y, cx - x, cy - y);
		}
		else if (x < y) 
		{
			addLine(cx + x, cy + y, cx - x, cy + y);
			addLine(cx + x, cy - y, cx - x, cy - y);
			addLine(cx + y, cy + x, cx - y, cy + x);
			addLine(cx + y, cy - x, cx - y, cy - x);
		}
	}

	/**
	 * Adds a set of pairs that make up a box.
	 * @param x0 origin x-coordinate.
	 * @param y0 origin y-coordinate.
	 * @param x1 end x-coordinate.
	 * @param y1 end y-coordinate.
	 * @return itself, to chain method calls.
	 */
	public PairGroup addBox(int x0, int y0, int x1, int y1)
	{
		addLine(x0, y0, x1, y0);
		addLine(x1, y0, x1, y1);
		addLine(x1, y1, x0, y1);
		addLine(x0, y1, x0, y0);
		return this;
	}
	
	/**
	 * Adds a set of pairs that make up a filled box.
	 * @param x0 origin x-coordinate.
	 * @param y0 origin y-coordinate.
	 * @param x1 end x-coordinate.
	 * @param y1 end y-coordinate.
	 * @return itself, to chain method calls.
	 */
	public PairGroup addBoxFilled(int x0, int y0, int x1, int y1)
	{
		for (int j = y0; j <= y1; j++)
			for (int i = x0; i <= x1; i++)
				add(i, j);
		return this;
	}
	
	/**
	 * Removes a pair.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @return itself, to chain method calls.
	 */
	public PairGroup remove(int x, int y)
	{
		Cache c = getCache();
		c.pair.x = x;
		c.pair.y = y;
		pairList.remove(c.pair);
		return this;
	}
	
	/**
	 * Removes all points in this group that are specified
	 * in another group.
	 * Compare to {@link #difference(PairGroup)}, which returns a NEW PairGroup.
	 * @param group the other group.
	 * @return itself, to chain method calls.
	 */
	public PairGroup remove(PairGroup group)
	{
		for (Pair p : group)
			remove(p.x, p.y);
		return this;
	}
	
	/**
	 * Removes all points in this group that are NOT specified
	 * in another group.
	 * Compare to {@link #intersection(PairGroup)}, which returns a NEW PairGroup.
	 * @param group the other group.
	 * @return itself, to chain method calls.
	 */
	public PairGroup removeNotIn(PairGroup group)
	{
		for (Pair p : group)
			if (!contains(p.x, p.y))
				remove(p.x, p.y);
		return this;
	}
	
	/**
	 * Checks if a pair is in this group.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @return true if so, false if not.
	 */
	public boolean contains(int x, int y)
	{
		Cache c = getCache();
		c.pair.x = x;
		c.pair.y = y;
		return pairList.contains(c.pair);
	}

	/**
	 * Translate all of the Pairs in this group by
	 * some amount on each axis.
	 * @param x x-coordinate amount.
	 * @param y y-coordinate amount.
	 * @return itself, to chain method calls.
	 */
	public PairGroup translate(int x, int y)
	{
		for (Pair p : this)
		{
			p.x += x;
			p.y += y;
		}
		return this;
	}

	/**
	 * Creates a new PairGroup that is a copy of this PairGroup. 
	 */
	public PairGroup copy()
	{
		PairGroup out = new PairGroup();
		for (Pair p : pairList)
			out.add(p.x, p.y);
		return out;
	}
	
	/**
	 * Creates a new PairGroup that is the union of
	 * two groups. The new group will have the Pairs that
	 * are in this group plus the provided group.
	 */
	public PairGroup union(PairGroup group)
	{
		PairGroup out = copy();
		out.add(group);
		return out;
	}

	/**
	 * Creates a new PairGroup that is the intersection of
	 * two groups. The new group will only have the Pairs that
	 * are both in this group and the provided group.
	 */
	public PairGroup intersection(PairGroup group)
	{
		PairGroup out = new PairGroup();
		for (Pair p : this)
			if (group.contains(p.x, p.y))
				out.add(p.x, p.y);
		return out;
	}

	/**
	 * Creates a new PairGroup that is a copy of this group
	 * minus the pairs in the provided group.
	 */
	public PairGroup difference(PairGroup group)
	{
		PairGroup out = copy();
		out.remove(group);
		return out;
	}

	/**
	 * Creates a new PairGroup that is a union of this group
	 * and the provided group, minus the intersection.
	 */
	public PairGroup xor(PairGroup group)
	{
		PairGroup out = copy();
		for (Pair p : group)
			if (out.contains(p.x, p.y))
				out.remove(p.x, p.y);
			else
				out.add(p.x, p.y);
		return out;
	}
	
	/**
	 * Creates a new PairGroup that is a randomly-selected
	 * collection of the pairs inside this group.
	 * @param random the random number generator to use.
	 */
	public PairGroup random(Random random)
	{
		return random(random, 0.5f);
	}

	/**
	 * Creates a new PairGroup that is a randomly-selected
	 * collection of the pairs inside this group, using a factor
	 * that decides the chance that each pair gets picked.
	 * @param random the random number generator to use.
	 * @param chance the chance, from 0 to 1, that a pair is selected.
	 * 0 or less is NEVER, 1 or greater is ALWAYS. In the event of these
	 * values being passed in, no numbers are randomly generated.
	 */
	public PairGroup random(Random random, float chance)
	{
		if (chance <= 0f)
			return empty();
		else if (chance >= 1f)
			return copy();

		PairGroup out = new PairGroup();
		for (Pair p : this)
			if (random.nextFloat() < chance)
				out.add(p.x, p.y);
		
		return out;
	}

	/**
	 * Creates a new PairGroup that is a randomly-selected
	 * collection of a number of pairs inside this group.
	 * @param random the random number generator to use.
	 * @param count the amount of objects to return. if this is 0 or less,
	 * no random numbers are picked and this returns an empty set. if this is
	 * greater than {@link #size()}, then it will return a group no greater than {@link #size()}.
	 */
	public PairGroup random(Random random, int count)
	{
		if (count <= 0)
			return empty();
		
		Cache c = getCache();
		int amount = Math.min(size(), count);
		c.doRandom(random, size(), amount);
		
		PairGroup out = empty();
		for (int i = 0; i < amount; i++)
		{
			Pair p = pairList.getByIndex(c.randomOrdering[i]);
			out.add(p.x, p.y);
		}
		
		return out;
	}
	
	@Override
	public ResettableIterator<Pair> iterator()
	{
		return pairList.iterator();
	}
	
	/**
	 * Returns how many Pairs that this group contains.
	 */
	@Override
	public int size()
	{
		return pairList.size();
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}

	@Override
	public String toString()
	{
		return pairList.toString();
	}
}
