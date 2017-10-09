<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 开始学习百度地图API最简单的方式是看一个简单的示例。以下代码创建了一个地图并以天安门作为地图的中心：
 -->
 <!DOCTYPE html>  
<html>  
<head>  
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
<title>Hello, World</title>  
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
<div id="container"></div> 
<script type="text/javascript"> 
var map = new BMap.Map("container");          // 创建地图实例  

//var point = new BMap.Point(116.404, 39.915,15);  // 创建点坐标  
//map.centerAndZoom(point,15);                     //用城市名字来创建地图
map.centerAndZoom("武汉", 15);                 // 初始化地图，设置中心点坐标和地图级别  
map.enableScrollWheelZoom(true);//支持滚轮缩放
</script>  
</body>  
</html>
