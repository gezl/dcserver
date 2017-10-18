<%@page import="com.dc.platform.SEntityList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	SEntityList sEntityList = (SEntityList)request.getAttribute("sEntityList");
	int pageNum = sEntityList.getPageNum();
	int pageSize =sEntityList.getPageSize();
	int totalPage= sEntityList.getTotalPage();
	int totalCount = sEntityList.getTotalCount();
	String type = request.getParameter("type");
	//
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	</head> 
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="zxcus">
			<!-- 检索  -->
			<form action="sdb?opr=tableAndViewService&ac=getTableAndViewList" method="post" name="tvForm" id="tvConfigForm">
			<table>
				<tr>
					<td>
						<span class="input-icon">
							<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${keyword }" placeholder="这里输入关键词" />
							<i id="nav-search-icon" class="icon-search"></i>
						</span>
					</td>
					<td style="vertical-align:top;"><button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button></td>
				</tr>
			</table>
			<!-- 检索  -->
			<!-- 列表 开始-->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<input id="backname" name="backname" type="hidden"/>
				<input id="backvalue" name="backvalue" type="hidden"/>
				<thead>
					<tr>
						<th class="center">
						<label><input type="checkbox" id="zcheckbox" readonly/><span class="lbl"></span></label>
						</th>
						<th>表/视图名称</th>
						<th>数据结构</th>
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
									<a class="btn btn-mini btn-success" title="选择" onclick="searchBack('${item.columns.id}','${item.columns.name }')"> <i class="icon-ok"></i>
								</a>
								</td>
								<td>${item.columns.name }</td>
								<td>${item.columns.type }</td>
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
			<!-- 列表结束 -->
			<!-- 分页-->
		<div class="page-header position-relative">
			<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
				<td style="vertical-align:top;">
					<div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">
						<ul>
							<li><a>共<font color=red><%=totalCount %></font>条</a></li>
							<li><input type="number" value="" id="toGoPage" style="width:50px;text-align:center;float:left" placeholder="页码"/></li>
							<li style="cursor:pointer;"><a onclick="toTZ();"  class="btn btn-mini btn-success">跳转</a></li>
							<li style="cursor:pointer;"><a onclick="nextPage(1)">首页</a></li>
							<li style="cursor:pointer;"><a onclick="nextPage(<%=pageNum-1 %>)">上页</a></li>
							<%
							int showTag = 5;//分页标签显示数量
							int startTag = 1;
							if(pageNum>showTag){
								startTag = pageNum-1;
							}
							int endTag = startTag+showTag-1;
							for(int i=startTag; i<=totalPage && i<=endTag; i++){
								    if(pageNum==i){
						    %>  
						    <li><a href="javaScript:void(0)"><font color='#808080'><%=i%></font></a></li>
						    <%
									}else{
						    %>  
						    <li style="cursor:pointer;"><a onclick="nextPage(<%=i %>)"><%=i %></a></li>
						    <% 
									}
								    }
							%>
							<li style="cursor:pointer;"><a onclick="nextPage(<%=pageNum+1 %>)">下页</a></li>
							<li style="cursor:pointer;"><a onclick="nextPage(<%=totalPage %>)">尾页</a></li>
							<li><a>第<%=pageNum %>页</a></li>
							<li><a>共<%=totalPage %>页</a></li>
							<li>
								<select id="pageSize" name="pageSize" title='显示条数' style="width:55px;float:left;" onchange="changeCount(this.value)">
									<option value='10' <%=pageSize==10?"selected":"" %>>10</option>
								 	<option value='20' <%=pageSize==20?"selected":"" %>>20</option>
									<option value='30' <%=pageSize==30?"selected":"" %>>30</option>
									<option value='40' <%=pageSize==40?"selected":"" %>>40</option>
									<option value='50' <%=pageSize==50?"selected":"" %>>50</option>
									<option value='60' <%=pageSize==60?"selected":"" %>>60</option>
									<option value='70' <%=pageSize==70?"selected":"" %>>70</option>
									<option value='80' <%=pageSize==80?"selected":"" %>>80</option>
									<option value='90' <%=pageSize==90?"selected":"" %>>90</option>
									<option value='99' <%=pageSize==99?"selected":"" %>>99</option>
								</select>
							</li>
						</ul>
					</div>
				</td>
			</tr>
		</table>
		</div>
		</form>
	</div>
  </div>
</div>
</div>
		<%@ include file="/top.jsp"%> 
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
			$("#tvForm").submit();
		}
		
		function nextPage(page){
			 top.jzts();
			 var pageSize = $("#pageSize").val();
			 var url = document.forms[0].getAttribute("action");
			 url+="&pageNum="+page+"&pageSize="+pageSize;
			 document.forms[0].action = url;
			 document.forms[0].submit();
		}
		
		function changeCount(pageSize){
			 top.jzts();
			 var url = document.forms[0].getAttribute("action");
			 url+="&pageSize="+pageSize;
			 document.forms[0].action = url;
			 document.forms[0].submit();
		}
		
		function searchBack(value,name){
			$("#backname").val(name);
			$("#backvalue").val(value);
			 top.Dialog.close();
		}
		
</script>
<script type="text/javascript">
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

