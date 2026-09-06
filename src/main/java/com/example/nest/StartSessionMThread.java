package com.example.nest;

import java.util.Date;
import java.util.List;

import com.example.nest.api.Nest;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.example.business.base.SystemConfig;
import com.example.nest.api.Nest.Link;
import com.example.nest.api.Nest.Node;
import com.example.nutz.DBTools;

//1.创建一个实现了Runnable接口的类
public class StartSessionMThread implements Runnable {
	
	Dao dao = DBTools.getDao();
	
	private int sessionid;
	private List<Node> nodes;
	private List<Link> links;
	private List<Nest.Server> servers;
	
	public StartSessionMThread(int sessionid, List<Node> nodes, List<Link> links, List<Nest.Server> servers) {
		this.sessionid = sessionid;
		this.nodes = nodes;
		this.links = links;
		this.servers = servers;
	}

    //2.实现类去实现Runnable中的抽象方法：run()
    @Override
    public void run() {
    	Date now = new Date();
	    System.out.println(this.sessionid + "--->仿真(" +  now + "): 开始");
	    String nestIP = SystemConfig.getStringValue("nestIP");
	    
	    try {
	    	NestClient client = new NestClient(nestIP, 50051);
	    	boolean res = false;
		    res = client.startSession(sessionid, nodes, links,servers);
		    System.out.println(this.sessionid + "--->仿真(" +  now + "): 开始--->" + res);
			
		    Chain chain = res ? Chain.make("running", 1) : Chain.make("running", 0);
			if (res) {
				Date started = new Date();
				chain.add("startedtime", started);
				
				dao.update("simulation", chain, Cnd.where("sessionid", "=", sessionid));	
			} else {
				dao.update("simulation", chain, Cnd.where("sessionid", "=", sessionid));	
			}
			
	    } catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    
		    dao.update("simulation", Chain.make("running", -1), Cnd.where("sessionid", "=", sessionid));	
	    }
	
    }
}