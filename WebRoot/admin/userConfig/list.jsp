<%@page import="com.dc.platform.SEntityList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	//
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


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">


	<div class="row-fluid">
	
			<!-- 检索  -->
			<form action="sdb?opr=userConfigService&ac=listAllUserConfig" method="post" name="userConfigForm" id="userConfigForm">
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
		
		
			<table id="table_report" class="table table-striped table-bordered table-hover">
				
				<thead>
					<tr>
						<th>用户</th>
						<th>提取数据源</th>
						<th>触发器</th>
						<th>状态</th>
						<th class="center">操作</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty sEntityList}">
						<c:forEach items="${sEntityList}" var="item" varStatus="vs">
							<tr>
								<td>${item.columns.account }</td>
								<td>${item.columns.table_view_name }</td>
								<td>${item.columns.trigger_name }</td>
								<td style="width:60px;" class="center">
									<label>
										<input  onclick="enableUConfig('${item.columns.id}','${item.columns.status}')" type="checkbox" class="ace-switch ace-switch-3" id="enable" <c:if test="${item.columns.status == 1 }">checked</c:if> />
										<span class="lbl"></span>
									</label>
								</td>
								<td class="center">
										<a class='btn btn-mini btn-group' title="订阅字段" onclick="editColumnsConfig('${item.columns.id }','${item.columns.table_view_name }');"><i class='icon-cog'></i></a>
										<a class='btn btn-mini btn-info' title="编辑" onclick="toEdit('${item.columns.id }','${item.columns.account }');">编辑<i class='icon-edit'></i></a>
										<a class='btn btn-mini btn-info' title="设置客户关联字段" onclick="toEditUserColumn('${item.columns.id }','${item.columns.table_view_name }');"><i class='icon-cog'>设置客户关联字段</i></a>
										
										<a class='btn btn-mini btn-info'  id="tid_${item.columns.id}" title="生成建表语句" onclick="createTable('${item.columns.id }','${item.columns.db_type }','${item.columns.totablename }','${item.columns.account }');"><i class='icon-cog'>
										<c:if test="${not empty item.columns.tablesql }">
											更新建表语句
										</c:if>
										<c:if test="${empty item.columns.tablesql }">
											生成建表语句
										</c:if>
										</i></a>
										<a class='btn btn-mini btn-info' title="下载"  href="DownLoadServlet?filename=${item.columns.account}_${item.columns.totablename }.sql">下载<i class='icon-edit'></i></a>
								</td>
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
			
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					<a class="btn btn-small btn-success" onclick="addConfig();">新增</a>
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
		</form>
	</div>
 
 
 <div id="msgBox"></div>
 
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
		<script type="text/javascript" src="<%=basePath %>/static/js/jquery.alerts.js"></script>
		
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
			$("#userConfigForm").submit();
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
		function addConfig(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>/admin/userConfig/edit.jsp';
			 diag.Width = 600;
			 diag.Height = 420;
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
		
		
		
		//编辑
		function toEdit(id,account){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL =  'sdb?opr=userConfigService&ac=toEdit&id='+id+'&account='+account ;
			 diag.Width = 600;
			 diag.Height = 420;
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
		
		
		//获取订阅字段
		function editColumnsConfig(id,tablename){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="获取订阅字段";
			 diag.URL = 'sdb?opr=userConfigService&ac=getColumnsConfig&id='+id+'&tablename='+tablename;
			 diag.Width = 600;
			 diag.Height = 800;
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
		
		
		//设客户关联字段
		function toEditUserColumn(id,tablename){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="获取订阅字段";
			 diag.URL = 'sdb?opr=userConfigService&ac=toEditUserColumn&id='+id;
			 diag.Width = 600;
			 diag.Height = 300;
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
		
		function createTable(id,dbType,tableName,account){
			if(dbType==""){
				$("#tid_"+id).tips({
					side:3,
		            msg:'请给用户配置数据库类型.',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#tid_"+id).focus();
				return false;
			}
			if(tableName==""){
				$("#tid_"+id).tips({
					side:3,
		            msg:'请给用户配置所需的表名.',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#tid_"+id).focus();
				return false;
			}
			
			top.jzts();
			var url = '<%=basePath%>sdb?opr=userConfigService&ac=createTable';
			var data={id:id,account:account};
			$.post(url,data,function(result){
				if(result.state=="200"){
					top.hangge();
					jAlert(result.msg,"");
				}
			});
		}
		
		
		//启用/禁用
		function enableUConfig(id,enable){
			top.jzts();
			var url = "sdb?opr=userConfigService&ac=enableUConfig&id="+id+"&enable="+enable;
			$.get(url,function(data){
				nextPage(<%=pageNum%>);
			});
		}
		
		
		$(function() {
			//日期框
			$('.date-picker').datepicker();
			//下拉框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
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

