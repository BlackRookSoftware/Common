/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.test;

import java.util.Arrays;

import com.blackrook.commons.Common;
import com.blackrook.commons.search.SearchSet;

public class SearchTest
{
	public static void main(String[] args)
	{
		String[] strings = {"Amazon is deploying a fleet of futuristic flying robots to deliver refurbished moisture-wicking underwear to your doorstep. Some cynics have suggested this won't happen. They say it's a daydream calculated to generate tons of free publicity.","The cynics are wrong. Drones are real. Amazon has released a promotional video of what a safe and problem-free delivery would look like. These are irrefutable facts.","Of course, Amazon isn't the only company steering our ungrateful society toward a better future. The Olive Garden is embracing technology like never before. While the Italian-ish restaurant chain may not seem like a competitor to Amazon, their new tech-driven initiatives place them in a unique position as a disruptive force.","If you follow technology coverage at all, you should know that disruption is basically the most important thing.","The first phase of The Olive Garden's cyber rollout will introduce their Neverending Pneumatic Pasta Tube. This works on the same principal as bank drive-thru deposit tubes, but with unfrozen linguini and spaghetti.","On one end of the system, an array of lukewarm pasta vats feeds into the complex tube network. On the other end, a pivoting hole dangles from the ceiling above each table in the restaurant. Open your mouth and a laser-based sensor grid signals the flow of pasta.","Early tests prove the delivery system to be extremely accurate. A powerful stream of pasta and sauce travels nearly three feet through the air before landing almost directly on your gob. Had enough? Simply close your mouth or scream. The intelligent sensor software will interpret your signal and quickly stop the flow of pasta within ten seconds.","The Neverending Pneumatic Pasta Tube is currently in the prototype stage. Olive Garden will be ready to launch the program as soon as the U.S. government eases its restrictions on radioactive materials.","Phase two of The Olive Garden's initiative introduces their disruptive take on Google's disruptive Google Glass, Garden Glass.","While the sole function of Google Glass is revealing its wearer as an asshole who treats the flaunting of new technology as part of his or her self-identity, Garden Glass also allows you to look at the entire Olive Garden menu.","Garden Glass uses augmented reality to project text on top of the restaurant's menus, which are now blank and practically useless to anyone who doesn't own a set of the futuristic specs. Put on the glasses and you can place an order. Simple. Incredible. Revolutionary.","It gets better. The real selling point of Garden Glass (estimated at $400 per pair) is that the connected, cloud-based experience goes beyond the restaurant. Look at the road. It is covered with salad choices rendered in a stunning 3D Papyrus font. Look at your family. Their faces are playgrounds for descriptive text about artisanal soup. As long as you are wearing the device you can see the menu.","Finally, we come to the most futuristic concept of all.","Let's say you walk into The Olive Garden wearing Garden Glass. You place your order. You enjoy several jet streams from the Neverending Pneumatic Pasta Tube. Now you're thirsty.","Reach for your soft drink. Take a long, slow sip. Savor the refreshing sensation as the liquid splashes across your sauce-crusted lips and tickles your tongue.","It's not a soft drink at all. It's a hologram. Remarkable.","Soon every glass will have a built-in nanoprojector. This cutting edge tech makes it possible to project a completely realistic mirage, making an empty glass appear to be full of practically any beverage.","What about the sensation of liquid? The taste? Both are simulated through the very creative use of miniature surgical lasers. By targeting and burning away very specific parts of your lips and tongue, The Olive Garden's team of cyber whizzes can trick your body into thinking that it's experiencing genuine sensations. In layman's terms it's basically the same computer wizardry that was in The Matrix. Whoa!","– Dennis \"Corin Tucker's Stalker\" Farrell"};
		
		SearchSet<String> searchSet = new SearchSet<String>(3);
		for (String s : strings)
			for (String split : s.split("\\."))
				searchSet.add(split);

		Common.noop();
		
		String[] out = new String[searchSet.size()];
		
		long time = -1L;
		for (int i = 0; i < 1000; i++)
		{
			time = System.nanoTime();
			searchSet.search("menu", out, 0);
			time = System.nanoTime() - time;
	}
		System.out.println(Arrays.toString(out));
		
		Common.noop();
}
}
