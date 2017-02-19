
package com.wtlib.base.start;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.wtlib.base.constants.GlobalConstants;


/**
 * ClassName: InterfaceUrlInti
 * @Description: 項目啓動配置文件初始化
 * @author dapengniao
 * @date 2016年6月24日 下午4:02:48
 */
public class InterfaceUrlInti {

	public synchronized static void init(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		if(GlobalConstants.interfaceUrlProperties==null){
			GlobalConstants.interfaceUrlProperties = new Properties();
		}
		InputStream in = null;
		try {
			in = cl.getResourceAsStream("init_test.properties");
			props.load(in);
			for(Object key : props.keySet()){
				GlobalConstants.interfaceUrlProperties.put(key, props.get(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

}
