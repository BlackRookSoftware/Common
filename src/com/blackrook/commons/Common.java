/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.awt.Color;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.*;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.zip.ZipFile;

import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.linkedlist.Stack;
import com.blackrook.commons.list.List;
import com.blackrook.commons.math.RMath;

/**
 * This class serves as a static delegate for a slew of common methods
 * and functions.
 * @author Matthew Tropiano
 */
public final class Common
{
	/** ThreadLocal HashMap */
	private static final ThreadLocal<HashMap<String, Object>> THREADLOCAL_HASHMAP = new ThreadLocal<HashMap<String,Object>>()
	{
		protected HashMap<String,Object> initialValue()
		{
			return new HashMap<String, Object>();
		}
	};
	
	/** The size of a character in bytes. */
	public static final int SIZEOF_CHAR = Character.SIZE/Byte.SIZE;
	/** The size of an int in bytes. */
	public static final int SIZEOF_INT = Integer.SIZE/Byte.SIZE;
	/** The size of a float in bytes. */
	public static final int SIZEOF_FLOAT = Float.SIZE/Byte.SIZE;
	/** The size of a byte in bytes (This should always be 1, or Sun screwed up).*/
	public static final int SIZEOF_BYTE = Byte.SIZE/Byte.SIZE;
	/** The size of a short in bytes. */
	public static final int SIZEOF_SHORT = Short.SIZE/Byte.SIZE;
	/** The size of a long in bytes. */
	public static final int SIZEOF_LONG = Long.SIZE/Byte.SIZE;
	/** The size of a double in bytes. */
	public static final int SIZEOF_DOUBLE = Double.SIZE/Byte.SIZE;

	private static final String PARSE_ARRAY_SEPARATOR_PATTERN = "(\\s|\\,)+";

	/** Is this running on an x86 architecture? */
	private static boolean IS_X86 = false;
	/** Is this running on an x64 architecture? */
	private static boolean IS_X64 = false;
	/** Is this running on a Power PC architecture? */
	private static boolean IS_PPC = false;
	/** Are we using Windows? */
	private static boolean IS_WINDOWS = false;
	/** Are we using Windows 95/98? */
	private static boolean IS_WINDOWS_9X = false;
	/** Are we using Windows Me (God forbid)?*/
	private static boolean IS_WINDOWS_ME = false;
	/** Are we using Windows 2000? */
	private static boolean IS_WINDOWS_2000 = false;
	/** Are we using Windows XP? */
	private static boolean IS_WINDOWS_XP = false;
	/** Are we using Windows XP? */
	private static boolean IS_WINDOWS_VISTA = false;
	/** Are we using Windows 7? */
	private static boolean IS_WINDOWS_7 = false;
	/** Are we using Windows 8? */
	private static boolean IS_WINDOWS_8 = false;
	/** Are we using Windows NT? */
	private static boolean IS_WINDOWS_NT = false;
	/** Are we using Windows 2003 (Server)? */
	private static boolean IS_WINDOWS_2003 = false;
	/** Are we using Windows 2008 (Server)? */
	private static boolean IS_WINDOWS_2008 = false;
	/** Are we using Win32 mode? */
	private static boolean IS_WIN32 = false;
	/** Are we using Win64 mode? */
	private static boolean IS_WIN64 = false;
	/** Is this Mac OS X? */
	private static boolean IS_OSX = false;
	/** Is this Mac OS X x86 Edition? */
	private static boolean IS_OSX86 = false;
	/** Is this Mac OS X x64 Edition? */
	private static boolean IS_OSX64 = false;
	/** Is this Mac OS X Power PC Edition? */
	private static boolean IS_OSXPPC = false;
	/** Is this a Linux distro? */
	private static boolean IS_LINUX = false;
	/** Is this a Solaris distro? */
	private static boolean IS_SOLARIS = false;
	
	/** Current working directory. */
	public static String WORK_DIR = System.getProperty("user.dir");
	/** 
	 * Current application settings directory.
	 * In Windows, this is set to whatever the APPDATA environment variable is set to.
	 * On other operating systems, this is set to whatever the HOME environment variable is set to.
	 */
	public static String APP_DIR = null;		// set in static method.
	/** Current user's home directory. */
	public static String HOME_DIR = System.getProperty("user.home");

	/** The relay buffer size, used by relay(). */
	private static int RELAY_BUFFER_SIZE = 8192;
	/** The input wrapper used by getLine(). */
	private static BufferedReader SYSTEM_IN_READER;
	
	static
	{
		String osName = System.getProperty("os.name");
		//String osVer = System.getProperty("os.version");
		String osArch = System.getProperty("os.arch");
		IS_X86 = osArch.contains("86");
		IS_X64 = osArch.contains("64");
		IS_PPC = osArch.contains("ppc") || osArch.contains("Power PC");
		IS_WINDOWS = osName.contains("Windows");
		if (IS_WINDOWS)
		{
			IS_WINDOWS_9X = osName.contains("95") || osName.contains("98");
			IS_WINDOWS_ME = osName.contains("Me");
			IS_WINDOWS_2000 = osName.contains("2000");
			IS_WINDOWS_XP = osName.contains("XP");
			IS_WINDOWS_NT = osName.contains("NT");
			IS_WINDOWS_2003 = osName.contains("2003");
			IS_WINDOWS_2008 = osName.contains("2008");
			IS_WINDOWS_VISTA = osName.contains("Vista");
			IS_WINDOWS_7 = osName.contains("7");
			IS_WINDOWS_8 = osName.contains("8");
			IS_WIN32 = IS_X86;
			IS_WIN64 = IS_X64;
		}
		IS_OSX = osName.contains("OS X");
		if (IS_OSX)
		{
			IS_OSX86 = IS_X86;
			IS_OSX64 = IS_X64;
			IS_OSXPPC = IS_PPC;
		}
		IS_LINUX = osName.contains("Linux");
		if (IS_LINUX)
		{
			
		}
		IS_SOLARIS = osName.contains("Solaris");
		if (IS_SOLARIS)
		{
			
		}
		
		// Application data folder.
		if (IS_WINDOWS)
			APP_DIR = System.getenv("APPDATA");
		else if (IS_OSX)
			APP_DIR = System.getenv("HOME");
		else if (IS_LINUX)
			APP_DIR = System.getenv("HOME");
		else if (IS_SOLARIS)
			APP_DIR = System.getenv("HOME");
		
	}
	
	private Common() {}
	
	/** Are we using a Linux distro? */
	public static boolean isLinux()
	{
		return IS_LINUX;
	}

	/** Are we using Mac OS X? */
	public static boolean isOSX()
	{
		return IS_OSX;
	}

	/** Are we using 64-bit Mac OS X? */
	public static boolean isOSX64()
	{
		return IS_OSX64;
	}

	/** Are we using x86 Mac OS X? */
	public static boolean isOSX86()
	{
		return IS_OSX86;
	}

	/** Are we using Power PC Mac OS X? */
	public static boolean isOSXPPC()
	{
		return IS_OSXPPC;
	}

	/** Is this running on an Power PC architecture? */
	public static boolean isPPC()
	{
		return IS_PPC;
	}

	/** Are we using 32-bit Windows? */
	public static boolean isWin32()
	{
		return IS_WIN32;
	}

	/** Are we using 64-bit Windows? */
	public static boolean isWin64() 
	{
		return IS_WIN64;
	}

	/** Are we using Windows? */
	public static boolean isWindows()
	{
		return IS_WINDOWS;
	}

	/** Are we using Windows 2000? */
	public static boolean isWindows2000()
	{
		return IS_WINDOWS_2000;
	}

	/** Are we using Windows 2003? */
	public static boolean isWindows2003()
	{
		return IS_WINDOWS_2003;
	}

	/** Are we using Windows 2008? */
	public static boolean isWindows2008()
	{
		return IS_WINDOWS_2008;
	}

	/** Are we using Windows Vista? */
	public static boolean isWindowsVista()
	{
		return IS_WINDOWS_VISTA;
	}

	/** Are we using Windows 7? */
	public static boolean isWindows7()
	{
		return IS_WINDOWS_7;
	}

	/** 
	 * Are we using Windows 8?
	 * @since 2.13.0 
	 */
	public static boolean isWindows8()
	{
		return IS_WINDOWS_8;
	}

	/** Are we using Windows 95/98? */
	public static boolean isWindows9X()
	{
		return IS_WINDOWS_9X;
	}

	/** Are we using Windows Me? Or better yet, should we just kill the program now? */
	public static boolean isWindowsME()
	{
		return IS_WINDOWS_ME;
	}

	/** Are we using Windows NT? */
	public static boolean isWindowsNT()
	{
		return IS_WINDOWS_NT;
	}

	/** Are we using Windows XP? */
	public static boolean isWindowsXP()
	{
		return IS_WINDOWS_XP;
	}

	/** Is this running on an x64 architecture? */
	public static boolean isX64()
	{
		return IS_X64;
	}

	/** Is this running on an x86 architecture? */
	public static boolean isX86()
	{
		return IS_X86;
	}

	/** Is this running on Sun Solaris? */
	public static boolean isSolaris()
	{
		return IS_SOLARIS;
	}

