package java.util;

/**
 * Simplified view of JDK 1.8 HashMap Source Code for Interview Preparation.
 * Focuses on: Constants, Hash, Put, Resize, and Treeify.
 */
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {

    // ---------------- Constants ----------------
    /**
     * The default initial capacity - MUST be a power of two.
     * 
     * INTERVIEW TIP: Why must capacity be a Power of 2?
     * 
     * 1. Performance (Index Calculation):
     *    - CPU processes Bitwise AND (&) much faster than Modulo (%).
     *    - Formula: index = hash & (n - 1)
     *    - This trick ONLY works if n is a power of 2.
     *      Example: n=16 (10000), n-1=15 (01111).
     *      hash & 01111 is equivalent to hash % 16.
     *      If n=15 (01111), n-1=14 (01110). hash & 14 is NOT hash % 15.
     * 
     * 2. Efficient Resizing (Re-hashing):
     *    - When resizing (doubling), we don't need to re-calculate hash % newCap.
     *    - We only check ONE bit: (e.hash & oldCap).
     *    - If 0: Stay at index [j].
     *    - If 1: Move to index [j + oldCap].
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    // The critical threshold for converting a chain to a Red-Black Tree
    /*
     * INTERVIEW TIP: Why Red-Black Tree? (JDK 1.8+)
     * 
     * 1. Performance (O(n) -> O(log n)):
     *    - In JDK 1.7, heavy collisions caused Linked Lists to grow long, degrading get() to O(n).
     *    - Red-Black Trees ensure O(log n) worst-case performance.
     * 
     * 2. Why Red-Black Tree and not AVL Tree?
     *    - AVL Trees are strictly balanced (faster lookups, but slow inserts/deletes due to frequent rotations).
     *    - Red-Black Trees are loosely balanced (faster inserts/deletes, slightly slower lookups).
     *    - Since HashMap involves frequent put/remove, RB Tree is the best trade-off.
     * 
     * 3. Security:
     *    - Mitigates Hash Collision DoS attacks (where attackers flood a single bucket).
     */
    static final int TREEIFY_THRESHOLD = 8;
    
    // The threshold for converting a Tree back to a chain (during resize)
    static final int UNTREEIFY_THRESHOLD = 6;
    
    // The minimum array capacity required to allow treeification.
    // If array is smaller (e.g. 16 or 32), it will resize instead of treeifying.
    static final int MIN_TREEIFY_CAPACITY = 64;

    // ---------------- Hash Function ----------------
    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.
     * 
     * === VISUAL EXPLANATION: Why (h ^ (h >>> 16))? ===
     * 
     * Problem: Table size n is usually small (e.g., 16).
     * Index = (n - 1) & hash.
     * If n=16, (n-1) is 0000...1111 (binary). Only the last 4 bits of hash determine the index.
     * 
     * Example WITHOUT XOR:
     * Hash A: 1111 0000 0000 0000 ... 0000 0101  (High bits differ) -> Index 5
     * Hash B: 0000 1111 0000 0000 ... 0000 0101  (High bits differ) -> Index 5
     * Result: COLLISION! High bits were completely ignored.
     * 
     * Example WITH XOR (h ^ (h >>> 16)):
     * We mix high bits into low bits.
     * Hash A Low bits become: (0000 0101) ^ (1111 0000) -> Changed!
     * Hash B Low bits become: (0000 0101) ^ (0000 1111) -> Changed differently!
     * Result: Different indexes. Collision avoided.
     * 
     * INTERVIEW TIP: Why is String the most suitable Key?
     * 1. Immutability: String is immutable. Once created, it cannot change.
     *    - If a Key object is mutable and modified after insertion, its hashCode() changes.
     *    - The Map will look in the wrong bucket and fail to find the entry.
     * 2. Cached HashCode: String caches its hashCode after the first calculation.
     *    - Subsequent calls are O(1), avoiding re-calculation during Resizing or Get operations.
     * 3. Final: String is final, preventing malicious overrides of hashCode/equals.
     */
    static final int hash(Object key) {
        int h;
        // XORs the high 16 bits with the low 16 bits
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    // ---------------- Put / PutVal ----------------
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * Implements Map.put and related methods
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        
        // 1. Lazy Initialization: If table is null or empty, resize() to init
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
            
        // 2. Calculate Index: (n - 1) & hash. 
        // If bucket is empty, create new Node directly.
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            
            // 3. Check first node: If hash and key match, we found it.
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
                
            // 4. TreeNode: If it's already a Tree, use putTreeVal
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                
            // 5. Linked List: Traverse the chain
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        // Append to end (Tail Insertion - JDK 1.8)
                        p.next = newNode(hash, key, value, null);
                        
                        // 6. Treeify Check: If chain length >= 8 (binCount is 0-indexed, so >= 7 means 8th node added)
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    // Key found in chain
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            
            // Existing key update
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        // 7. Resize Check: If size > threshold (capacity * 0.75)
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    // ---------------- Resize ----------------
    /**
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        
        // ... (Calculation of newCap and newThr omitted for brevity) ...
        // Usually newCap = oldCap << 1 (Double capacity)
        
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    
                    // Case 1: Single Node
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                        
                    // Case 2: Tree Node (Split tree)
                    else if (e instanceof TreeNode) {
                        // ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                        
                        /* 
                         * INTERVIEW TIP: How does a Red-Black Tree resize?
                         * 
                         * 1. It treats the TreeNodes like a Linked List first!
                         *    - TreeNode extends Node, so it still has 'next' pointers.
                         *    - It iterates through the list just like Case 3.
                         * 
                         * 2. Splits into two lists (Lo and Hi) based on (e.hash & oldCap):
                         *    - loHead: Stay at index [j]
                         *    - hiHead: Move to index [j + oldCap]
                         * 
                         * 3. UNTREEIFY Check (Crucial):
                         *    - If a resulting list is too small (<= UNTREEIFY_THRESHOLD, i.e., 6),
                         *      it degrades back to a plain Linked List (untreeify).
                         *    - Otherwise, it rebuilds the Red-Black Tree (treeify) for the new bucket.
                         */
                         ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    }
                        
                    // Case 3: Linked List (Optimization)
                    else { 
                        // Lo and Hi lists preserve order (Tail Insertion)
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            // Optimization: Check if the bit at oldCap is 0 or 1
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null) loHead = e;
                                else loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null) hiHead = e;
                                else hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        
                        // Place lists into new table
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead; // Index stays same
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead; // Index = j + oldCap
                        }
                    }
                }
            }
        }
        return newTab;
    }
    
    // ---------------- Safe Key Example ----------------
    /**
     * INTERVIEW TIP: How to design a custom Object as a Key safely?
     * 
     * 1. Override equals() and hashCode():
     *    - Mandatory. Two logical equal objects must have same hashCode.
     * 
     * 2. Immutability (Crucial):
     *    - Fields used in hashCode() MUST NOT change after insertion.
     *    - Best Practice: Make fields 'final' and class 'final'.
     * 
     * Example of a Safe Key:
     */
    static final class SafeKey {
        private final int id;
        private final String name;
        
        // Cache hash if calculation is expensive (like String does)
        private int cachedHash = 0; 

        public SafeKey(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SafeKey safeKey = (SafeKey) o;
            return id == safeKey.id && Objects.equals(name, safeKey.name);
        }

        @Override
        public int hashCode() {
            // Return cached hash if available
            if (cachedHash != 0) return cachedHash;
            
            // Calculate and cache
            int result = id;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            cachedHash = result;
            return result;
        }
        
        // NO Setters! (Ensures Immutability)
    }
}
