<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">

<!-- jsp文件头和头部 -->
<%@ include file="top.jsp"%>

</head>
<body>

	<div class="container-fluid" id="main-container">

			<div id="page-content" class="clearfix">

				<div class="page-header position-relative">
					<h1>
						后台首页 <small><i class="icon-double-angle-right"></i> </small>
					</h1>
				</div>
				<!--/page-header-->
				<!-- 检索  上线人数-->
				<form action="sdb?opr=userCountService&ac=findUserCount" method="post" name="userCountForm" id="userCountForm"></form>
				<!-- 检索 上线人数-->
				<div class="row-fluid">
					<div class="row-fluid">
							<table>
								<tr bgcolor="#E0E0E0">
									<td>在线人数：</td>
									<td style="width:39px;" id="onlineCount">${sEntityList.size()}</td>
								</tr>
							</table>
	
							<table id="table_report" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>用户名</th>
										<th class="center" style="width: 100px;">状态</th>
									</tr>
								</thead>
													
								<tbody id="userlist">
									<c:forEach  items="${sEntityList}" var="u" >
										<tr>
											<td>${u.columns.account }</td>
											<td>${u.columns.online_status }</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
					</div>
				</div>


		</div>
		<!-- #main-content -->
	</div>
	<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->

	<script type="text/javascript">
	$(top.hangge());
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
	
	
		$(function(){
			$("#onlineCount").tips({
				side:2,
	            msg:'在线人数更新中',
	            bg:'#AE81FF',
	            time:2
	        });
			//setInterval("updateUserNumber()",8000);
		});
		
		function updateUserNumber(){
			$("#userCountForm").submit();			
		}
	</script>

</body>
</html>
