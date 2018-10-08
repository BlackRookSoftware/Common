/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.logging.driver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import com.blackrook.commons.IO;
import com.blackrook.commons.logging.LoggingDriver;
import com.blackrook.commons.logging.LoggingFactory.LogLevel;

/**
 * A logging driver that writes to a text file.
 * @author Matthew Tropiano
 */
public class FileLogger implements LoggingDriver
{
	/** Mutex for set and write. */
	private Object MUTEX;
	/** The current PrintWriter to write to. */
	private PrintWriter writer;
	/** The current File to write to. */
	private File file;
	
	/**
	 * Creates a new file logger the writes to a specific file.
	 * @param logFile the file to write to.
	 * @throws IOException if the file could not be opened.
	 */
	public FileLogger(File logFile) throws IOException
	{
		MUTEX = new Object();
		setFile(logFile);
	}
	
	/**
	 * Sets the log file to a new file.
	 * The previous file is closed.
	 * @param logFile the file to write to.
	 * @throws IOException if the file could not be opened.
	 */
	protected void setFile(File logFile) throws IOException
	{
		synchronized (MUTEX)
		{
			if (file != null)
			{
				IO.close(writer);
				closeFile(file);
				writer = null;
				file = null;
			}
			
			file = logFile;
			writer = new PrintWriter(new FileOutputStream(file), true);
		}
	}
	
	/**
	 * Called after the writer to the previous file is closed
	 * on a file switch via {@link #setFile(File)}
	 * @param closeFile the file that was closed.
	 * @throws IOException if the file could not be closed cleanly.
	 */
	protected void closeFile(File closeFile) throws IOException
	{
		// Does nothing by default.
	}
	
	@Override
	public void log(Date time, LogLevel level, String source, String message, Throwable throwable)
	{
		if (writer == null)
			return;
		
		synchronized (MUTEX)
		{
			writer.println(String.format("[%tF %tT.%tL] (%s) %s: %s", time, time, time, source, level.name(), message));
			if (throwable != null)
				throwable.printStackTrace(writer);
		}
	}

}
