/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.logging;

import java.util.Date;

import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.logging.driver.ConsoleLogger;

/**
 * Some kind of logger for logging messages.
 * @author Matthew Tropiano
 */
public class LoggingFactory
{
	/** Logging levels. */
	public static enum LogLevel
	{
		FATAL,
		SEVERE,
		ERROR,
		WARNING,
		INFO,
		DEBUG;
	}
	
	/** Out queue. */
	private Queue<LogObject> outQueue;
	
	/** Stream to send logs out to. */
	private Queue<LoggingDriver> drivers;
	/** This logging factory's logging level. */
	private LogLevel loggingLevel;
	/** Logger thread. */
	private LoggerThread loggerThread;
	
	/**
	 * Creates a new logging factory.
	 * The starting logging level is {@link LogLevel#DEBUG}.
	 * @param drivers the logging driver to use for directing output.
	 */
	public LoggingFactory(LoggingDriver... drivers)
	{
		this(LogLevel.DEBUG, drivers);
	}
	
	/**
	 * Creates a new logging factory.
	 * @param drivers the logging driver to use for directing output.
	 * @param level the starting logging level.
	 */
	public LoggingFactory(LogLevel level, LoggingDriver... drivers)
	{
		this.drivers = new Queue<LoggingDriver>();
		this.outQueue = new Queue<LogObject>(); 
		this.loggingLevel = level;

		addDriver(drivers);
	}
	
	/**
	 * A convenience method for creating a Console-appending logger factory.
	 * <p>Equivalent to: <code>new LoggingFactory(new ConsoleLogger(), LogLevel.DEBUG)</code></p>
	 * @return the new logging factory.
	 * @deprecated In 2.20.3 due to unclear method wording. Please switch to {@link LoggingFactory#createConsoleLoggingFactory()}.
	 */
	@Deprecated
	public static LoggingFactory createConsoleLogger()
	{
		return createConsoleLoggingFactory();
	}

	/**
	 * A convenience method for creating a Console-appending logger factory.
	 * <p>Equivalent to: <code>new LoggingFactory(new ConsoleLogger(), LogLevel.DEBUG)</code></p>
	 * @return the new logging factory.
	 * @since 2.21.0
	 */
	public static LoggingFactory createConsoleLoggingFactory()
	{
		return new LoggingFactory(LogLevel.DEBUG, new ConsoleLogger());
	}

	/**
	 * A convenience method for creating a Console-appending logger for a particular source by name.
	 * <p>Equivalent to: <code>createConsoleLoggingFactory().getLogger(name)</code></p>
	 * <p>It may be better, thread-resource-wise to create a single factory if you are logging multiple sources/classes through it.</p>
	 * @param name the source name of this logger.
	 * @return the new logger.
	 * @see #getLogger(String)
	 * @since 2.21.0
	 */
	public static Logger createConsoleLoggerFor(String name)
	{
		return createConsoleLoggingFactory().getLogger(name);
	}
	
	/**
	 * A convenience method for creating a Console-appending logger for a particular source by class name.
	 * <p>Equivalent to: <code>createConsoleLoggingFactory().getLogger(clazz)</code></p>
	 * <p>It may be better, thread-resource-wise to create a single factory if you are logging multiple sources/classes through it.</p>
	 * @param clazz the class to create the logger for.
	 * @return the new logger.
	 * @see #getLogger(Class)
	 * @since 2.21.0
	 */
	public static Logger createConsoleLoggerFor(Class<?> clazz)
	{
		return createConsoleLoggingFactory().getLogger(clazz);
	}
	
	/**
	 * A convenience method for creating a Console-appending logger for a particular source by class name.
	 * <p>Equivalent to: <code>createConsoleLoggingFactory().getLogger(clazz, fullyQualified)</code></p>
	 * <p>It may be better, thread-resource-wise to create a single factory if you are logging multiple sources/classes through it.</p>
	 * @param clazz the class to create the logger for.
	 * @param fullyQualified if true, the output name is a fully-qualified name.
	 * @return the new logger.
	 * @see #getLogger(Class, boolean)
	 * @since 2.21.0
	 */
	public static Logger createConsoleLoggerFor(Class<?> clazz, boolean fullyQualified)
	{
		return createConsoleLoggingFactory().getLogger(clazz, fullyQualified);
	}
	
