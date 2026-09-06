package com.example.nest;
import java.util.Date;
import java.util.List;

import com.example.nest.api.Nest;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.example.business.base.SystemConfig;

import com.example.nutz.DBTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

//    @Autowired
//    private Dao dao;
    Dao dao = DBTools.getDao();
    @Async
    public void startSession(int sessionid, List<Nest.Node> nodes, List<Nest.Link> links, List<Nest.Server> servers) {
        Date now = new Date();
        System.out.println(sessionid + "--->仿真(" + now + "): 开始");
        String nestIP = SystemConfig.getStringValue("nestIP");

        try {
            NestClient client = new NestClient(nestIP, 50051);
            boolean res = client.startSession(sessionid, nodes, links, servers);
            System.out.println(sessionid + "--->仿真(" + now + "): 开始--->" + res);

            Chain chain = res ? Chain.make("running", 1) : Chain.make("running", 0);
            if (res) {
                Date started = new Date();
                chain.add("startedtime", started);
            }
            dao.update("simulation", chain, Cnd.where("sessionid", "=", sessionid));
        } catch (InterruptedException e) {
            e.printStackTrace();
            dao.update("simulation", Chain.make("running", -1), Cnd.where("sessionid", "=", sessionid));
        }
    }
    @Async
    public void stopSession(int sessionid) {
        Date now = new Date();
        System.out.println(sessionid + "--->仿真(" +  now + "): 结束");
        String nestIP = SystemConfig.getStringValue("nestIP");

        try {
            NestClient client = new NestClient(nestIP, 50051);
            boolean res = false;
            res = client.stopSession(sessionid);
            System.out.println(sessionid + "--->仿真(" +  now + "): 结束--->" + res);

            boolean delRes = false;
            delRes = client.deleteSession(sessionid);
            System.out.println(sessionid + "--->仿真sessionid(" +  now + "): 回收--->" + delRes);

            if (res) {
                Chain chain = Chain.make("running", 0);
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

