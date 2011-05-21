package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AppSensorIntegrationThreadContext {
	
  /**
   * Private internal log instance.
   */
  private static final Logger log = LoggerFactory.getLogger(AppSensorIntegrationThreadContext.class);

  private static final String HTTP_REQUEST_THREAD_CONTEXT_KEY = 	"shiroappsensor.integration.http.request";
  private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<Map<Object, Object>>();

  /**
   * Default no-argument constructor.
   */
  protected AppSensorIntegrationThreadContext() {
  }

  /**
   * Returns the value bound in the {@code ThreadContext} under the specified {@code key}, or {@code null} if there
   * is no value for that {@code key}.
   *
   * @param key the map key to use to lookup the value
   * @return the value bound in the {@code ThreadContext} under the specified {@code key}, or {@code null} if there
   *         is no value for that {@code key}.
   * @since 1.0
   */
  private static Object getValue(Object key) {
      return resources.get().get(key);
  }

  /**
   * Returns the object for the specified <code>key</code> that is bound to
   * the current thread.
   *
   * @param key the key that identifies the value to return
   * @return the object keyed by <code>key</code> or <code>null</code> if
   *         no value exists for the specified <code>key</code>
   */
  private static Object get(Object key) {
      if (log.isTraceEnabled()) {
          String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
          log.trace(msg);
      }

      Object value = getValue(key);
      if ((value != null) && log.isTraceEnabled()) {
          String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" +
                  key + "] " + "bound to thread [" + Thread.currentThread().getName() + "]";
          log.trace(msg);
      }
      return value;
  }

  /**
   * Binds <tt>value</tt> for the given <code>key</code> to the current thread.
   * <p/>
   * <p>A <tt>null</tt> <tt>value</tt> has the same effect as if <tt>remove</tt> was called for the given
   * <tt>key</tt>, i.e.:
   * <p/>
   * <pre>
   * if ( value == null ) {
   *     remove( key );
   * }</pre>
   *
   * @param key   The key with which to identify the <code>value</code>.
   * @param value The value to bind to the thread.
   * @throws IllegalArgumentException if the <code>key</code> argument is <tt>null</tt>.
   */
  private static void put(Object key, Object value) {
      if (key == null) {
          throw new IllegalArgumentException("key cannot be null");
      }

      if (value == null) {
          remove(key);
          return;
      }

      resources.get().put(key, value);

      if (log.isTraceEnabled()) {
          String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" +
                  key + "] to thread " + "[" + Thread.currentThread().getName() + "]";
          log.trace(msg);
      }
  }

  /**
   * Unbinds the value for the given <code>key</code> from the current
   * thread.
   *
   * @param key The key identifying the value bound to the current thread.
   * @return the object unbound or <tt>null</tt> if there was nothing bound
   *         under the specified <tt>key</tt> name.
   */
  private static Object remove(Object key) {
      Object value = resources.get().remove(key);

      if ((value != null) && log.isTraceEnabled()) {
          String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" +
                  key + "]" + "from thread [" + Thread.currentThread().getName() + "]";
          log.trace(msg);
      }

      return value;
  }

  /**
   * {@link ThreadLocal#remove Remove}s the underlying {@link ThreadLocal ThreadLocal} from the thread.
   * <p/>
   * This method is meant to be the final 'clean up' operation that is called at the end of thread execution to
   * prevent thread corruption in pooled thread environments.
   *
   * @since 1.0
   */
  public static void clearAll() {
      resources.remove();
  }

	public static HttpServletRequest getCurrentRequest() {
	  return (HttpServletRequest) get(HTTP_REQUEST_THREAD_CONTEXT_KEY);
  }

	public static void setCurrentRequest(HttpServletRequest request) {
		put(HTTP_REQUEST_THREAD_CONTEXT_KEY, request);
  }

  private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {
      protected Map<Object, Object> initialValue() {
          return new HashMap<Object, Object>();
      }

      /**
       * This implementation was added to address a
       * <a href="http://jsecurity.markmail.org/search/?q=#query:+page:1+mid:xqi2yxurwmrpqrvj+state:results">
       * user-reported issue</a>.
       * @param parentValue the parent value, a HashMap as defined in the {@link #initialValue()} method.
       * @return the HashMap to be used by any parent-spawned child threads (a clone of the parent HashMap).
       */
      @SuppressWarnings({"unchecked"})
      protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
          if (parentValue != null) {
              return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
          } else {
              return null;
          }
      }
  }

}