	/**
	 * Adds a logging driver or drivers from this factory.
	 * @param drivers the drivers to add.
	 */
	public void addDriver(LoggingDriver... drivers)
	{
		for (LoggingDriver d : drivers)
			this.drivers.add(d);
	}
	
	/**
	 * Removes a logging driver or drivers from this factory.
	 * @param drivers the drivers to remove.
	 */
	public void removeDriver(LoggingDriver... drivers)
	{
		for (LoggingDriver d : drivers)
			this.drivers.remove(d);
	}
	
	/**
	 * Returns the current logging level.
	 * Anything logged using {@link Logger}s generated by this factory is tested
	 * against the current logging level. If the logging level of the message is less than or equal
	 * to the current logging level, it is logged.
	 * @return the current logging level.
	 */
	public LogLevel getLoggingLevel()
	{
		return loggingLevel;
	}
	
	/**
	 * Returns the current logging level.
	 * Anything logged using {@link Logger}s generated by this factory is tested
	 * against the current logging level. If the logging level of the message is less than or equal
	 * to the current logging level, it is logged.
	 * @param level the new logging level.
	 */
	public void setLoggingLevel(LogLevel level)
	{
		this.loggingLevel = level;
	}
	
	/**
	 * Creates a new Logger for outputting logs.
	 * This logger uses the logging level and driver defined on this logging factory.
	 * @param name the source name.
	 * @return a logger to call to output logging to.
	 */
	public Logger getLogger(String name)
	{
		return new LoggerDelegate(name);
	}
	
	/**
	 * Creates a new Logger for outputting logs, using the name of the class as a source name.
	 * This logger uses the logging level and driver defined on this logging factory.
	 * @param clz the class to use.
	 * @return a logger to call to output logging to.
	 */
	public Logger getLogger(Class<?> clz)
	{
		return getLogger(clz, false);
	}
	
	/**
	 * Creates a new Logger for outputting logs, using the name of the class as a source name.
	 * This logger uses the logging level and driver defined on this logging factory.
	 * @param clz the class to use.
	 * @param fullyQualified if true, use the fully-qualified name. 
	 * @return a logger to call to output logging to.
	 */
	public Logger getLogger(Class<?> clz, boolean fullyQualified)
	{
		return new LoggerDelegate(fullyQualified ? clz.getName() : clz.getSimpleName());
	}
	
	/**
	 * Adds a log message to the logger queue.
	 * @param level the target logging level.
	 * @param source the source name.
	 * @param message the message to output.
	 * @param throwable the throwable to dump, if any.
	 */
	private void addLog(LogLevel level, LogLevel localLevel, String source, String message, Throwable throwable)
	{
		if (!checkLoggingLevel(level, localLevel))
			return;
		
		synchronized (outQueue)
		{
			outQueue.add(new LogObject(new Date(), level, source, message, throwable));
			if (loggerThread == null || !loggerThread.isAlive())
				(loggerThread = new LoggerThread()).start();
			outQueue.notify();
		}
	}
	
