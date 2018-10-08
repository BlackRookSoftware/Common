package com.blackrook.commons;

import com.blackrook.commons.hash.HashMap;

/**
 * Simple threading utility functions.
 * Split from {@link Common}.
 * @author Matthew Tropiano
 * @since 2.32.0
 */
public final class ThreadUtils
{
	/** ThreadLocal HashMap */
	private static final ThreadLocal<HashMap<String, Object>> THREADLOCAL_HASHMAP = new ThreadLocal<HashMap<String,Object>>()
	{
		protected HashMap<String,Object> initialValue()
		{
			return new HashMap<String, Object>();
		}
	};
	
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
		try {Thread.sleep(millis);	} catch (InterruptedException e) {}
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
		try {Thread.sleep(millis, nanos);	} catch (InterruptedException e) {}
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
		return THREADLOCAL_HASHMAP.get().get(key);
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
		THREADLOCAL_HASHMAP.get().put(key, object);
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
	@SuppressWarnings("unchecked")
	public static <T> T getLocal(Class<T> clazz)
	{
		String className = clazz.getCanonicalName();
		if (className == null)
			throw new IllegalArgumentException("Provided class must be anonymous, primitive, or has no cannonical name.");
		
		T out;
		HashMap<String, Object> objectMap = THREADLOCAL_HASHMAP.get();
		String classKey = "$$" + className;
		
		if (!objectMap.containsKey(classKey))
			setLocal(classKey, out = Reflect.create(clazz));
		else
			out = (T)getLocal(classKey);
		
		return out;
	}

}
