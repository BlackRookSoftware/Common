package com.blackrook.commons.logging.driver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import com.blackrook.commons.Common;
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
	 */
	protected void setFile(File logFile) throws IOException
	{
		synchronized (MUTEX)
		{
			if (file != null)
			{
				Common.close(writer);
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
	 */
	protected void closeFile(File closeFile)
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
