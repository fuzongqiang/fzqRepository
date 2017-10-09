<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
<title>地图中心点在北京,3秒钟后平移到广州</title>  
<style type="text/css">  
html{height:100%}  
body{height:100%;margin:0px;padding:0px}  
#container{height:100%}  
</style>  
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=0NupjITs3RFxKTjgg00FMApfKrAs0rlk">
//v2.0版本的引用方式：src="http://api.map.baidu.com/api?v=2.0&ak=您的密钥"
//v1.4版本及以前版本的引用方式：src="http://api.map.baidu.com/api?v=1.4&key=您的密钥&callback=initialize"
</script>
</head> 
<body>
 <div id="allmap"></div>
 <script type="text/javascript">
   
 var map = new BMap.Map("allmap");       
	map.centerAndZoom(new BMap.Point(116.4035,39.915),8); //北京的

 setTimeout(function(){
	map.planTo(113.262232,23.154345);//广州的
}, 3000);
 </script>
</body>
</html>