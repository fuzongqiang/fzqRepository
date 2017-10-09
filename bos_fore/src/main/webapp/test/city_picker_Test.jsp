<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
		<script src="../plugins/bootstrap.min.js" type="text/javascript"></script>

<script src="../plugins/jquery.min.js"></script>
<script src="../plugins/city-picker.js""></script>
<script src="../plugins/city-picker.data.js"></script>
<link rel="/plugins/city-picker.css">
<link rel="stylesheet" type="text/css" href="../plugins/bootstrap.min.css">
</head>
<body>
   <div style="position;relative">
   <input readonly type="text" data-toggle="city-picker" id="city_picker_1"/>
   </div>
    <button id="btn1" name="cityBtn" type="button" onclick="doReset()">重置</button>
    <script type="text/javascript">
      function doReset(){
    	  $("#city_picker_1").data-toggle("reset");
      }
    </script>
</body>
</html>