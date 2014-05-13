/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.logging;

/**
 * Logger interface for all log writing.
 * @author Matthew Tropiano  
 */
public interface Logger
{
	/**
	 * Outputs a FATAL log message.
	 * @param message the object to convert to a string to dump.
	 */
	public void fatal(Object message);

	/**
	 * Outputs a FATAL log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void fatalf(String formatString, Object... args);

	/**
	 * Outputs a FATAL log message.
	 * @param t the throwable to print along with the message.
	 * @param message the object to convert to a string to dump.
	 */
	public void fatal(Throwable t, Object message);

	/**
	 * Outputs a FATAL log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param t the throwable to print along with the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void fatalf(Throwable t, String formatString, Object... args);

	/**
	 * Outputs a SEVERE log message.
	 * @param message the object to convert to a string to dump.
	 */
	public void severe(Object message);
	
	/**
	 * Outputs a SEVERE log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void severef(String formatString, Object... args);	
	
	/**
	 * Outputs a SEVERE log message.
	 * @param t the throwable to print along with the message.
	 * @param message the object to convert to a string to dump.
	 */
	public void severe(Throwable t, Object message);
	
	/**
	 * Outputs a SEVERE log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param t the throwable to print along with the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void severef(Throwable t, String formatString, Object... args);	
	
	/**
	 * Outputs a ERROR log message.
	 * @param message the object to convert to a string to dump.
	 */
	public void error(Object message);
	
	/**
	 * Outputs a ERROR log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void errorf(String formatString, Object... args);	
	
	/**
	 * Outputs a ERROR log message.
	 * @param t the throwable to print along with the message.
	 * @param message the object to convert to a string to dump.
	 */
	public void error(Throwable t, Object message);
	
	/**
	 * Outputs a ERROR log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param t the throwable to print along with the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void errorf(Throwable t, String formatString, Object... args);	
	
	/**
	 * Outputs a WARNING log message.
	 * @param message the object to convert to a string to dump.
	 */
	public void warn(Object message);
	
	/**
	 * Outputs a WARNING log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void warnf(String formatString, Object... args);	
	
	/**
	 * Outputs a INFO log message.
	 * @param message the object to convert to a string to dump.
	 */
	public void info(Object message);
	
	/**
	 * Outputs a INFO log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void infof(String formatString, Object... args);	
	
	/**
	 * Outputs a DEBUG log message.
	 * @param message the object to convert to a string to dump.
	 */
	public void debug(Object message);
	
	/**
	 * Outputs a DEBUG log message, formatted.
	 * A newline is always appended to the end of the message.
	 * @param formatString the formatting string to use to render the args.
	 * @param args the additional parameters for the formatter.
	 */
	public void debugf(String formatString, Object... args);	
	
}
