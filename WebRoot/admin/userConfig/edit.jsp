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
		if($("#account").val()==""){
			$("#account").tips({
				side:3,
	            msg:'请选择用户.',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#account").focus();
			return false;
		}
		
	if($("#tv_name").val()==""){
			
			$("#tv_name").tips({
				side:3,
	            msg:'请选择数据来源.',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#tv_name").focus();
			return false;
		}
		
		
		if($("#trigger_name").val()==""){
			$("#trigger_name").tips({
				side:3,
	            msg:'请设置数据触发器.',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#trigger_name").focus();
			return false;
		}
		
		if($("#relation_fk_id").val()==""){
			$("#relation_fk_id").tips({
				side:3,
	            msg:'请设置提取数据源中主表ID .',
	            bg:'#AE81FF',
	            time:3
	        });
			
			$("#relation_fk_id").focus();
			return false;
		}
		
		if($("#totablename").val()==""){
			$("#totablename").tips({
				side:3,
	            msg:'请设置目标数据库表名 .',
	            bg:'#AE81FF',
	            time:3
	        });
			
			$("#totablename").focus();
			return false;
		}
		
		$("#userConfigForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
	
	//查询用户
	function searchU(isAttr,inputid,inputname){
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="用户信息列表";
		 diag.URL = '<%=path%>/sdb?opr=cfdUserService&ac=getAccountList';
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
		 var backvalue = $("#backvalue",$(diag.innerFrame.contentWindow.document)).val();
		 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
	 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
 		 {
	 		 var id = $("#"+inputid).val();
	 		 if(isAttr){
	 			 if(id.length>0) id+=",";
	 				$("#"+inputid).val(id+backvalue);
		 		 }else{
			 		$("#"+inputid).val(backvalue);
		 		 }
 		 }
	 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
 		 {
	 		 var name = $("#"+inputname).val();
	 		 if(isAttr){
	 			 if(name.length>0) name+=",";
	 			$("#"+inputname).val(name+backname);
	 		 }else{
		 		$("#"+inputname).val(backname);
	 		 }
 		 }
			diag.close();
		 };
		 diag.show();
	}
	
	
	//查询提取数据列表
	function searchTV(isAttr,inputid,inputname){
		var dbtype = $("#type").val();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="提取数据信息列表";
		 diag.URL = '<%=path%>/sdb?opr=tableAndViewService&ac=getTableAndViewList&type='+dbtype;
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
			 var backvalue = $("#backvalue",$(diag.innerFrame.contentWindow.document)).val();
			 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
			 console.info(backvalue+"--"+backname);
		 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
		 		 var id = $("#"+inputid).val();
		 		 if(isAttr){
		 			 if(id.length>0) id+=",";
		 			$("#"+inputid).val(id+backvalue);
		 		 }else{
			 		$("#"+inputid).val(backvalue);
		 		 }
	 		 }
		 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
		 		 var name = $("#"+inputname).val();
		 		 if(isAttr){
		 			 if(name.length>0) name+=",";
		 			$("#"+inputname).val(name+backname);
		 		 }else{
			 		$("#"+inputname).val(backname);
		 		 }
	 		 }
				diag.close();
		 };
		 diag.show();
	}
	
	
	//查询触发器配置列表
	function searchTrigger(isAttr,inputid,inputname){
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="触发器配置信息列表";
		 diag.URL = '<%=path%>/sdb?opr=triggerManagerService&ac=getTriggerList';
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
			 var backvalue = $("#backvalue",$(diag.innerFrame.contentWindow.document)).val();
			 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
		 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
		 		 var id = $("#"+inputid).val();
		 		 if(isAttr){
		 			 if(id.length>0) id+=",";
		 			$("#"+inputid).val(id+backvalue);
		 		 }else{
			 		$("#"+inputid).val(backvalue);
		 		 }
	 		 }
		 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
		 		 var name = $("#"+inputname).val();
		 		 if(isAttr){
		 			 if(name.length>0) name+=",";
		 			$("#"+inputname).val(name+backname);
		 		 }else{
			 		$("#"+inputname).val(backname);
		 		 }
	 		 }
				diag.close();
		 };
		 diag.show();
	}
	
	
	//选择提取数据列表中对应客户的字段
	function searchTVColumn(inputvalue){
		if($("#tv_name").val()==""){
					
			$("#tv_name").tips({
				side:3,
	            msg:'请选择提取数据来源.',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#tv_name").focus();
			return false;
		}
		var tablename = $("#tv_name").val();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="提取信息字段列表";
		 diag.URL = '<%=path%>/sdb?opr=userConfigService&ac=getColumns&tablename='+tablename;
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
			 var backvalue = $("#backvalue",$(diag.innerFrame.contentWindow.document)).val();
		 console.info(backvalue);
		 	 if(backvalue.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
		 			backvalue= backvalue.substring(0,backvalue.length );
			 		$("#"+inputvalue).val(backvalue);
	 		 }
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
	<form action="<%=basePath%>sdb?opr=userConfigService&ac=edit" name="userConfigForm" id="userConfigForm" method="post">
		<div id="zhongxin">
		<table class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:150px;text-align: right;">用户：</td>
				<td style="white-space: nowrap;">
						<input type="hidden" value="${sEntityConfig.columns.id }" name="id">
						<input type="hidden" id="accountId" name="accountId" value="${sEntityConfig.columns.user_id }">
						<input  autocomplete="off"  type="text" readonly="readonly" name="account" id="account" value="${sEntityConfig.columns.account }" maxlength="32"  placeholder="选择用户信息" title="用户名称" />
						<i id="nav-search-icon" class="icon-search" onclick="searchU(false, 'accountId', 'account');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">提取数据源类型：</td>
				<td style="white-space: nowrap;">
						<select name="type" id="type">
							<option value="table" <c:if test="${sEntityConfig.columns.type =='table' }">selected</c:if>	>表</option>
							<option value="view"  <c:if test="${sEntityConfig.columns.type =='view' }">selected</c:if>   >视图</option>
						</select>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">提取数据源：</td>
				<td style="white-space: nowrap;">
						<input  autocomplete="off"  type="text" readonly="readonly" name="tv_name" id="tv_name" value="${sEntityConfig.columns.table_view_name }" maxlength="32"  placeholder="选择提取数据信息" title="设置 提取数据来源" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTV(false, 'tv_id', 'tv_name');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">数据源主表触发器：</td>
				<td style="white-space: nowrap;">
						<input type="hidden" id="trigger_id" name="trigger_id" value="${sEntityConfig.columns.db_trigger_id }">
						<input  autocomplete="off"  type="text" readonly="readonly" name="trigger_name" id="trigger_name" value="${sEntityConfig.columns.trigger_name }" maxlength="32"  placeholder="选择触发器信息" title="设置关联触发器" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTrigger(false, 'trigger_id', 'trigger_name');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">关联数据源中主表ID：</td>
				<td style="white-space: nowrap;">
						<input  autocomplete="off"  type="text" readonly="readonly" name="relation_fk_id" id="relation_fk_id" value="${sEntityConfig.columns.relation_fk_id }" maxlength="32"  placeholder="选择数据源中主表ID" title="设置数据源中主表ID" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTVColumn('relation_fk_id');" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">目标数据库表名：</td>
				<td style="white-space: nowrap;">
						<input  autocomplete="off"  type="text"  name="totablename" id="totablename" value="${sEntityConfig.columns.totablename }" maxlength="32"  placeholder="目标数据库表名" title="设置目标数据库表名" />
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