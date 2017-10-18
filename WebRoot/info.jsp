<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
  <head>
  <meta charset="utf-8" />
  <base href="<%=basePath%>">
<title>应用程序暂未开放 (UFO)</title> 

<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
    <style type="text/css"> 
        body { background-color: #fff; color: #666; text-align: center; font-family: arial, sans-serif; }
        div.dialog {
            width: 80%;
            padding: 1em 4em;
            margin: 4em auto 0 auto;
            border: 1px solid #ccc;
            border-right-color: #999;
            border-bottom-color: #999;
        }
        h1 { font-size: 100%; color: #f00; line-height: 1.5em; }
    </style> 
</head> 
 
<body> 
  <div class="dialog"> 
    <h1>应用程序暂未开放</h1> 
    <p>抱歉！您访问的功能正在完善，请耐心等待。</p> 
    <div style="text-align: center;" id="info">
    	<iframe frameborder="no" border="0" marginwidth="0" marginheight="0" width=520 height=86 src="//music.163.com/outchain/player?type=2&id=422104315&auto=0&height=66"></iframe>
    </div>
  </div>
  
  <script type="text/javascript">
  $(top.hangge());
  </script>
</body> 
</html>
