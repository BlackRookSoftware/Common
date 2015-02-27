/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.logging.driver;

/**
 * A logger driver that outputs to Standard Out.
 * @author Matthew Tropiano
 */
public class ConsoleLogger extends PrintStreamLogger
{
	/**
	 * Creates a new console logger.
	 */
	public ConsoleLogger()
	{
		super(System.out);
	}
}
