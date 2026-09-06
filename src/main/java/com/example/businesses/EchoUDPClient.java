package com.example.businesses;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.example.utils.Utils;

public class EchoUDPClient {
	  private int remotePort;// = 5000;//Integer.parseInt(Utils.getValue("ndt.exata.port"));
	  private InetAddress remoteIP;// = InetAddress.getByName("172.16.10.209");//InetAddress.getByName(Utils.getValue("ndt.exata.ip"));// = InetAddress.getByName("172.16.10.209");//172.16.10.209  127.0.0.1

	  private DatagramSocket socket; //UDP套接字 

	  public EchoUDPClient()throws IOException{

	      //创建一个UDP套接字，与本地任意一个未使用的UDP端口绑定
	      socket=new DatagramSocket(); 
	      //与本地一个固定的UDP端口绑定
	  //   socket=new DatagramSocket(9000);  
	      this.remotePort = Integer.parseInt(Utils.getValue("ndt.exata.port"));
	      this.remoteIP = InetAddress.getByName(Utils.getValue("ndt.exata.ip"));
	      
	  }

	  //定义一个数据的发送方法。
	  public void send(byte[] msg){
	    try {
	        //先准备一个待发送的数据报
	        byte[] outputData=msg;//.getBytes("UTF8")
	        //构建一个数据报文。  
	        DatagramPacket outputPacket=new DatagramPacket(outputData,
	                                 outputData.length,this.remoteIP,this.remotePort);
	        //给EchoUDPServer发送数据报
	        socket.send(outputPacket);  //给EchoUDPServer发送数据报
	    } catch (IOException ex) { }
	  }

	  //定义一个数据的接收方法。
	  public String receive(){//throws IOException{
	    String msg;
	    //先准备一个空数据报文
	    DatagramPacket inputPacket=new DatagramPacket(new byte[512],512);
	      try {
	          //阻塞语句，有数据就装包，以装完或装满为此.
	          socket.receive(inputPacket);
	          //从报文中取出字节数据并装饰成字符。
	          msg=new String(inputPacket.getData(),
	                         0,inputPacket.getLength(),"UTF8");
	      } catch (IOException ex) {
	        msg=null;
	      }
	    return msg;
	  }

	  public void close(){
	    if(socket!=null)  
	       socket.close();//释放本地端口.
	  }
	  
	  public static void main(String args[])throws IOException{
		  //签到
//		  byte[] b={(byte)0x12,(byte)0x34,(byte)0x01,(byte)0x01,(byte)0x13,(byte)0x88,(byte)0x0d,(byte)0x31,(byte)0x37,(byte)0x32,(byte)0x2e,(byte)0x31,(byte)0x36,(byte)0x2e,(byte)0x31,(byte)0x30,(byte)0x2e,(byte)0x32,(byte)0x30,(byte)0x39}; 
		  //响应
		  byte[] b={(byte)0x12,(byte)0x34,(byte)0x03,(byte)0x02,(byte)0x00,(byte)0x01}; 

	      new EchoUDPClient().send(b);
	    }  
}

