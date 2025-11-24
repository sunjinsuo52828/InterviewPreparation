package java.lang;

import java.lang.ref.WeakReference;

/**
 * Simplified structure to show how Thread, ThreadLocal, and ThreadLocalMap relate.
 */
public class Thread implements Runnable {
    
    /* 
     * QUESTION: Is ThreadLocal referenced in the current Thread?
     * ANSWER: Yes, but INDIRECTLY.
     * 
     * 1. The Thread object holds a reference to the MAP (threadLocals).
     * 2. The MAP holds an array of ENTRIES.
     * 3. The ENTRY holds the ThreadLocal object as a WEAK REFERENCE (Key).
     */
    
    // The Thread holds the Map
    ThreadLocal.ThreadLocalMap threadLocals = null;

    // ... other thread fields ...

    /* 
     * The ThreadLocal class (The Key) 
     */
    public class ThreadLocal<T> {
        
        public void set(T value) {
            Thread t = Thread.currentThread();
            // Get the map from the Thread
            ThreadLocalMap map = getMap(t);
            if (map != null)
                map.set(this, value); // 'this' is the ThreadLocal object
            else
                createMap(t, value);
        }

        ThreadLocalMap getMap(Thread t) {
            return t.threadLocals;
        }

        /*
         * The Inner Map Class
         */
        static class ThreadLocalMap {
            
            // The Entry extends WeakReference!
            // This means: "I hold the Key (ThreadLocal), but I won't stop GC from collecting it."
            static class Entry extends WeakReference<ThreadLocal<?>> {
                /** The value associated with this ThreadLocal. */
                Object value;

                Entry(ThreadLocal<?> k, Object v) {
                    super(k); // Key is Weakly Referenced
                    value = v; // Value is Strongly Referenced (The Leak Source!)
                }
            }

            private Entry[] table;
            
            // ...
        }
    }
}