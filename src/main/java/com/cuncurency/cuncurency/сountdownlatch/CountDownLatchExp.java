package com.cuncurency.cuncurency.сountdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExp {
    private final static int RIDER_COUNT = 5;
    private static long TRACK_LENGTH = 3000;
    private static CountDownLatch countDownLatch; // object have to be sync

    static class Rider implements Runnable {
        private final int riderNumber;
        private final int riderSpeed;

        public Rider(int riderNumber) {
            this.riderNumber = riderNumber;
            this.riderSpeed = (int) (Math.random() * 10 + 5);
        }

        @Override
        public void run() {
            try {
                System.out.printf("Rider number %d got ready to start \n", riderNumber);
                countDownLatch.await(); // wait until latch will not be available for threads
                Thread.sleep(TRACK_LENGTH / riderSpeed);
                System.out.printf("Rider number %d finished\n", riderNumber);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        countDownLatch = new CountDownLatch(RIDER_COUNT + 3);
        for (int i = 1; i <= RIDER_COUNT; i++) {
            new Thread(new Rider(i)).start();
            countDownLatch.countDown();

        }
        while (countDownLatch.getCount() > 3) {
            Thread.sleep(100);
        }

        Thread.sleep(1000);
        System.out.println("GET READY");
        countDownLatch.countDown();
        Thread.sleep(1000);
        System.out.println("ATTENTION");
        countDownLatch.countDown();
        Thread.sleep(1000);
        System.out.println("START");
        countDownLatch.countDown();
    }
}
