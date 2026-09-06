package com.example.nest;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.example.business.base.SystemConfig;
import com.example.nest.api.Nest.Link;
import com.example.nest.api.Nest.Node;
import com.example.nutz.DBTools;

//1.创建一个实现了Runnable接口的类
public class StopSessionMThread implements Runnable {
	
	Dao dao = DBTools.getDao();
	
	private int sessionid;
	
	public StopSessionMThread(int sessionid) {
		this.sessionid = sessionid;
	}

    //2.实现类去实现Runnable中的抽象方法：run()
    @Override
    public void run() {
    	Date now = new Date();
	    System.out.println(this.sessionid + "--->仿真(" +  now + "): 结束");
	    String nestIP = SystemConfig.getStringValue("nestIP");
	    
	    try {
	    	NestClient client = new NestClient(nestIP, 50051);
	    	boolean res = false;
		    res = client.stopSession(sessionid);
		    System.out.println(this.sessionid + "--->仿真(" +  now + "): 结束--->" + res);

			boolean delRes = false;
			delRes = client.deleteSession(sessionid);
			System.out.println(this.sessionid + "--->仿真sessionid(" +  now + "): 回收--->" + delRes);
			
			if (res) {
				Chain chain = Chain.make("running", 0);//running 0:未启动（正常关闭） 1：正常启动 2： 启动失败 3：关闭失败 -1：异常
				Date stoped = new Date();
				chain.add("stopedtime", stoped);
				
				dao.update("simulation", chain, Cnd.where("sessionid", "=", sessionid));	
			} else {
				Chain chain = Chain.make("running", 2);
				dao.update("simulation", chain, Cnd.where("sessionid", "=", sessionid));	
			}
			
	    } catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    
		    dao.update("simulation", Chain.make("running", -1), Cnd.where("sessionid", "=", sessionid));	
	    }
	
    }
}