package com.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hp.hpl.sparta.xpath.TrueExpr;
import com.itcast.crm.domain.Customer;

/**
*@author Fuzongqiang
*@version Date:2017年9月22日下午1:25:04
*说明:****
*/
public interface CustomerDao extends JpaRepository<Customer, Integer>{
  
  List<Customer> findByFixedAreaId(String fixedAreaId);
  
  List<Customer>findByFixedAreaIdIsNull();
  
  //关联传进来的定区id到customer中
  @Modifying
  @Query(value="update Customer set fixedAreaId=?1 where id=?2")
   void assignCustomer2fixedArea(String fixedAreaId, Integer customerId);
  
 //将传进来的定区id对应的customer要先清空它的fixedAreaId,避免重复设置
  @Modifying(clearAutomatically=true)
  @Query("update Customer set fixedAreaId=null where fixedAreaId=?")
  void terminateCustomersAccociated2FixedAreaId(String fixedAreaId);

  Customer findByTelephone(String telephone);
  
  @Modifying
  @Query("update Customer set type=1 where telephone=?")
  void updateCustomer(String telephone);

  Customer findByTelephoneAndPassword(String telephone, String password);
 
  @Query("select fixedAreaId from Customer where address =?")
  String findByAddress(String sendAddress);

}
