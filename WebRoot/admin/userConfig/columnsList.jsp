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
			<form action="sdb?opr=userConfigService&ac=editColumnsConfig" method="post" name="formId" id="formId">
			<div id="zhongxin">
				<input type="hidden" name="id" value="${id }">
			<table>
				<tr>
					<td>
						<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				</tr>
			</table>
		<br>
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th>字段www</th>
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
									<label>
									<input type='checkbox'   <c:if test="${item.columns.col}">checked</c:if>  name="column_name"  value="${item.columns.column_name }"  id="column_name" alt="${item.columns.column_name }"/>
									<span class="lbl"></span>
									</label>
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
		</form>
	</div>
 
 <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
 
 
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
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
		
		//检索
		function search(){
			top.jzts();
			$("#formId").submit();
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
		
		
		//保存
		function save(){
			
			$("#formId").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		</script>
		
	</body>
</html>

