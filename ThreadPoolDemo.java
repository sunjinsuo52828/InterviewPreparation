import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // 1. 手动创建线程池，配置所有 7 个参数
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                                      // corePoolSize: 核心线程数 (常驻)
            5,                                      // maximumPoolSize: 最大线程数 (核心+临时)
            3,                                      // keepAliveTime: 临时线程空闲存活时间
            TimeUnit.SECONDS,                       // unit: 时间单位
            new ArrayBlockingQueue<>(3),            // workQueue: 任务队列 (有界队列，容量3)
            new ThreadFactory() {                   // threadFactory: 线程工厂 (用于命名等)
                private final AtomicInteger count = new AtomicInteger(1);
                public Thread newThread(Runnable r) {
                    return new Thread(r, "My-Custom-Thread-" + count.getAndIncrement());
                }
            },
            new ThreadPoolExecutor.AbortPolicy()    // handler: 拒绝策略 (默认抛异常)
            // 可选策略:
            // 1. AbortPolicy: 抛出 RejectedExecutionException (默认)
            // 2. CallerRunsPolicy: 由调用者线程(main)直接执行该任务
            // 3. DiscardPolicy: 默默丢弃，不抛异常
            // 4. DiscardOldestPolicy: 丢弃队列里最老的任务，尝试再次提交当前任务
        );

        System.out.println("=== 全参数线程池演示开始 ===");

        // 模拟提交 10 个任务
        // 核心(2) + 队列(3) = 5。第6,7,8个任务会触发创建临时线程(达到最大5)。
        // 第9个任务及其之后，因为达到最大线程数且队列已满，会触发拒绝策略。
        for (int i = 1; i <= 10; i++) {
            int taskId = i;
            try {
                System.out.println("提交任务-" + taskId);
                
                // 模拟不同类型的业务任务，展示线程是通用的工作者
                final String taskType;
                long duration;
                
                if (i % 3 == 1) {
                    taskType = "订单计算(CPU密集)";
                    duration = 1000;
                } else if (i % 3 == 2) {
                    taskType = "文件保存(IO密集)";
                    duration = 2000;
                } else {
                    taskType = "邮件发送(网络IO)";
                    duration = 1500;
                }

                executor.submit(() -> {
                    printThreadInfo("任务-" + taskId + " [" + taskType + "]");
                    simulateWork(duration); 
                });
            } catch (RejectedExecutionException e) {
                System.err.println(">>> 任务-" + taskId + " 被拒绝! 线程池已满。");
            }
        }

        // 关闭线程池
        executor.shutdown();
    }

    private static void printThreadInfo(String taskName) {
        // 打印当前执行该任务的线程名称
        // 你会发现同一个线程名称（如 pool-1-thread-1）会出现在不同的任务中
        String threadName = Thread.currentThread().getName();
        System.out.println("线程 [" + threadName + "] 正在执行: " + taskName);
    }

    private static void simulateWork(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
