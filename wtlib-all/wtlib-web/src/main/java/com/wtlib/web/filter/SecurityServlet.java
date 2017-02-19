package com.wtlib.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * ClassName: SecurityServlet
 * @Description: filter过滤器
 * @author dapengniao
 * @date 2016年7月5日 下午1:39:06
 */
public class SecurityServlet extends HttpServlet implements Filter {
	private static final long serialVersionUID = 1L;
	private FilterConfig filterConfig;
	private List<String> exclusions = new ArrayList<String>();
	private static Logger logger = Logger.getLogger(SecurityServlet.class);

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Origin", "*");
		chain.doFilter(req, res);

	}
	
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		String exclu = filterConfig.getInitParameter("exclusions");
		logger.error(exclu);
		if (null != exclu) {
			String[] exclus = exclu.split(",");
			for (int i = 0; i < exclus.length; i++) {
				exclusions.add(exclus[i]);
			}
		}
	}

}
