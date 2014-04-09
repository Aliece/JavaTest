package com.socket.thread;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hsqldb.lib.HashMap;

/**
 * @title: test.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年3月24日
 * @version 1.0
 */

public class test {
	
		static class Printer implements Runnable{
			static int num = 1; //开始数字
			static final int END = 75;
			int id;
			Object o; //这就是三个线程的“公证人”，有点寒酸吧
			
			public Printer(int id, Object o) {
				this.id = id;
				this.o = o;
			}
			
			@Override
			public void run(){
				synchronized (Printer.class) {
					while(num <= END){
						if(num / 5 % 3 == id){ //如果是属于自己的数，依次打印出来五个
							System.out.print(id + ":");
							for(int i = 0; i < 5; i++){
								System.out.print(num++ + ", ");
							}
							System.out.println();
							Printer.class.notifyAll();
//							o.notifyAll();//放弃CPU使用权，唤醒在o对象的等待队列上的线程
						}else{
							try {
								Printer.class.wait();
//								o.wait(); //如果不属于自己的数，把当前线程挂在o这个对象的等待队列上（也放弃了CPU使用权），等待唤醒
							} catch (InterruptedException e) {
								System.out.println("id" + "被打断了");
							}
						}
					}
				}
			}
		}
		
		
		public static void main(String[] args) {
			//下面可以不按0，1，2的顺序来,而且在两两中间随便sleep()，都会正确打印出来
			Object o = new Object();
			new Thread( new Printer(0, o)).start();
			new Thread( new Printer(1, o)).start();
			new Thread( new Printer(2, o)).start();
		}

}
