package com.socket.java7;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @title: CountTaskext.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年3月31日
 * @version 1.0
 */

public class CountTask extends RecursiveTask<Integer>{
	
	private static final int all= 2;
	private int start;
	private int end;

	public CountTask(int start, int end) {
		this.start= start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		
		int sum = 0;
		
		boolean canCompute = (end-start) <= all;
		
		if(canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		}else {
			 int middle = (start+end) / 2;
			 CountTask leftTask = new CountTask(start, middle);
			 CountTask rightTask = new CountTask(middle + 1,end);
			 
			 leftTask.fork();
			 rightTask.fork();
			 
			 int leftResult = leftTask.join();
			 int rightResult = rightTask.join();
			 
			 sum = leftResult + rightResult;
		}
		return sum;
	}
	
	public static void main(String[] args) {
		SortedSet<String> set = new TreeSet<String>();
		
		List<String> list = new ArrayList<String>();
		
		list.add("sdasd");
		list.add("sdasdsd");
		list.add("sdasd");
		list.add("sdasdasd");
		list.add("sdasdsda");
		list.add("sdasd");
		
		
	    Iterator<String> iterator = list.iterator();   
        while (iterator.hasNext()) {
        	System.out.println(iterator.next());
        }   
		
//		ForkJoinPool forkJoinPool = new ForkJoinPool();
//		
//		CountTask task = new CountTask(1, 4);
//		
//		forkJoinPool.execute(task);  
//		
//		Future<Integer> future = forkJoinPool.submit(task);
//		
//		try {
//			System.out.println(task.get());
//			System.out.println(future.get());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
		
	}

}
