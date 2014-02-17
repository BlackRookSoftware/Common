/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import com.blackrook.commons.Common;

/**
 * Ticker class that keeps a steady rate of ticks per second.
 * It spawns a thread on starting that does its best to maintain a constant
 * tick rate while keeping the time between ticks as accurate as possible (to the millisecond).
 * @author Matthew Tropiano
 */
public abstract class Ticker
{
	/** The name of this ticker. */
	private String name;
	/** The ticker's updates per second. */
	private int updatesPerSecond;
	
	/** The ticker thread. Spawned when started, killed when stopped. */
	private TickerThread ticker;
	/** The current tick count. */
	private long currentTick;
	/** Is this suspended? */
	private boolean suspend;
	/** Milliseconds to wait. */
	private long millis;
	/** Remainder nanoseconds to wait (fraction of milliseconds). */
	private long nanos;
	/** 
	 * Creates a new Ticker that updates at a constant rate.
	 * @param updatesPerSecond	the amount of times that doTick() is called in one second. 
	 */
	public Ticker(int updatesPerSecond)
	{
		this(null,updatesPerSecond);
	}
	
	/** 
	 * Creates a new Ticker that updates at a constant rate.
	 * @param updatesPerSecond	the amount of times that doTick() is called in one second. 
	 */
	public Ticker(String name, int updatesPerSecond)
	{
		currentTick = 0;
		setName(name);
		setUpdatesPerSecond(updatesPerSecond);
	}
	
	/**
	 * Sets the name of the Ticker.
	 */
	public void setName(String name)
	{
		this.name = name;
		if (ticker != null)
			ticker.setName(name);
	}
	
	/**
	 * Gets the name of this ticker.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the updates per second of the Ticker.
	 */
	public void setUpdatesPerSecond(int updatesPerSecond)
	{
		this.updatesPerSecond = updatesPerSecond;
		double millisPerUpdate = updatesPerSecond != 0 ? (1000/(double)updatesPerSecond) : 0;
		millis = (long)millisPerUpdate;
		nanos = (long)((millisPerUpdate-millis)*1000000);
	}
	
	/**
	 * Gets the updates per second of the Ticker.
	 */
	public int getUpdatesPerSecond()
	{
		return updatesPerSecond;
	}
	
	/**
	 * Called on each tick this is where work should be done each tick.
	 * @param tick	the current game tick useful for jobs to be performed on staggered intervals.
	 * 				the value of tick always increases by one each call.
	 */
	public abstract void doTick(long tick);
	
	/**
	 * Starts the ticker and a new instance of the internal Thread.
	 * Calling this while the ticker is active does nothing.
	 * Calling this while the ticker is suspended resumes it.
	 */
	public void start()
	{
		if (ticker != null)
			setSuspended(false);
		else
		{
			ticker = new TickerThread();
			if (name != null)
				ticker.setName(name);
			ticker.start();
		}
	}

	/**
	 * Stops the game ticker entirely, clearing its state and running out the internal Thread.
	 */
	public void stop()
	{
		if (ticker != null)
			ticker.killswitch = true;
		ticker = null;
	}

	/**
	 * Sets if this ticker is suspended or not.
	 * If true, the thread still operates but does not call doTick().
	 * If false, the thread operates normally.
	 * Tickers do NOT start suspended unless this was set before it was started.
	 * It is less expensive, and probably more desirable to supend and resume
	 * the Ticker than to start and stop it.
	 */
	public void setSuspended(boolean value)
	{
		suspend = value;
	}
	
	/**
	 * Is this ticker suspended?
	 */
	public boolean isSuspended()
	{
		return suspend;
	}
	
	/**
	 * Is the ticker active?
	 */
	public boolean isActive()
	{
		return ticker != null && ticker.isAlive();
	}
	
	@Override
	public void finalize() throws Throwable
	{
		stop();
		super.finalize();
	}

	/**
	 * The ticker Thread.
	 * @author Matthew Tropiano
	 */
	private class TickerThread extends Thread
	{
		boolean killswitch;
		
		public TickerThread()
		{
			super();
			setDaemon(true);
			suspend = false;
			killswitch = false;
		}
		
		public void run()
		{
			long nanoCount = 0;
			long lastNanos = System.nanoTime();

			while (!killswitch)
			{
				long totalNanos = millis*1000000 + nanos;

				long nt = System.nanoTime();
				nanoCount += nt - lastNanos;
				lastNanos = nt;
				
				if (totalNanos == 0 || nanoCount >= totalNanos)
				{
					nanoCount -= totalNanos;
					
					if (!suspend)
						doTick(currentTick++);
				}
				
//				if (totalNanos > 0) 
					Common.sleep(0, 500000);
			}
		}
		
	}
	
}
