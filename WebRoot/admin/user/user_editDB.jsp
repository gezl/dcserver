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
	
	//保存
	function save(){
			$("#userForm").submit();
	}
</script>
<style type="text/css">
td{
	width: 100px;
}
</style>
	</head>
<body>
	<form action="sdb?opr=cfdUserService&ac=editDB" name="userForm" id="userForm" method="post">
		<div id="zhongxin">
		<input type="hidden" name="user_id" id="user_id" value="${cus.columns.id }"/>
		<table class="table table-striped table-bordered table-hover">
			<tr >
				<td style="width:80px;text-align: right;">数据库类型：</td>
				<td style="white-space: nowrap;">
					 	<select class="chzn-select" name="db_type" id="db_type" data-placeholder="请选择数据库类型"  style="vertical-align:top;width: 120px;">
						<option value=""></option>
						<c:forEach items="${DBType}" var="dbType">
							<option value="${dbType.key }" <c:if test="${dbType.key==cus.columns.db_type}">selected</c:if>>${dbType.name }</option>
						</c:forEach>
					  	</select>
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