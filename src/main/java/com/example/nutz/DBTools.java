package com.example.nutz;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.springframework.stereotype.Component;

//@Configuration
@Component
public class DBTools {
	
	public static Ioc ioc;
	
	public static Dao dao;
	
    
	//使用nutz数据源
    static {
//		try {
//			//使用复合加载器
//			ioc = new NutIoc(new ComboIocLoader("*json","com/example/nutz/dao.json"));
//			dao = ioc.get(NutDao.class, "dao");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//使用JsonLoader
    	ioc = new NutIoc(new JsonLoader("dao.json"));
    	
		dao = ioc.get(NutDao.class, "dao");     
    }
	
//    //使用spring数据源
//    static {
//        try {
//            Properties props = new Properties();
//            
//            System.out.println(System.getProperty("user.dir"));
//            
////            BufferedReader bufferedReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/java/com/example/nutz/db_spring.properties"));
//            props=PropertiesLoaderUtils.loadAllProperties("db_spring.properties");
////            prop.load(bufferedReader);
//
//            // 创建一个数据源
//            SimpleDataSource dataSource = new SimpleDataSource();
//            dataSource.setJdbcUrl(props.getProperty("mysql.url"));
//            dataSource.setUsername(props.getProperty("mysql.username"));
//            dataSource.setPassword(props.getProperty("mysql.password"));
//            dataSource.setDriverClassName(props.getProperty("mysql.driverClassName"));
//
//            // 创建一个NutDao实例,在真实项目中, NutDao通常由ioc托管, 使用注入的方式获得.
//            dao = new NutDao(dataSource);
//            System.out.println("create: " + dao);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static Dao getDao(){
//        System.out.println("get: " + dao);
        //回收的问题，参见https://www.wenjiangs.com/doc/nutz-create-datasource
        if (ioc == null) {
        	ioc = new NutIoc(new JsonLoader("ioc/dao.js")); 
        	dao = ioc.get(NutDao.class, "dao");     
        }
        return dao;
    }
	
}
