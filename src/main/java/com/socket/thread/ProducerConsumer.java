package com.socket.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * java多线程模拟生产者消费者问题
 * 
 * ProducerConsumer是主类，Producer生产者，Consumer消费者，Product产品，Storage仓库
 * 
 * @version 1.0 2013-7-24 下午04:49:02
 */
public class ProducerConsumer {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        Storage s = pc.new Storage(new ArrayBlockingQueue<Product>(30));

        ExecutorService service = Executors.newCachedThreadPool();
        Producer p = pc.new Producer("张三", s, 20);
//        Producer p2 = pc.new Producer("李四", s, 1);
        Consumer c = pc.new Consumer("王五", s, 2);
        Consumer c2 = pc.new Consumer("老刘", s, 3);
        Consumer c3 = pc.new Consumer("老林", s, 5);
        service.submit(p);
//        service.submit(p2);
        service.submit(c);
        service.submit(c2);
        service.submit(c3);
        
        service.shutdown();
        
    }

    /**
     * 消费者
     * 
     * @version 1.0 2013-7-24 下午04:53:30
     */
    class Consumer implements Runnable {
        private String name;
        private Storage s;
        private int num;

        public Consumer(String name, Storage s, int num) {
            this.name = name;
            this.s = s;
            this.num = num;
        }

        public void run() {
            try {
                while (true) {
                	for (int i = 0; i <= num; i++) {
                		Product product = s.pop();
                		System.out.println(name + "已消费(" + product.toString() + ").");
					}
                	Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 生产者
     * 
     * @version 1.0 2013-7-24 下午04:53:44
     */
    class Producer implements Runnable {
        private String name;
        private Storage s;
        private int num;

        public Producer(String name, Storage s, int num) {
            this.name = name;
            this.s = s;
            this.num = num;
        }

        public void run() {
            try {
                while (true) {
                    Product product = new Product(num); // 产生0~9999随机整数
//                    System.out.println(name + "准备生产(" + product.toString() + ").");
                    s.push(product);
                    System.out.println(name + "已退(" + product.toString() + ").");
//                    System.out.println("===============");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        }
    }

    /**
     * 仓库，用来存放产品
     * 
     * @version 1.0 2013-7-24 下午04:54:16
     */
    public class Storage {
    	private BlockingQueue<Product> queues;
    	public Storage (BlockingQueue<Product> queues) {
    		this.queues = queues;
    	}
    	
        /**
         * 生产
         * 
         * @param p
         *            产品
         * @throws InterruptedException
         */
        public void push(Product p) throws InterruptedException {
            queues.put(p);
        }

        /**
         * 消费
         * 
         * @return 产品
         * @throws InterruptedException
         */
        public Product pop() throws InterruptedException {
            return queues.take();
        }
    }

    /**
     * 产品
     * 
     * @version 1.0 2013-7-24 下午04:54:04
     */
    public class Product {
        private int id;

        public Product(int id) {
            this.id = id;
        }

        public String toString() {// 重写toString方法
            return "产品：" + this.id;
        }
    }

}