package com.socket.nio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.socket.SerializableUtil;

/**
 * @title: Client.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月7日
 * @version 1.0
 */

public class Client {
	 private final static Logger logger = Logger.getLogger(Client.class.getName());  
     
	    public static void main(String[] args) throws Exception {  
//	        for (int i = 0; i < 100; i++) {  
//	            final int idx = i;  
//	            new Thread(new MyRunnable(idx)).start();  
//	        }  
	        new Thread(new MyRunnable(0)).start();  
	    }  
	      
	    private static final class MyRunnable implements Runnable {  
	          
	        private final int idx;  
	  
	        private MyRunnable(int idx) {  
	            this.idx = idx;  
	        }  
	  
	        public void run() {  
	            SocketChannel socketChannel = null;  
	            try {  
	                socketChannel = SocketChannel.open();  
	                SocketAddress socketAddress = new InetSocketAddress("localhost", 10000);  
	                socketChannel.connect(socketAddress);  
	  
//	                MyRequestObject myRequestObject = new MyRequestObject("request_" + idx, "request_" + idx);  
//	                logger.log(Level.INFO, myRequestObject.toString());  
//	                sendData(socketChannel, myRequestObject);  
	                
	                sendFile(socketChannel, new File("E:/test/client_send.zip"));  
	                receiveFile(socketChannel, new File("E:/test/client_receive.msi"));  
	                  
//	                MyResponseObject myResponseObject = receiveData(socketChannel);  
//	                logger.log(Level.INFO, myResponseObject.toString());  
	            } catch (Exception ex) {  
	                logger.log(Level.SEVERE, null, ex);  
	            } finally {  
	                try {  
	                    socketChannel.close();  
	                } catch(Exception ex) {}  
	            }  
	        }
	        
	        private void sendFile(SocketChannel socketChannel, File file) throws IOException {
	        	FileInputStream  fis = new FileInputStream(file);
	        	
	        	FileChannel channel = fis.getChannel();
	        	try{
	        	
	        	ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	        	int size = 0;
	        	while ((size = channel.read(buffer)) != -1) {
					buffer.rewind();
					buffer.limit(size);
					socketChannel.write(buffer);
					buffer.clear();
				}socketChannel.socket().shutdownOutput();  
		        } finally {  
	                try {  
	                    channel.close();  
	                } catch(Exception ex) {}  
	                try {  
	                    fis.close();  
	                } catch(Exception ex) {}  
	            } 
	        }
	        
	        private void receiveFile(SocketChannel socketChannel, File file) throws IOException {
	        	FileOutputStream fos = new FileOutputStream(file);
	        	
	        	FileChannel channel = fos.getChannel();
	        	
	        	try {
	        		ByteBuffer buffer = ByteBuffer.allocateDirect(1024); 
	        		
	        		int size = 0 ;
	        		
	        		while ((size = socketChannel.read(buffer)) != -1) {
						buffer.flip();
						if(size > 0) {
							buffer.limit(size);
							channel.write(buffer);
							buffer.clear();
						}
					}
					
				} finally {
					try {  
	                    channel.close();  
	                } catch(Exception ex) {}  
	                try {  
	                    fos.close();  
	                } catch(Exception ex) {}  
	            } 
				}
	        	
	  
	        private void sendData(SocketChannel socketChannel, MyRequestObject myRequestObject) throws IOException {  
	            byte[] bytes = SerializableUtil.toBytes(myRequestObject);  
	            ByteBuffer buffer = ByteBuffer.wrap(bytes);  
	            socketChannel.write(buffer);  
	            socketChannel.socket().shutdownOutput();  
	        }  
	  
	        private MyResponseObject receiveData(SocketChannel socketChannel) throws IOException {  
	            MyResponseObject myResponseObject = null;  
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	              
	            try {  
	                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);  
	                byte[] bytes;  
	                int count = 0;  
	                while ((count = socketChannel.read(buffer)) >= 0) {  
	                    buffer.flip();  
	                    bytes = new byte[count];  
	                    buffer.get(bytes);  
	                    baos.write(bytes);  
	                    buffer.clear();  
	                }  
	                bytes = baos.toByteArray();  
	                Object obj = SerializableUtil.toObject(bytes);  
	                myResponseObject = (MyResponseObject) obj;  
	                socketChannel.socket().shutdownInput();  
	            } finally {  
	                try {  
	                    baos.close();  
	                } catch(Exception ex) {}  
	            }  
	            return myResponseObject;  
	        }  
	    }  
}
