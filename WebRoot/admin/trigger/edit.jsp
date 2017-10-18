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
	
	
	
	function save(){
		var primaryTableName = $.trim($("#primaryTableName").val());
		var triggerName = $.trim($("#triggerName").val());
		
		//触发器字段验证
		if(jQuery.trim(primaryTableName).length ==0){
			$("#primaryTableName").tips({
				side:3,
	            msg:'选择数据表',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#primaryTableName").focus();
			$("#primaryTableName").val('');
			$("#primaryTableName").css("background-color","red");
			return false;
		}
		//判断触发器是否符合ORACLE规则
		if(triggerName.length >30){
			 $("#triggerName").tips({
					side:3,
		            msg:'触发器命名已超出Oracle命名规则限制，请联系开发人员!',
		            bg:'#AE81FF',
		            time:5
		        });
			 return false;
		}
		
		//验证是否可以提交
		$.ajax({
			type: "POST",
			url: '<%=path%>/sdb?opr=triggerManagerService&ac=hasTrigger',
			data: {triggerName:triggerName},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("error" == data.result){
					 $("#triggerName").tips({
							side:2,
				            msg:'数据表同步触发器已存在!',
				            bg:'#AE81FF',
				            time:5
				        });
					 	$("#triggerName").focus();
						return false;
				 }else{
					 $("#triggerForm").submit();
					 $("#zhongxin").hide();
					 $("#zhongxin2").show();
				 }
			}
		});
	}
	
	
	//查询提取数据列表
	function searchTV(){
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="提取数据信息列表";
		 diag.URL = '<%=path%>/sdb?opr=tableAndViewService&ac=listAllTable';
		 diag.Width = 1000;
		 diag.Height = 800;
		 diag.CancelEvent = function(){ //关闭事件
			 var backname = $("#backname",$(diag.innerFrame.contentWindow.document)).val();
		 	 if(backname.replace(/(^s*)|(s*$)/g, "").length >0)
	 		 {
			 		$("#primaryTableName").val(backname);
			 		$("#triggerName").val("TRG_"+backname+"_DB");
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
	<form action="<%=basePath%>sdb?opr=triggerManagerService&ac=saveAndCreateTrigger" name="triggerForm" id="triggerForm" method="post">
		<div id="zhongxin">
		<table class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:150px;text-align: right;">数据源：</td>
				<td style="white-space: nowrap;">
						<input  autocomplete="off"  type="text" readonly="readonly" name="primaryTableName" id="primaryTableName" value="" maxlength="32"  placeholder="选择提取数据信息" title="设置 提取数据来源" />
						<i id="nav-search-icon" class="icon-search" onclick="searchTV();" ></i>
				</td>
			</tr>
			<tr>
				<td style="width:150px;text-align: right;">数据源触发器：</td>
				<td style="white-space: nowrap;">
						<input  id="triggerName" name="triggerName" type="text" value="" maxlength="29">
				</td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="3" >
					<a class="btn btn-mini btn-primary" onclick="save();">创建触发器</a>
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