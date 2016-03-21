/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.logging;

import java.util.Date;

import com.blackrook.commons.logging.LoggingFactory.LogLevel;

/**
 * A logging output driver that outputs messages to a log when it receives a set of inputs. 
 * @author Matthew Tropiano
 */
public interface LoggingDriver
{
	/**
	 * Processes a logging message.
	 * @param time the time that this message was logged.
	 * @param level the logging level.
	 * @param source the source of the message.
	 * @param message the message to object.
	 * @param throwable the throwable to output along with the message.
	 */
	public void log(Date time, LogLevel level, String source, String message, Throwable throwable);
	
}
