/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.test;

import java.net.URL;

import com.blackrook.commons.Common;

public final class NetTest
{
	public static void main(String[] args) throws Exception
	{
		System.out.println(Common.getHTTPContent(new URL("http://google.com")));
}

}
