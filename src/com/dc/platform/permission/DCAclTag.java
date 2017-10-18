package com.dc.platform.permission;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class DCAclTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	/**
	 * 传入进来的值，权限判断需要的条见
	 */
	private int control;
	private int permission;

	public int getControl() {
		return control;
	}

	public void setControl(int control) {
		this.control = control;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	/**
	 * doStartTag方法，如果value为true，那么 就计算tagbody的值，否则不计算body的值。
	 */
	public int doStartTag() throws JspTagException {
		if (PermissionUtil.checkPermission(permission, control)) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * 覆盖doEndTag方法
	 */
	public int doEndTag() throws JspTagException {
		try {
			if (bodyContent != null) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
		} catch (java.io.IOException e) {
			throw new JspTagException("IO Error: " + e.getMessage());
		}
		return EVAL_PAGE;
	}

}
