/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import com.blackrook.commons.Common;
import com.blackrook.commons.math.RMath;

public class CommonTest
{
	public static void main(String[] args) throws Exception
	{
		Random random = new Random();
		
		byte[] b = new byte[1024*1024];
		for (int i = 0; i < b.length; i++)
			b[i] = RMath.randByte(random);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		System.out.println(Common.relay(bis, bos, 65536, 123434));
		
	}
	
}
