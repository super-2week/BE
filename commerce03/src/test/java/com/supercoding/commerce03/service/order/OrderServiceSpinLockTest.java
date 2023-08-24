package com.supercoding.commerce03.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrderServiceSpinLockTest {

    @Test
    @DisplayName("동시에 접근할 경우, 락 작동 테스트")
    public void testConcurrentLockAcquisition() throws InterruptedException {
        OrderService.InMemorySpinLock inMemorySpinLock = new OrderService.InMemorySpinLock();

        AtomicReference<Integer> testInt = new AtomicReference<>(0);
        AtomicReference<Integer> testInt2 = new AtomicReference<>(0);

        Long resourceId1 = 1L; // Assuming resource ID is 1 for this example
        Long resourceId2 = 2L; // Assuming resource ID is 2 for this example
        Runnable concurrentTask = () -> {

            System.out.println("Thread " + Thread.currentThread().getId() + " acquiring lock for resourceId1");
            inMemorySpinLock.acquireLock(resourceId1);
            System.out.println("Thread " + Thread.currentThread().getId() + " acquired lock for resourceId1");
            if(testInt.get()+5 <10){
                testInt.updateAndGet(v -> v + 5);
                System.out.println(testInt.get());
            }

            // Simulate some work while holding the lock
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            inMemorySpinLock.releaseLock(resourceId1);
            System.out.println("Thread " + Thread.currentThread().getId() + " released lock for resourceId1");
        };
        Runnable concurrentTask2 = () -> {

            System.out.println("Thread " + Thread.currentThread().getId() + " acquiring lock for resourceId2");
            inMemorySpinLock.acquireLock(resourceId2);
            System.out.println("Thread " + Thread.currentThread().getId() + " acquired lock for resourceId2");
            if(testInt2.get()+3 <10){
                testInt2.updateAndGet(v -> v + 3);
                System.out.println(testInt2.get());
            }

            // Simulate some work while holding the lock
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            inMemorySpinLock.releaseLock(resourceId2);
            System.out.println("Thread " + Thread.currentThread().getId() + " released lock for resourceId2");
        };

        Thread thread1 = new Thread(concurrentTask);
        Thread thread2 = new Thread(concurrentTask);
        Thread thread3 = new Thread(concurrentTask2);
        Thread thread4 = new Thread(concurrentTask2);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        assertEquals(5, testInt.get());
        assertEquals(6, testInt2.get());
    }
}
