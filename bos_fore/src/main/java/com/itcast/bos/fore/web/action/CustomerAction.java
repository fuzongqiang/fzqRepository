package com.itcast.bos.fore.web.action;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.itcast.bos.fore.utils.MailUtils;
import com.itcast.bos.fore.utils.SmsUtils;
import com.itcast.bos.fore.utils.service.Customer;
import com.itcast.bos.fore.utils.service.CustomerService;
import com.lowagie.text.pdf.TSAClientBouncyCastle;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.jasperreports.olap.mapping.MappingMetadata;
import net.sf.jasperreports.web.actions.SaveAction;

/**
*@author Fuzongqiang
*@version Date:2017年9月23日下午1:19:21
*说明:****
*/
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
  
  private static final long serialVersionUID = 1L;
    private Customer customer = new Customer();
    String validatCode=null;
    public Customer getCustomer() {
      return customer;
    }
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RedisTemplate<String,String > redisTemplate;//注入redisTemplate
    @Autowired
    private JmsTemplate jmsTemplate;
  public Customer getModel() {
    return customer;
  }
//通过ajax访问吉信通短信平台发送信息到客户手机
  @Action(value="customerAction_sendValidateCode")
  public String sendValidateCode() throws IOException{
    String num = RandomStringUtils.randomNumeric(4);
    System.out.println(num);
    //把随机生成的验证码存到session中去
    ServletActionContext.getRequest().getSession().setAttribute("code", num);
   // String result = SmsUtils.sendSmsByWebService(customer.getTelephone(), "尊敬的客户你好，您本次获取的验证码为："+num);
      
    /*String  flag ="";
    if(StringUtils.isNotEmpty(result)&&result.length()==16){
       flag="ok";
     }else{
       flag="error";
     }*/
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("application/json;charset=utf-8");
   // response.getWriter().write(flag);
    return NONE ;
  }
  
  private String checkcode;
  private  String radio1;
   
  public void setCheckcode(String checkcode) {
    this.checkcode = checkcode;
  }
  public void setRadio1(String radio1) {
    this.radio1 = radio1;
  }
  //注册
  @Action(value="customerAction_regist",results={
  @Result(name="success",location="signup-success.html",type="redirect")
  ,@Result(name="error",location="signup-fail.html",type="redirect")})
  public String regist(){
    System.out.println("走了注册的方法 ");
    String  oldCode = (String) ServletActionContext.getRequest().getSession().getAttribute("code");//从session中取生成时候存的验证码
     if(StringUtils.isNotEmpty(oldCode)&&oldCode.equals(checkcode)&&radio1.equals("yes")){
       customerService.regist(customer);
       
       validatCode= RandomStringUtils.randomNumeric(36);//生成随机的验证码
       //将validateCode存到redis数据库中去,保存24小时
       saveValidateCode(validatCode,customer.getTelephone());
       
      //用activemq重构,发送消息
       jmsTemplate.send("msg_message",new MessageCreator() {
         String emailBody="感谢您注册速运快递 ,请您在24小时内点击下面的链接激活您的帐号"
         + "<br/><a href='http://localhost:8280/bos_fore/customerAction_active.action?telephone="+customer.getTelephone()+"&returnCode="+validatCode+"'>点击这里激活</a>";

        @Override
        public Message createMessage(Session session) throws JMSException {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("email", customer.getEmail());
            mapMessage.setString("emailBody",emailBody);
          return mapMessage;
        }
      });
     //以下是发送激活邮件
     //  MailUtils.sendMail(customer.getEmail(), "这是一封注册激活邮件(请勿回复)",emailBody );
       return SUCCESS;
     }else{
       return ERROR;
     }
  }
 
  //用户邮件激活(将customer的type改为1表示激活状态)
  private String returnCode;
  
  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
  }
  @Action(value="customerAction_active",results={
     @Result(name="success",location="signup-success.html",type="redirect")
   ,@Result(name="error",location="loadfail.html",type="redirect"),
   @Result(name="actived",location="index.html",type="redirect")})
  public String active(){
    System.out.println("走了激活验证的active方法");
    String codeFormRedis =  getValidateCodefromRedis(customer.getTelephone());
    System.out.println(codeFormRedis);
    System.out.println(returnCode);
    System.out.println(customer.getTelephone());
    if(StringUtils.isNotEmpty(returnCode)&&StringUtils.isNotEmpty(codeFormRedis)&&returnCode.equals(codeFormRedis)){
     Customer customer2 = customerService.findByTelephone(customer.getTelephone());
     //如果用户是已经激活的就去主页吧,别再重复了
     if(customer2.getType()!=null&&customer2.getType()==1){
       return "actived";
     }
     //将其type该为1
     customerService.updateCustomer(customer.getTelephone());
    }else{
      return ERROR;
    }
    return SUCCESS;
  }
  
  //存到redis的方法存放24 小时
  private void saveValidateCode(String validatCode ,String telephone) {
    redisTemplate.opsForValue().set(telephone, validatCode,24,TimeUnit.HOURS);
  }
  //从redis中根据key取邮件返回来的验证码
  private String getValidateCodefromRedis(String telephone){
    return redisTemplate.opsForValue().get(telephone);
  };
  
  
  
  //通过ajax异步请求检查用户名是否存在
  @Action(value="customerAction_checkTelephone")
  public String checkTelephone() throws IOException{
    System.out.println("走了ajax验证电话号码的方法"+customer.getTelephone());
 Customer customer2 = customerService.findByTelephone(customer.getTelephone());
 String b=null;
    if(customer2!=null&&customer2.getType()==1){
      b="ok";
    }else{ b="error";
    }
   HttpServletResponse response = ServletActionContext.getResponse();
   response.setContentType("application/text;charset=utf-8");
   response.getWriter().write(b);
    return NONE;
  }
  
  
  //登录(调用customerService的findBytelephoneAndPassword())
  @Action(value="customerAction_login",results={
   @Result(name="success",location="index.html",type="redirect")
   ,@Result(name="login",location="login.html",type="redirect")})
  public String login(){
     //先从session中去取生成时候的验证码
  String codeFromSession = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
   //只有先通过验证码比较正确后
  if(StringUtils.isNotEmpty(checkcode)&&checkcode.equals(codeFromSession)){ 
    Customer customer2 =  customerService.findByTelephoneAndPassword(customer.getTelephone(),customer.getPassword());
    if(customer2!=null){
   ServletActionContext.getRequest().getSession().setAttribute("customer", customer2);
    return SUCCESS;
    }else{
      return LOGIN;
    }
  }
    return LOGIN ;
  }
}
