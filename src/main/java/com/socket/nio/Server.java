package com.socket.nio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.socket.SerializableUtil;

/**
 * @title: Server.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月7日
 * @version 1.0
 */

public class Server {
	private final static Logger logger = Logger.getLogger(Server.class.getName());  
    
    public static void main(String[] args) {
    	
        Selector selector = null;  
        ServerSocketChannel serverSocketChannel = null;  
          
        try {  
            // Selector for incoming time requests  
            selector = Selector.open();  
  
            // Create a new server socket and set to non blocking mode  
            serverSocketChannel = ServerSocketChannel.open();  
            serverSocketChannel.configureBlocking(false);  
              
            // Bind the server socket to the local host and port  
            serverSocketChannel.socket().setReuseAddress(true);  
            serverSocketChannel.socket().bind(new InetSocketAddress(10000));  
              
            // Register accepts on the server socket with the selector. This  
            // step tells the selector that the socket wants to be put on the  
            // ready list when accept operations occur, so allowing multiplexed  
            // non-blocking I/O to take place.  
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
      
            // Here's where everything happens. The select method will  
            // return when any operations registered above have occurred, the  
            // thread has been interrupted, etc.  
            while (selector.select() > 0) {  
                // Someone is ready for I/O, get the ready keys  
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();  
      
                // Walk through the ready keys collection and process date requests.  
                while (it.hasNext()) {  
                    SelectionKey readyKey = it.next();  
                    it.remove();  
                      
                    // The key indexes into the selector so you  
                    // can retrieve the socket that's ready for I/O  
//                    execute((ServerSocketChannel) readyKey.channel()); 
                    doit((ServerSocketChannel) readyKey.channel());
                }  
            }  
        } catch (ClosedChannelException ex) {  
            logger.log(Level.SEVERE, null, ex);  
        } catch (IOException ex) {  
            logger.log(Level.SEVERE, null, ex);  
        } finally {  
            try {  
                selector.close();  
            } catch(Exception ex) {}  
            try {  
                serverSocketChannel.close();  
            } catch(Exception ex) {}  
        }  
    }
    
    private static void doit(final  ServerSocketChannel serverSocketChannel) throws IOException {
    	SocketChannel socketChannel = null;
    	
    	try {
			
    		socketChannel = serverSocketChannel.accept();
    		
    		received(socketChannel, new File("E:/test/server_receive.zip"));  
    		sendFile(socketChannel, new File("E:/test/server_send.msi")); 
		}finally {
			try {
				socketChannel.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    }
    
    private static void received(SocketChannel socketChannel , File file) throws IOException {
    	FileOutputStream fos = null;
    	FileChannel channel = null;
    	try {  
    	
    	fos = new FileOutputStream(file);
    	
    	channel = fos.getChannel();
    	
    	ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    	
    	int size = 0;
    	
    	while((size = socketChannel.read(buffer))!= -1) {
    		buffer.flip();
    		if (size > 0) {  
                buffer.limit(size);  
                channel.write(buffer);  
                buffer.clear();  
            } 
    		
    	} }finally {  
            try {  
                channel.close();  
            } catch(Exception ex) {}  
            try {  
                fos.close();  
            } catch(Exception ex) {}  
        } 
    }
    
    private static void sendFile(SocketChannel socketChannel , File file) throws IOException {
    	FileInputStream fis = null;
    	FileChannel channel = null;
    	try {
    		fis = new FileInputStream(file);
    		channel = fis.getChannel();
    		
    		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    		
    		int size = 0;
    		while((size = channel.read(buffer))!= -1) {
    			buffer.rewind();
    			buffer.limit(size);  
    			socketChannel.write(buffer);  
                buffer.clear();  
    		}
    		socketChannel.socket().shutdownOutput();  
			
		} finally {
            try {  
                channel.close();  
            } catch(Exception ex) {}  
            try {  
                fis.close();  
            } catch(Exception ex) {}
        }  
    }
  
    private static void execute(ServerSocketChannel serverSocketChannel) throws IOException {  
        SocketChannel socketChannel = null;  
        try {  
            socketChannel = serverSocketChannel.accept();  
            MyRequestObject myRequestObject = receiveData(socketChannel);  
            logger.log(Level.INFO, myRequestObject.toString());  
              
            MyResponseObject myResponseObject = new MyResponseObject(  
                    "response for " + myRequestObject.getName(),   
                    "response for " + myRequestObject.getValue());  
            sendData(socketChannel, myResponseObject);  
            logger.log(Level.INFO, myResponseObject.toString());  
        } finally {  
            try {  
                socketChannel.close();  
            } catch(Exception ex) {}  
        }  
    }  
      
    private static MyRequestObject receiveData(SocketChannel socketChannel) throws IOException {  
        MyRequestObject myRequestObject = null;  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
          
        try {  
            byte[] bytes;  
            int size = 0;  
            while ((size = socketChannel.read(buffer)) >= 0) {  
                buffer.flip();  
                bytes = new byte[size];  
                buffer.get(bytes);  
                baos.write(bytes);  
                buffer.clear();  
            }  
            bytes = baos.toByteArray();  
            Object obj = SerializableUtil.toObject(bytes);  
            myRequestObject = (MyRequestObject)obj;  
        } finally {  
            try {  
                baos.close();  
            } catch(Exception ex) {}  
        }  
        return myRequestObject;  
    }  
  
    private static void sendData(SocketChannel socketChannel, MyResponseObject myResponseObject) throws IOException {  
        byte[] bytes = SerializableUtil.toBytes(myResponseObject);  
        ByteBuffer buffer = ByteBuffer.wrap(bytes);  
        socketChannel.write(buffer);  
    }  
}
