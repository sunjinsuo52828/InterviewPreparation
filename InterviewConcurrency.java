import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.StampedLock;

/**
 * Java Concurrency Interview Scenarios
 * 
 * Covers high-frequency interview topics:
 * 1. Volatile & JMM (Visibility)
 * 2. ThreadLocal (Thread Isolation & Memory Leaks)
 * 3. Deadlock (How to write one)
 * 4. CAS vs LongAdder (High contention performance)
 * 5. CompletableFuture (Async Orchestration)
 * 6. BlockingQueue (Producer-Consumer)
 * 7. Interrupt Mechanism (Graceful Shutdown)
 * 8. Singleton (Double-Checked Locking)
 * 9. ConcurrentHashMap (Compound Operations Trap)
 * 10. Fork/Join Framework (Work Stealing)
 * 11. StampedLock (Optimistic Locking)
 */
public class InterviewConcurrency {

    // === 1. Volatile Visibility ===
    // Without 'volatile', the reader thread might cache 'running' in a register/L1 cache
    // and never see the update from the main thread.
    static volatile boolean running = true; 
    // Try removing 'volatile' to see if it hangs (Note: behavior depends on JVM/CPU)

    // === 2. Deadlock Resources ===
    static final Object resourceA = new Object();
    static final Object resourceB = new Object();

    // === 3. ThreadLocal ===
    // Each thread gets its own independent copy of the variable.
    // Critical for storing user sessions, database connections, etc.
    static ThreadLocal<String> userContext = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Starting Interview Concurrency Scenarios ===\n");

        // 1. Volatile Demo
        System.out.println("--- 1. Volatile Visibility Demo ---");
        demoVolatile();
        
        // 2. ThreadLocal Demo
        System.out.println("\n--- 2. ThreadLocal Demo ---");
        demoThreadLocal();

        // 3. Atomic vs LongAdder
        System.out.println("\n--- 3. CAS (Atomic) vs LongAdder ---");
        demoAtomics();

        // 4. Deadlock (Uncomment to test)
        System.out.println("\n--- 4. Deadlock Demo ---");
        System.out.println("Skipping actual deadlock execution to prevent hanging.");
        System.out.println("To test: Call demoDeadlock() manually.");
        // demoDeadlock(); 

        // 5. CompletableFuture
        System.out.println("\n--- 5. CompletableFuture Demo ---");
        demoCompletableFuture();

        // 6. Producer-Consumer
        System.out.println("\n--- 6. Producer-Consumer (BlockingQueue) Demo ---");
        demoProducerConsumer();

        // 7. Interrupts
        System.out.println("\n--- 7. Interrupt Mechanism Demo ---");
        demoInterrupt();

        // 8. Singleton DCL
        System.out.println("\n--- 8. Singleton Double-Checked Locking ---");
        Singleton.getInstance();
        System.out.println("Singleton instance retrieved.");

        // 9. ConcurrentHashMap Trap
        System.out.println("\n--- 9. ConcurrentHashMap Atomicity Trap ---");
        demoConcurrentHashMap();

        // 10. Fork/Join
        System.out.println("\n--- 10. Fork/Join Framework ---");
        demoForkJoin();

