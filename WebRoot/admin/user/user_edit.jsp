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
		<meta charset="utf-8" />
		<title></title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
		<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
		<!-- 下拉框 -->
		<link rel="stylesheet" href="<%=basePath%>static/css/chosen.css" />
		<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />
		<link rel="stylesheet" href="<%=basePath%>static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="<%=basePath%>static/css/ace-skins.min.css" />
		<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.7.2.js"></script>
		<!--提示框-->
		<script type="text/javascript" src="<%=basePath%>static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	$(top.hangge());
	$(document).ready(function(){
		if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}
	});
	
	//保存
	function save(){
		//用户名验证
		if(jQuery.trim($("#loginname").val()).length ==0){
			$("#loginname").tips({
				side:3,
	            msg:'输入用户名',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#loginname").focus();
			$("#loginname").val('');
			$("#loginname").css("background-color","white");
			return false;
		}else{
			$("#loginname").val(jQuery.trim($('#loginname').val()));
		}
		
		//用户密码验证
		if($("#user_id").val()=="" && $("#password").val()==""){
			$("#password").tips({
				side:3,
	            msg:'输入密码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#password").focus();
			return false;
		}
		
	if($("#password").val()!=$("#chkpwd").val()){
			
			$("#chkpwd").tips({
				side:3,
	            msg:'两次密码不相同',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#chkpwd").focus();
			return false;
		}
		
		
		if($("#user_id").val()==""){
			hasU();
		}else{
			$("#userForm").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
	}
	
	
	//判断用户名是否存在
	function hasU(){
		var loginname = $.trim($("#loginname").val());
		$.ajax({
			type: "POST",
			url: '<%=path%>/sdb?opr=cfdUserService&ac=hasU',
			data: {loginname:loginname},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					$("#userForm").submit();
					$("#zhongxin").hide();
					$("#zhongxin2").show();
				 }else{
					 $("#loginname").tips({
							side:3,
				            msg:loginname+'用户名已存在!',
				            bg:'#AE81FF',
				            time:5
				        });
					$("#loginname").css("border-color","red");
					setTimeout("$('#loginname').val('')",500);
				 }
			}
		});
	}
	
	//查询客户
	function searchCus(isAttr,status,cusId,cusName){
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="客户信息列表";
		 diag.URL = '<%=path%>/sdb?opr=customerService&ac=searchCus&status='+status;
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
		 var backvalue = $("#backvalue",$(diag.innerFrame.contentWindow.document)).val();
		 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
	 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
 		 {
	 		 var id = $("#"+cusId).val();
	 		 if(isAttr){
	 			 if(id.length>0) id+=",";
	 			$("#"+cusId).val(id+backvalue);
	 		 }else{
		 		$("#"+cusId).val(backvalue);
	 		 }
 		 }
	 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
 		 {
	 		 var name = $("#"+cusName).val();
	 		 if(isAttr){
	 			 if(name.length>0) name+=",";
	 			$("#"+cusName).val(name+backname);
	 		 }else{
		 		$("#"+cusName).val(backname);
	 		 }
 		 }
			diag.close();
		 };
		 diag.show();
	}
	
	
	//查询收货地
	function searchSlx(isAttr,cusName){
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="收货地信息列表";
		 diag.URL = '<%=path%>/sdb?opr=customerService&ac=getSPARESTR8List';
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
		 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
	 	 if(backname.replace(/(^s*)|(s*$)/g, "").length >0)
 		 {
	 		 var name = $("#"+cusName).val();
	 		 if(isAttr){
	 			 if(name.length>0) name+=",";
	 			$("#"+cusName).val(name+backname);
	 		 }else{
		 		$("#"+cusName).val(backname);
	 		 }
 		 }
			diag.close();
		 };
		 diag.show();
	}
</script>
	</head>
<body>
	<form action="sdb?opr=cfdUserService&ac=editCus" name="userForm" id="userForm" method="post">
		<div id="zhongxin">
		<input type="hidden" name="id" id="user_id" value="${cus.columns.id }"/>
		<input type="hidden" name="method" id="method" value="${method}">
		<table class="table table-striped table-bordered table-hover">
			<tr>
				<td >用户名：</td>
				<td style="white-space: nowrap;">
					<input  type="text" name="loginname" id="loginname" value="${cus.columns.account }" maxlength="32" placeholder="这里输入用户名" title="用户名" />
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td >密码：</td>
				<td>
				<input type="password" name="password" id="password"  value="${cus.columns.password}" maxlength="32" placeholder="输入密码" title="密码"/>
				<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td >确认密码：</td>
				<td><input type="password" name="chkpwd" id="chkpwd" value="${cus.columns.password }"  maxlength="32" placeholder="确认密码" title="确认密码" /></td>
			</tr>
			<tr>
				<td >联系人：</td>
				<td><input type="text" name="linkman" id="linkman"  value="${cus.columns.linkman }"  maxlength="32" placeholder="这里输入联系人" title="联系人"/></td>
			</tr>
			<tr>
				<td >手机号：</td>
				<td><input type="text" name="phone" id="phone"  value="${cus.columns.phone }"  maxlength="32" placeholder="这里输入手机号" title="手机号"/></td>
			</tr>
			<tr>
				<td >备注：</td>
				<td><input type="text" name="remarks" id="remarks"value="${cus.columns.remarks }" placeholder="这里输入备注" maxlength="64" title="备注"/></td>
			</tr>
			<tr>
				<td >货主：</td>
				<td style="white-space: nowrap;">
						<input type="hidden" id="hz_id" name="hz_id" value="${cus.columns.hz_id }">
						<input  style="width: 1000px;" autocomplete="off"  type="text" readonly="readonly" name="hz_name" id="hz_name" value="${cus.columns.hz_name }" maxlength="32"  placeholder="选择客户信息" title="客户名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchCus(true,'hz_status', 'hz_id', 'hz_name');" ></i>
				</td>
			</tr>
			<tr>
				<td >货代：</td>
				<td style="white-space: nowrap;">
						<input type="hidden" id="hd_id" name="hd_id" value="${cus.columns.hd_id }">
						<input  style="width: 1000px;" autocomplete="off"  type="text" readonly="readonly" name="hd_name" id="hd_name" value="${cus.columns.hd_name }" maxlength="32"  placeholder="选择客户信息" title="客户名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchCus(true,'hd_status', 'hd_id', 'hd_name');" ></i>
				</td>
			</tr>
			<tr>
				<td >流向：</td>
				<td style="white-space: nowrap;">
						<input type="hidden" id="lx_id" name="lx_id" value="${cus.columns.lx_id }">
						<input style="width: 1000px;"  autocomplete="off"  type="text" readonly="readonly" name="lx_name" id="lx_name" value="${cus.columns.lx_name }" maxlength="32"  placeholder="选择客户信息" title="客户名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchCus(true, 'lx_status','lx_id', 'lx_name');" ></i>
				</td>
			</tr>
			<tr>
				<td >实际收货地：</td>
				<td style="white-space: nowrap;">
						<input style="width: 1000px;"  autocomplete="off"  type="text" readonly="readonly" name="s_name" id="s_name" value="${cus.columns.small_lx }" maxlength="32"  placeholder="选择客户信息" title="客户名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchSlx(true, 's_name');" ></i>
				</td>
			</tr>
			 
			<tr>
				<td style="text-align: center;" colspan="3" >
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
		
	</form>
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='<%=basePath%>static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="<%=basePath%>static/js/bootstrap.min.js"></script>
		<script src="<%=basePath%>static/js/ace-elements.min.js"></script>
		<script src="<%=basePath%>static/js/ace.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="<%=basePath %>static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$(".date-picker").datepicker();
			
		});
		
		</script>
	
</body>
</html>