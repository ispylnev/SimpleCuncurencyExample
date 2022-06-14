package com.cuncurency.cuncurency.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

    private static final int COUNT_CONTROL_PLACES = 5;
    private static final int COUNT_RIDERS = 7;
    // control flags  available places
    private static boolean[] CONTROL_PLACES = null;
    private static Semaphore SEMAPHORE = null;

    public static class Rider implements Runnable {
        private final int riderNum;

        public Rider(int riderNum) {
            this.riderNum = riderNum;
        }

        @Override
        public void run() {
            System.out.printf("Rider %d got to control zone", riderNum);
            try {
                SEMAPHORE.acquire();
                System.out.printf("\t Rider %d  is checking an available controller \n", riderNum);
                int controlPlaceNum = -1;
                synchronized (CONTROL_PLACES) {
                    for (int i = 0; i < COUNT_CONTROL_PLACES; i++) {
                        if (CONTROL_PLACES[i]) {
                            CONTROL_PLACES[i] = false;
                            controlPlaceNum = i;
                            System.out.printf("\t Rider %d  found an available controller\n", riderNum);
                            break;
                        }
                    }
                    Thread.sleep(1000);
                    synchronized (CONTROL_PLACES) {
                        CONTROL_PLACES[controlPlaceNum] = true;
                    }
                    SEMAPHORE.release();
                    System.out.printf("\tRider %d  finished checking\n", riderNum);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
            // Определяем количество мест контроля
            CONTROL_PLACES = new boolean[COUNT_CONTROL_PLACES];
            // Флаги мест контроля [true-свободно,false-занято]
            for (int i = 0; i < COUNT_CONTROL_PLACES; i++)
                CONTROL_PLACES[i] = true;
            /*
             *  Определяем семафор со следующими параметрами :
             *  - количество разрешений 5
             *  - флаг очередности fair=true (очередь
             *                             first_in-first_out)
             */
            SEMAPHORE = new Semaphore(CONTROL_PLACES.length,
                    true);

            for (int i = 1; i <= COUNT_RIDERS; i++) {
                new Thread(new Rider(i)).start();
                Thread.sleep(400);
            }
    }
}

