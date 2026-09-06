package com.example.commons;

public class BusinessConfigType {
	//执行funcs；继续next（输入或展平后的输入就是基本单元）
	public static final int normal = 1;	
	
	//执行funcs进行mapper，执行reducer；继续next（输入或展平后的输入就是基本单元）
	public static final int mapper = 2;  
	
	//通过field_target、filed_table、field_source、field_result、filed_relation执行；
	//继续next（输入或展平后的输入就是基本单元）；
	//funcs可以继续对子单元进行处理（value就是子单元）
	public static final int inside_id_to_value = 3;	
	
	//通过field_target、filed_table、field_source、field_result执行；
	//继续next（输入或展平后的输入就是基本单元）；
	//funcs可以继续对子单元进行处理（value就是子单元）
	public static final int inside_replace_id_with_value = 11;	
	
	//适用于关联表的情况
	public static final int inside_id_to_value_from_relation = 7;	
	
	//通过field_target执行；
	//继续next（基本单元变成value）；
	//funcs没有使用
	public static final int inside_get_value = 4;
	
	//去除record内的某些字段
	public static final int inside_remove_value = 10;
	
	//增加自增id，field_target作为数据库名，field_table作为表名
	public static final int inside_add_auto_increment_id = 8;
	
	//由于数据库操作是通过InParamsDb实现的，需要将record放入InParamsDb
	public static final int put_record_into_inParamsDb = 5;
	
	//对于分页的情况，对list进行处理
	public static final int pagination = 6;
	
	//需要获取当前认证用户
	public static final int auth_user = 9;
	
	
	
}
