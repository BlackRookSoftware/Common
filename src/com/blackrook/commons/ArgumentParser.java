/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import com.blackrook.commons.hash.CaseInsensitiveHashMap;
import com.blackrook.commons.list.List;

/**
 * Parses and manages scope of command line arguments.
 * @author Matthew Tropiano
 */
public final class ArgumentParser
{
	private String[] baseArgs;
	private CaseInsensitiveHashMap<String[]>[] argTables;
	private CaseInsensitiveHashMap<Integer> prefixTable; // string -> table index
	
	/**
	 * Creates a new CmdArgHandler.
	 * This also populates the tables inside this object.
	 * @param args				the list of arguments to parse.
	 * @param switchPrefixes	the prefixes for switches within the command line (for example, "-" or "+" or whatever).
	 */
	@SuppressWarnings("unchecked")
	public ArgumentParser(String[] args, String[] switchPrefixes)
	{
		prefixTable = new CaseInsensitiveHashMap<Integer>(2);
		argTables = new CaseInsensitiveHashMap[switchPrefixes.length];
		for (int i = 0; i < switchPrefixes.length; i++)
			argTables[i] = new CaseInsensitiveHashMap<String[]>(2);
		
		final int 
			STATE_BASE = 0,
			STATE_ARGS = 1;
		int state = STATE_BASE;
		int x = 0;
		
		String currSwitch = null;
		int currTIndex = -1;
		List<String> currArg = new List<String>();
		
		while (x < args.length)
		{
			switch (state)
			{
				case STATE_BASE:
				{
					int tindex = startsWith(args[x],switchPrefixes); 
					if (tindex < 0)
						currArg.add(args[x]);
					else
					{
						baseArgs = toStringArray(currArg);
						currArg = new List<String>();

						currSwitch = args[x].replace(switchPrefixes[tindex], "");
						currTIndex = tindex;
						state = STATE_ARGS;
					}
				}
					break;

				case STATE_ARGS:
				{
					int tindex = startsWith(args[x],switchPrefixes); 
					if (tindex < 0)
						currArg.add(args[x]);
					else
					{
						argTables[currTIndex].put(currSwitch,toStringArray(currArg));
						currArg = new List<String>();

						currSwitch = args[x].replace(switchPrefixes[tindex], "");
						currTIndex = tindex;
					}
				}
					break;
					
			}
			x++;
		}
		
		if (currTIndex != -1)
			argTables[currTIndex].put(currSwitch,toStringArray(currArg));
		else
			baseArgs = toStringArray(currArg);
	}

	private int startsWith(String s, String[] test)
	{
		for (int i = 0; i < test.length; i++)
			if (s.startsWith(test[i]))
				return i;
		return -1;
	}
	
	private String[] toStringArray(List<String> args)
	{
		String[] out = new String[args.size()];
		args.toArray(out);
		return out;
	}
	
	/**
	 * Checks to see if a particular switch was added to the line.
	 * Do not enter in the name of the switch along with the prefix (use "help" instead of "--help");
	 * @param switchName the name of the switch.
	 * @return false if not, true if so (it must have been prefixed by a switch prefix).
	 */
	public boolean hasSwitch(String switchName)
	{
		for (CaseInsensitiveHashMap<String[]> t : argTables)
			if (t.containsKey(switchName))
				return true;
		
		return false;
	}

	/**
	 * Returns the list of switch arguments associated with this switch.
	 * For example, if the command line was:
	 * <pre>-get /home/gelatin +map 47 92</pre>
	 * ...<code>getSwitchArgs("map")</code> would return <code>[47, 92]</code>.
	 * <p>Do not enter in the name of the switch along with the prefix (use "help" instead of "--help");
	 * @param switchName the name of the switch.
	 */
	public String[] getSwitchArgs(String switchName)
	{
		for (CaseInsensitiveHashMap<String[]> t : argTables)
			if (t.containsKey(switchName))
				return t.get(switchName);
		
		return null;
	}
	
	/**
	 * Gets all arguments entered in without an associated switch.
	 */
	public String[] getBaseArgs()
	{
		return baseArgs;
	}
	
	/**
	 * Returns the table that stores all arguments that start with a specific prefix.
	 * Returns null if the prefix is invalid.
	 */
	public CaseInsensitiveHashMap<String[]> getTableForPrefix(String prefix)
	{
		Integer i = prefixTable.get(prefix);
		if (i == null)
			return null;
		return argTables[i];
	}
	
}
