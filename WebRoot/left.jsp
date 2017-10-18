<%
	String pathl = request.getContextPath();
	String basePathl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+pathl+"/";
%>
		<!-- 本页面涉及的js函数，都在head.jsp页面中     -->
		<div id="sidebar" ><!-- class="menu-min" 菜单缩放 -->
				<!-- 菜单上四个方块 -->
				<div id="sidebar-shortcuts">
					<div id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>
						<span class="btn btn-info"></span>
						<span class="btn btn-warning"></span>
						<span class="btn btn-danger"></span>
					</div>
				</div>
				<!-- 菜单上四个方块 -->

				<ul class="nav nav-list">

					<li class="active" id="fhindex">
					  <a href="<%=basePathl%>/sdb?opr=cfdLoginService&ac=login4Index"><i class="icon-dashboard"></i><span>后台首页</span></a>
					</li>
					
					
				<c:forEach items="${listAllMenus}" var="menu">
				<li id="lm${menu.key.columns.menu_id }">
						  <a style="cursor:pointer;" class="dropdown-toggle" >
							<i class="${menu.key.columns.menu_icon == null ? 'icon-desktop' : menu.key.columns.menu_icon}"></i>
							<span>${menu.key.columns.menu_name }</span>
							<b class="arrow icon-angle-down"></b>
						  </a>
						  <ul class="submenu">
								<c:forEach items="${menu.value}" var="sub">
									<c:choose>
										<c:when test="${not empty sub.columns.menu_url}">
										<li id="z${sub.columns.menu_id }">
										<a style="cursor:pointer;" target="mainFrame"  onclick="siMenu('z${sub.columns.menu_id }','lm${menu.key.columns.menu_id }','${sub.columns.menu_name }','<%=basePathl%>${sub.columns.menu_url }')"><i class="icon-double-angle-right"></i>${sub.columns.menu_name }</a></li>
										</c:when>
										<c:otherwise>
										<li><a href="javascript:void(0);"><i class="icon-double-angle-right"></i>${sub.columns.menu_name }</a></li>
										</c:otherwise>
									</c:choose>
								</c:forEach>
					  		</ul>
					</li>
				</c:forEach>
				</ul><!--/.nav-list-->

				<div id="sidebar-collapse"><i class="icon-double-angle-left"></i></div>

			</div><!--/#sidebar-->

