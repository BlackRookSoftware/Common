/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.blackrook.commons.Common;

public class Test
{
	public static void main(String[] args) throws IOException
	{
		BufferedReader br = Common.openTextFile("E:\\Users\\Matt\\Desktop\\fasdfasdf.txt");
		String line = null;
		while ((line = br.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(line, "\t");
			String s0 = st.nextToken();
			st.nextToken();
			String s2 = st.nextToken();
			System.out.println(String.format("put(\"%s\", '\\u%s');", s0, s2.trim()));
		}
		Common.close(br);
	}

}