	/**
	 * Checks if the logging level allows for a log statement to be logged.
	 * If the localLevel is not provided, this uses the parent logger's level.
	 * @param level the level for the logged statement.
	 * @param localLevel the local logger's log level to check against, if any.
	 * @return true if the log entry passes, false if it doesn't.
	 */
	private boolean checkLoggingLevel(LogLevel level, LogLevel localLevel)
	{
		if (localLevel != null)
		{
			if (level.ordinal() > localLevel.ordinal())
				return false;
		}
		else if (loggingLevel != null)
		{
			if (level.ordinal() > loggingLevel.ordinal())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Delegate class that accepts logging input.
	 */
	private class LoggerDelegate implements Logger
	{
		/** The source of the message. */
		private LogLevel localLevel;
		/** The source of the message. */
		private String source;

		public LoggerDelegate(String source)
		{
			this.localLevel = null;
			this.source = source;
		}

		@Override
		public void setLoggingLevel(LogLevel loglevel) 
		{
			localLevel = loglevel;
		}

		@Override
		public LogLevel getLoggingLevel() 
		{
			return localLevel;
		}
		
		@Override
		public void fatal(Object message)
		{
			addLog(LogLevel.FATAL, localLevel, source, String.valueOf(message), null);
		}

		@Override
		public void fatalf(String formatString, Object... args)
		{
			addLog(LogLevel.FATAL, localLevel, source, String.format(formatString, args), null);
		}

		@Override
		public void fatal(Throwable t, Object message)
		{
			addLog(LogLevel.FATAL, localLevel, source, String.valueOf(message), t);
		}

		@Override
		public void fatalf(Throwable t, String formatString, Object... args)
		{
			addLog(LogLevel.FATAL, localLevel, source, String.format(formatString, args), t);
		}

		@Override
		public void severe(Object message)
		{
			addLog(LogLevel.SEVERE, localLevel, source, String.valueOf(message), null);
		}

		@Override
		public void severef(String formatString, Object... args)
		{
			addLog(LogLevel.SEVERE, localLevel, source, String.format(formatString, args), null);
		}

		@Override
		public void severe(Throwable t, Object message)
		{
			addLog(LogLevel.SEVERE, localLevel, source, String.valueOf(message), t);
		}

		@Override
		public void severef(Throwable t, String formatString, Object... args)
		{
			addLog(LogLevel.SEVERE, localLevel, source, String.format(formatString, args), t);
		}

		@Override
		public void error(Object message)
		{
			addLog(LogLevel.ERROR, localLevel, source, String.valueOf(message), null);
		}

		@Override
		public void errorf(String formatString, Object... args)
		{
			addLog(LogLevel.ERROR, localLevel, source, String.format(formatString, args), null);
		}

		@Override
		public void error(Throwable t, Object message)
		{
			addLog(LogLevel.ERROR, localLevel, source, String.valueOf(message), t);
		}

		@Override
		public void errorf(Throwable t, String formatString, Object... args)
		{
			addLog(LogLevel.ERROR, localLevel, source, String.format(formatString, args), t);
		}

		@Override
		public void warn(Object message)
		{
			addLog(LogLevel.WARNING, localLevel, source, String.valueOf(message), null);
		}

		@Override
		public void warnf(String formatString, Object... args)
		{
			addLog(LogLevel.WARNING, localLevel, source, String.format(formatString, args), null);
		}

		@Override
		public void info(Object message)
		{
			addLog(LogLevel.INFO, localLevel, source, String.valueOf(message), null);
		}

		@Override
		public void infof(String formatString, Object... args)
		{
			addLog(LogLevel.INFO, localLevel, source, String.format(formatString, args), null);
		}

		@Override
		public void debug(Object message)
		{
			addLog(LogLevel.DEBUG, localLevel, source, String.valueOf(message), null);
		}

		@Override
		public void debugf(String formatString, Object... args)
		{
			addLog(LogLevel.DEBUG, localLevel, source, String.format(formatString, args), null);
		}

	}
	
	/**
	 * Logger queue object.
	 */
	private static class LogObject
	{
		Date time;
		LogLevel level;
		String source;
		String message;
		Throwable throwable;
		
		LogObject(Date time, LogLevel level, String source, String message, Throwable throwable)
		{
			this.time = time;
			this.level = level;
			this.source = source;
			this.message = message;
			this.throwable = throwable;
		}
	}
	
	/**
	 * The thread that reads the output queue and dumps stuff. 
	 */
	private class LoggerThread extends Thread
	{
		public LoggerThread()
		{
			setName("LoggerThread-"+drivers.getClass().getSimpleName());
			setDaemon(false);
		}
		
		@Override
		public void run()
		{
			while (true)
			{
				try {
					
					LogObject logobj = null;
					synchronized (outQueue)
					{
						if (outQueue.isEmpty())
							outQueue.wait(100);
						if (outQueue.isEmpty())
							break;
						logobj = outQueue.dequeue();
					}
					
					for (LoggingDriver d : drivers)
						d.log(logobj.time, logobj.level, logobj.source, logobj.message, logobj.throwable);
					
				} catch (Throwable e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
}
