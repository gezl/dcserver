<%@page import="com.dc.platform.SEntity"%>
<%@page import="com.dc.platform.SEntityList"%>
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
	<div class="row-fluid">
			<div id="zhongxin">
			<input  type="hidden" name="backvalue" id="backvalue"/>
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th>字段</th>
						<th>注释</th>
					</tr>
				</thead>
										
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty sEntityList}">
						<c:forEach items="${sEntityList}" var="item" varStatus="vs">
							<tr>
							<td class='center' style="width: 30px;">
									<a class="btn btn-mini btn-success" title="选择" onclick="searchBack('${item.columns.column_name }')"> <i class="icon-ok"></i>
								</a>
								</td>
								<td>${item.columns.column_name }</td>
								<td>${item.columns.comments }</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				
				</tbody>
			</table>
			</div>
	</div>
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->

<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='<%=basePath %>static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="<%=basePath %>/static/js/bootstrap.min.js"></script>
		<script src="<%=basePath %>/static/js/ace-elements.min.js"></script>
		<script src="<%=basePath %>/static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="<%=basePath %>static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="<%=basePath %>static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="<%=basePath %>static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		
		<script type="text/javascript" src="<%=basePath %>static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		
		$(top.hangge());
		
		function searchBack(name){
			$("#backvalue").val(name);
			 top.Dialog.close();
		}
		
		$(function() {
			//复选框
			$('table th input:checkbox').on('click' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
			});
		});
		
		
		</script>
		
	</body>
</html>

