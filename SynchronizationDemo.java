import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizationDemo {

    static class Counter {
        private int count = 0;
        private final Lock lock = new ReentrantLock();

        // 1. 不安全的累加 (存在竞态条件)
        // 多个线程同时执行 count++ 时，会发生"丢失更新"
        public void unsafeIncrement() {
            count++; 
        }

        // 2. 使用 synchronized 关键字 (隐式锁)
        // JVM 保证同一时刻只有一个线程能进入此方法
        public synchronized void syncIncrement() {
            count++;
        }

        // 3. 使用 ReentrantLock (显式锁)
        // 提供了比 synchronized 更灵活的锁机制 (如尝试加锁、超时等待等)
        public void lockIncrement() {
            lock.lock(); // 手动加锁
            try {
                count++;
            } finally {
                lock.unlock(); // 务必在 finally 块中释放锁，防止死锁
            }
        }

        public int getCount() {
            return count;
        }
        
        public void reset() {
            count = 0;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        int threadCount = 10;
        int incrementsPerThread = 10000;
        int expected = threadCount * incrementsPerThread;

        System.out.println("=== Java 多线程同步演示 ===");
        System.out.println("场景: " + threadCount + " 个线程，每个累加 " + incrementsPerThread + " 次。");
        System.out.println("期望最终结果: " + expected);
        System.out.println("--------------------------------------------------");

        // --- 演示 1: 不安全 (无锁) ---
        runTest("无锁 (Unsafe)", counter, threadCount, incrementsPerThread, counter::unsafeIncrement);
        
        // 重置计数器
        counter.reset();

        // --- 演示 2: Synchronized (隐式锁) ---
        runTest("Synchronized", counter, threadCount, incrementsPerThread, counter::syncIncrement);
        
        // 重置计数器
        counter.reset();

        // --- 演示 3: ReentrantLock (显式锁) ---
        runTest("ReentrantLock", counter, threadCount, incrementsPerThread, counter::lockIncrement);
    }

    private static void runTest(String testName, Counter counter, int threadCount, int incrementsPerThread, Runnable task) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        
        long start = System.currentTimeMillis();
        
        // 启动所有线程
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    task.run();
                }
            });
            threads[i].start();
        }

        // 等待所有线程结束 (join)
        for (Thread t : threads) {
            t.join();
        }
        
        long end = System.currentTimeMillis();
        int result = counter.getCount();
        
        System.out.printf("[%s]\n", testName);
        System.out.printf("  最终结果: %d %s\n", result, (result == (threadCount * incrementsPerThread) ? "✅ 正确" : "❌ 错误 (数据丢失)"));
        System.out.printf("  耗时: %d ms\n", (end - start));
        System.out.println("--------------------------------------------------");
    }
}