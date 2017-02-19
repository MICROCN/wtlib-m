package com.wtlib.base.start;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * ClassName: InterfaceUrlIntiServlet
 * @Description: 項目文件初始化
 * @author dapengniao
 * @date 2016年6月24日 下午4:03:34
 */
public class InterfaceUrlIntiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Override
	public void init(ServletConfig config) throws ServletException {
		InterfaceUrlInti.init();
	}

}
