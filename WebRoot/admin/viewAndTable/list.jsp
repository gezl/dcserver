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

		
<div id="page-content">
						
  <div class="row-fluid">

	<div class="row-fluid">
	
	<!-- 检索  -->
			<form action="sdb?opr=tableAndViewService&ac=listAllTableConfig&type=${type }" method="post" name="userForm" id="userForm">
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
			</form>
	
	<div>
	<div id="breadcrumbs">
		<table class="center" style="width:100%;">
			<tr height="35">
						<td style="width:100px;" class="center" <c:choose><c:when test='${type eq "table"}'>bgcolor="#FFC926" onMouseOut="javascript:this.bgColor='#FFC926';"</c:when><c:otherwise>bgcolor="#E5E5E5" onMouseOut="javascript:this.bgColor='#E5E5E5';"</c:otherwise></c:choose> onMouseMove="javascript:this.bgColor='#FFC926';" >
							<a href="<%=basePath%>/sdb?opr=tableAndViewService&ac=listAllTableConfig&type=table" style="text-decoration:none; display:block;" >
								<li class=" icon-group"></li>&nbsp;
								<font color="#666666">数据表</font>
							</a>
						</td>
						<td style="width:5px;"></td>
						<td style="width:100px;" class="center"  <c:choose><c:when test='${type eq "view"}'>bgcolor="#FFC926" onMouseOut="javascript:this.bgColor='#FFC926';"</c:when><c:otherwise>bgcolor="#E5E5E5" onMouseOut="javascript:this.bgColor='#E5E5E5';"</c:otherwise></c:choose> onMouseMove="javascript:this.bgColor='#FFC926';">
							<a href="<%=basePath%>/sdb?opr=tableAndViewService&ac=listAllTableConfig&type=view" style="text-decoration:none; display:block;" >
								<li class=" icon-group"></li>&nbsp;
								<font color="#666666">视图</font>
							</a>
						</td>
						<td></td>
			</tr>
		</table>
	</div>	
		
		
	</div>
	<table id="table_report" class="table table-striped table-bordered table-hover">
		<thead>
		<tr>
			<th class="center">名称</th>
			<th class="center">类型</th>
			<th class="center">注释</th>
			<th class="center">开始使用时间</th><%--
			<th style="width:155px;"  class="center">操作</th>
		--%></tr>
		</thead>
		<c:choose>
			<c:when test="${not empty sEntityList}">
				<c:forEach items="${sEntityList}" var="var" varStatus="vs">
				<tr>
					<td class="center">${var.columns.name }</td>
					<td class="center">${var.columns.type }</td>
					<td class="center">${var.columns.comments }</td>
					<td class="center"><fmt:formatDate value="${var.columns.create_time }" type="both" pattern="yyyy-MM-dd "/></td><%--
					<td class="center">
						<a class='btn btn-mini btn-group' title="预设功能" onclick="javascript:alert('预留功能,待开发');"><i class='icon-cog'></i></a>
					</td>
				--%></tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
				<td colspan="100" class="center" >没有相关数据</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
	
	<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					<c:choose>
						<c:when test='${type eq "view"}'>
							<a class="btn btn-small btn-success" onclick="javascript:alert('预留功能,待开发');">创建视图</a>
						</c:when>
					</c:choose>
						<a class="btn btn-small btn-success" onclick="add('${type}')">新增</a></td>
				<td style="vertical-align:top;">
					<div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">
						<ul>
							<li><a>共<font color=red><%=totalCount %></font>条</a></li>
							<li><input type="number" value="" id="toGoPage" style="width:50px;text-align:center;float:left" placeholder="页码"/></li>
							<li style="cursor:pointer;"><a onclick="toTZ();"  class="btn btn-mini btn-success">跳转</a></li>
							<li style="cursor:pointer;"><a onclick="nextPage(1)">首页</a></li>
							<li style="cursor:pointer;"><a onclick="nextPage(<%=pageNum-1 %>)">上页</a></li>
							<%
								for(int i =1; i<= totalPage;i++){
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
		<script src="static/1.9.1/jquery.min.js"></script>
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		<script type="text/javascript">
		top.hangge();
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
		
		//新增
		function add(name){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>admin/viewAndTable/edit.jsp?type='+name;
			 diag.Width = 370;
			 diag.Height = 235;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if(<%=pageNum%> == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(1);
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		</script>
		
	</body>
</html>