        // 11. StampedLock
        System.out.println("\n--- 11. StampedLock (Optimistic Read) ---");
        demoStampedLock();
    }

    static void demoVolatile() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("Reader Thread: Started, waiting for flag to become false...");
            long count = 0;
            while (running) {
                // Busy wait - tight loop
                // If 'running' is not volatile, JIT may hoist the read out of the loop
                // or CPU may not refresh the cache line.
                count++;
            }
            System.out.println("Reader Thread: Detected flag change! Loop count: " + count);
        });
        t.start();

        Thread.sleep(1000);
        System.out.println("Main Thread: Setting running = false");
        running = false;
        
        t.join(2000);
        if (t.isAlive()) {
            System.out.println("WARNING: Thread is still alive! Visibility problem occurred.");
            // Force stop for the sake of the program finishing
            System.exit(1); 
        } else {
            System.out.println("Success: Thread stopped.");
        }
    }

    static void demoThreadLocal() throws InterruptedException {
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            // Set value specific to this thread
            userContext.set("Context-for-" + threadName);
            
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            
            // Read value
            System.out.println(threadName + " reads: " + userContext.get());
            
            // CRITICAL: Always remove to prevent memory leaks in thread pools
            // ThreadLocalMap keys are WeakReferences, but values are StrongReferences.
            userContext.remove(); 
        };

        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static void demoAtomics() {
        // AtomicInteger uses CAS (Compare-And-Swap) loop.
        // Good for low-medium contention.
        AtomicInteger atomicInt = new AtomicInteger(0);

        // LongAdder uses Cell[] array to stripe contention.
        // Much higher throughput under high contention.
        LongAdder longAdder = new LongAdder();

        // Simulate some ops
        atomicInt.incrementAndGet();
        longAdder.increment();

        System.out.println("AtomicInteger value: " + atomicInt.get());
        System.out.println("LongAdder value: " + longAdder.sum());
        System.out.println("Tip: Use LongAdder for statistics/metrics gathering in high-load systems.");
    }

    static void demoDeadlock() {
        Thread t1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("T1: Holding A...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("T1: Waiting for B...");
                synchronized (resourceB) {
                    System.out.println("T1: Got B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("T2: Holding B...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("T2: Waiting for A...");
                synchronized (resourceA) {
                    System.out.println("T2: Got A");
                }
            }
        });
        t1.start();
        t2.start();
    }

    static void demoCompletableFuture() {
        System.out.println("Starting async tasks...");
        
        // 1. SupplyAsync: Run task asynchronously
        CompletableFuture<String> orderTask = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            System.out.println("Task 1: Order Created");
            return "Order#123";
        });

        // 2. ThenApply: Process result (Transformation)
        CompletableFuture<String> paymentTask = orderTask.thenApply(orderId -> {
            System.out.println("Task 2: Payment Processed for " + orderId);
            return orderId + "-PAID";
        });

        // 3. ThenCompose: Chain another async task (Dependent)
        CompletableFuture<String> shippingTask = paymentTask.thenCompose(paidOrder -> CompletableFuture.supplyAsync(() -> {
            sleep(300);
            System.out.println("Task 3: Shipped " + paidOrder);
            return "Tracking#999";
        }));

        // 4. Exception Handling
        shippingTask.exceptionally(ex -> {
            System.err.println("Error occurred: " + ex.getMessage());
            return "Error-Tracking";
        }).join(); // Wait for completion

        System.out.println("All tasks completed successfully.");
    }

    static void demoProducerConsumer() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Producer: Producing " + i);
                    queue.put(i); // Blocks if full
                    sleep(200);
                }
                queue.put(-1); // Poison pill
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer val = queue.take(); // Blocks if empty
                    if (val == -1) break;
                    System.out.println("Consumer: Consumed " + val);
                    sleep(500); // Slower consumer to force blocking
                }
                System.out.println("Consumer: Finished");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    static void demoInterrupt() throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("Worker: Working hard...");
            try {
                // Simulate long task
                while (!Thread.currentThread().isInterrupted()) {
                    // Do work...
                    Thread.sleep(500); // Sleep throws InterruptedException and CLEARS interrupted status
                    System.out.println("Worker: Still working...");
                }
            } catch (InterruptedException e) {
                System.out.println("Worker: Detected interruption during sleep! Stopping gracefully.");
                // Best Practice: Re-interrupt if you are not the owner of the thread (e.g., in a library)
                // Thread.currentThread().interrupt(); 
            }
        });
        worker.start();

        sleep(1200);
        System.out.println("Main: Interrupting worker...");
        worker.interrupt();
        worker.join();
    }

    // === 8. Singleton (Double-Checked Locking) ===
    static class Singleton {
        // Volatile is CRITICAL here to prevent instruction reordering.
        // Without volatile, 'instance = new Singleton()' is not atomic:
        // 1. Allocate memory
        // 2. Initialize object
        // 3. Assign reference to 'instance'
        // JVM might reorder to 1 -> 3 -> 2. Another thread might see non-null 'instance' (step 3)
        // before it's fully initialized (step 2), leading to a crash.
        private static volatile Singleton instance;

        private Singleton() { System.out.println("Singleton Initialized"); }

        public static Singleton getInstance() {
            if (instance == null) { // First check (Performance)
                synchronized (Singleton.class) {
                    if (instance == null) { // Second check (Safety)
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }
    }

    // === 9. ConcurrentHashMap (Atomicity Trap) ===
    static void demoConcurrentHashMap() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        // Scenario: Increment a value safely
        // WRONG WAY (Not Atomic):
        // Integer old = map.get("key");
        // if (old == null) map.put("key", 1); else map.put("key", old + 1);
        
        // RIGHT WAY (Atomic):
        map.merge("key", 1, Integer::sum);
        System.out.println("Value after merge: " + map.get("key"));
        
        // RIGHT WAY (Compute):
        map.computeIfAbsent("key2", k -> 100);
        System.out.println("Value after computeIfAbsent: " + map.get("key2"));
    }

    // === 10. Fork/Join Framework (Work Stealing) ===
    static void demoForkJoin() {
        ForkJoinPool pool = new ForkJoinPool();
        Long result = pool.invoke(new SumTask(1, 1000));
        System.out.println("ForkJoin Sum (1-1000): " + result);
    }

    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 100;
        private final int start;
        private final int end;

        SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i <= end; i++) sum += i;
                return sum;
            } else {
                int mid = (start + end) / 2;
                SumTask left = new SumTask(start, mid);
                SumTask right = new SumTask(mid + 1, end);
                left.fork(); // Push to work-stealing queue
                return right.compute() + left.join(); // Compute right, then wait for left
            }
        }
    }

    // === 11. StampedLock (Optimistic Locking) ===
    static void demoStampedLock() {
        StampedLock sl = new StampedLock();
        // Simulate shared data
        class Point { double x, y; }
        Point p = new Point();
        p.x = 10; p.y = 20;

        // Optimistic Read
        long stamp = sl.tryOptimisticRead();
        double currentX = p.x;
        double currentY = p.y;

        // Validate if a write occurred during the read
        if (!sl.validate(stamp)) {
            System.out.println("StampedLock: Write occurred during optimistic read, upgrading to full read lock...");
            stamp = sl.readLock();
            try {
                currentX = p.x;
                currentY = p.y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        System.out.println("StampedLock: Read Point(" + currentX + ", " + currentY + ")");
    }
}
