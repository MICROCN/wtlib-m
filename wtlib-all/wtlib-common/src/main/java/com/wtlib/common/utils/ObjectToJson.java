package com.wtlib.common.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectToJson {
	private static Logger logger = LoggerFactory.getLogger(ObjectToJson.class);

	public static <T> String listToJson(List<T> object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		}
		return json;
	}

	public static <T> String mapToJson(Map<String, List<T>> object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
		String json = "";
		try {
			for (String key : object.keySet()) {
				json = json + key + ":" + mapper.writeValueAsString(object.get(key)) + "\r";
			}
		} catch (JsonGenerationException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		}
		return json;
	}

	public static <T> String mapToJson2(Map<Integer, T> object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
		String json = "";
		try {
			for (Integer key : object.keySet()) {
				json = json + key + ":" + mapper.writeValueAsString(object.get(key)) + "\r";
			}
		} catch (JsonGenerationException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		}
		return json;
	}

	public static <T> String mapToJson3(Map<String, T> object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
		String json = "";
		try {
			for (String key : object.keySet()) {
				json = json + key + ":" + mapper.writeValueAsString(object.get(key)) + "\r";
			}
		} catch (JsonGenerationException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		}
		return json;
	}

	public static <T> String mapToJson4(Map<Long, T> object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
		String json = "";
		try {
			for (Long key : object.keySet()) {
				json = json + key + ":" + mapper.writeValueAsString(object.get(key)) + "\r";
			}
		} catch (JsonGenerationException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("转换失败 " + ", object=" + object + ", " + e.getMessage(), e);
		}
		return json;
	}
}
