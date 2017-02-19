package com.wtlib.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import com.wtlib.base.constants.CommonConstant;
import com.wtlib.web.controller.BaseController;
import com.wtlib.base.pojo.User;

public class ForumFilter implements Filter {

	private static final String FILTERD_REQUEST = "@@session_context_filtered_request";

	private static final String[] INHERENT_EACAPE_URIS = { "/index.htm",
			"index.htm", "/login.htm", "/login/doLogin.htm", "/register.htm" };

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request != null && request.getAttribute(FILTERD_REQUEST) != null) {
			chain.doFilter(request, response);
		} else {
			request.setAttribute(FILTERD_REQUEST, Boolean.TRUE);
			HttpServletRequest httpSerletRequest = (HttpServletRequest) request;
			User userContext = BaseController.getSessionUser(httpSerletRequest);
			if (userContext == null
					&& !isURILogin(httpSerletRequest.getRequestURI(),
							httpSerletRequest)) {
				String toUrl = httpSerletRequest.getRequestURL().toString();
				if (!StringUtils.isEmpty(httpSerletRequest.getQueryString())) {
					toUrl += "?" + httpSerletRequest.getQueryString();
				}
				httpSerletRequest.getSession().setAttribute(
						CommonConstant.LOGIN_TO_URL, toUrl);
				
				request.getRequestDispatcher("/login.html").forward(request, response);
				return;
			}
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

	private boolean isURILogin(String requestURI, HttpServletRequest request) {
		if (request.getContextPath().equalsIgnoreCase(requestURI)
				|| (request.getContextPath() + "/")
						.equalsIgnoreCase(requestURI)) {
			return true;
		} else {
			for (String uri : INHERENT_EACAPE_URIS) {
				if (requestURI != null && requestURI.indexOf(uri) >= 0) {
					return true;
				}
			}
		}
		return false;
	}

}
