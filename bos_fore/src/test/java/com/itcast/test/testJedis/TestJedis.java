package com.itcast.test.testJedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.util.ArrayUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.sf.json.JSONArray;

/**
*@author Fuzongqiang
*@version Date:2017年9月23日下午8:10:50
*说明:****
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestJedis {
  @Autowired
  private RedisTemplate<String,String > redisTemplate;
  
  
  @Test
  public void test01(){
    redisTemplate.opsForValue().set("name", "共叁晓",30,TimeUnit.MINUTES);
    redisTemplate.opsForValue().set("password", "1223",30,TimeUnit.MINUTES);
    
  }
  //获取数据
  @Test
  public void test02(){
    String name = redisTemplate.opsForValue().get("name");
    String   password = redisTemplate.opsForValue().get("password");
    System.out.println(password);
    System.out.println(name);
    System.out.println(redisTemplate.opsForValue().get("11"));
  }
  //存list集合到redis
  @Test
  public void save(){
    List<String> list = new ArrayList<>();
    for(int i =0;i<10;i++){
      list.add("["+i+"]");
    }
    String[] arr = new String[list.size()];
      arr = list.toArray(arr);
    String json = JSONArray.fromObject(list).toString();
    //可以存字符串数组(用opsforset)
   //redisTemplate.opsForSet().add("redistempalelist", arr);
    //也可以存json字符串
    redisTemplate.opsForSet().add("redistempalteJson", json);
  }
  
  //删除数据
  @Test
  public void delete(){
    redisTemplate.delete("10");
  }
  //添加数据并指定过期时间
  @Test
  public void addDateAndTimeOut(){
    redisTemplate.opsForValue().set("张三的名字", "张三",24,TimeUnit.HOURS);
  }
}
