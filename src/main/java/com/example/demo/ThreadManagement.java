package com.example.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class ThreadManagement {

        public static void main(String[] args) throws InterruptedException {
            // The number of threads that will be created 
            //Request is to use between 3 and 20;
            int tn = 20; 
            CountDownLatch cdLatch = 
                new CountDownLatch( tn  );
            Thread.currentThread();
            int threadID = Thread.activeCount();
            int sleep = 300;
            for (int i = 0; i < tn; i++) {
                int threadId = i + 1;
                new Thread(() -> {
                    try {
                        System.out.println("Thread # " + threadId + " Waiting...");
                        Thread.sleep(sleep);
    
                        System.out.println("Thread # " + threadId + " Ready...");
                        Thread.sleep(sleep);
    
                        System.out.println("Thread  # " + threadId + " Running...");
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread  # " + threadId + " Interrupted.");
                    } catch (Exception e) { 
                        System.out.println("Thread # " + threadId + " Exception occurred: " + e.getMessage());
                    } finally {
                        System.out.println("Thread # " + threadId + " Completed.");
                        cdLatch.countDown();
                    }
                })
                .start();
                threadID = threadId;
            }
    
            cdLatch.await();
            System.out.println("All  " + threadID + "  threadscompleted.");
            System.out.println("ForkJoinPool Manager started...");

            // We can call for Fork/Join Pool Manager to run bigger number of threads = 10000
            forkJoinPoolManager(); 
        }

    // This is second option to manage threads using Fork/Join Pool
    // That is important to handle large number of threads and limit the number of threads in order to avoid resource overload.
    public static void forkJoinPoolManager() throws InterruptedException {
        // NUmber of thread tasks to be executed...
        int tn = 10000;
        ForkJoinPool pool = new ForkJoinPool();
        CountDownLatch cdlatch = new CountDownLatch(tn);

        for (int i = 0; i < tn; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(100);
                    System.out.println("Task started by " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Task interrupted." + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Exception occurred: " + e.getMessage());
                } finally {
                    cdlatch.countDown();
                }
            });
        }

        cdlatch.await();
        System.out.println("All " + tn + " tasks completed.");
        pool.shutdown();
    }        
}
        

