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
		$("#userConfigForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
		
	//选择提取数据列表中对应客户的字段
	function searchTVColumn(inputvalue){
		var table_name = $("#table_name").val();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="提取信息字段列表";
		 diag.URL = 'sdb?opr=userConfigService&ac=getColumns&tablename='+table_name;
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
			var backvalue = $("#backvalue",$(diag.innerFrame.contentWindow.document)).val();
	 		$("#"+inputvalue).val(backvalue);
			diag.close();
		 };
		 diag.show();
	}
</script>
<style type="text/css">
td{
	width: 100px;
}
</style>
	</head>
<body>
	<form action="sdb?opr=userConfigService&ac=editUserColumn" name="userConfigForm" id="userConfigForm" method="post">
		<div id="zhongxin">
		<input type="hidden" id="id" name="id"  value="${sEntity.columns.id }">
		<input type="hidden" id="table_name" name="table_name"  value="${sEntity.columns.table_view_name }">
		<table class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:150px;text-align: right;">货主关联字段：</td>
				<td style="white-space: nowrap;">
						<input style="width: 400px"  autocomplete="off"  type="text" readonly="readonly" name="hz_column" id="hz_column" value="${sEntity.columns.hz_column }" maxlength="32"  placeholder="选择触发器信息" title="触发器名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTVColumn('hz_column');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">货代关联字段：</td>
				<td style="white-space: nowrap;">
						<input style="width: 400px"  autocomplete="off"  type="text" readonly="readonly" name="hd_column" id="hd_column" value="${sEntity.columns.hd_column }" maxlength="32"  placeholder="选择触发器信息" title="触发器名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTVColumn('hd_column');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">流向关联字段：</td>
				<td style="white-space: nowrap;">
						<input style="width: 400px"  autocomplete="off"  type="text" readonly="readonly" name="lx_column" id="lx_column" value="${sEntity.columns.lx_column }" maxlength="32"  placeholder="选择触发器信息" title="触发器名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTVColumn('lx_column');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">具体收货地关联字段：</td>
				<td style="white-space: nowrap;">
						<input style="width: 400px"  autocomplete="off"  type="text" readonly="readonly" name="small_lx" id="small_lx" value="${sEntity.columns.small_lx }" maxlength="32"  placeholder="选择触发器信息" title="触发器名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTVColumn('small_lx');" ></i>
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
			//下拉框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			//日期框
			$(".date-picker").datepicker();
			
		});
		
		</script>
	
</body>
</html>