	/**
	 * Returns the first object in the supplied list of objects that isn't null. 
	 * @param objects the list of objects.
	 * @return the first object that isn't null in the list, 
	 * or null if all of the objects are null.
	 */
	public static <T> T coalesce(T ... objects)
	{
		for (int i = 0; i < objects.length; i++)
			if (objects[i] != null)
				return objects[i];
		return null;
	}
	
	/**
	 * Returns a valid index of an element in an array if an object is contained in an array. 
	 * Sequentially searches for first match via {@link #equals(Object)}.
	 * Can search for null. 
	 * @param object the object to search for. Can be null.
	 * @param searchArray the list of objects to search.
	 * @return the index of the object, or -1 if it cannot be found.
	 * @since 2.13.2
	 */
	public static <T> int indexOf(T object, T[] searchArray)
	{
		for (int i = 0; i < searchArray.length; i++)
		{
			if (object == null && searchArray[i] == null)
				return i;
			else if (object.equals(searchArray[i]))
				return i;
		}
		return -1;
	}
	
	/**
	 * Calls Thread.sleep() but in an encapsulated try
	 * to avoid catching InterruptedException. Convenience
	 * method for making the current thread sleep when you don't
	 * care if it's interrupted or not and want to keep code neat.
	 */
	public static void sleep(long millis)
	{
		try {Thread.sleep(millis);	} catch (InterruptedException e) {}
	}
	
	/**
	 * Calls Thread.sleep() but in an encapsulated try
	 * to avoid catching InterruptedException. Convenience
	 * method for making the current thread sleep when you don't
	 * care if it's interrupted or not and want to keep code neat.
	 */
	public static void sleep(long millis, int nanos)
	{
		try {Thread.sleep(millis, nanos);	} catch (InterruptedException e) {}
	}
	
	/**
	 * Gets the package path for a particular class (for classpath resources).
	 * @param cls the class for which to get to get the path.
	 */
	public static String getPackagePathForClass(Class<?> cls)
	{
		return cls.getPackage().getName().replaceAll("\\.", "/");		
	}
	
	/**
	 * Adds a list of files to the JVM Classpath during runtime.
	 * The files are added to the current thread's class loader.
	 * Directories in the list of files are exploded down to actual
	 * files, so that no directories remain.
	 * @param files	the list of files to add.
	 */
	public static void addFilesToClassPath(File ... files)
	{
		addURLsToClassPath(getURLsForFiles(files));
	}
	
