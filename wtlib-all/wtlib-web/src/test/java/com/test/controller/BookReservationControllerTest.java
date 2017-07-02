package com.test.controller;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.unitils.spring.annotation.SpringBean;
import org.unitils.spring.annotation.SpringBeanByType;

import com.wtlib.web.controller.UserCenterController;

public class BookReservationControllerTest{

	@SpringBeanByType
	private AnnotationMethodHandlerAdapter handlerAdaper;
	
	@SpringBean("userCenterController")
	private UserCenterController userCenterController;
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();
    
	@Test
	public void firstTest() throws Exception {
		request.setRequestURI("/login");
		request.setContentType("application/json;charset=UTF-8");
		JSONObject json = new JSONObject();
		json.put("loginId", "admin");
		json.put("password", "cbjcl0204");
		Map<String,String> a = new HashMap<String, String>();
		a.put("loginId", "admin");
		request.addParameters(a);
		ModelAndView view = handlerAdaper.handle(request, response, userCenterController);
		
	}
}
