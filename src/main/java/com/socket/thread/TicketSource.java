package com.socket.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @title: TicketSource.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月13日
 * @version 1.0
 */
public class TicketSource {
    public static void main(String args[])
    {
    	ExecutorService pool = Executors.newFixedThreadPool(2);
    	 java.util.concurrent.locks.Lock lock = new ReentrantLock(); 
    	Tickets tickets = new Tickets(29);
    	
    	
        for(int i=1;i<50;i++) {
        	
        	BuyTicket a = new BuyTicket("a", 5, tickets, lock);
        	BuyTicket b = new BuyTicket("b", 10, tickets, lock);
        	BuyTicket c = new BuyTicket("c", 3, tickets, lock);
        	ReturnTicket f = new ReturnTicket("f", 2, tickets);
        	BuyTicket d = new BuyTicket("d", 5, tickets, lock);
        	BuyTicket e = new BuyTicket("e", 20, tickets, lock);
        	ReturnTicket g = new ReturnTicket("d", 3, tickets);
        	
        	pool.execute(new Thread(a,"杭州"));
        	pool.execute(new Thread(b,"北京"));
        	pool.execute(new Thread(c,"河北"));
        	pool.execute(new Thread(d,"南京"));
        	pool.execute(new Thread(f,"南宁"));
        	pool.execute(new Thread(e,"杭州"));
        	pool.execute(new Thread(g,"杭州"));
        	
        	
//        	Station sta1 = new Station(tickets, 1, "a", lock);
//        	Station sta2 = new Station(tickets, 2, "q", lock);
//        	Station sta3 = new Station(tickets, 3, "b", lock);
//        	pool.execute(new Thread(sta1,"杭州"));
//        	pool.execute(new Thread(sta2,"杭州"));
//        	pool.execute(new Thread(sta3,"北京"));
//        	new Thread(sta1,"杭州").start();
//        	new Thread(sta2,"杭州").start();
//        	new Thread(sta3,"北京").start();
        }
    	  pool.shutdown(); 
//    	
    } 

} 
class Station implements Runnable {
    private String state;
    private int people;
    private Tickets tickets;
    private java.util.concurrent.locks.Lock  myLock;
    
    public Station(Tickets tickets, int people, String state,  java.util.concurrent.locks.Lock myLock) {
    	this.tickets = tickets;
    	this.people = people;
    	this.state = state;
    	this.myLock = myLock;
    }
    
    public void run()
    {
        	myLock.lock();
    	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if(tickets.getTicket() >= people) {
    			for(int j=0 ; j <= people; j ++) {
    				System.out.println(Thread.currentThread().getName()+ state + "号窗口卖出" + tickets.getTicket()+"号票");
    				tickets.setTicket(tickets.getTicket()-1);
    			}
    			
    		} else if(tickets.getTicket() > 0 && tickets.getTicket() < people){
    			for(int j=0 ; j < people; j ++) {
    				System.out.println(Thread.currentThread().getName()+ state + "号窗口卖出" + tickets.getTicket()+"号票");
    				tickets.setTicket(tickets.getTicket()-1);
    			}
    		} else {
    			System.out.println("票已售完 请等待！");
    			try {
					Thread.currentThread().sleep(1000);;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        		myLock.unlock(); 
    }
}


class BuyTicket implements Runnable{
	String name;
	int num;
	Tickets tickets;
	Lock lock;
	
	public BuyTicket(String name, int num, Tickets tickets, Lock lock) {
		this.name = name;
		this.num = num;
		this.tickets = tickets;
		this.lock = lock;
	}
	
	@Override
	public void run() {
//		lock.lock();
		 synchronized (tickets) {
			 System.out.println(num + ":" + tickets.getTicket());
				if(tickets.getTicket() >= num) {
					for(int j = 0 ; j < num; j ++) {
						System.out.println(Thread.currentThread().getName()+ name + "买走" + tickets.getTicket()+"号票");
						tickets.setTicket(tickets.getTicket()-1);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else if(tickets.getTicket() > 0 && tickets.getTicket() < num){
					
					int random=(int)(Math.random()*tickets.getTicket());
					
					System.out.println("剩余票不足,随机购买");
	    			for(int j=0 ; j <= random; j ++) {
	    				System.out.println(Thread.currentThread().getName()+ name + "买走" + tickets.getTicket()+"号票");
	    				tickets.setTicket(tickets.getTicket()-1);
	    				try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	    			}
	    		} else {
	    			System.out.println("票已售完 请等待！");
	    			try {
						Thread.currentThread().sleep(1000);;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
			}
//		lock.unlock();
	}
}

class ReturnTicket implements Runnable {
	String name;
	int num;
	Tickets tickets;
	
	public ReturnTicket(String name, int num, Tickets tickets) {
		this.name = name;
		this.num = num;
		this.tickets = tickets;
	}
	
	public void run() {
		synchronized (tickets) { 
			for(int j = 0 ; j < num; j ++) {
				System.out.println(Thread.currentThread().getName()+ name + "退进" + (tickets.getTicket()+1)+"号票");
				tickets.setTicket(tickets.getTicket()+1);
				 try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class Tickets { 
	private int ticket; //余票 

	 Tickets(int ticket) {
	         this.ticket = ticket; 
	 } 

	 public synchronized int getTicket() {
	         return ticket; 
	 } 

	 public synchronized  void setTicket(int ticket) {
	         this.ticket = ticket; 
	 } 
	}

