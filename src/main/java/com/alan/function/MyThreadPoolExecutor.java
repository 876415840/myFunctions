package com.alan.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Description: 线程池
 * @Author MengQingHao
 * @Date 2020/3/27 12:01 下午
 * @Version 1.3.0
 */
public class MyThreadPoolExecutor {
    public static final Logger log = LoggerFactory.getLogger(MyThreadPoolExecutor.class);

    private static Random random = new Random();
    private static ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(80), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception {
        completableFuture();
//        completionService();
//        future();
//        execute();
    }

    /**
     * 升级版Future
     *
     * @author stephen
     * @date 2020/11/17 11:38 上午
     */
    private static void completableFuture() {
        log.info("-------------- completable future begin --------------");
        CompletableFuture[] futures = new CompletableFuture[100];
        for (int i = 0; i < 100; i++) {
            final int index = i;
            CompletableFuture<String> future = CompletableFuture
                    // 新建一个线程来运行Runnable对象(无返回值)
                    //.runAsync()
                    // 新建线程来运行Supplier<T>对象(有返回值)
                    .supplyAsync(() -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(random.nextInt(200));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        log.info("index:[{}]", index);
                        Assert.isTrue(index % 20 > 0, "整除20：" + index);
                        return "" + index;
                    }, executorService)
                    // 执行当前任务的线程，执行完任务后继续执行whenComplete里任务
                    .whenComplete((result, e) -> {
                        log.info("任务执行完后，index:[{}]，errorMsg:[{}]", index, e == null ? null : e.getMessage(), e);
                    })
                    // 把 whenCompleteAsync 这个任务继续提交给线程池来进行执行，也就是并行执行
                    //.whenCompleteAsync((result, e) -> {})
                    // 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化
                    //.thenApply()
                    // thenAccept接收上一阶段的输出作为本阶段的输入，并消费处理，无返回结果
                    //.thenAccept(result -> {})
                    // 不关心前一阶段的计算结果，因为它不需要输入参数，进行消费处理，无返回结果
                    //.thenRun(() -> {})
                    // 会把两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块交给 thenCombine 来处理
                    //.thenCombine(null, (str, obj) -> { return ""; })
                    // 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的转化操作
                    //.applyToEither(null, str -> { return ""; })
                    // 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的消耗操作
                    //.acceptEither(null, str -> {})
                    // 执行任务出现异常，执行此处任务
                    .exceptionally(e -> {
                        log.info("发生异常：", e);
                        return "发生异常";
                    });
            futures[i] = future;
        }
        log.info("-------------- completable future after --------------");
        // 使用allOf方法来表示所有的并行任务
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);
        log.info("----------------------------- allOf");
        // 阻塞主线程
        allFutures.join();
        log.info("----------------------------- join");
    }

    /**
     * jdk1.8之前较优方式
     *
     * 异步执行，且优先阻塞获取已完成任务
     * 所有任务都先扔到线程池(线程执行和队列内，数量超出走拒绝策略)
     * 扔完所有任务，主线程继续往下执行。阻塞主线程遍历获取任务执行结果，优先获取已完成的
     *
     * @author stephen
     * @date 2020/11/17 11:38 上午
     */
    private static void completionService() throws InterruptedException, ExecutionException {
        log.info("-------------- completion submit begin --------------");
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            completionService.submit(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("index:[{}]", index);
                return "" + index;
            });
        }
        executorService.shutdown();
        log.info("-------------- completion submit after --------------");
        for (int i = 0; i < 100; i++) {
            // 获取任意一个已完成的任务，一个也没有则阻塞
            System.out.println(completionService.take().get());
        }
        log.info("-------------- completion take get after --------------");
    }

    /**
     * 异步执行，按顺序阻塞获取
     * 所有任务都先扔到线程池(线程执行和队列内，数量超出走拒绝策略)
     * 扔完所有任务，主线程继续往下执行。阻塞主线程遍历获取任务执行结果，按照先后顺序(前面的未执行完会阻塞)
     *
     * @author stephen
     * @date 2020/11/17 11:38 上午
     */
    private static void future() throws InterruptedException, ExecutionException, TimeoutException {
        log.info("-------------- submit begin --------------");
        List<Future> futures = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            Future future = executorService.submit(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("index:[{}]", index);
            });
            futures.add(future);
        }
        executorService.shutdown();
        log.info("-------------- submit after --------------");
        // 阻塞 - 按顺序获取结果
        for (Future future : futures) {
            // 设置超时时间，任务执行超过该时间抛出异常
            future.get(1, TimeUnit.SECONDS);
        }
        log.info("-------------- get future after --------------");
    }

    /**
     * 异步执行，不等待结果
     * 所有任务都先扔到线程池(线程执行和队列内，数量超出走拒绝策略)
     * 扔完所有任务，主线程继续往下执行。不关心线程池执行情况
     *
     * @author stephen
     * @date 2020/11/17 11:38 上午
     */
    private static void execute() {
        log.info("-------------- execute begin --------------");
        for (int i = 0; i < 100; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(100) * index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("index:[{}]", index);
            });
        }
        executorService.shutdown();
        log.info("-------------- execute after --------------");
    }

}
