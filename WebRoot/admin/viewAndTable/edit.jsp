<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String type = request.getParameter("type");
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
		
<style type="text/css">
td{
	width: 100px;
}
</style>
	</head>
<body>
	<form action="sdb?opr=tableAndViewService&ac=editTableConfig" name="tvForm" id="tvForm" method="post">
		<div id="zhongxin">
		<input type="hidden" name="type" id="type" value="${cus.columns.id }"/>
		<table class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:80px;text-align: right;">表/视图名称：</td>
				<td style="white-space: nowrap;">
						<input  autocomplete="off"  type="text" readonly="readonly" name="table_name" id="table_name" value="" maxlength="32"  placeholder="选择表/视图信息" title="表/视图名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTV('<%=type %>');" ></i>
				</td>
			</tr>
			<tr>
					<td style="width:80px;text-align: right;"> 类型：</td>
					<td style="white-space: nowrap;">
						<input  autocomplete="off"  type="text" readonly="readonly" name="table_type" id="table_type" value="" maxlength="32"  placeholder="类型" title="表/视图类型" />
					 </td>
			</tr>
			<tr>
					<td style="width:80px;text-align: right;"> 注释：</td>
					<td style="white-space: nowrap;">
					<input  autocomplete="off"  type="text" readonly="readonly" name="comments" id="comments" value="" maxlength="32"  placeholder="注释" title="表/视图注释" />
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
		$(top.hangge());
		
		//保存
		function save(){
			var tableName = $.trim($("#table_name").val());
			//用户名验证
			if(tableName.length ==0){
				$("#table_name").tips({
					side:3,
		            msg:'选择表/视图信息.',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#table_name").focus();
				$("#table_name").css("background-color","white");
				return false;
			}
			$.ajax({
				type: "POST",
				url: '<%=path%>/sdb?opr=tableAndViewService&ac=hasTV',
				data: {tableName:tableName},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" == data.result){
						$("#tvForm").submit();
						$("#zhongxin").hide();
						$("#zhongxin2").show();
					 }else{
						 $("#table_name").tips({
								side:3,
					            msg:tableName+'已存在!',
					            bg:'#AE81FF',
					            time:5
					        });
						$("#table_name").css("border-color","red");
					 }
				}
			});
		}
		//查询数据表视图信息 
		function searchTV(type){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="信息列表";
			 diag.URL = '<%=path%>/sdb?opr=tableAndViewService&ac=listAllTable&type='+type;
			 diag.Width = 1000;
			 diag.Height = 800;
			 diag.CancelEvent = function(){ //关闭事件
			 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
			 var backtype = $("#backtype",$(diag.innerFrame.contentWindow.document)).val();
			 var comments = $("#comments",$(diag.innerFrame.contentWindow.document)).val();
		 	 if(backname.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
		 		$("#table_name").val(backname);
		 		$("#table_type").val(backtype);
		 		$("#comments").val(comments);
	 		 }
				diag.close();
			 };
			 diag.show();
		}
		
		</script>
	
</body>
</html>