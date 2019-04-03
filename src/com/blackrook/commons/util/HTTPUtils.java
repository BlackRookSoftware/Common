/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Simple HTTP functions.
 * @author Matthew Tropiano
 * @since 2.32.0
 */
public final class HTTPUtils
{
	// Keep alphabetical.
	private static final String[] VALID_HTTP = new String[]{"http", "https"};

	private HTTPUtils() {}
	
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
	    return getHTTPContent(url, "GET", null, null, null, null, "UTF-8", 30000).getStringContent();
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
	    return getHTTPContent(url, "GET", null, null, null, null, "UTF-8", socketTimeoutMillis).getStringContent();
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
	    return getHTTPContent(url, "GET", null, null, null, defaultResponseCharset, "UTF-8", socketTimeoutMillis).getStringContent();
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
	    HTTPResponse response = getHTTPContent(url, requestMethod, data, dataContentType, dataContentEncoding, defaultResponseEncoding, defaultResponseCharset, socketTimeoutMillis);
	
	    // redirect permanent or redirect temporary
	    if (response.statusCode == 301 || response.statusCode == 307 || response.statusCode == 308)
	    {
	        if (response.location == null)
	            throw new IOException("Response was code "+response.statusCode+", but no location specified!");
	
	        URL nextLocation = null;
	        try {
	            nextLocation = new URL(response.location);
	        } catch (MalformedURLException ex) {
	            throw new IOException("Redirect location was malformed!", ex);
	        }
	
	        HTTPResponse redirectedResponse = getResolvedHTTPContent(nextLocation, requestMethod, data, dataContentType, dataContentEncoding, defaultResponseEncoding, defaultResponseCharset, socketTimeoutMillis);
	        redirectedResponse.referrer = url;
	        redirectedResponse.redirected = true;
	        return redirectedResponse;
	    }
	
	    return response;
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
	    if (Arrays.binarySearch(VALID_HTTP, url.getProtocol()) < 0)
	        throw new IOException("This is not an HTTP URL.");
	
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setReadTimeout(socketTimeoutMillis);
	    conn.setRequestMethod(requestMethod);
	
	    // set up POST data.
	    if (data != null)
	    {
	        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
	        conn.setRequestProperty("Content-Type", dataContentType == null ? "application/octet-stream" : dataContentType);
	        if (dataContentEncoding != null)
	            conn.setRequestProperty("Content-Encoding", dataContentEncoding);
	        conn.setDoOutput(true);
	        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	        dos.write(data);
	        dos.close();
	    }
	
	    HTTPResponse out = new HTTPResponse();
	    out.statusCode = conn.getResponseCode();
	    out.statusMessage = conn.getResponseMessage();
	    out.encoding = conn.getContentEncoding();
	    out.contentType = conn.getContentType();
	    
	    int charsetindex;
	    if ((charsetindex = out.contentType.toLowerCase().indexOf("charset=")) >= 0)
	    {
	    	int endIndex = out.contentType.indexOf(";", charsetindex);
	    	if (endIndex >= 0)
	    		out.charset = out.contentType.substring(charsetindex + "charset=".length(), endIndex).trim();
	    	else
	    		out.charset = out.contentType.substring(charsetindex + "charset=".length()).trim();
	    }
	    
	    if (out.charset == null)
	        out.charset = defaultResponseCharset;
	    
	    out.location = conn.getHeaderField("Location"); // if any.
	
	    int range = out.statusCode / 100;
	    if (range >= 2 && range <= 3)
	    {
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        BufferedInputStream in = null;
	        try {
	            in = new BufferedInputStream(conn.getInputStream());
	            IOUtils.relay(in, bos);
	            out.content = bos.toByteArray();
	        } finally {
	        	IOUtils.close(in);
	        }
	    }
	
	    conn.disconnect();
	
	    return out;
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
	
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(conn.getInputStream());
			return IOUtils.getBinaryContents(in);
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.close(in);
			conn.disconnect();
		}
	}

	/**
	 * Gets the byte content from a opening a URL, using a default timeout
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
	 * Gets the byte content from a opening a URL.
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
	
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(conn.getInputStream());
			return IOUtils.getBinaryContents(in);
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.close(in);
		}
	}

	/**
	 * Response from an HTTP call.
	 * @since 2.31.1
	 */
	public static class HTTPResponse
	{
	    private int statusCode;
	    private String statusMessage;
	    private String location;
	    private URL referrer;
	    private String charset;
	    private String contentType;
	    private String encoding;
	    private byte[] content;
	    private boolean redirected;

	    /**
	     * Gets the string contents of the response, decoded using the response's charset.
	     * @return the content of the response as a native string, decoded.
	     * @throws UnsupportedEncodingException if the response is not in an understood charset.
	     */
	    public String getStringContent() throws UnsupportedEncodingException
	    {
	    	if (charset == null)
	    		throw new UnsupportedEncodingException("No charset specified.");
	        return content != null ? new String(content, charset) : null;
	    }
	
	    /**
	     * @return the response status code.
	     */
	    public int getStatusCode()
	    {
	        return statusCode;
	    }
	
	    /**
	     * @return the response status message.
	     */
	    public String getStatusMessage()
	    {
	        return statusMessage;
	    }
	
	    /**
	     * @return the url of the next location, if this is a 3xx redirect response.
	     */
	    public String getLocation()
	    {
	        return location;
	    }
	
	    /**
	     * @return the response's charset. can be null.
	     */
	    public String getCharset()
	    {
	        return charset;
	    }
	
	    /**
	     * @return the response's content type. can be null.
	     */
	    public String getContentType()
	    {
	        return contentType;
	    }
	
	    /**
	     * @return the response's encoding. can be null.
	     */
	    public String getEncoding()
	    {
	        return encoding;
	    }
	
	    /**
	     * @return the response's content. can be null.
	     */
	    public byte[] getContent()
	    {
	        return content;
	    }
	
	    /**
	     * @return the response's referrer URL. can be null.
	     */
	    public URL getReferrer()
	    {
	        return referrer;
	    }
	
	    /**
	     * @return true if this response was the result of a redirect, false otherwise.
	     */
	    public boolean isRedirected()
	    {
	        return redirected;
	    }
	
	}
	
}