	/**
	 * Adds a list of URLs to the JVM Classpath during runtime.
	 * The URLs are added to the current thread's class loader.
	 * @param urls	the list of files to add.
	 */
	public static void addURLsToClassPath(URL ... urls)
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URLClassLoader ucl = new URLClassLoader(urls,cl);
		Thread.currentThread().setContextClassLoader(ucl);
	}
	
	/**
	 * Creates a list of URLs from a list of files or directories.
	 * Directories in the list of files are exploded down to actual
	 * files, so that no directories remain. Because of this,
	 * the output list of URLs may be longer than the input file list!
	 * @param files	the list of files to convert.
	 * @since 2.16.0
	 */
	public static URL[] getURLsForFiles(File ... files)
	{
		File[] flist = explodeFiles(files);
		URL[] urls = new URL[flist.length];
		for (int i = 0; i < flist.length; i++)
		{
			try {
				urls[i] = flist[i].toURI().toURL();
			} catch (MalformedURLException e) {
				RuntimeException re = new RuntimeException("A malformed URL was created somehow.");
				re.initCause(e);
				throw re;
			}
		}
		return urls;
	}
	
	/**
	 * Adds a list of files/directories to the library path of the JVM.
	 * This utilizes a fix originally authored by Antony Miguel.
	 * @param libs the files to add. 
	 * @throws SecurityException if the JVM does not have access to the native library path field.
	 * @throws RuntimeException if the JVM cannot find the native library path field.
	 */
	public static void addLibrariesToPath(File ... libs)
	{
		for (File f : libs)
			addLibraryToPath(f);
	}
	
	/**
	 * Adds a file/directory to the library path of the JVM.
	 * This utilizes a fix originally authored by Antony Miguel.
	 * @param lib the file to add. 
	 * @throws SecurityException if the JVM does not have access to the native library path field.
	 * @throws RuntimeException if the JVM cannot find the native library path field.
	 */
	public static void addLibraryToPath(File lib)
	{
		String libPath = lib.getPath();
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[])field.get(null);
			for (int i = 0; i < paths.length; i++)
			{
				if (libPath.equals(paths[i])) {
					return;
			}
		}
			String[] tmp = new String[paths.length+1];
			System.arraycopy(paths,0,tmp,0,paths.length);
			tmp[paths.length] = libPath;
			field.set(null,tmp);
		} catch (IllegalAccessException e) {
			throw new SecurityException("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Failed to get field handle to set library path");
		}
		System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + libPath);
	}
	
	/**
	 * Gets a String representation of a Throwable type,
	 * including a line-by-line breakdown of the stack trace.
	 */
	public static String getExceptionString(Throwable t)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(t.getClass().getName()+": "+t.getLocalizedMessage());
		sb.append('\n');
		for (StackTraceElement ent : t.getStackTrace())
		{
			sb.append(ent.toString());
			sb.append('\n');
		}
		if (t.getCause() != null)
		{
			sb.append("...Caused by:\n");
			sb.append(getExceptionString(t.getCause()));
		}
		return sb.toString();
	}
	
	/**
	 * Explodes a list of files into a larger list of files,
	 * such that all of the files in the resultant list are not
	 * directories, by traversing directory paths.
	 *
	 * The returned list is not guaranteed to be in any order
	 * related to the input list, and may contain files that are
	 * in the input list if they are not directories.
	 *
	 * @param files	the list of files to expand.
	 * @return	a list of all files found in the subdirectory search.
	 * @throws	NullPointerException if files is null.
	 */
	public static File[] explodeFiles(File ... files)
	{
		Queue<File> fileQueue = new Queue<File>();
		List<File> fileList = new List<File>();

		for (File f : files)
			fileQueue.enqueue(f);

		while (!fileQueue.isEmpty())
		{
			File dequeuedFile = fileQueue.dequeue();
			if (dequeuedFile.isDirectory())
			{
				for (File f : dequeuedFile.listFiles())
					fileQueue.enqueue(f);
			}
			else
			{
				fileList.add(dequeuedFile);
			}
		}

		File[] out = new File[fileList.size()];
		fileList.toArray(out);
		return out;
	}

	/**
	 * Convenience method for
	 * <code>new BufferedReader(new InputStreamReader(in))</code>
	 * @param in the stream to read.
	 * @return an open buffered reader for the provided stream.
	 * @throws IOException if an error occurred opening the stream for reading.
	 * @throws SecurityException if you do not have permission for opening the stream.
	 * @since 2.9.0
	 */
	public static BufferedReader openTextStream(InputStream in) throws IOException
	{
		return new BufferedReader(new InputStreamReader(in));
	}
	
	/**
	 * Convenience method for
	 * <code>new BufferedReader(new InputStreamReader(new FileInputStream(file)))</code>
	 * @param file the file to open.
	 * @return an open buffered reader for the provided file.
	 * @throws IOException if an error occurred opening the file for reading.
	 * @throws SecurityException if you do not have permission for opening the file.
	 * @since 2.9.0
	 */
	public static BufferedReader openTextFile(File file) throws IOException
	{
		return openTextStream(new FileInputStream(file));
	}
	
	/**
	 * Convenience method for
	 * <code>new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))</code>
	 * @param filePath the path of the file to open.
	 * @return an open buffered reader for the provided path.
	 * @throws IOException if an error occurred opening the file for reading.
	 * @throws SecurityException if you do not have permission for opening the file.
	 * @since 2.9.0
	 */
	public static BufferedReader openTextFile(String filePath) throws IOException
	{
		return openTextFile(new File(filePath));
	}
	
	/**
	 * Convenience method for
	 * <code>new BufferedReader(new InputStreamReader(System.in))</code>
	 * @return an open buffered reader for {@link System#in}.
	 * @throws IOException if an error occurred opening Standard IN.
	 * @throws SecurityException if you do not have permission for opening Standard IN.
	 * @since 2.9.0
	 */
	public static BufferedReader openSystemIn() throws IOException
	{
		return openTextStream(System.in);
	}
	
	/**
	 * Retrieves the ASCII contents of a file.
	 * @param f		the file to use.
	 * @return		a contiguous string (including newline characters) of the file's contents.
	 * @throws FileNotFoundException	if the file cannot be found.
	 * @throws IOException				if the read cannot be done.
	 */
	public static String getASCIIContents(File f) throws IOException
	{
		FileInputStream fis = new FileInputStream(f);
		String out = getTextualContents(fis, "ASCII");
		fis.close();
		return out;
	}
	
	/**
	 * Retrieves the textual contents of a file in the system's current encoding.
	 * @param f	the file to use.
	 * @return		a contiguous string (including newline characters) of the file's contents.
	 * @throws IOException	if the read cannot be done.
	 * @since 2.13.1
	 */
	public static String getTextualContents(File f) throws IOException
	{
		FileInputStream fis = new FileInputStream(f);
		String out = getTextualContents(fis);
		fis.close();
		return out;
	}
	
	/**
	 * Retrieves the textual contents of a stream in the system's current encoding.
	 * @param in	the input stream to use.
	 * @return		a contiguous string (including newline characters) of the stream's contents.
	 * @throws IOException	if the read cannot be done.
	 */
	public static String getTextualContents(InputStream in) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
			sb.append('\n');
		}
		br.close();
		return sb.toString();
	}
	
	/**
	 * Retrieves the textual contents of a stream.
	 * @param in		the input stream to use.
	 * @param encoding	name of the encoding type.
	 * @return		a contiguous string (including newline characters) of the stream's contents.
	 * @throws IOException				if the read cannot be done.
	 */
	public static String getTextualContents(InputStream in, String encoding) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
		String line;
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
			sb.append('\n');
		}
		br.close();
		return sb.toString();
	}
	
	/**
	 * Retrieves the binary contents of a file.
	 * @param f		the file to use.
	 * @return		an array of the bytes that make up the file.
	 * @throws FileNotFoundException	if the file cannot be found.
	 * @throws IOException				if the read cannot be done.
	 */
	public static byte[] getBinaryContents(File f) throws IOException
	{
		FileInputStream fis = new FileInputStream(f);
		byte[] b = getBinaryContents(fis, (int)f.length());
		fis.close();
		return b;
	}

	/**
	 * Retrieves the binary contents of a stream.
	 * @param in	the input stream to use.
	 * @param len	the amount of bytes to read.
	 * @return		an array of len bytes that make up the stream.
	 * @throws IOException				if the read cannot be done.
	 */
	public static byte[] getBinaryContents(InputStream in, int len) throws IOException
	{
		byte[] b = new byte[len];
		in.read(b);
		return b;
	}

	/**
	 * Retrieves the binary contents of a stream until it hits the end of the stream.
	 * @param in	the input stream to use.
	 * @return		an array of len bytes that make up the data in the stream.
	 * @throws IOException	if the read cannot be done.
	 */
	public static byte[] getBinaryContents(InputStream in) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Common.relay(in, bos);
		return bos.toByteArray();
	}
	
	/**
	 * Gets the content from a opening an HTTP URL, using the default timeout
	 * of 30 seconds (30000 milliseconds).
	 * @param url the URL to open and read.
	 * @return the content from opening an HTTP request.
	 * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx).
	 * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.5.0
	 */
	public static String getHTTPContent(URL url) throws IOException, SocketTimeoutException
	{
		return getHTTPContent(url, 30000);
	}
	
	/**
	 * Gets the content from a opening an HTTP URL.
	 * @param url the URL to open and read.
	 * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
	 * @return the content from opening an HTTP request.
	 * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx).
	 * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.6.0
	 */
	public static String getHTTPContent(URL url, int socketTimeoutMillis) throws IOException, SocketTimeoutException
	{
		if (!url.getProtocol().equalsIgnoreCase("http") && !url.getProtocol().equalsIgnoreCase("https"))
			throw new IOException("This is not an HTTP URL.");
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setReadTimeout(socketTimeoutMillis);
		
		int code = conn.getResponseCode();
		String codeString = conn.getResponseMessage();
		String encoding = conn.getContentEncoding();
		
		if (!(code >= 200 && code < 300))
		{
			conn.disconnect();
			throw new IOException("Response was code "+code+": "+codeString);
		}
		
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			if (encoding != null)
				isr = new InputStreamReader(conn.getInputStream(), encoding);
			else
				isr = new InputStreamReader(conn.getInputStream());
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line).append("\n");
		} catch (IOException e) {
			throw e;
			} finally {
			Common.close(br);
		}
		
		conn.disconnect();
		
		return sb.toString();
	}

	/**
	 * Gets the byte content from a opening an HTTP URL, using the default timeout
	 * of 30 seconds (30000 milliseconds).
	 * @param url the URL to open and read.
	 * @return the content from opening an HTTP request.
	 * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx).
	 * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.5.0
	 */
	public static byte[] getHTTPByteContent(URL url) throws IOException, SocketTimeoutException
	{
		return getHTTPByteContent(url, 30000);
	}
	
	/**
	 * Gets the byte content from a opening an HTTP URL.
	 * @param url the URL to open and read.
	 * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
	 * @return the content from opening an HTTP request.
	 * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx).
	 * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.6.0
	 */
	public static byte[] getHTTPByteContent(URL url, int socketTimeoutMillis) throws IOException, SocketTimeoutException
	{
		if (!url.getProtocol().equalsIgnoreCase("http") && !url.getProtocol().equalsIgnoreCase("https"))
			throw new IOException("This is not an HTTP URL.");
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setReadTimeout(socketTimeoutMillis);

		int code = conn.getResponseCode();
		String codeString = conn.getResponseMessage();
		
		if (!(code >= 200 && code < 300))
		{
			conn.disconnect();
			throw new IOException("Response was code "+code+": "+codeString);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(conn.getInputStream());
			Common.relay(in, bos);
		} catch (IOException e) {
			throw e;
			} finally {
			Common.close(in);
		}
		
		conn.disconnect();
		
		return bos.toByteArray();
	}

	/**
	 * Gets the byte content from a opening an FTP URL, using a default timeout
	 * of 30 seconds (30000 milliseconds).
	 * @param url the URL to open and read.
	 * @return the content from opening an HTTP request.
	 * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx).
	 * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.5.0
	 */
	public static byte[] getByteContent(URL url) throws IOException, SocketTimeoutException
	{
		return getByteContent(url, 30000);
	}
	
	/**
	 * Gets the byte content from a opening an FTP URL.
	 * @param url the URL to open and read.
	 * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
	 * @return the content from opening an HTTP request.
	 * @throws IOException if an error happens during the read.
	 * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.6.0
	 */
	public static byte[] getByteContent(URL url, int socketTimeoutMillis) throws IOException, SocketTimeoutException
	{
		URLConnection conn = url.openConnection();
		conn.setReadTimeout(socketTimeoutMillis);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(conn.getInputStream());
			Common.relay(in, bos);
		} catch (IOException e) {
			throw e;
			} finally {
			Common.close(in);
		}
		
		return bos.toByteArray();
	}

	/**
	 * Returns a hash of a set of bytes digested by an encryption algorithm.
	 * Can return null if this Java implementation cannot perform this.
	 * @param bytes the bytes to encode.
	 * @param algorithmName the name to the algorithm to use.
	 * @since 2.10.0
	 */
	public static byte[] digest(byte[] bytes, String algorithmName)
	{
		try {
			return MessageDigest.getInstance(algorithmName).digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	/**
	 * Returns a 20-byte SHA-1 hash of a set of bytes.
	 * Can return null if this Java implementation cannot perform this,
	 * but it shouldn't, since SHA-1 is mandatorily implemented for all
	 * @since 2.9.0
	 */
	public static byte[] sha1(byte[] bytes)
	{
		return digest(bytes, "SHA-1");
	}
	
	/**
	 * Returns a 16-byte MD5 hash of a set of bytes.
	 * Can return null if this Java implementation cannot perform this.
	 * @since 2.9.0
	 */
	public static byte[] md5(byte[] bytes)
	{
		return digest(bytes, "MD5");
	}
	
	/**
	 * Encodes a series of bytes as a Base64 encoded string.
	 * Uses + and / as characters 62 and 63.
	 * @param in the input stream to read to convert to Base64.
	 * @return a String of encoded bytes, or null if the message could not be encoded.
	 * @since 2.9.0
	 */
	public static String asBase64(InputStream in) throws IOException
	{
		return asBase64(in, '+', '/');
	}

	/**
	 * Encodes a series of bytes as a Base64 encoded string.
	 * @param in the input stream to read to convert to Base64.
	 * @param sixtyTwo the character to use for character 62 in the Base64 index.
	 * @param sixtyThree the character to use for character 63 in the Base64 index.
	 * @return a String of encoded bytes, or null if the message could not be encoded.
	 * @since 2.9.0
	 */
	public static String asBase64(InputStream in, char sixtyTwo, char sixtyThree) throws IOException
	{
		final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final char BLANK = '=';
		
		String alph = ALPHABET + sixtyTwo + sixtyThree;
		
		StringBuilder out = new StringBuilder();
		int octetBuffer = 0x00000000;
		int bidx = 0;
		
		byte[] buffer = new byte[RELAY_BUFFER_SIZE];
		int buf = 0;
		
		while ((buf = in.read(buffer)) > 0) for (int i = 0; i < buf; i++)
		{
			byte b = buffer[i];
			
			octetBuffer |= ((b & 0x0ff) << ((2 - bidx) * 8));
			bidx++;
			if (bidx == 3)
			{
				out.append(alph.charAt((octetBuffer & (0x3f << 18)) >> 18));
				out.append(alph.charAt((octetBuffer & (0x3f << 12)) >> 12));
				out.append(alph.charAt((octetBuffer & (0x3f << 6)) >> 6));
				out.append(alph.charAt(octetBuffer & 0x3f));
				octetBuffer = 0x00000000;
				bidx = 0;
			}
		}
		
		if (bidx == 2)
		{
			out.append(alph.charAt((octetBuffer & (0x3f << 18)) >> 18));
			out.append(alph.charAt((octetBuffer & (0x3f << 12)) >> 12));
			out.append(alph.charAt((octetBuffer & (0x3f << 6)) >> 6));
			out.append(BLANK);
		}
		else if (bidx == 1)
		{
			out.append(alph.charAt((octetBuffer & (0x3f << 18)) >> 18));
			out.append(alph.charAt((octetBuffer & (0x3f << 12)) >> 12));
			out.append(BLANK);
			out.append(BLANK);
		}
		
		return out.toString();
	}
	
	/**
	 * Creates a blank file or updates its last modified date.
	 * @param filePath	the abstract path to use.
	 * @return true if the file was made/updated, false otherwise.
	 * @throws IOException	if creating/modifying the file violates something.
	 */
	public static boolean touch(String filePath) throws IOException
	{
		File f = new File(filePath);
		FileOutputStream fos = new FileOutputStream(f,true);
		fos.close();
		return true;
	}

	/**
	 * Securely deletes a file. Overwrites its data with zeroes and its filename
	 * a bunch of times before it finally deletes it. Places an exclusive
	 * lock on the file as it is getting deleted. The length of time taken
	 * depends the file's length and the efficiency of the medium that contains it.
	 * <p>
	 * <b>DISCLAIMER:</b> Black Rook Software makes NO CLAIMS of COMPLETE SECURITY
	 * concerning deletions of files using this method. This method is provided AS-IS.
	 * It is reasonably secure insofar as deleting file content and headers as Java
	 * may allow. This does not alter dates of files.
	 * @param file the file to delete.
	 * @throws FileNotFoundException if the file does not exist.
	 * @throws IOException if the delete cannot be completed.
	 * @since 2.9.0
	 */
	public static void secureDelete(File file) throws IOException
	{
		secureDelete(file, 1);
	}
	
	/**
	 * Securely deletes a file. Overwrites its data and its filename
	 * a bunch of times before it finally deletes it. Places an exclusive
	 * lock on the file as it is getting deleted. The length of time taken
	 * depends the file's length and the efficiency of the medium that contains it. 
	 * <p>
	 * <b>DISCLAIMER:</b> Black Rook Software makes NO CLAIMS of COMPLETE SECURITY
	 * concerning deletions of files using this method. This method is provided AS-IS.
	 * It is reasonably secure insofar as deleting file content and headers as Java
	 * may allow. This does not alter dates of files directly.
	 * @param file the file to delete.
	 * @param passes the amount of passes for overwriting this file.
	 * The last pass is always zero-filled. If less than 1, it is 1.
	 * @throws FileNotFoundException if the file does not exist.
	 * @throws IOException if the delete cannot be completed.
	 * @since 2.9.0
	 */
	public static void secureDelete(File file, int passes) throws IOException
	{
		passes = passes < 1 ? 1 : passes;
		boolean bitval = passes == 1 ? false : (passes % 2) == 0;

		// Overwrite.
		RandomAccessFile raf = new RandomAccessFile(file, "rws");
		FileLock lock = raf.getChannel().lock();
		byte[] buffer = new byte[65536];
		while (passes-- > 0)
		{
			Arrays.fill(buffer, (byte)(bitval ? 0xFF : 0x00));
			long end = raf.length();
			raf.seek(0L);
			long n = 0L;
			while (n < end)
			{
				raf.write(buffer, 0, Math.min(buffer.length, (int)(end - n)));
				n = raf.getFilePointer();
			}
		}
		lock.release();
		raf.close();
		
		// Overwrite filename.
		String newName = null;
		char[] namebuf = new char[file.getName().length()];
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ9876543210".toCharArray();
		for (char c : alphabet)
		{
			Arrays.fill(namebuf, c);
			newName = new String(namebuf);
			File ren = new File(file.getParent() + File.separator + newName);
			file.renameTo(ren);
			file = ren;
		}
		
		file.delete();
	}
	
	/**
	 * Creates a file filled with random data with the specified length.
	 * If <code>file</code> refers to an existing file, it will be OVERWRITTEN.
	 * @param file the file to write.
	 * @throws IOException if an I/O error occurs.
	 * @since 2.9.0
	 */
	public static void createJunkFile(File file, int length) throws IOException
	{
		byte[] buffer = new byte[65536];
		Random r = new Random();
		int n = 0;
		
		FileOutputStream fos = new FileOutputStream(file);
		while (n < length)
		{
			RMath.randBytes(r, buffer);
			int len = Math.min(buffer.length, length - n);
			fos.write(buffer, 0, len);
			fos.flush();
			n += len;
		}
		fos.close();
	}
	
	/**
	 * Makes a new String with escape sequences in it.
	 * @param s		the original string.
	 * @return		the new one with escape sequences in it.
	 */
	public static String withEscChars(String s)
    {
    	char[] c = s.toCharArray();
    	String out = "";
    	for (int i = 0; i < c.length; i++)
    		switch (c[i])
    		{
				case '\0':
					out += "\\0";
					break;
    			case '\b':
    				out += "\\b";
    				break;
    			case '\t':
    				out += "\\t";
    				break;
    			case '\n':
    				out += "\\n";
    				break;
    			case '\f':
    				out += "\\f";
    				break;
    			case '\r':
    				out += "\\r";
    				break;
    			case '\\':
    				out += "\\\\";
    				break;
    			case '"':
    				if (i != 0 && i != c.length-1)
    					out += "\\\"";    					
    				else
    					out += "\"";
    				break;
    			default:
    				out += c[i];
    				break;
    		}
    	return out;
    }

	/**
	 * Escapes a string so that it can be input safely into a URL string.
	 */
	public static String urlEscape(String inString)
	{
		StringBuffer sb = new StringBuffer();
		char[] inChars = inString.toCharArray();
		int i = 0;
		while (i < inChars.length)
		{
			char c = inChars[i];
			if (!((c >= 0x30 && c <= 0x39) || (c >= 0x41 && c <= 0x5a) || (c >= 0x61 && c <= 0x7a)))
				sb.append(String.format("%%%02x", (short)c));
			else
				sb.append(c);
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * Decodes a URL-encoded string.
	 * @since 2.19.0
	 */
	public static String urlUnescape(String inString)
	{
		StringBuffer sb = new StringBuffer();
		char[] inChars = inString.toCharArray();
		char[] chars = new char[2];
		int x = 0;
		
		final int STATE_START = 0;
		final int STATE_DECODE = 1;
		int state = STATE_START;
		
		int i = 0;
		while (i < inChars.length)
		{
			char c = inChars[i];
			
			switch (state)
			{
				case STATE_START:
					if (c == '%')
					{
						x = 0;
						state = STATE_DECODE;
					}
					else
						sb.append(c);
					break;
				case STATE_DECODE:
					chars[x++] = c;
					if (x == 2)
					{
						int v = 0;
						try {
							v = Integer.parseInt(new String(chars), 16);
							sb.append((char)(v & 0x0ff));
					} catch (NumberFormatException e) {
							sb.append('%').append(chars[0]).append(chars[1]);
						}
						state = STATE_START;
					}
					break;
			}
			
			i++;
		}
		
		if (state == STATE_DECODE)
		{
			sb.append('%');
			for (int n = 0; n < x; n++)
				sb.append(chars[n]);
		}
		
		return sb.toString();
	}

	/**
	 * Creates the necessary directories for a file path.
	 * @param file	the abstract file path.
	 * @return		true if the paths were made (or exists), false otherwise.
	 */
	public static boolean createPathForFile(File file)
	{
		return createPathForFile(file.getAbsolutePath());
	}

	/**
	 * Creates the necessary directories for a file path.
	 * @param path	the abstract path.
	 * @return		true if the paths were made (or exists), false otherwise.
	 */
	public static boolean createPathForFile(String path)
	{
		int sindx = -1;
		
		if ((sindx = Math.max(
				path.lastIndexOf(File.separator), 
				path.lastIndexOf("/"))) != -1)
		{
			return createPath(path.substring(0, sindx));
		}
		return true;
	}
	
	/**
	 * Creates the necessary directories for a file path.
	 * @param path	the abstract path.
	 * @return		true if the paths were made (or exists), false otherwise.
	 */
	public static boolean createPath(String path)
	{
		File dir = new File(path);
		if (dir.exists())
			return true;
		return dir.mkdirs();
	}
	
	/**
	 * Gets the relative path to a file path.
	 * @param sourcePath	source file path.
	 * @param targetPath	target file path to create the path to.
	 */
	public static String getRelativePath(String sourcePath, String targetPath) throws IOException
	{
		return getRelativePath(new File(sourcePath), new File(targetPath));
	}
	
	/**
	 * Gets the relative path to a file path.
	 * @param source	source file.
	 * @param target	target file to create the path to.
	 */
	public static String getRelativePath(File source, File target) throws IOException
	{
		Stack<File> sourcePath = new Stack<File>();
		Stack<File> targetPath = new Stack<File>();

		source = source.getCanonicalFile();
		sourcePath.push(source);
		while (source.getParentFile() != null)
		{
			source = source.getParentFile();
			sourcePath.push(source);
		}

		target = new File(target.getCanonicalPath());
		targetPath.push(target);
		while (target.getParentFile() != null)
		{
			target = target.getParentFile();
			targetPath.push(target);
		}
		
		if (isWindows())
		{
			String sroot = sourcePath.peek().getPath();
			String troot = targetPath.peek().getPath();
			if (!sroot.equals(troot))
				return targetPath.tail().getCanonicalPath();
		}
		
		while (!sourcePath.isEmpty() && !targetPath.isEmpty() && 
				sourcePath.peek().equals(targetPath.peek()))
		{
			sourcePath.pop();
			targetPath.pop();
		}
		
		StringBuilder sb = new StringBuilder();
		if (sourcePath.isEmpty())
		{
			sb.append(".");
			sb.append(File.separator);
		}
		else while (!sourcePath.isEmpty())
		{
			sb.append("..");
			sb.append(File.separator);
			sourcePath.pop();
		}
		
		while (!targetPath.isEmpty())
		{
			sb.append(targetPath.pop().getName());
			if (!targetPath.isEmpty())
				sb.append(File.separator);
		}

		return sb.toString();
	}
	
	/**
	 * Returns the extension of a filename.
	 * @param filename the file name.
	 * @param extensionSeparator the text or characters that separates file name from extension.
	 * @return the file's extension, or an empty string for no extension.
	 * @since 2.5.0
	 */
	public static String getFileExtension(String filename, String extensionSeparator)
	{
		int extindex = filename.lastIndexOf(extensionSeparator);
		if (extindex >= 0)
			return filename.substring(extindex+1);
		return "";
	}
	
	/**
	 * Returns the extension of a file's name.
	 * @param file the file.
	 * @param extensionSeparator the text or characters that separates file name from extension.
	 * @return the file's extension, or an empty string for no extension.
	 * @since 2.5.0
	 */
	public static String getFileExtension(File file, String extensionSeparator)
	{
		return getFileExtension(file.getName(), extensionSeparator);
	}
	
	/**
	 * Returns the extension of a filename.
	 * Assumes the separator to be ".".
	 * @param filename the file name.
	 * @return the file's extension, or an empty string for no extension.
	 * @since 2.5.0
	 */
	public static String getFileExtension(String filename)
	{
		return getFileExtension(filename, ".");
	}
	
	/**
	 * Returns the extension of a file's name.
	 * Assumes the separator to be ".".
	 * @param file the file.
	 * @return the file's extension, or an empty string for no extension.
	 * @since 2.5.0
	 */
	public static String getFileExtension(File file)
	{
		return getFileExtension(file.getName(), ".");
	}

	/**
	 * Checks if a file's name matches a DOS/UNIX-style wildcard pattern (uses system case and path separator policy).
	 * This checks the pattern against the file's filename, path, and absolute path 
	 * (<code>target.getName()</code>, <code>target.getPath()</code>, <code>target.getAbsolutePath()</code>).
	 * <p>
	 * A star (*) in a pattern matches zero or more characters except a slash (back or forward).
	 * A question mark (?) matches a single character, but not a slash (back or forward).
	 * </p>
	 * @param pattern the pattern to use.
	 * @param target the target file to check.
	 * @return true if the pattern matches, false otherwise.
	 * @since 2.10.0
	 */
	public static boolean matchWildcardPattern(String pattern, File target)
	{
		return matchWildcardPattern(pattern, target, isWindows() ? true : false);
	}

	/**
	 * Checks if a file's name matches a DOS/UNIX-style wildcard pattern.
	 * Treats slashes and backslashes as equal.
	 * This checks the pattern against the file's filename, path, and absolute path 
	 * (<code>target.getName()</code>, <code>target.getPath()</code>, <code>target.getAbsolutePath()</code>).
	 * <p>
	 * A star (*) in a pattern matches zero or more characters except a slash (back or forward).
	 * A question mark (?) matches a single character, but not a slash (back or forward).
	 * </p>
	 * @param pattern the pattern to use.
	 * @param target the target file to check.
	 * @param caseInsensitive if true, this will not use letter case to evaluate.
	 * @return true if the pattern matches, false otherwise.
	 * @since 2.10.0
	 */
	public static boolean matchWildcardPattern(String pattern, File target, boolean caseInsensitive)
	{
		boolean slashAgnostic = File.separatorChar == '\\';
		return 
			matchWildcardPattern(pattern, target.getName(), caseInsensitive, slashAgnostic)
			|| matchWildcardPattern(pattern, target.getPath(), caseInsensitive, slashAgnostic)
			|| matchWildcardPattern(pattern, target.getAbsolutePath(), caseInsensitive, slashAgnostic);
	}

	/**
	 * Checks if a string matches a DOS/UNIX-style wildcard pattern.
	 * <p>
	 * A star (*) in a pattern matches zero or more characters except a slash (back or forward).
	 * A question mark (?) matches a single character, but not a slash (back or forward).
	 * </p>
	 * @param pattern the pattern to use.
	 * @param target the target string to check.
	 * @param caseInsensitive if true, this will not use letter case to evaluate.
	 * @return true if the pattern matches, false otherwise.
	 * @since 2.10.0
	 */
	public static boolean matchWildcardPattern(String pattern, String target, boolean caseInsensitive)
	{
		return matchWildcardPattern(pattern, target, caseInsensitive, false);
	}
	
	/**
	 * Checks if a string matches a DOS/UNIX-style wildcard pattern.
	 * <p>
	 * A star (*) in a pattern matches zero or more characters except a slash (back or forward).
	 * A question mark (?) matches a single character, but not a slash (back or forward).
	 * </p>
	 * @param pattern the pattern to use.
	 * @param target the target string to check.
	 * @param caseInsensitive if true, this will not use letter case to evaluate.
	 * @param slashAgnostic if true, treats slashes and backslashes as equal.
	 * @return true if the pattern matches, false otherwise.
	 * @since 2.10.0
	 */
	public static boolean matchWildcardPattern(String pattern, String target, boolean caseInsensitive, boolean slashAgnostic)
	{
		if (pattern.length() == 0 && target.length() == 0)
			return true;
		
		final char ANY_ALL_CHAR = '*';
		final char ANY_ONE_CHAR = '?';

		int pi = 0;
		int ti = 0;
		int plen = pattern.length();
		int tlen = target.length();
		
		while (pi < plen && ti < tlen)
		{
			char p = pattern.charAt(pi);
			char t = target.charAt(ti);
			if (p != ANY_ALL_CHAR)
			{
				if (p == ANY_ONE_CHAR)
				{
					if (t == '/' || t == '\\')
						return false;
					else
					{
						pi++; 
						ti++;						
					}
				}
				else if (p == t)
				{
					pi++; 
					ti++;
				}
				else if (caseInsensitive && Character.toLowerCase(p) == Character.toLowerCase(t))
				{
					pi++; 
					ti++;
				}
				else if (slashAgnostic && (p == '/' || p == '\\') && (t == '/' || t == '\\'))
				{
					pi++; 
					ti++;
				}
				else
					return false;
			}
			else
			{
				char nextChar = pi+1 < plen ? pattern.charAt(pi+1) : '\0';
				if (nextChar == ANY_ALL_CHAR)
					pi++;
				else if (nextChar != '\0')
				{
					// does not match a slash.
					if (t == '/' || t == '\\')
						pi++;
					else if (nextChar == t)
						pi++;
					else if (caseInsensitive && Character.toLowerCase(nextChar) == Character.toLowerCase(t))
						pi++;
					else if (slashAgnostic && (p == '/' || p == '\\') && (t == '/' || t == '\\'))
						pi++; 
					else
						ti++;
				}
				// does not match a slash.
				else if (t == '/' || t == '\\')
					pi++;
				else
					ti++;
			}
		}
		
		if (pi == plen - 1)
			return pattern.charAt(pi) == ANY_ALL_CHAR && ti == tlen;
		return pi == plen && ti == tlen;
	}
	
	/**
	 * Returns a list of files that match a wildcard path.
	 * @param path the file path, relative or absolute that
	 * 		contains wildcards in the file name portion.
	 * @return a list of matching files. Can return an array of zero length if nothing matches.
	 * @since 2.10.1
	 */
	public static File[] getFilesByWildcardPath(String path)
	{
		return getFilesByWildcardPath(path, false);
	}
	
	/**
	 * Returns a list of files that match a wildcard path.
	 * @param path the file path, relative or absolute that
	 * 		contains wildcards in the file name portion.
	 * @param hidden if true, include hidden files.
	 * @return a list of matching files. Can return an array of zero length if nothing matches.
	 * @since 2.10.1
	 */
	public static File[] getFilesByWildcardPath(String path, boolean hidden)
	{
		Queue<File> out = new Queue<File>();
		
		boolean slashAgnostic = isWindows();
		boolean caseInsensitive = isWindows();
		
		String parent = null;
		String name = null;
		File pathFile = new File(path);
		
		if (pathFile.exists() && pathFile.isDirectory())
		{
			parent = path;
			name = "*";
		}
		else
		{
			int sidx = Math.max(path.lastIndexOf(File.separator), path.lastIndexOf("/"));
			parent = sidx >= 0 ? path.substring(0, sidx) : ".";
			name = sidx == path.length() - 1 ? "*" : path.substring(sidx + 1, path.length());
		}
		
		File dir = new File(parent);
		
		if (!(dir.exists() && dir.isDirectory()))
			return new File[0];
		
		for (File f : dir.listFiles())
		{
			if (!hidden && f.isHidden())
				continue;
			
			if (matchWildcardPattern(name, f.getName(), caseInsensitive, slashAgnostic))
				out.add(f);
		}
		
		File[] files = new File[out.size()];
		out.toArray(files);
		return files;
	}
	
	/**
	 * Checks if bits are set in a value.
	 * @param value		the value.
	 * @param test		the testing bits.
	 * @return			true if all of the bits set in test are set in value, false otherwise.
	 */
	public static boolean bitIsSet(long value, long test)
	{
		return (value & test) == test;
	}
	
	/**
	 * Sets the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to set.
	 * @return			the resulting number.
	 */
	public static long setBits(long value, long bits)
	{
		return value | bits;
	}

	/**
	 * Sets the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to set.
	 * @return			the resulting number.
	 */
	public static int setBits(int value, int bits)
	{
		return value | bits;
	}

	/**
	 * Clears the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to clear.
	 * @return			the resulting number.
	 */
	public static long clearBits(long value, long bits)
	{
		return value & ~bits;
	}

	/**
	 * Clears the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to clear.
	 * @return			the resulting number.
	 */
	public static int clearBits(int value, int bits)
	{
		return value & ~bits;
	}

	/**
	 * Converts a series of boolean values to bits,
	 * going from least-significant to most-significant.
	 * TRUE booleans set the bit, FALSE ones do not.
	 * @param bool	list of booleans. cannot exceed 32.
	 */
	public static int booleansToInt(boolean ... bool)
	{
		int out = 0;
		for (int i = 0; i < Math.min(bool.length, 32); i++)
			if (bool[i])
				out |= (1 << i);
		return out;
	}
	
	/**
	 * Converts a series of boolean values to bits,
	 * going from least-significant to most-significant.
	 * TRUE booleans set the bit, FALSE ones do not.
	 * @param bool	list of booleans. cannot exceed 64.
	 */
	public static long booleansToLong(boolean ... bool)
	{
		int out = 0;
		for (int i = 0; i < Math.min(bool.length, 64); i++)
			if (bool[i])
				out |= (1 << i);
		return out;
	}
	
	/**
	 * Returns a new byte array, delta-encoded.<br>Use with deltaDecode() to decode.
	 * <p>After the first byte, the change in value from the last byte is stored.
	 * <br>For example, a byte sequence <code>[64,92,-23,33]</code> would be returned
	 * as: <code>[64,28,-115,56]</code>.
	 */
	public static byte[] deltaEncode(byte[] b)
	{
		byte[] delta = new byte[b.length];
		delta[0] = b[0];
		for (int i = 1; i < b.length; i++)
			delta[i] = (byte)(b[i] - b[i-1]);
		
		return delta;
	}
	
	/**
	 * Returns a new byte array, delta-decoded.<br>Decodes sequences made with deltaEncode().
	 * <p>After the first byte, the change in value from the last byte is used to get the original value.
	 * <br>For example, a byte sequence <code>[64,28,-115,56]</code> would be returned
	 * as: <code>[64,92,-23,33]</code>.
	 */
	public static byte[] deltaDecode(byte[] b)
	{
		byte[] delta = /*decompressBytes(b)*/ b;
		
		byte[] out = new byte[delta.length];
		out[0] = delta[0];
		for (int i = 1; i < b.length; i++)
			out[i] = (byte)(out[i-1] + delta[i]);
		
		return out;
	}

	/**
	 * Returns a new byte array, Carmacized.
	 * Named after John D. Carmack, this will compress a sequence
	 * of bytes known to be alike in contiguous sequences.
	 */
	public static byte[] carmacize(byte[] b)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int seq = 1;
		byte prev = b[0];

		for(int i = 1; i < b.length; i++)
		{
			if (b[i] == prev)
				seq++;
			else if (prev == -1 || seq > 3)
			{
				while (seq > 0)
				{
					bos.write(255);
					bos.write(prev);
					bos.write(seq > 255 ? 255 : seq);
					seq -= 255;
				}
				prev = b[i];
				seq = 1;
			}
			else
			{
				for (int x = 0; x < seq; x++)
					bos.write(prev);
				prev = b[i];
				seq = 1;
			}
		}

		if (seq > 3)
			while (seq > 0)
			{
				bos.write(255);	
				bos.write(prev);
				bos.write(seq > 255 ? 255 : seq);
				seq -= 255;
			}
		else
			for (int x = 0; x < seq; x++)
				bos.write(prev);
		
		return bos.toByteArray(); 
	}

	/**
	 * Returns a Camacized byte array, de-Carmacized.
	 * Named after John D. Carmack, this will decompress a series of
	 * bytes encoded in the Carmacizing algorithm.
	 */
	public static byte[] decarmacize(byte[] b)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int i = 0; i < b.length; i++)
		{
			if (b[i] == -1)
			{
				i++;
				byte z = b[i];
				i++;
				int x = b[i] < 0? b[i] + 256 : b[i];
				for (int j = 0; j < x; j++)
					bos.write(z);
			}
			else
				bos.write(b[i]);
		}
		return bos.toByteArray();
	}

	/**
	 * Allocates a DIRECT ByteBuffer using byte array data.
	 * Useful for native wrappers that require direct ByteBuffers.
	 * @param b	the byte array to wrap. 
	 * @return	a direct buffer that can hold <i>len</i> items.
	 */
	public static ByteBuffer wrapDirectBuffer(byte[] b)
	{
		ByteBuffer buf = allocDirectByteBuffer(b.length);
		buf.put(b);
		buf.rewind();
		return buf;
	}
	
	/**
	 * Allocates space for a DIRECT ByteBuffer in native byte order
	 * (which really doesn't matter).
	 * @param len	the length (IN BYTES) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static ByteBuffer allocDirectByteBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_BYTE);
		b.order(ByteOrder.nativeOrder());
		return b;
	}
	
	/**
	 * Allocates space for a DIRECT IntBuffer in native byte order.
	 * @param len	the length (IN INTS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static IntBuffer allocDirectIntBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_INT);
		b.order(ByteOrder.nativeOrder());
		return b.asIntBuffer();
	}
	
	/**
	 * Allocates space for a DIRECT FloatBuffer in native byte order.
	 * @param len	the length (IN FLOATS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static FloatBuffer allocDirectFloatBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_FLOAT);
		b.order(ByteOrder.nativeOrder());
		return b.asFloatBuffer();
	}
	
	/**
	 * Allocates space for a DIRECT LongBuffer in native byte order
	 * @param len	the length (IN LONGS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static LongBuffer allocDirectLongBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_LONG);
		b.order(ByteOrder.nativeOrder());
		return b.asLongBuffer();
	}
	
	/**
	 * Allocates space for a DIRECT ShortBuffer in native byte order
	 * @param len	the length (IN SHORTS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static ShortBuffer allocDirectShortBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_SHORT);
		b.order(ByteOrder.nativeOrder());
		return b.asShortBuffer();
	}
	
	/**
	 * Allocates space for a DIRECT CharBuffer in native byte order
	 * @param len	the length (IN CHARS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static CharBuffer allocDirectCharBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_CHAR);
		b.order(ByteOrder.nativeOrder());
		return b.asCharBuffer();
	}
	
	/**
	 * Allocates space for a DIRECT DoubleBuffer in native byte order
	 * @param len	the length (IN DOUBLES) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static DoubleBuffer allocDirectDoubleBuffer(int len)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(len*SIZEOF_DOUBLE);
		b.order(ByteOrder.nativeOrder());
		return b.asDoubleBuffer();
	}
	
	/**
	 * Reads from an input stream, reading in a consistent set of data
	 * and writing it to the output stream. The read/write is buffered
	 * so that it does not bog down the OS's other I/O requests.
	 * This method finishes when the end of the source stream is reached.
	 * Note that this may block if the input stream is a type of stream
	 * that will block if the input stream blocks for additional input.
	 * This method is thread-safe.
	 * @param in	the input stream to grab data from.
	 * @param out	the output stream to write the data to.
	 * @return		the total amount of bytes relayed.
	 */
	public static int relay(InputStream in, OutputStream out) throws IOException
	{
		return relay(in, out, RELAY_BUFFER_SIZE, -1);
	}
	
	/**
	 * Reads from an input stream, reading in a consistent set of data
	 * and writing it to the output stream. The read/write is buffered
	 * so that it does not bog down the OS's other I/O requests.
	 * This method finishes when the end of the source stream is reached.
	 * Note that this may block if the input stream is a type of stream
	 * that will block if the input stream blocks for additional input.
	 * This method is thread-safe.
	 * @param in the input stream to grab data from.
	 * @param out the output stream to write the data to.
	 * @param bufferSize the buffer size for the I/O. Must be > 0.
	 * @return the total amount of bytes relayed.
	 * @since 2.14.0
	 */
	public static int relay(InputStream in, OutputStream out, int bufferSize) throws IOException
	{
		return relay(in, out, bufferSize, -1);
	}
	
	/**
	 * Reads from an input stream, reading in a consistent set of data
	 * and writing it to the output stream. The read/write is buffered
	 * so that it does not bog down the OS's other I/O requests.
	 * This method finishes when the end of the source stream is reached.
	 * Note that this may block if the input stream is a type of stream
	 * that will block if the input stream blocks for additional input.
	 * This method is thread-safe.
	 * @param in the input stream to grab data from.
	 * @param out the output stream to write the data to.
	 * @param bufferSize the buffer size for the I/O. Must be > 0.
	 * @param maxLength the maximum amount of bytes to relay, or a value < 0 for no max.
	 * @return the total amount of bytes relayed.
	 * @since 2.14.0
	 */
	public static int relay(InputStream in, OutputStream out, int bufferSize, int maxLength) throws IOException
	{
		int total = 0;
		int buf = 0;
			
		byte[] RELAY_BUFFER = new byte[bufferSize];
		
		while ((buf = in.read(RELAY_BUFFER, 0, Math.min(maxLength < 0 ? Integer.MAX_VALUE : maxLength, bufferSize))) > 0)
		{
			out.write(RELAY_BUFFER, 0, buf);
			total += buf;
			if (maxLength >= 0)
				maxLength -= buf;
		}
		return total;
	}
	
	/**
	 * Sets the size of the buffer in bytes for {@link Common#relay(InputStream, OutputStream)}.
	 * It would be unwise to set this during a call to relay().
	 * Size cannot be 0 or less.
	 */
	public static void setRelayBufferSize(int size)
	{
		if (size <= 0)
			throw new IllegalArgumentException();
		RELAY_BUFFER_SIZE = size;
	}
	
	/**
	 * Returns the size of the relay buffer for {@link Common#relay(InputStream, OutputStream)} in bytes.
	 */
	public static int getRelayBufferSize()
	{
		return RELAY_BUFFER_SIZE;
	}

	/**
	 * Reads a line from standard in; throws a RuntimeException
	 * if something absolutely serious happens. Should be used
	 * just for convenience.
	 */
	public static String getLine()
	{
		String out = null;
		try {
			if (SYSTEM_IN_READER == null)
				SYSTEM_IN_READER = openSystemIn();
			out = SYSTEM_IN_READER.readLine();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return out;
	}
	
	/**
	 * Prints a message out to standard out, word-wrapped
	 * to a set column width (in characters). The width cannot be
	 * 1 or less or this does nothing. This will also turn any whitespace
	 * character it encounters into a single space, regardless of speciality.
	 * @param message the output message.
	 * @param width the width in characters.
	 * @return the ending column for subsequent calls.
	 */
	public static int printWrapped(CharSequence message, int width)
	{
		return printWrapped(System.out, message, width);
	}

	/**
	 * Prints a message out to a PrintStream, word-wrapped
	 * to a set column width (in characters). The width cannot be
	 * 1 or less or this does nothing. This will also turn any whitespace
	 * character it encounters into a single space, regardless of speciality.
	 * @param out the print stream to use. 
	 * @param message the output message.
	 * @param width the width in characters.
	 * @return the ending column for subsequent calls.
	 */
	public static int printWrapped(PrintStream out, CharSequence message, int width)
	{
		return printWrapped(out, message, 0, width);
	}

	/**
	 * Prints a message out to a PrintStream, word-wrapped
	 * to a set column width (in characters). The width cannot be
	 * 1 or less or this does nothing. This will also turn any whitespace
	 * character it encounters into a single space, regardless of speciality.
	 * @param out the print stream to use. 
	 * @param message the output message.
	 * @param startColumn the starting column.
	 * @param width the width in characters.
	 * @return the ending column for subsequent calls.
	 */
	public static int printWrapped(PrintStream out, CharSequence message, int startColumn, int width)
	{
		if (width <= 1) return startColumn;
		
		StringBuffer token = new StringBuffer();
		StringBuffer line = new StringBuffer();
		int ln = startColumn;
		int tok = 0;
		for (int i = 0; i < message.length(); i++)
		{
			char c = message.charAt(i);
			if (c == '\n')
			{
				line.append(token);
				ln += token.length();
				token.delete(0, token.length());
				tok = 0;
				out.println(line.toString());
				line.delete(0, line.length());
				ln = 0;
			}
			else if (Character.isWhitespace(c))
			{
				line.append(token);
				ln += token.length();
				if (ln < width-1)
				{
					line.append(' ');
					ln++;
				}
				token.delete(0, token.length());
				tok = 0;
			}
			else if (c == '-')
			{
				line.append(token);
				ln += token.length();
				line.append('-');
				ln++;
				token.delete(0, token.length());
				tok = 0;
			}
			else if (ln + token.length() + 1 > width-1)
			{
				out.println(line.toString());
				line.delete(0, line.length());
				ln = 0;
				token.append(c);
				tok++;
			}
			else
			{
				token.append(c);
				tok++;
			}
		}
		
		String linestr = line.toString();
		if (line.length() > 0)
			out.print(linestr);
		if (token.length() > 0)
			out.print(token.toString());
		
		return ln + tok;
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(ByteBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(CharBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(ShortBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(IntBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(FloatBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(LongBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(DoubleBuffer buffer, PrintStream out)
	{
		int len = buffer.capacity();
		out.print("[");
		for (int i = 0; i < len; i++)
		{
			out.print(buffer.get(i));
			if (i + 1 < len)
				out.print(", ");
		}
		out.println("]");
	}
	
	/**
	 * Converts an ARGB 32-bit color value to a color.
	 */
	public static Color argbToColor(int argb)
	{
		return new Color(
			(argb & (0x0ff << 16)) >>> 16,
			(argb & (0x0ff << 8)) >>> 8,
			(argb & (0x0ff << 0)),
			(argb & (0x0ff << 24)) >>> 24
			);
	}

	/**
	 * Converts a color to an ARGB 32-bit color value.
	 */
	public static int colorToARGB(Color c)
	{
		return
			((c.getAlpha() & 0x0ff) << 24) |
			((c.getRed() & 0x0ff) << 16) |
			((c.getGreen() & 0x0ff) << 8) |
			((c.getBlue() & 0x0ff) << 0);
	}
	
	/**
	 * Checks if a value is "empty."
	 * The following is considered "empty":
	 * <ul>
	 * <li><i>Null</i> references.
	 * <li>{@link Array} objects that have a length of 0.
	 * <li>{@link Boolean} objects that are false.
	 * <li>{@link Character} objects that are the null character ('\0', '\u0000').
	 * <li>{@link Number} objects that are zero.
	 * <li>{@link String} objects that are the empty string, or are {@link String#trim()}'ed down to the empty string.
	 * <li>{@link Collection} objects where {@link Collection#isEmpty()} returns true.
	 * <li>{@link Sizable} objects where {@link Sizable#isEmpty()} returns true.
	 * </ul> 
	 * @param obj the object to check.
	 * @return true if the provided object is considered "empty", false otherwise.
	 * @since 2.10.4
	 * @since 2.13.1 - handles Array lengths.
	 */
	public static boolean isEmpty(Object obj)
	{
		if (obj == null)
			return true;
		else if (Reflect.isArray(obj))
			return Array.getLength(obj) == 0;
		else if (obj instanceof Boolean)
			return !((Boolean)obj);
		else if (obj instanceof Character)
			return ((Character)obj) == '\0';
		else if (obj instanceof Number)
			return ((Number)obj).doubleValue() == 0.0;
		else if (obj instanceof String)
			return ((String)obj).trim().length() == 0;
		else if (obj instanceof Collection<?>)
			return ((Collection<?>)obj).isEmpty();
		else if (obj instanceof Sizable)
			return ((Sizable)obj).isEmpty();
		
		return false;
	}
	
	/**
	 * Attempts to parse a boolean from a string.
	 * If the string is null, this returns false.
	 * If the string does not equal "true" (case ignored), this returns false.
	 * @param s the input string.
	 */
	public static boolean parseBoolean(String s)
	{
		if (s == null || !s.equalsIgnoreCase("true"))
			return false;
		else
			return true;
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 */
	public static byte parseByte(String s)
	{
		if (s == null)
			return 0;
		try {
			return Byte.parseByte(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Attempts to parse a short from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 */
	public static short parseShort(String s)
	{
		if (s == null)
			return 0;
		try {
			return Short.parseShort(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Attempts to parse a char from a string.
	 * If the string is null or the empty string, this returns '\0'.
	 * @param s the input string.
	 * @return the first character in the string.
	 */
	public static char parseChar(String s)
	{
		if (isEmpty(s))
			return '\0';
		else
			return s.charAt(0);
	}
	
	/**
	 * Attempts to parse an int from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 */
	public static int parseInt(String s)
	{
		if (s == null)
			return 0;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Attempts to parse a long from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 */
	public static long parseLong(String s)
	{
		if (s == null)
			return 0L;
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
	
	/**
	 * Attempts to parse a float from a string.
	 * If the string is null or the empty string, this returns 0.0f.
	 * @param s the input string.
	 */
	public static float parseFloat(String s)
	{
		if (s == null)
			return 0f;
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return 0f;
		}
	}
	
	/**
	 * Attempts to parse a double from a string.
	 * If the string is null or the empty string, this returns 0.0.
	 * @param s the input string.
	 */
	public static double parseDouble(String s)
	{
		if (s == null)
			return 0.0;
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	/**
	 * Attempts to parse a boolean from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * If the string does not equal "true," this returns false.
	 * @param s the input string.
	 */
	public static boolean parseBoolean(String s, boolean def)
	{
		if (isEmpty(s))
			return def;
		else if (!s.equalsIgnoreCase("true"))
			return false;
		else
			return true;
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 */
	public static byte parseByte(String s, byte def)
	{
		if (isEmpty(s))
			return def;
		try {
			return Byte.parseByte(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a short from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 */
	public static short parseShort(String s, short def)
	{
		if (isEmpty(s))
			return def;
		try {
			return Short.parseShort(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @return the first character in the string.
	 */
	public static char parseChar(String s, char def)
	{
		if (isEmpty(s))
			return def;
		else
			return s.charAt(0);
	}

	/**
	 * Attempts to parse an int from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 */
	public static int parseInt(String s, int def)
	{
		if (isEmpty(s))
			return def;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Attempts to parse a long from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 */
	public static long parseLong(String s, long def)
	{
		if (isEmpty(s))
			return def;
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	/**
	 * Attempts to parse a float from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 */
	public static float parseFloat(String s, float def)
	{
		if (isEmpty(s))
			return def;
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	/**
	 * Attempts to parse a double from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 */
	public static double parseDouble(String s, double def)
	{
		if (isEmpty(s))
			return def;
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	/**
	 * Attempts to parse an array of booleans from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"true, false, apple, false"</code> -> <code>[true, false, false, false]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseBoolean(String)
	 */
	public static boolean[] parseBooleanArray(String s, boolean[] def)
	{
		return parseBooleanArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of bytes from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> -> <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseByte(String)
	 */
	public static byte[] parseByteArray(String s, byte[] def)
	{
		return parseByteArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of shorts from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> -> <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseShort(String)
	 */
	public static short[] parseShortArray(String s, short[] def)
	{
		return parseShortArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of chars from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"apple, pear, b, g"</code> -> <code>['a', 'p', 'b', 'g']</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseChar(String)
	 */
	public static char[] parseCharArray(String s, char[] def)
	{
		return parseCharArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of integers from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2.1, grape"</code> -> <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseInt(String)
	 */
	public static int[] parseIntArray(String s, int[] def)
	{
		return parseIntArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of floats from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0.5, -5.4, 2, grape"</code> -> <code>[0.5f, -5.4f, 2.0f, 0f]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseFloat(String)
	 */
	public static float[] parseFloatArray(String s, float[] def)
	{
		return parseFloatArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of longs from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> -> <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseLong(String)
	 */
	public static long[] parseLongArray(String s, long[] def)
	{
		return parseLongArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of doubles from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0.5, -5.4, 2, grape"</code> -> <code>[0.5, -5.4, 2.0, 0.0]</code>
	 * @param s the input string.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseDouble(String)
	 */
	public static double[] parseDoubleArray(String s, double[] def)
	{
		return parseDoubleArray(s, PARSE_ARRAY_SEPARATOR_PATTERN, def);
	}
	
	/**
	 * Attempts to parse an array of booleans from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseBoolean(String)
	 */
	public static boolean[] parseBooleanArray(String s, String separatorRegex, boolean[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		boolean[] out = new boolean[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseBoolean(token);
		return out;
	}

	/**
	 * Attempts to parse an array of bytes from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseByte(String)
	 */
	public static byte[] parseByteArray(String s, String separatorRegex, byte[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		byte[] out = new byte[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseByte(token);
		return out;
	}

	/**
	 * Attempts to parse an array of shorts from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseShort(String)
	 */
	public static short[] parseShortArray(String s, String separatorRegex, short[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		short[] out = new short[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseShort(token);
		return out;
	}

	/**
	 * Attempts to parse an array of chars from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseChar(String)
	 */
	public static char[] parseCharArray(String s, String separatorRegex, char[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		char[] out = new char[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseChar(token);
		return out;
	}

	/**
	 * Attempts to parse an array of integers from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseInt(String)
	 */
	public static int[] parseIntArray(String s, String separatorRegex, int[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		int[] out = new int[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseInt(token);
		return out;
	}

	/**
	 * Attempts to parse an array of floats from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseFloat(String)
	 */
	public static float[] parseFloatArray(String s, String separatorRegex, float[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		float[] out = new float[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseFloat(token);
		return out;
	}

	/**
	 * Attempts to parse an array of longs from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseLong(String)
	 */
	public static long[] parseLongArray(String s, String separatorRegex, long[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		long[] out = new long[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseLong(token);
		return out;
	}

	/**
	 * Attempts to parse an array of doubles from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseDouble(String)
	 */
	public static double[] parseDoubleArray(String s, String separatorRegex, double[] def)
	{
		if (isEmpty(s))
			return def;
		String[] tokens = s.split(separatorRegex);
		double[] out = new double[tokens.length];
		int i = 0;
		for (String token : tokens)
			out[i++] = parseDouble(token);
		return out;
	}

	/**
	 * Concatenates a set of arrays together, such that the contents of each
	 * array are joined into one array. Null arrays are skipped.
	 * @param arrays the list of arrays.
	 * @return a new array with all objects in each provided array added 
	 * to the resultant one in the order in which they appear.
	 * @since 2.17.0
	 */
	public static <T> T[] joinArrays(T[]...  arrays)
	{
		int totalLen = 0;
		for (T[] a : arrays)
			if (a != null)
				totalLen += a.length;
		
		Class<?> type = Reflect.getArrayType(arrays);
		@SuppressWarnings("unchecked")
		T[] out = (T[])Array.newInstance(type, totalLen);
		
		int offs = 0;
		for (T[] a : arrays)
		{
			System.arraycopy(a, 0, out, offs, a.length);
			offs += a.length;
		}
		
		return out;
	}
	
	/**
	 * Joins an array of strings into one string, with a separator between them.
	 * @param separator the separator to insert between strings. Can be empty or null.
	 * @param strings the strings to join.
	 * @return a string of all joined strings and separators.
	 * @since 2.20.0
	 */
	public static String joinStrings(String separator, String... strings)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.length; i++)
		{
			sb.append(strings[i]);
			if (i < strings.length - 1)
				sb.append(separator);
		}
		return sb.toString();
	}
	
	/**
	 * Joins an array of strings into one string, with a separator between them.
	 * @param startIndex the starting index in the string array.
	 * @param separator the separator to insert between strings. Can be empty or null.
	 * @param strings the strings to join.
	 * @return a string of all joined strings and separators.
	 * @since 2.20.0
	 */
	public static String joinStrings(int startIndex, String separator, String... strings)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < strings.length; i++)
		{
			sb.append(strings[i]);
			if (!Common.isEmpty(separator) && i < strings.length - 1)
				sb.append(separator);
		}
		return sb.toString();
	}
	
	/**
	 * Attempts to close a {@link Closeable} object.
	 * If the object is null, this does nothing.
	 * @since 2.3.0
	 */
	public static void close(Closeable c)
	{
		if (c == null) return;
		try { c.close(); 	} catch (IOException e){}
	}
	
	/**
	 * Attempts to close a {@link Connection} object.
	 * If the object is null, this does nothing.
	 * @since 2.15.0
	 */
	public static void close(Connection c)
	{
		if (c == null) return;
		try { c.close(); 	} catch (SQLException e){}
	}
	
	/**
	 * Attempts to close a {@link Statement} object.
	 * If the object is null, this does nothing.
	 * @since 2.15.0
	 */
	public static void close(Statement s)
	{
		if (s == null) return;
		try { s.close(); 	} catch (SQLException e){}
	}
	
	/**
	 * Attempts to close a {@link ResultSet} object.
	 * If the object is null, this does nothing.
	 * @since 2.15.0
	 */
	public static void close(ResultSet rs)
	{
		if (rs == null) return;
		try { rs.close(); 	} catch (SQLException e){}
	}
	
	/**
	 * Attempts to close a {@link ZipFile}.
	 * If the object is null, this does nothing.
	 * @since 2.13.1
	 */
	public static void close(ZipFile zf)
	{
		if (zf == null) return;
		try { zf.close(); 	} catch (IOException e){}
	}

	/**
	 * Gets an object local to the current thread via a String key that
	 * was once set by {@link #setLocal(String, Object)}.
	 * @param key the String key to use.
	 * @since 2.10.0
	 */
	public static Object getLocal(String key)
	{
		return THREADLOCAL_HASHMAP.get().get(key);
	}

	/**
	 * Sets an object local to the current thread via a String key that
	 * can be retrieved by {@link #getLocal(String)}. Objects set this way
	 * are accessible to the thread in which they were set.
	 * @param key the String key to use.
	 * @param object the object to set.
	 * @since 2.10.0
	 */
	public static void setLocal(String key, Object object)
	{
		THREADLOCAL_HASHMAP.get().put(key, object);
	}

	/**
	 * Does absolutely nothing.
	 * Useful for setting debug breakpoints on a line that is
	 * designed to do nothing.
	 * @since 2.4.0
	 */
	public static void noop()
	{
		// Do nothing.
	}
	
	
	
}
