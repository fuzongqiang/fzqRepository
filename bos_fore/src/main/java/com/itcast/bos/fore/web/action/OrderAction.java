package com.itcast.bos.fore.web.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itcast.bos.domain.base.Area;
import com.itcast.bos.domain.take_delivery.Order;
import com.itcast.bos.service.take_delivery.OrderService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
*@author Fuzongqiang
*@version Date:2017年9月25日下午8:59:04
*说明:****
*/
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order>{
  private Order order = new Order();
  private String sendAreaInfo;//发送的区域信息
  private String recAreaInfo;//接收的区域信息
  
  @Autowired
  private OrderService serviceProxy;
  public void setSendAreaInfo(String sendAreaInfo) {
    this.sendAreaInfo = sendAreaInfo;
  }
  public void setRecAreaInfo(String recAreaInfo) {
    this.recAreaInfo = recAreaInfo;
  }
  @Override
  public Order getModel() {
    return order;
  }
//增加订单
  @Action(value="orderAction_add", results={@Result(name="success", location="index.html", type="redirect"),
                                            @Result(name="error",location="order.html",type="redirect" )})
  public String add(){
    System.out.println("走了订单的添加方法");
    if(sendAreaInfo!=null){                            //如发送方的发货地址不为空就创建一个区域,
      String[] s1 = sendAreaInfo.split("/");
      //将省市区最后一个字截掉,用以匹配area对象中的数据
      String province = s1[0];
      //province = province.substring(0, province.length()-1);
      String city = s1[1];
     // city=city.substring(0, city.length()-1);
      String district = s1[2];
    //  district = district.substring(0, district.length()-1);
      Area sendArea = new Area(province,city,district);
      
     order.setSendArea(sendArea);                        //添加发货区域的对象
    }
    if(recAreaInfo!=null  ){                            //如发送方的发货地址不为空就创建一个区域,
      String[] s1 = recAreaInfo.split("/");
      String province = s1[0];
      province = province.substring(0, province.length()-1);
      String city = s1[1];
      city=city.substring(0, city.length()-1);
      String district = s1[2];
      district = district.substring(0, district.length()-1);
      
      Area recArea = new Area(province,city,district);                              //添加收货区域的对象
        order.setRecArea(recArea);
    }
    serviceProxy.save(order);
    return SUCCESS ;
  }
}
