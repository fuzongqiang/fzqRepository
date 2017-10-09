package com.itcast.crm.service;
/**
*@author Fuzongqiang
*@version Date:2017年9月22日下午1:27:20
*说明:****
*/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itcast.crm.dao.CustomerDao;
import com.itcast.crm.domain.Customer;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
  @Autowired
  private CustomerDao dao;
        public List<Customer> findAll() {
          return dao.findAll();
        }
        //查找没有关联定区的
        public List<Customer> findCustomersNotAssociated() {
          // TODO Auto-generated method stub
          return dao.findByFixedAreaIdIsNull();
        }
        //查询已经关联指定定区id的
        public List<Customer> findCustomersAssociated( String fixedAreaId) {
          // TODO Auto-generated method stub
          return dao.findByFixedAreaId(fixedAreaId);
        }
      //将集合中customer的fixedAreaId全部改为传进来的定区id.就完成了将客户关联到这个定区中了
        public void assignCustomer2fixedArea(String fixedAreaId, List<Integer> customerIds) {
           //先要将该定区的客户解绑,再重新将右边下拉框的绑定
             dao.terminateCustomersAccociated2FixedAreaId(fixedAreaId);
             if(fixedAreaId!=null &&customerIds!=null){
             for (Integer customerId : customerIds) {
               dao.assignCustomer2fixedArea(fixedAreaId ,customerId);  
            }
             }       
        }
       //注册保存customer
        public void  regist(Customer customer) {
           dao.save(customer);
        }
       //通过i电话返回一个对象
        public Customer findByTelephone(String telephone) {
          // TODO Auto-generated method stub
          return dao.findByTelephone(telephone);
        }
        //通过电话修改其type的值,表示已经激活
        public void updateCustomer(String telephone) {
          dao.updateCustomer(telephone);
          
        }
        //登录
        public Customer findByTelephoneAndPassword(String telephone, String password) {
          return dao.findByTelephoneAndPassword(telephone,password);
        }
        @Override
        public String findByAddress(String sendAddress) {
          return dao.findByAddress(sendAddress);
        }
}
