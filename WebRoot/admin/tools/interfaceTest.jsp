<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- jsp文件头和头部 -->
	<%@ include file="/top.jsp"%> 
	</head> 
<body>
		
<div class="container-fluid" id="main-container">



<div id="page-content" class="clearfix">
						
  <div class="row-fluid">


 	<div class="span12">
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat wi1dget-header-large">
				<h4 class="lighter">服务器接口测试</h4>
			</div>
			<div class="widget-body">
			 
			 
			 <div class="widget-main">
			 
			 
			 
					<div class="step-content row-fluid position-relative">
						<label style="float:left;padding-left: 5px;"><input name="form-field-radio1" id="form-field-radio1" onclick="setType('POST');" type="radio" value="icon-edit" checked="checked"><span class="lbl">POST</span></label>
						<label style="float:left;padding-left: 15px;"><input name="form-field-radio1" id="form-field-radio2" onclick="setType('GET');" type="radio" value="icon-edit"><span class="lbl">GET</span></label>
					
						<label style="float:left;padding-left: 35px;"><input name="key" id="form-field-radio1" type="radio" value="icon-edit" checked="checked" ><span class="lbl">加密参数:</span></label>
						<label style="float:left;padding-left: 5px;margin-top: -5px;"><input name="S_TYPE_S" id="S_TYPE_S" style="width:79px;" type="text" value="USERNAME" title="例子"></label>
					
						
					</div>
					<div class="step-content row-fluid position-relative">
						<div style="float: left;">
							<span class="input-icon">
								<input type="text" id="serverUrl" title="输入请求地址" value="<%=basePath%>/appuser/getAppuserByUm.do?USERNAME=dsfsd" style="width:540px;">
								<i class="icon-globe"></i>
							</span>
						</div>
						<div style="margin-top: -5px;">
							&nbsp;&nbsp;<a class="btn btn-small btn-success" onclick="sendSever();">请求</a>
							&nbsp;&nbsp;<a class="btn btn-small btn-info" onclick="gReload();">重置</a>
						</div>
					</div>
					<div >
						<a class="btn btn-small btn-success" id="paramBut" onclick="createNode();">参数</a>
					</div>
					<div id="paramKV" class="step-content row-fluid position-relative">
					</div>
					<br> <br> <br>
					<div class="step-content row-fluid position-relative">
					<textarea id="json-field" title="返回结果" class="autosize-transition span12" style="width:690px;"></textarea>
				 	</div>
				 	
				 	<div class="step-content row-fluid position-relative">
					<label style="float:left;padding-left: 35px;">服务器响应：<font color="red" id="stime">-</font>&nbsp;毫秒</label>
					<label style="float:left;padding-left: 35px;">客户端请求：<font color="red" id="ctime">-</font>&nbsp;毫秒</label>
					</div>
					
					<div class="step-content row-fluid position-relative">
						<ul class="unstyled spaced2">
							<li class="text-warning orange"><i class="icon-warning-sign"></i>
								相关参数协议：errCode: 0	请求失败 ; 1	请求成功; msg:信息描述
							</li>
							<li class="text-warning green"><i class="icon-star blue"></i>
								校验加密规则：(密码+混淆码",incoshare," 然后md5加密，密文采用Base64算法，将加密后的字节变成字符串 的值作为 参数pwd的值提交)
							</li>
						</ul>
					</div>
					
				 
				 <input type="hidden" name="S_TYPE" id="S_TYPE" value="POST"/>
				 
			 </div><!--/widget-main-->
			</div><!--/widget-body-->
		</div>
	</div>
 
 
 
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<!-- 引入 -->
		<!--MD5-->
		<script type="text/javascript" src="static/js/jQuery.md5.js"></script>
		<!--提示框-->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<!--引入属于此页面的js -->
		<script type="text/javascript" src="static/js/myjs/interfaceTest.js"></script>
		
		<script type="text/javascript">
		
			function createNode() {
				  var paramBut = document.getElementById("paramKV");  //获取组件
				  var elemeDiv= document.createElement("div"); 
				  var eleme= document.createElement("br"); 
				  var eleme1 = document.createElement("input");   //创建组件
				  eleme1.setAttribute("placeholder", "参数-key");	//设置组件属性
				  var eleme2 = document.createElement("input");   //创建组件
				  eleme2.setAttribute("placeholder", "值-value");		//设置组件属性
				  var eleme3 = document.createElement("a");   //创建组件
				  var elemeRmoveText = document.createTextNode("删除");
				  eleme3.appendChild(elemeRmoveText);
				  eleme3.setAttribute("class", "btn btn-small btn-success");
				  eleme3.setAttribute("onclick", "javascript:$(this).parent('div').remove();");
				  //加入组件
				  elemeDiv.appendChild(eleme); 
				  elemeDiv.appendChild(eleme1); 
				  elemeDiv.appendChild(eleme2);  
				  elemeDiv.appendChild(eleme3);  
				  paramBut.appendChild(elemeDiv);
				}
		</script>
	</body>
</html>

