package com.example.businesses;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.stereotype.Component;

import com.example.nutz.DBTools;
import com.example.utils.Utils;

public class EchoUDPServer {
	
	static Dao dao = DBTools.getDao();
	
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int port;
    private DatagramSocket socket;
    
    private static byte synsheadhigh = (byte)0x12;
    private static byte synsheadlow = (byte)0x34;
    
    private List<Timer> timerList = new ArrayList<Timer>();
    
//    private int mysql_code;
//    private String mysql_exataip;
//    private String mysql_exataport;
    
    //创建 SingleObject 的一个对象
    private static EchoUDPServer instance = new EchoUDPServer();
    
    private EchoUDPServer(){
    	
//    	Record r = dao.fetch("exata", Cnd.where("id","=",1));
//    	
//    	this.mysql_code = Integer.parseInt(r.getString("code"));
//    	this.mysql_exataip = r.getString("exataip");
//    	this.mysql_exataport = r.getString("exataport");
    	
    	try {
			this.port = Integer.parseInt(Utils.getValue("ndt.udp.port"));
			socket =new DatagramSocket(this.port);
			System.out.println("UDP服务器已启动.....");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //获取唯一可用的对象
    public static EchoUDPServer getInstance(){
       return instance;
    }

    public void service(){
        while(true){
            try{
            	byte[] buf = new byte[2048];
                DatagramPacket packet =new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String msg = new String(packet.getData(),0,packet.getLength(),"UTF-8");
                System.out.println(packet.getAddress()+" : "+packet.getPort()+">"+msg);
                
                byte b1 = (byte)packet.getData()[0];
                byte b2 = (byte)packet.getData()[1];
                if ( b1 == synsheadhigh && b2 == synsheadlow ) {//是exata发送的
                	String type =  String.format("%X", (byte)packet.getData()[2]);//(byte)packet.getData()[2] + "";
//        			System.out.println(softwareCode);
                	if (type.equals("1")) { //签到包
                		String softwareCode =  String.format("%X", (byte)packet.getData()[3]);//(byte)packet.getData()[2] + "";
        				//端口2B
//                		String port = new String(packet.getData(),3,4,"UTF-8");
        				String port16 = String.format("%X", (byte)packet.getData()[4]) + "" + String.format("%X", (byte)packet.getData()[5]);
        				
        				BigInteger port10 = new BigInteger(port16, 16);
        				String port = String.valueOf(port10);
        				
        				String ipLength = String.format("%d", (byte)packet.getData()[6]);
        				int ipL = Integer.parseInt(ipLength);
        				String ip = new String(packet.getData(),7,7+ipL,"UTF-8");
//        				System.out.println(ip);
        				
        				//存数据库
        				dao.update("exata", Chain.make("code",softwareCode.trim()).add("exataip", ip.trim()).add("exataport", port.trim()).add("status", 1).add("signtime", new Date()), Cnd.where("id","=",1));
                    	
//        				if (Integer.toString(this.mysql_code).equals(softwareCode) && this.mysql_exataip.equals(ip) && this.mysql_exataport.equals(port) ) {
//        					continue;
//        				} else {
//        					dao.update("exata", Chain.make("code",softwareCode).add("exataip", ip).add("exataport", port).add("status", 1).add("signtime", new Date()), Cnd.where("id","=",1));
//                        	this.mysql_code = Integer.parseInt(softwareCode);
//                        	this.mysql_exataip = ip;
//                        	this.mysql_exataport = port;
//        				}
                    	
        			} else if (type.equals("3")) { //运行控制包
        				String cmdCode =  String.format("%X", (byte)packet.getData()[3]);
        				
        				if ("2".equals(cmdCode)) { //响应
        					String exercise16 = String.format("%X", (byte)packet.getData()[4]) + "" + String.format("%X", (byte)packet.getData()[5]);
            				BigInteger exercise10 = new BigInteger(exercise16, 16);
            				String exercise = String.valueOf(exercise10);
            				
            				if (exercise != null && exercise.length() > 0) {
            					//存数据库
            					Date now = new Date();
                				dao.update("exercise", Chain.make("exercisestatus",3).add("starttime", now).add("status", 1), Cnd.where("id","=",exercise));
                				dao.update("exata", Chain.make("starttime",now).add("exercisestatus", 3).add("status", 1), Cnd.where("id","=",1));
                				
                				//发送攻击指令
                				autoSendAttack(exercise);
            				}
        				} else { //测试用
        					
        					System.out.println("运行控制包测试："+msg);
        				}
        				
        				
        				
        			} else { //测试用
    					
    					System.out.println("其他包测试："+msg);
    				}
                	
                }
                
            
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
    
    public void cancleTasks() {
    	for(int i = 0 ; i<this.timerList.size(); i++) {
    		this.timerList.get(i).cancel();
    	}
    	this.timerList.clear();
    }
    
    public void autoSendAttack(String exercise) throws IOException {
    	List<Record> list = dao.query("cyberattackcase", Cnd.where("exercise","=",exercise).and("status", "=", 1), null);
    	Record exata = dao.fetch("exata", Cnd.where("id", "=", 1));
    	int hasCountedTime = exata.getString("counttime") !=null ? exata.getInt("counttime") : 0 ;
    	EchoUDPClient client = new EchoUDPClient();
    	
    	for (int i = 0; i < list.size(); i++ ) {
    		Record r = list.get(i);
    		//firewall
    		if (r.getString("node") != null  && r.getInt("firewallstatus") == 1) {
    			
    			String nodeid = r.getString("node");
    			String firewallstatus = r.getString("firewallstatus") != null ? r.getString("firewallstatus") : "2";
    			String cmd = "firewall " + nodeid + " ";
    			if (firewallstatus.equals("1")) {
    				cmd = cmd + "on";
    			} else {
    				cmd = cmd + "off";
    			}
    			sendCMD(cmd, client);
    		}
    		if (r.getInt("scheduledtime") * 1000 >= hasCountedTime) {
    			int scheduletime =r.getInt("scheduledtime") * 1000 - hasCountedTime;
        		Timer timer = new Timer();
        		this.timerList.add(timer);
        		MyTask t = new MyTask(r);
        		timer.schedule(t, scheduletime);
    		}
    		
    	}
    }
    
    public static void sendCMD(String cmd, EchoUDPClient client) {
    	if (cmd != null) {
			
			byte[] buf = new byte[2048];
			
			buf[0] = synsheadhigh;
			buf[1] = synsheadlow;
			buf[2] = (byte)0x04;
			buf[3] = (byte)(cmd.length());//攻击指令长度
			byte[] temp = cmd.getBytes();
//			String[] result = Arrays.copyOf(first, first.length + second.length); 
//			System.arraycopy(second, 0, result, first.length, second.length); 
			byte[] result = new byte[4+temp.length];
	        System.arraycopy(buf, 0, result, 0, 4);  
	        System.arraycopy(temp, 0, result, 4, temp.length);  
	        
	        client.send(result);
		}
    }
    
    public static class MyTask extends TimerTask {
    	
    	public Record r;
    	
    	
    	public MyTask(Record r) {
    		this.r = r;
    	}
    	
    	public void sendAttack(Record r) throws IOException {
        	EchoUDPClient client = new EchoUDPClient();
        	String cmd = null;
        	
        	int attackType = r.getInt("attacktype");
    		//旧
    		//DOS 10 5 11 12 13 14 15 BASIC 1234 0 512 10MS 10S 20S
    		//DOS <victim> <num-of-attacker> <attacker-1> .... <attacker-N>
    		//<attack-type> <victim port> <item-count> <item-size> <interval><start-time> <end-time> 
    		//新
    		//dos 3 4 1 2 4 5 Basic 8080 1MS 30S
    		//dos <victim> <num-of-attackers> [<attacker-1> .... <attacker-N>]
    		//<attack-type> <victim port> <interval> <duration>
    		if (attackType == 1) { //dos  
    			String victim =  r.getString("victim") != null ? r.getString("victim") : "0";
    			cmd = "dos " + victim;
    			int count = 0;
    			String attckerString= "";
    			String attackers = r.getString("attackers") != null ? r.getString("attackers") : " ";
    			String[] attackerList = attackers.split(" ");
    			for (int j = 0 ; j < attackerList.length; j++) {
    				if (attackerList[j] != " ") {
    					attckerString = attckerString + attackerList[j] + " ";
    					count++;
    				}
    			}
    			if (count == 0) {
    				cmd = cmd + " 0";
    			} else {
    				cmd = cmd + " " + count + " " + attckerString;
    			}
    			//attack-type  
    			String dosattacktype = r.getString("dosattacktype") != null ? r.getString("dosattacktype") : "1";
    			if (dosattacktype.equals("1")) {
    				cmd= cmd + "BASIC ";
    			} else if (dosattacktype.equals("2")) {
    				cmd= cmd + "SYN ";
    			} else {
    				cmd= cmd + "FRAG ";
    			}
    			//victim port
    			String victimport = r.getString("victimport") != null ? r.getString("victimport") : "0";
//    			cmd= cmd + victimport + " 0 512 ";
    			cmd= cmd + victimport + " ";
    			//interval
    			String interval = r.getString("intervaltime") != null ? r.getString("intervaltime") : "0";
    			if (interval.toLowerCase().endsWith("ms")) {
    				interval = interval.substring(0, interval.length()-2);
    			}
    			cmd= cmd + interval + "MS ";
//    			//start-time
//    			String scheduledtime = r.getString("scheduledtime") != null ? r.getString("scheduledtime") : "0";
//    			if (scheduledtime.toLowerCase().endsWith("s")) {
//    				scheduledtime = scheduledtime.substring(0, scheduledtime.length()-1);
//    			}
//    			cmd= cmd + scheduledtime + "S ";
//    			
//    			//end-time
//    			String duration = r.getString("duration") != null ? r.getString("duration") : "0";
//    			if (duration.toLowerCase().endsWith("s")) {
//    				duration = duration.substring(0, duration.length()-1);
//    			}
//    			int start = Integer.parseInt(scheduledtime);
//    			int du = Integer.parseInt(duration);
//    			int end = start + du;
//    			cmd= cmd + end + "S";
    			//duration
    			String duration = r.getString("duration") != null ? r.getString("duration") : "0";
    			if (duration.toLowerCase().endsWith("s")) {
    				duration = duration.substring(0, duration.length()-1);
    			}
    			cmd= cmd + duration + "S";
    			
    		} else if (attackType == 2) { //jam
    			//旧
    			//JAMMER 2               20S     30S 0 SILENT 100
        		//JAMMER <jammer-node> <duration>30S <scanner index> SILENT <min date rate>
    			//新
    			//jammer 2 20S 0 POWER 15 RAMP-UP-TIME 0S SILENT 1500000
    			//jammer <attacker> <duration> <scanner-index>[POWER <jammer-power>] [RAMP-UP-TIME <ramp-up-time>][SILENT <min-data-rate>]
    			//20210521
//    			//jammer 2 20S 0 POWER 15 SILENT 1500000
//    			//jammer <attacker> <duration> <scanner-index>[POWER <jammer-power>] [SILENT <min-data-rate>]
    			String source =  r.getString("source") != null ? r.getString("source") : "0";
    			cmd = "jammer " + source + " ";
//    			//start-time
//    			String scheduledtime = r.getString("scheduledtime") != null ? r.getString("scheduledtime") : "0";
//    			if (scheduledtime.toLowerCase().endsWith("s")) {
//    				scheduledtime = scheduledtime.substring(0, scheduledtime.length()-1);
//    			}
//    			cmd= cmd + scheduledtime + "S ";
//    			
//    			//end-time
//    			String duration = r.getString("duration") != null ? r.getString("duration") : "0";
//    			if (duration.toLowerCase().endsWith("s")) {
//    				duration = duration.substring(0, duration.length()-1);
//    			}
//    			int start = Integer.parseInt(scheduledtime);
//    			int du = Integer.parseInt(duration);
//    			int end = start + du;
//    			cmd= cmd + end + "S ";
    			//duration
    			String duration = r.getString("duration") != null ? r.getString("duration") : "0";
    			if (duration.toLowerCase().endsWith("s")) {
    				duration = duration.substring(0, duration.length()-1);
    			}
    			cmd= cmd + duration + "S ";
    			//scanner index
    			String scannerindex = r.getString("scannerindex") != null ? r.getString("scannerindex") : "0";
    			cmd= cmd + scannerindex;
    			//POWER
    			String power = r.getString("power") != null ? r.getString("power") : "0";
    			cmd= cmd + " POWER " + power;
//    			//RAMP-UP-TIME
//    			String rampUpTime = r.getString("rampuptime") != null ? r.getString("rampuptime") : "0";
//    			if (rampUpTime.toLowerCase().endsWith("s")) {
//    				rampUpTime = rampUpTime.substring(0, rampUpTime.length()-1);
//    			}
//    			cmd= cmd + " RAMP-UP-TIME " + rampUpTime + "S";
    			//SILENT
    			int silent = r.getString("enablesilentjammer") != null ? r.getInt("enablesilentjammer") : 0;
    			if (silent == 1) {
    				cmd= cmd  + " SILENT ";
    			}
    			int mindatarate = r.getString("mindatarate") != null ? r.getInt("mindatarate") : 0;
    			cmd = cmd + mindatarate;
    			
    		} else if (attackType == 3) { //virus
    			String source =  r.getString("source") != null ? r.getString("source") : "0";
    			String victimip = r.getString("victimip") != null ? r.getString("victimip") : "0";
    			cmd = "attack " + source + " " + victimip;
    		} else if (attackType == 4) { //signal
    			String source =  r.getString("source") != null ? r.getString("source") : "0";
    			String scannerindex = r.getString("scannerindex") != null ? r.getString("scannerindex") : "0";
    			String duration = r.getString("duration") != null ? r.getString("duration") : "0";
    			if (duration.toLowerCase().endsWith("s")) {
    				duration = duration.substring(0, duration.length()-1);
    			}
    			cmd = "sigint " + source + " " + scannerindex + " " + duration;
    		} else if (attackType == 5) { //port 
    			//portscan <attacker> <victim> <inter-scan-interval> <first-port> <last-port> [<reporting-interval>]
    			String source =  r.getString("source") != null ? r.getString("source") : "0";
    			String victim =  r.getString("victim") != null ? r.getString("victim") : "0";
    			String intervaltime1 = r.getString("intervaltime1") != null ? r.getString("intervaltime1") : "0";
    			String port1 = r.getString("firstport") != null ? r.getString("firstport") : "1025";
    			String port2 = r.getString("lastport") != null ? r.getString("lastport") : "1025";
    			
    			cmd = "portscan " + source + " " + victim + " " + intervaltime1 + " " + port1 + " " + port2 ;
    			
    			
    			if (r.getString("intervaltime1output") != null) {
    				String intervaltime1output = r.getString("intervaltime1output");
    				cmd = cmd + " " + intervaltime1output;	
    			}
    			
    		} else if (attackType == 6) { //network
    			//netscan <attacker> <inter-scan-interval> <first-address> <last-address> [<reporting-interval>]
    			String source =  r.getString("source") != null ? r.getString("source") : "0";
    			String intervaltime1 = r.getString("intervaltime1") != null ? r.getString("intervaltime1") : "0";
    			String ip1 = r.getString("firstip") != null ? r.getString("firstip") : "0";
    			String ip2 = r.getString("lastip") != null ? r.getString("lastip") : "0";
    			
    			cmd = "netscan " + source + " " + intervaltime1 + " " + ip1 + " " + ip2 ;
    			
    			
    			if (r.getString("intervaltime1output") != null) {
    				String intervaltime1output = r.getString("intervaltime1output");
    				cmd = cmd + " " + intervaltime1output;	
    			}
    			
    		} else if (attackType == 7) { //firewall
    			//firewall <node-id> <table-desc> <chain-desc>
    			String nodeid =  r.getString("node") != null ? r.getString("node") : "0";
    			
    			String tabledesc = r.getString("tabledesc") != null ? r.getString("tabledesc") : "0";
    			String chaindesc = r.getString("chaindesc") != null ? r.getString("chaindesc") : "0";
    			
    			cmd = "firewall " + nodeid + " " + tabledesc + " " + chaindesc;
    			
    		}
    		
    		
    		System.out.println(cmd);
    		if (cmd != null) {
    			
    			byte[] buf = new byte[2048];
    			
    			buf[0] = synsheadhigh;
    			buf[1] = synsheadlow;
    			buf[2] = (byte)0x04;
    			buf[3] = (byte)(cmd.length());//攻击指令长度
    			byte[] temp = cmd.getBytes();
//    			String[] result = Arrays.copyOf(first, first.length + second.length); 
//    			System.arraycopy(second, 0, result, first.length, second.length); 
    			byte[] result = new byte[4+temp.length];
    	        System.arraycopy(buf, 0, result, 0, 4);  
    	        System.arraycopy(temp, 0, result, 4, temp.length);  
    	        
    	        client.send(result);
    		}
        	
        }
    	
    	@Override
        public void run() {
    		System.out.println(this.r.getString("attackname"));
            System.out.println("task begin:"+SIMPLE_DATE_FORMAT.format(new Date()));
            
            try {
				sendAttack(this.r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            System.out.println("task end:"+SIMPLE_DATE_FORMAT.format(new Date()));	
    	}
    }

    
    
    
    public static byte[] toLH(int n) {
	    byte[] b = new byte[4];
	    b[0] = (byte) (n & 0xff);
	    b[1] = (byte) (n >> 8 & 0xff);
	    b[2] = (byte) (n >> 16 & 0xff);
	    b[3] = (byte) (n >> 24 & 0xff);
	    return b;
	}
   
    public static int littleEndian2Int(byte[] b) {
    	
    	ByteBuffer buffer= ByteBuffer.allocate(2);
    	buffer.wrap(b);
    	buffer.order(ByteOrder.LITTLE_ENDIAN);
    	int r = buffer.getInt();
    	 return r;
    	
//        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    
    public static void main(String args[])throws IOException{
        new EchoUDPServer().service();
//    	
//    	byte[] b = toLH(5000);
//    	System.out.println(b);
    	
//    	 ByteBuffer buffer= ByteBuffer.allocate(4);
//         buffer.order(ByteOrder.BIG_ENDIAN);
//         buffer.asIntBuffer().put(16);
//         System.out.println(buffer.array()[3]);
//         buffer.order(ByteOrder.LITTLE_ENDIAN);
//         System.out.println(buffer.array()[3]);
//    	byte[] b = {(byte)0x13,(byte)0x10};
//    	int r = littleEndian2Int(b);
//    	System.out.println(r);
    }    

}
