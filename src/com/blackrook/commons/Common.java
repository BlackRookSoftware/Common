/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.*;
import java.util.Collection;
import java.util.Comparator;

import com.blackrook.commons.util.ArrayUtils;
import com.blackrook.commons.util.BitUtils;
import com.blackrook.commons.util.BufferUtils;
import com.blackrook.commons.util.EncodingUtils;
import com.blackrook.commons.util.FileUtils;
import com.blackrook.commons.util.HTTPUtils;
import com.blackrook.commons.util.IOUtils;
import com.blackrook.commons.util.OSUtils;
import com.blackrook.commons.util.ObjectUtils;
import com.blackrook.commons.util.StringUtils;
import com.blackrook.commons.util.ThreadUtils;
import com.blackrook.commons.util.ValueUtils;
import com.blackrook.commons.util.HTTPUtils.HTTPResponse;

/**
 * This class serves as a static delegate for a slew of common methods
 * and functions.
 * @author Matthew Tropiano
 * @deprecated as of 2.32.0.
 */
public final class Common
{
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

	private static boolean IS_WINDOWS = false;
	/** Is this Mac OS X? */
	private static boolean IS_OSX = false;
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

	static
	{
		String osName = System.getProperty("os.name");
		IS_WINDOWS = osName.contains("Windows");
		IS_OSX = osName.contains("OS X") || osName.contains("macOS");
		IS_LINUX = osName.contains("Linux");
		IS_SOLARIS = osName.contains("Solaris");
		
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
	
	/** @return true if we using a Linux distro. */
	public static boolean isLinux()
	{
		return OSUtils.isLinux();
	}

	/** @return true if we using Mac OS X. */
	public static boolean isOSX()
	{
		return OSUtils.isOSX();
	}

	/** @return true if we using 64-bit Mac OS X. */
	public static boolean isOSX64()
	{
		return OSUtils.isOSX64();
	}

	/** @return true if we using x86 Mac OS X. */
	public static boolean isOSX86()
	{
		return OSUtils.isOSX86();
	}

	/** @return true if we using Power PC Mac OS X. */
	public static boolean isOSXPPC()
	{
		return OSUtils.isOSXPPC();
	}

	/** @return true if this is running on an Power PC architecture. */
	public static boolean isPPC()
	{
		return OSUtils.isPPC();
	}

	/** @return true if we using 32-bit Windows. */
	public static boolean isWin32()
	{
		return OSUtils.isWin32();
	}

	/** @return true if we using 64-bit Windows. */
	public static boolean isWin64() 
	{
		return OSUtils.isWin64();
	}

	/** @return true if we using Windows. */
	public static boolean isWindows()
	{
		return OSUtils.isWindows();
	}

	/** @return true if we using Windows 2000. */
	public static boolean isWindows2000()
	{
		return OSUtils.isWindows2000();
	}

	/** @return true if we using Windows 2003. */
	public static boolean isWindows2003()
	{
		return OSUtils.isWindows2003();
	}

	/** @return true if we using Windows 2008. */
	public static boolean isWindows2008()
	{
		return OSUtils.isWindows2008();
	}

	/** @return true if we using Windows Vista. */
	public static boolean isWindowsVista()
	{
		return OSUtils.isWindowsVista();
	}

	/** @return true if we using Windows 7. */
	public static boolean isWindows7()
	{
		return OSUtils.isWindows7();
	}

	/** 
	 * @return true if we using Windows 8.
	 * @since 2.13.0 
	 */
	public static boolean isWindows8()
	{
		return OSUtils.isWindows8();
	}

	/** 
	 * @return true if we using Windows 10. 
	 * @since 2.21.0 
	 */
	public static boolean isWindows10()
	{
		return OSUtils.isWindows10();
	}

	/** @return true if we using Windows 95/98. */
	public static boolean isWindows9X()
	{
		return OSUtils.isWindows9X();
	}

	/** @return true if we are using Windows ME, or better yet, if we should just kill the program now. */
	public static boolean isWindowsME()
	{
		return OSUtils.isWindowsME();
	}

	/** @return true if we using Windows NT. */
	public static boolean isWindowsNT()
	{
		return OSUtils.isWindowsNT();
	}

	/** @return true if we using Windows XP. */
	public static boolean isWindowsXP()
	{
		return OSUtils.isWindowsXP();
	}

	/** @return true if this is running on an x64 architecture. */
	public static boolean isX64()
	{
		return OSUtils.isX64();
	}

	/** @return true if this is running on an x86 architecture. */
	public static boolean isX86()
	{
		return OSUtils.isX86();
	}

	/** @return true if this is running on Sun Solaris. */
	public static boolean isSolaris()
	{
		return OSUtils.isSolaris();
	}

	/**
	 * Returns the first object if it is not null, otherwise returns the second. 
	 * @param <T> class that extends Object.
	 * @param testObject the first ("tested") object.
	 * @param nullReturn the object to return if testObject is null.
	 * @return testObject if not null, nullReturn otherwise.
	 * @since 2.21.0
	 */
	public static <T> T isNull(T testObject, T nullReturn)
	{
		return ObjectUtils.isNull(testObject, nullReturn);
	}
	
	/**
	 * Returns the first object in the supplied list of objects that isn't null. 
	 * @param <T> class that extends Object.
	 * @param objects the list of objects.
	 * @return the first object that isn't null in the list, 
	 * or null if all of the objects are null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T coalesce(T ... objects)
	{
		return ObjectUtils.coalesce(objects);
	}
	
	/**
	 * Returns a valid index of an element in an array if an object is contained in an array. 
	 * Sequentially searches for first match via {@link #equals(Object)}.
	 * Can search for null. 
	 * @param <T> class that extends Object.
	 * @param object the object to search for. Can be null.
	 * @param searchArray the list of objects to search.
	 * @return the index of the object, or -1 if it cannot be found.
	 * @since 2.13.2
	 */
	public static <T> int indexOf(T object, T[] searchArray)
	{
		return ArrayUtils.indexOf(object, searchArray);
	}

	/**
	 * Calls Thread.sleep() but in an encapsulated try
	 * to avoid catching InterruptedException. Convenience
	 * method for making the current thread sleep when you don't
	 * care if it's interrupted or not and want to keep code neat.
	 * @param millis the amount of milliseconds to sleep.
	 * @see #sleep(long)
	 */
	public static void sleep(long millis)
	{
		ThreadUtils.sleep(millis);
	}
	
	/**
	 * Calls Thread.sleep() but in an encapsulated try
	 * to avoid catching InterruptedException. Convenience
	 * method for making the current thread sleep when you don't
	 * care if it's interrupted or not and want to keep code neat.
	 * @param millis the amount of milliseconds to sleep.
	 * @param nanos the amount of additional nanoseconds to sleep.
	 * @see #sleep(long, int)
	 */
	public static void sleep(long millis, int nanos)
	{
		ThreadUtils.sleep(millis, nanos);
	}
	
	/**
	 * Gets the package path for a particular class (for classpath resources).
	 * @param cls the class for which to get to get the path.
	 * @return the equivalent path for finding a class.
	 */
	public static String getPackagePathForClass(Class<?> cls)
	{
		return Reflect.getPackagePathForClass(cls);		
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
		Reflect.addFilesToClassPath(files);
	}
	
	/**
	 * Adds a list of URLs to the JVM Classpath during runtime.
	 * The URLs are added to the current thread's class loader.
	 * @param urls	the list of files to add.
	 */
	public static void addURLsToClassPath(URL ... urls)
	{
		Reflect.addURLsToClassPath(urls);
	}
	
	/**
	 * Creates a list of URLs from a list of files or directories.
	 * Directories in the list of files are exploded down to actual
	 * files, so that no directories remain. Because of this,
	 * the output list of URLs may be longer than the input file list!
	 * @param files	the list of files to convert.
	 * @return an array of URLs that point to individual files.
	 * @since 2.16.0
	 */
	public static URL[] getURLsForFiles(File ... files)
	{
		return FileUtils.getURLsForFiles(files);
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
		Reflect.addLibrariesToPath(libs);
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
		Reflect.addLibraryToPath(lib);
	}
	
	/**
	 * Gets a full String representation of a Throwable type,
	 * including a line-by-line breakdown of the stack trace.
	 * @param t the throwable to render into a string.
	 * @return a multi-line string of the exception, similar to the stack dump.
	 */
	public static String getExceptionString(Throwable t)
	{
		return StringUtils.getExceptionString(t);
	}
	
	/**
	 * Gets a full String representation of an exception as the JVM dumps it.
	 * @param t the throwable to render into a string.
	 * @return a multi-line string of the exception, similar to the stack dump.
	 * @see #getExceptionString(Throwable)
	 * @since 2.21.0
	 */
	public static String getJREExceptionString(Throwable t)
	{
		return StringUtils.getJREExceptionString(t);
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
		return FileUtils.explodeFiles(files);
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
		return IOUtils.openTextStream(in);
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
		return IOUtils.openTextFile(file);
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
		return IOUtils.openTextFile(filePath);
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
		return IOUtils.openSystemIn();
	}
	
	/**
	 * Opens an {@link InputStream} to a resource using the current thread's {@link ClassLoader}.
	 * @param pathString the resource pathname.
	 * @return an open {@link InputStream} for reading the resource or null if not found.
	 * @see ClassLoader#getResourceAsStream(String)
	 * @since 2.20.0
	 */
	public static InputStream openResource(String pathString)
	{
		return IOUtils.openResource(pathString);
	}
	
	/**
	 * Opens an {@link InputStream} to a resource using a provided ClassLoader.
	 * @param classLoader the provided {@link ClassLoader} to use.
	 * @param pathString the resource pathname.
	 * @return an open {@link InputStream} for reading the resource or null if not found.
	 * @see ClassLoader#getResourceAsStream(String)
	 * @since 2.20.0
	 */
	public static InputStream openResource(ClassLoader classLoader, String pathString)
	{
		return IOUtils.openResource(classLoader, pathString);
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
		return IOUtils.getASCIIContents(f);
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
		return IOUtils.getTextualContents(f);
	}
	
	/**
	 * Retrieves the textual contents of a stream in the system's current encoding.
	 * @param in	the input stream to use.
	 * @return		a contiguous string (including newline characters) of the stream's contents.
	 * @throws IOException	if the read cannot be done.
	 */
	public static String getTextualContents(InputStream in) throws IOException
	{
		return IOUtils.getTextualContents(in);
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
		return IOUtils.getTextualContents(in, encoding);
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
		return IOUtils.getBinaryContents(f);
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
		return IOUtils.getBinaryContents(in, len);
	}

	/**
	 * Retrieves the binary contents of a stream until it hits the end of the stream.
	 * @param in	the input stream to use.
	 * @return		an array of len bytes that make up the data in the stream.
	 * @throws IOException	if the read cannot be done.
	 */
	public static byte[] getBinaryContents(InputStream in) throws IOException
	{
		return IOUtils.getBinaryContents(in);
	}

    /**
     * Gets the content from a opening an HTTP URL, using the default timeout of 30 seconds (30000 milliseconds).
     * @param url the URL to open and read.
     * @return the content from opening an HTTP request.
     * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx).
     * @throws SocketTimeoutException if the socket read times out.
	 * @since 2.5.0
     */
    public static String getHTTPContent(URL url) throws IOException
    {
        return HTTPUtils.getHTTPContent(url);
    }

    /**
     * Gets the content from a opening an HTTP URL.
     * @param url the URL to open and read.
     * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
     * @return the content from opening an HTTP request.
     * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx/3xx).
     * @throws SocketTimeoutException if the socket read times out.
     * @throws UnsupportedEncodingException if the content cannot be decoded into a string using the provided encoding.
	 * @since 2.6.0
     */
    public static String getHTTPContent(URL url, int socketTimeoutMillis) throws IOException
    {
        return HTTPUtils.getHTTPContent(url, socketTimeoutMillis);
    }

    /**
     * Gets the content from a opening an HTTP URL.
     * @param url the URL to open and read.
     * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
     * @param defaultResponseCharset if the response charset is not specified, use this one.
     * @return the content from opening an HTTP request.
     * @throws IOException if an error happens during the read, or a bad response is returned (not 2xx/3xx).
     * @throws SocketTimeoutException if the socket read times out.
     * @throws UnsupportedEncodingException if the content cannot be decoded into a string using the provided encoding.
	 * @since 2.6.0
     */
    public static String getHTTPContent(URL url, String defaultResponseCharset, int socketTimeoutMillis) throws IOException
    {
        return HTTPUtils.getHTTPContent(url, defaultResponseCharset, socketTimeoutMillis);
    }

    /**
     * Gets the content from a opening an HTTP URL, automatically handling redirects (301, 307, 308).
     * @param url the URL to open and read.
     * @param requestMethod the request method.
     * @param data if not null, send this object as JSON in the content.
     * @param dataContentType if data is not null, this is the content type. If this is null, uses "application/octet-stream".
     * @param dataContentEncoding if data is not null, this is the encoding for the written data. Can be null.
     * @param defaultResponseEncoding if the response encoding is not specified, use this one.
     * @param defaultResponseCharset if the response charset is not specified, use this one.
     * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
     * @return the content from opening an HTTP request.
     * @throws IOException if an error happens during the read.
     * @throws SocketTimeoutException if the socket read times out.
     * @throws UnsupportedEncodingException if the content cannot be decoded into a string using the provided encoding.
     * @throws StackOverflowError if a redirect loop occurs.
     * @since 2.31.1
     */
    public static HTTPResponse getResolvedHTTPContent(URL url, String requestMethod, byte[] data, String dataContentType, String dataContentEncoding, String defaultResponseEncoding, String defaultResponseCharset, int socketTimeoutMillis) throws IOException
    {
        return HTTPUtils.getResolvedHTTPContent(url, requestMethod, data, dataContentType, dataContentEncoding, defaultResponseEncoding, defaultResponseCharset, socketTimeoutMillis);
    }

    /**
     * Gets the content from a opening an HTTP URL.
     * @param url the URL to open and read.
     * @param requestMethod the request method.
     * @param data if not null, send this data in the content.
     * @param dataContentType if data is not null, this is the content type. If this is null, uses "application/octet-stream".
     * @param dataContentEncoding if data is not null, this is the encoding for the written data. Can be null.
     * @param defaultResponseEncoding if the response encoding is not specified, use this one.
     * @param defaultResponseCharset if the response charset is not specified, use this one.
     * @param socketTimeoutMillis the socket timeout time in milliseconds. 0 is forever.
     * @return the content from opening an HTTP request.
     * @throws IOException if an error happens during the read/write.
     * @throws SocketTimeoutException if the socket read times out.
     * @throws ProtocolException if the requestMethod is incorrect.
     */
    public static HTTPResponse getHTTPContent(URL url, String requestMethod, byte[] data, String dataContentType, String dataContentEncoding, String defaultResponseEncoding, String defaultResponseCharset, int socketTimeoutMillis) throws IOException
    {
        return HTTPUtils.getHTTPContent(url, requestMethod, data, dataContentType, dataContentEncoding, defaultResponseEncoding, defaultResponseCharset, socketTimeoutMillis);
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
		return HTTPUtils.getHTTPByteContent(url);
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
		return HTTPUtils.getByteContent(url, socketTimeoutMillis);
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
		return HTTPUtils.getByteContent(url);
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
		return HTTPUtils.getByteContent(url, socketTimeoutMillis);
	}

	/**
	 * Returns a hash of a set of bytes digested by an encryption algorithm.
	 * Can return null if this Java implementation cannot perform this.
	 * Do not use this if you care if the algorithm is provided or not.
	 * @param bytes the bytes to encode.
	 * @param algorithmName the name to the algorithm to use.
	 * @return the resultant byte digest, or null if the algorithm is not supported.
	 * @since 2.10.0
	 */
	public static byte[] digest(byte[] bytes, String algorithmName)
	{
		return EncodingUtils.digest(bytes, algorithmName);
	}
	
	/**
	 * Returns a 20-byte SHA-1 hash of a set of bytes.
	 * Can return null if this Java implementation cannot perform this,
	 * but it shouldn't, since SHA-1 is mandatorily implemented for all implementations.
	 * @param bytes the input bytes.
	 * @return the resultant 20-byte digest.
	 * @since 2.9.0
	 * @see #digest(byte[], String)
	 */
	public static byte[] sha1(byte[] bytes)
	{
		return EncodingUtils.sha1(bytes);
	}
	
	/**
	 * Returns a 16-byte MD5 hash of a set of bytes.
	 * Can return null if this Java implementation cannot perform this,
	 * but it shouldn't, since MD5 is mandatorily implemented for all implementations.
	 * @param bytes the input bytes.
	 * @return the resultant 16-byte digest.
	 * @since 2.9.0
	 * @see #digest(byte[], String)
	 */
	public static byte[] md5(byte[] bytes)
	{
		return EncodingUtils.md5(bytes);
	}
	
	/**
	 * Encodes a series of bytes as a Base64 encoded string.
	 * Uses + and / as characters 62 and 63.
	 * @param in the input stream to read to convert to Base64.
	 * @return a String of encoded bytes, or null if the message could not be encoded.
	 * @since 2.9.0
	 * @throws IOException if the input stream cannot be read.
	 */
	public static String asBase64(InputStream in) throws IOException
	{
		return EncodingUtils.asBase64(in);
	}

	/**
	 * Encodes a series of bytes as a Base64 encoded string.
	 * @param in the input stream to read to convert to Base64.
	 * @param sixtyTwo the character to use for character 62 in the Base64 index.
	 * @param sixtyThree the character to use for character 63 in the Base64 index.
	 * @return a String of encoded bytes, or null if the message could not be encoded.
	 * @since 2.9.0
	 * @throws IOException if the input stream cannot be read.
	 */
	public static String asBase64(InputStream in, char sixtyTwo, char sixtyThree) throws IOException
	{
		return EncodingUtils.asBase64(in, sixtyTwo, sixtyThree);
	}
	
	/**
	 * Creates a blank file or updates its last modified date.
	 * @param filePath	the abstract path to use.
	 * @return true if the file was made/updated, false otherwise.
	 * @throws IOException if creating/modifying the file violates something.
	 */
	public static boolean touch(String filePath) throws IOException
	{
		return FileUtils.touch(filePath);
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
		FileUtils.secureDelete(file);
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
		FileUtils.secureDelete(file, passes);
	}
	
	/**
	 * Creates a file filled with random data with the specified length.
	 * If <code>file</code> refers to an existing file, it will be OVERWRITTEN.
	 * @param file the file to write.
	 * @param length the length of the file in bytes.
	 * @throws IOException if an I/O error occurs.
	 * @since 2.9.0
	 */
	public static void createJunkFile(File file, int length) throws IOException
	{
		FileUtils.createJunkFile(file, length);
	}
	
	/**
	 * Makes a new String with escape sequences in it.
	 * @param s	the original string.
	 * @return the new one with escape sequences in it.
	 */
	public static String withEscChars(String s)
    {
		return StringUtils.withEscChars(s);
    }

	/**
	 * Escapes a string so that it can be input safely into a URL string.
	 * @param inString the input string.
	 * @return the escaped string.
	 */
	public static String urlEscape(String inString)
	{
		return StringUtils.urlEscape(inString);
	}
	
	/**
	 * Decodes a URL-encoded string.
	 * @param inString the input string.
	 * @return the unescaped string.
	 * @since 2.19.0
	 */
	public static String urlUnescape(String inString)
	{
		return StringUtils.urlUnescape(inString);
	}

	/**
	 * Adds a character sequence from the start of a string if it does not start with the sequence.
	 * @param input the input string to change.
	 * @param sequence the sequence to potentially add.
	 * @return a modified string or the input string if no modification.
	 * @since 2.30.1
	 */
	public static String addStartingSequence(String input, String sequence)
	{
		return StringUtils.addStartingSequence(input, sequence);
	}
	
	/**
	 * Adds a character sequence from the end of a string if it does not end with the sequence.
	 * @param input the input string to change.
	 * @param sequence the sequence to potentially add.
	 * @return a modified string or the input string if no modification.
	 * @since 2.30.1
	 */
	public static String addEndingSequence(String input, String sequence)
	{
		return StringUtils.addEndingSequence(input, sequence);
	}
	
	/**
	 * Removes a character sequence from the start of a string if it starts with the sequence.
	 * @param input the input string to change.
	 * @param sequence the sequence to potentially remove.
	 * @return a modified string or the input string if no modification.
	 * @since 2.30.1
	 */
	public static String removeStartingSequence(String input, String sequence)
	{
		return StringUtils.removeStartingSequence(input, sequence);
	}
	
	/**
	 * Removes a character sequence from the end of a string if it exists.
	 * @param input the input string to change.
	 * @param sequence the sequence to potentially remove.
	 * @return a modified string or the input string if no modification.
	 * @since 2.30.1
	 */
	public static String removeEndingSequence(String input, String sequence)
	{
		return StringUtils.removeEndingSequence(input, sequence);
	}
	
	/**
	 * Creates the necessary directories for a file path.
	 * @param file	the abstract file path.
	 * @return		true if the paths were made (or exists), false otherwise.
	 */
	public static boolean createPathForFile(File file)
	{
		return FileUtils.createPathForFile(file);
	}

	/**
	 * Creates the necessary directories for a file path.
	 * @param path	the abstract path.
	 * @return		true if the paths were made (or exists), false otherwise.
	 */
	public static boolean createPathForFile(String path)
	{
		return FileUtils.createPathForFile(path);
	}
	
	/**
	 * Creates the necessary directories for a file path.
	 * @param path	the abstract path.
	 * @return		true if the paths were made (or exists), false otherwise.
	 */
	public static boolean createPath(String path)
	{
		return FileUtils.createPath(path);
	}
	
	/**
	 * Gets the relative path to a file path.
	 * @param sourcePath source file path.
	 * @param targetPath target file path to create the path to.
	 * @return a path string to the target path relative to the source path.
	 * @throws IOException if the canonical file paths cannot be resolved for either file.
	 */
	public static String getRelativePath(String sourcePath, String targetPath) throws IOException
	{
		return FileUtils.getRelativePath(sourcePath, targetPath);
	}
	
	/**
	 * Gets the relative path to a file path.
	 * @param source source file.
	 * @param target target file to create the path to.
	 * @return a path string to the target file relative to the source file.
	 * @throws IOException if the canonical file paths cannot be resolved for either file.
	 */
	public static String getRelativePath(File source, File target) throws IOException
	{
		return FileUtils.getRelativePath(source, target);
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
		return FileUtils.getFileExtension(filename, extensionSeparator);
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
		return FileUtils.getFileExtension(file, extensionSeparator);
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
		return FileUtils.getFileExtension(filename);
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
		return FileUtils.getFileExtension(file);
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
		return FileUtils.matchWildcardPattern(pattern, target);
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
		return FileUtils.matchWildcardPattern(pattern, target, caseInsensitive);
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
		return FileUtils.matchWildcardPattern(pattern, target, caseInsensitive);
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
		return FileUtils.matchWildcardPattern(pattern, target, caseInsensitive, slashAgnostic);
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
		return FileUtils.getFilesByWildcardPath(path);
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
		return FileUtils.getFilesByWildcardPath(path, hidden);
	}
	
	/**
	 * Checks if bits are set in a value.
	 * @param value		the value.
	 * @param test		the testing bits.
	 * @return			true if all of the bits set in test are set in value, false otherwise.
	 */
	public static boolean bitIsSet(long value, long test)
	{
		return BitUtils.bitIsSet(value, test);
	}
	
	/**
	 * Sets the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to set.
	 * @return			the resulting number.
	 */
	public static long setBits(long value, long bits)
	{
		return BitUtils.setBits(value, bits);
	}

	/**
	 * Sets the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to set.
	 * @return			the resulting number.
	 */
	public static int setBits(int value, int bits)
	{
		return BitUtils.setBits(value, bits);
	}

	/**
	 * Clears the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to clear.
	 * @return			the resulting number.
	 */
	public static long clearBits(long value, long bits)
	{
		return BitUtils.clearBits(value, bits);
	}

	/**
	 * Clears the bits of a value.
	 * @param value		the value.
	 * @param bits		the bits to clear.
	 * @return			the resulting number.
	 */
	public static int clearBits(int value, int bits)
	{
		return BitUtils.clearBits(value, bits);
	}

	/**
	 * Converts a series of boolean values to bits,
	 * going from least-significant to most-significant.
	 * TRUE booleans set the bit, FALSE ones do not.
	 * @param bool list of booleans. cannot exceed 32.
	 * @return the resultant bitstring in an integer.
	 */
	public static int booleansToInt(boolean ... bool)
	{
		return BitUtils.booleansToInt(bool);
	}
	
	/**
	 * Converts a series of boolean values to bits,
	 * going from least-significant to most-significant.
	 * TRUE booleans set the bit, FALSE ones do not.
	 * @param bool list of booleans. cannot exceed 64.
	 * @return the resultant bitstring in a long integer.
	 */
	public static long booleansToLong(boolean ... bool)
	{
		return BitUtils.booleansToLong(bool);
	}
	
	/**
	 * Returns a new byte array, delta-encoded.<br>Use with deltaDecode() to decode.
	 * <p>After the first byte, the change in value from the last byte is stored.
	 * <br>For example, a byte sequence <code>[64,92,-23,33]</code> would be returned
	 * as: <code>[64,28,-115,56]</code>.
	 * @param b the input bytestring.
	 * @return the output bytestring.
	 */
	public static byte[] deltaEncode(byte[] b)
	{
		return EncodingUtils.deltaEncode(b);
	}
	
	/**
	 * Returns a new byte array, delta-decoded.<br>Decodes sequences made with deltaEncode().
	 * <p>After the first byte, the change in value from the last byte is used to get the original value.
	 * <br>For example, a byte sequence <code>[64,28,-115,56]</code> would be returned
	 * as: <code>[64,92,-23,33]</code>.
	 * @param b the input bytestring.
	 * @return the output bytestring.
	 */
	public static byte[] deltaDecode(byte[] b)
	{
		return EncodingUtils.deltaDecode(b);
	}

	/**
	 * Returns a new byte array, Carmacized.
	 * Named after John D. Carmack, this will compress a sequence
	 * of bytes known to be alike in contiguous sequences.
	 * @param b the input bytestring.
	 * @return the output bytestring.
	 */
	public static byte[] carmacize(byte[] b)
	{
		return EncodingUtils.carmacize(b); 
	}

	/**
	 * Returns a Camacized byte array, de-Carmacized.
	 * Named after John D. Carmack, this will decompress a series of
	 * bytes encoded in the Carmacizing algorithm.
	 * @param b the input bytestring.
	 * @return the output bytestring.
	 */
	public static byte[] decarmacize(byte[] b)
	{
		return EncodingUtils.decarmacize(b); 
	}

	/**
	 * Allocates a DIRECT ByteBuffer using byte array data.
	 * Useful for native wrappers that require direct ByteBuffers.
	 * @param b	the byte array to wrap. 
	 * @return	a direct buffer that can hold <i>len</i> items.
	 */
	public static ByteBuffer wrapDirectBuffer(byte[] b)
	{
		return BufferUtils.wrapDirectBuffer(b);
	}
	
	/**
	 * Allocates space for a DIRECT ByteBuffer in native byte order
	 * (which really doesn't matter).
	 * @param len	the length (IN BYTES) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static ByteBuffer allocDirectByteBuffer(int len)
	{
		return BufferUtils.allocDirectByteBuffer(len);
	}
	
	/**
	 * Allocates space for a DIRECT IntBuffer in native byte order.
	 * @param len	the length (IN INTS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static IntBuffer allocDirectIntBuffer(int len)
	{
		return BufferUtils.allocDirectIntBuffer(len);
	}
	
	/**
	 * Allocates space for a DIRECT FloatBuffer in native byte order.
	 * @param len	the length (IN FLOATS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static FloatBuffer allocDirectFloatBuffer(int len)
	{
		return BufferUtils.allocDirectFloatBuffer(len);
	}
	
	/**
	 * Allocates space for a DIRECT LongBuffer in native byte order
	 * @param len	the length (IN LONGS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static LongBuffer allocDirectLongBuffer(int len)
	{
		return BufferUtils.allocDirectLongBuffer(len);
	}
	
	/**
	 * Allocates space for a DIRECT ShortBuffer in native byte order
	 * @param len	the length (IN SHORTS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static ShortBuffer allocDirectShortBuffer(int len)
	{
		return BufferUtils.allocDirectShortBuffer(len);
	}
	
	/**
	 * Allocates space for a DIRECT CharBuffer in native byte order
	 * @param len	the length (IN CHARS) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static CharBuffer allocDirectCharBuffer(int len)
	{
		return BufferUtils.allocDirectCharBuffer(len);
	}
	
	/**
	 * Allocates space for a DIRECT DoubleBuffer in native byte order
	 * @param len	the length (IN DOUBLES) of the buffer. 
	 * @return		a direct buffer that can hold <i>len</i> items.
	 */
	public static DoubleBuffer allocDirectDoubleBuffer(int len)
	{
		return BufferUtils.allocDirectDoubleBuffer(len);
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
	 * @return the total amount of bytes relayed.
	 * @throws IOException if a read or write error occurs.
	 */
	public static int relay(InputStream in, OutputStream out) throws IOException
	{
		return IOUtils.relay(in, out);
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
	 * @param bufferSize the buffer size for the I/O. Must be &gt; 0.
	 * @return the total amount of bytes relayed.
	 * @throws IOException if a read or write error occurs.
	 * @since 2.14.0
	 */
	public static int relay(InputStream in, OutputStream out, int bufferSize) throws IOException
	{
		return IOUtils.relay(in, out, bufferSize, -1);
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
	 * @param bufferSize the buffer size for the I/O. Must be &gt; 0.
	 * @param maxLength the maximum amount of bytes to relay, or a value &lt; 0 for no max.
	 * @return the total amount of bytes relayed.
	 * @throws IOException if a read or write error occurs.
	 * @since 2.14.0
	 */
	public static int relay(InputStream in, OutputStream out, int bufferSize, int maxLength) throws IOException
	{
		return IOUtils.relay(in, out, bufferSize, maxLength);
	}
	
	/**
	 * Sets the size of the buffer in bytes for {@link Common#relay(InputStream, OutputStream)}.
	 * Although you may not encounter this problem, it would be unwise to set this during a call to relay().
	 * Size cannot be 0 or less.
	 * @param size the size of the relay buffer. 
	 */
	public static void setRelayBufferSize(int size)
	{
		IOUtils.setRelayBufferSize(size);
	}
	
	/**
	 * @return the size of the relay buffer for {@link Common#relay(InputStream, OutputStream)} in bytes.
	 */
	public static int getRelayBufferSize()
	{
		return IOUtils.getRelayBufferSize();
	}

	/**
	 * Reads a line from standard in; throws a RuntimeException
	 * if something absolutely serious happens. Should be used
	 * just for convenience.
	 * @return a single line read from Standard In.
	 * @see #openSystemIn()
	 * @see BufferedReader#readLine()
	 */
	public static String getLine()
	{
		return IOUtils.getLine();
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
		return StringUtils.printWrapped(message, width);
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
		return StringUtils.printWrapped(out, message, width);
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
		return StringUtils.printWrapped(out, message, startColumn, width);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(ByteBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(CharBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(ShortBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(IntBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(FloatBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(LongBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
	}
	
	/**
	 * Prints the contents of a buffer to an output print stream.
	 * @param buffer the buffer to print.
	 * @param out the {@link PrintStream} to output the dump to.
	 * @since 2.17.0
	 */
	public static void printBuffer(DoubleBuffer buffer, PrintStream out)
	{
		BufferUtils.printBuffer(buffer, out);
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
		return ObjectUtils.isEmpty(obj);
	}
	
	/**
	 * Attempts to parse a boolean from a string.
	 * If the string is null, this returns false.
	 * If the string does not equal "true" (case ignored), this returns false.
	 * @param s the input string.
	 * @return the interpreted boolean.
	 */
	public static boolean parseBoolean(String s)
	{
		return ValueUtils.parseBoolean(s);
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted byte.
	 */
	public static byte parseByte(String s)
	{
		return ValueUtils.parseByte(s);
	}
	
	/**
	 * Attempts to parse a short from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted short.
	 */
	public static short parseShort(String s)
	{
		return ValueUtils.parseShort(s);
	}
	
	/**
	 * Attempts to parse a char from a string.
	 * If the string is null or the empty string, this returns '\0'.
	 * @param s the input string.
	 * @return the first character in the string.
	 */
	public static char parseChar(String s)
	{
		return ValueUtils.parseChar(s);
	}
	
	/**
	 * Attempts to parse an int from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted integer.
	 */
	public static int parseInt(String s)
	{
		return ValueUtils.parseInt(s);
	}
	
	/**
	 * Attempts to parse a long from a string.
	 * If the string is null or the empty string, this returns 0.
	 * @param s the input string.
	 * @return the interpreted long integer.
	 */
	public static long parseLong(String s)
	{
		return ValueUtils.parseLong(s);
	}
	
	/**
	 * Attempts to parse a float from a string.
	 * If the string is null or the empty string, this returns 0.0f.
	 * @param s the input string.
	 * @return the interpreted float.
	 */
	public static float parseFloat(String s)
	{
		return ValueUtils.parseFloat(s);
	}
	
	/**
	 * Attempts to parse a double from a string.
	 * If the string is null or the empty string, this returns 0.0.
	 * @param s the input string.
	 * @return the interpreted double.
	 */
	public static double parseDouble(String s)
	{
		return ValueUtils.parseDouble(s);
	}

	/**
	 * Attempts to parse a boolean from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * If the string does not equal "true," this returns false.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted boolean or def if the input string is blank.
	 */
	public static boolean parseBoolean(String s, boolean def)
	{
		return ValueUtils.parseBoolean(s, def);
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted byte or def if the input string is blank.
	 */
	public static byte parseByte(String s, byte def)
	{
		return ValueUtils.parseByte(s, def);
	}

	/**
	 * Attempts to parse a short from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted short or def if the input string is blank.
	 */
	public static short parseShort(String s, short def)
	{
		return ValueUtils.parseShort(s, def);
	}

	/**
	 * Attempts to parse a byte from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the first character in the string or def if the input string is blank.
	 */
	public static char parseChar(String s, char def)
	{
		return ValueUtils.parseChar(s, def);
	}

	/**
	 * Attempts to parse an int from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted integer or def if the input string is blank.
	 */
	public static int parseInt(String s, int def)
	{
		return ValueUtils.parseInt(s, def);
	}

	/**
	 * Attempts to parse a long from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted long integer or def if the input string is blank.
	 */
	public static long parseLong(String s, long def)
	{
		return ValueUtils.parseLong(s, def);
	}

	/**
	 * Attempts to parse a float from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted float or def if the input string is blank.
	 */
	public static float parseFloat(String s, float def)
	{
		return ValueUtils.parseFloat(s, def);
	}

	/**
	 * Attempts to parse a double from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the interpreted double or def if the input string is blank.
	 */
	public static double parseDouble(String s, double def)
	{
		return ValueUtils.parseDouble(s, def);
	}
	
	/**
	 * Attempts to parse an array of booleans from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"true, false, apple, false"</code> becomes <code>[true, false, false, false]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of booleans or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseBoolean(String)
	 */
	public static boolean[] parseBooleanArray(String s, boolean[] def)
	{
		return ValueUtils.parseBooleanArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of bytes from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of bytes or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseByte(String)
	 */
	public static byte[] parseByteArray(String s, byte[] def)
	{
		return ValueUtils.parseByteArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of shorts from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of shorts or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseShort(String)
	 */
	public static short[] parseShortArray(String s, short[] def)
	{
		return ValueUtils.parseShortArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of chars from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"apple, pear, b, g"</code> becomes <code>['a', 'p', 'b', 'g']</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of characters or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseChar(String)
	 */
	public static char[] parseCharArray(String s, char[] def)
	{
		return ValueUtils.parseCharArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of integers from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2.1, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of integers or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseInt(String)
	 */
	public static int[] parseIntArray(String s, int[] def)
	{
		return ValueUtils.parseIntArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of floats from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0.5, -5.4, 2, grape"</code> becomes <code>[0.5f, -5.4f, 2.0f, 0f]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of floats or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseFloat(String)
	 */
	public static float[] parseFloatArray(String s, float[] def)
	{
		return ValueUtils.parseFloatArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of longs from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0, -5, 2, grape"</code> becomes <code>[0, -5, 2, 0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of long integers or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseLong(String)
	 */
	public static long[] parseLongArray(String s, long[] def)
	{
		return ValueUtils.parseLongArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of doubles from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * This assumes that the elements of the array are separated by comma-or-whitespace characters.
	 * <p>
	 * Example: <code>"0.5, -5.4, 2, grape"</code> becomes <code>[0.5, -5.4, 2.0, 0.0]</code>
	 * @param s the input string.
	 * @param def the fallback value to return.
	 * @return the array of doubles or def if the input string is blank.
	 * @since 2.17.0
	 * @see Common#parseDouble(String)
	 */
	public static double[] parseDoubleArray(String s, double[] def)
	{
		return ValueUtils.parseDoubleArray(s, def);
	}
	
	/**
	 * Attempts to parse an array of booleans from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of booleans or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseBoolean(String)
	 */
	public static boolean[] parseBooleanArray(String s, String separatorRegex, boolean[] def)
	{
		return ValueUtils.parseBooleanArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of bytes from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of bytes or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseByte(String)
	 */
	public static byte[] parseByteArray(String s, String separatorRegex, byte[] def)
	{
		return ValueUtils.parseByteArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of shorts from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of shorts or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseShort(String)
	 */
	public static short[] parseShortArray(String s, String separatorRegex, short[] def)
	{
		return ValueUtils.parseShortArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of chars from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of characters or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseChar(String)
	 */
	public static char[] parseCharArray(String s, String separatorRegex, char[] def)
	{
		return ValueUtils.parseCharArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of integers from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of integers or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseInt(String)
	 */
	public static int[] parseIntArray(String s, String separatorRegex, int[] def)
	{
		return ValueUtils.parseIntArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of floats from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of floats or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseFloat(String)
	 */
	public static float[] parseFloatArray(String s, String separatorRegex, float[] def)
	{
		return ValueUtils.parseFloatArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of longs from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of long integers or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseLong(String)
	 */
	public static long[] parseLongArray(String s, String separatorRegex, long[] def)
	{
		return ValueUtils.parseLongArray(s, separatorRegex, def);
	}

	/**
	 * Attempts to parse an array of doubles from a string.
	 * If the string is null or the empty string, this returns <code>def</code>.
	 * @param s the input string.
	 * @param separatorRegex the regular expression to split the string into tokens.
	 * @param def the fallback value to return.
	 * @return the array of doubles or def if the input string is blank.
	 * @throws NullPointerException if separatorRegex is null.
	 * @since 2.17.0
	 * @see Common#parseDouble(String)
	 */
	public static double[] parseDoubleArray(String s, String separatorRegex, double[] def)
	{
		return ValueUtils.parseDoubleArray(s, separatorRegex, def);
	}

	/**
	 * Gets the element at an index in the array, but returns 
	 * null if the index is outside of the array bounds.
	 * @param <T> the array type.
	 * @param array the array to use.
	 * @param index the index to use.
	 * @return <code>array[index]</code> or null if out of bounds.
	 * @since 2.31.2
	 */
	public static <T> T arrayElement(T[] array, int index)
	{
		return ArrayUtils.arrayElement(array, index);
	}
	
	/**
	 * Swaps the contents of two indices of an array.
	 * @param <T> the object type stored in the array.
	 * @param array the input array.
	 * @param a the first index.
	 * @param b the second index.
	 * @since 2.21.0
	 */
	public static <T> void arraySwap(T[] array, int a, int b)
	{
		ArrayUtils.arraySwap(array, a, b);
	}

	/**
	 * Concatenates a set of arrays together, such that the contents of each
	 * array are joined into one array. Null arrays are skipped.
	 * @param <T> the object type stored in the arrays.
	 * @param arrays the list of arrays.
	 * @return a new array with all objects in each provided array added 
	 * to the resultant one in the order in which they appear.
	 * @since 2.17.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] joinArrays(T[]...  arrays)
	{
		return ArrayUtils.joinArrays(arrays);
	}
	
	/**
	 * Adds an object to an array that presumably contains sorted elements.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @return the final index in the array of the added object.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #sortFrom(Comparable[], int)
	 * @since 2.21.0 
	 */
	public static <T extends Comparable<T>> int addSorted(T[] array, T object, int start)
	{
		return ArrayUtils.addSorted(array, object, start);
	}
	
	/**
	 * Adds an object to an array that presumably contains sorted elements.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the arrays.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @param comparator the comparator to use for comparisons.
	 * @return the final index in the array of the added object.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #sortFrom(Object[], int, Comparator)
	 * @since 2.21.0 
	 */
	public static <T> int addSorted(T[] array, T object, int start, Comparator<T> comparator)
	{
		return ArrayUtils.addSorted(array, object, start, comparator);
	}
	
	/**
	 * Adds an object to an array that presumably contains sorted elements, but only if it isn't found via binary search.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @return the final index in the array of the added object, or -1 if not added.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #addSorted(Comparable[], Comparable, int)
	 * @since 2.21.0 
	 */
	public static <T extends Comparable<T>> int addSortedUnique(T[] array, T object, int start)
	{
		return ArrayUtils.addSortedUnique(array, object, start);
	}
	
	/**
	 * Adds an object to an array that presumably contains sorted elements, but only if it isn't found via binary search.
	 * An object is added at some point in the array, and the element is shifted down to an appropriate
	 * position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the arrays.
	 * @param array the array to add an object to.
	 * @param object the object to add.
	 * @param start the index to add it to (the contents are replaced).
	 * @param comparator the comparator to use for comparisons.
	 * @return the final index in the array of the added object, or -1 if not added.
	 * @throws NullPointerException if a comparison happens on a null object at some point.
	 * @see Comparable#compareTo(Object)
	 * @see #addSorted(Object[], Object, int, Comparator)
	 * @since 2.21.0 
	 */
	public static <T> int addSortedUnique(T[] array, T object, int start, Comparator<T> comparator)
	{
		return ArrayUtils.addSortedUnique(array, object, start, comparator);
	}
	
	/**
	 * Shifts an object to an appropriate position according to the object's {@link Comparable#compareTo(Object)} function.
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the array to shift the contents of.
	 * @param index the index to add it to (the contents are replaced).
	 * @return the final index in the array of the sorted object.
	 * @since 2.21.0 
	 */
	public static <T extends Comparable<T>> int sortFrom(T[] array, int index)
	{
		return ArrayUtils.sortFrom(array, index);
	}
	
	/**
	 * Shifts an object to an appropriate position according to the provided <code>comparator</code> function.
	 * @param <T> the object type stored in the arrays.
	 * @param array the array to shift the contents of.
	 * @param index the index to add it to (the contents are replaced).
	 * @param comparator the comparator to use.
	 * @return the final index in the array of the sorted object.
	 * @since 2.21.0 
	 */
	public static <T> int sortFrom(T[] array, int index, Comparator<? super T> comparator)
	{
		return ArrayUtils.sortFrom(array, index, comparator);
	}
	
	/**
	 * Performs an in-place QuickSort on the provided array.
	 * The array's contents will change upon completion.
	 * Convenience method for <code>quicksort(array, 0, array.length - 1);</code>
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the input array.
	 * @since 2.21.0
	 */
	public static <T extends Comparable<T>> void quicksort(T[] array)
	{
		ArrayUtils.quicksort(array);
	}
	
	/**
	 * Performs an in-place QuickSort on the provided array using a compatible Comparator.
	 * The array's contents will change upon completion.
	 * Convenience method for <code>quicksort(array, 0, array.length - 1, comparator);</code>
	 * @param <T> the object type stored in the array.
	 * @param array the input array.
	 * @param comparator the comparator to use for comparing.
	 * @since 2.21.0
	 */
	public static <T> void quicksort(T[] array, Comparator<? super T> comparator)
	{
		ArrayUtils.quicksort(array, comparator);
	}
	
	/**
	 * Performs an in-place QuickSort on the provided array within an interval of indices.
	 * The array's contents will change upon completion.
	 * If <code>lo</code> is greater than <code>hi</code>, this does nothing. 
	 * @param <T> the object type stored in the array that extends {@link Comparable}.
	 * @param array the input array.
	 * @param lo the low index to start the sort (inclusive).
	 * @param hi the high index to start the sort (inclusive).
	 * @since 2.21.0
	 */
	public static <T extends Comparable<T>> void quicksort(T[] array, int lo, int hi)
	{
		ArrayUtils.quicksort(array, lo, hi);
	}
	
	/**
	 * Performs an in-place QuickSort on the provided array within an interval of indices.
	 * The array's contents will change upon completion.
	 * If <code>lo</code> is greater than <code>hi</code>, this does nothing. 
	 * @param <T> the object type stored in the array.
	 * @param array the input array.
	 * @param lo the low index to start the sort (inclusive).
	 * @param hi the high index to start the sort (inclusive).
	 * @param comparator the comparator to use for comparing.
	 * @since 2.21.0
	 */
	public static <T> void quicksort(T[] array, int lo, int hi, Comparator<? super T> comparator)
	{
		ArrayUtils.quicksort(array, lo, hi, comparator);
	}
	
	/**
	 * Copies references from one array to another until 
	 * it hits a null sentinel reference or the end of the source array.
	 * @param <T> the object type stored in the arrays.
	 * @param source the source array.
	 * @param sourceOffset the source offset.
	 * @param destination the destination array.
	 * @param destinationOffset the starting destination offset.
	 * @return how many references were copied.
	 * @throws ArrayIndexOutOfBoundsException if this tries to resolve a destination that is out of bounds.
	 * @since 2.21.0
	 */
	public static <T> int arrayCopyToNull(T[] source, int sourceOffset, T[] destination, int destinationOffset)
	{
		return ArrayUtils.arrayCopyToNull(source, sourceOffset, destination, destinationOffset);
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
		return StringUtils.joinStrings(separator, strings);
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
		return StringUtils.joinStrings(startIndex, separator, strings);
	}
	
	/**
	 * Joins elements in an <code>Iterable&lt;String&gt;</code> into one string, with a separator between them.
	 * @param separator the separator to insert between strings. Can be empty or null.
	 * @param strings the strings to join.
	 * @return a string of all joined strings and separators.
	 * @since 2.20.0
	 */
	public static String joinStrings(String separator, Iterable<String> strings)
	{
		return StringUtils.joinStrings(separator, strings);
	}
	
	/**
	 * Attempts to close a {@link Closeable} object.
	 * If the object is null, this does nothing.
	 * @param c the reference to the closeable object.
	 * @since 2.3.0
	 */
	public static void close(Closeable c)
	{
		IOUtils.close(c);
	}
	
	/**
	 * Attempts to close an {@link AutoCloseable} object.
	 * If the object is null, this does nothing.
	 * @param c the reference to the AutoCloseable object.
	 * @since 2.31.1
	 */
	public static void close(AutoCloseable c)
	{
		IOUtils.close(c);
	}
	
	/**
	 * Gets an object local to the current thread via a String key that
	 * was once set by {@link #setLocal(String, Object)}.
	 * @param key the String key to use.
	 * @return the stored object or null if no object was stored. 
	 * @since 2.10.0
	 */
	public static Object getLocal(String key)
	{
		return ThreadUtils.getLocal(key);
	}

	/**
	 * Sets an object local to the current thread via a String key that
	 * can be retrieved by {@link #getLocal(String)}. Objects set this way
	 * are accessible ONLY to the thread in which they were set.
	 * @param key the String key to use.
	 * @param object the object to set.
	 * @since 2.10.0
	 */
	public static void setLocal(String key, Object object)
	{
		ThreadUtils.setLocal(key, object);
	}

	/**
	 * Gets a singleton object local to the current thread.
	 * This attempts to create a POJO object if it isn't set.
	 * If the object does not have a default public constructor, it cannot be instantiated.
	 * <br><b>NOTE: As this relies on reflection to pull data, is would be wise to NOT use this method in real-time circumstances.</b>
	 * @param <T> returned object class.
	 * @param clazz the class to instantiate/fetch.
	 * @return the associated class.
	 * @since 2.21.0
	 * @see #getLocal(String)
	 * @see Reflect#create(Class)
	 * @throws RuntimeException if instantiation cannot happen, either due to a non-existent constructor or a non-visible constructor.
	 * @throws IllegalArgumentException if clazz.getCannonicalName() would return null.
	 * @throws NullPointerException if clazz is null.
	 */
	public static <T> T getLocal(Class<T> clazz)
	{
		return ThreadUtils.getLocal(clazz);
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