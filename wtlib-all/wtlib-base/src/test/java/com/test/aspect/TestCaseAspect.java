package com.test.aspect;

import net.bytebuddy.asm.Advice.This;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.alibaba.fastjson.JSON;

@Aspect
public class TestCaseAspect {
	private static final Log logger = LogFactory.getLog(This.class);

	/**
	 * 添加切入点
	 */
	@Pointcut("execution(* com.wtlib.service.*.*.*(..))")
	public void aspect() {

	}

	/**
	 * 配置前置通知
	 */
	@Before("aspect()")
	public void before(JoinPoint jp) {
		// 获取方法名
		System.out.println("[before]"
				+ jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName() + "\n\tparams:"
				+ JSON.toJSONString(jp.getArgs()));
		logger.info("[before]" + jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName() + "\n\tparams:"
				+ JSON.toJSONString(jp.getArgs()));
	}

	/**
	 * @Description: 配置返回值
	 * @param @param jp
	 * @param @param returnValue
	 * @author zongzi
	 * @date 2016年8月22日 上午11:51:53
	 */
	@AfterReturning(pointcut = "aspect()", returning = "returnValue")
	public void afterReturn(JoinPoint jp, Object returnValue) {
		System.out.println("[AfterReturning]"
				+ jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName() + "\n\treturn="
				+ JSON.toJSONString(returnValue) + "\n\tparams="
				+ JSON.toJSONString(jp.getArgs()));
		logger.info("[AfterReturning]"
				+ jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName() + "\n\treturn="
				+ JSON.toJSONString(returnValue) + "\n\tparams="
				+ JSON.toJSONString(jp.getArgs()));
	}

	@After("aspect()")
	public void after(JoinPoint jp) {
		System.out.println("[AfterReturning]"
				+ jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName() + "\n\tparams="
				+ JSON.toJSONString(jp.getArgs()));
		logger.info("[AfterReturning]"
				+ jp.getSignature().getDeclaringTypeName() + "."
				+ jp.getSignature().getName() + "\n\tparams="
				+ JSON.toJSONString(jp.getArgs()));
	}

	/**
	 * @Description: 配置异常输出
	 * @param @param jp
	 * @param @param ex
	 * @author zongzi
	 * @date 2016年8月22日 下午4:29:48
	 */
	@AfterThrowing(pointcut = "aspect()", throwing = "ex")
	public void afterThrow(JoinPoint jp, Exception ex) {
		if (logger.isErrorEnabled()) {

			System.out.println("[ThrowException]"
					+ jp.getSignature().getDeclaringTypeName() + "."
					+ jp.getSignature().getName() + "\n\tparams="
					+ JSON.toJSONString(jp.getArgs()));
			logger.error(
					"[ThrowException]"
							+ jp.getSignature().getDeclaringTypeName() + "."
							+ jp.getSignature().getName() + "\n\tparams="
							+ JSON.toJSONString(jp.getArgs()) + "\n\t"
							+ ex.toString(), ex);
		} else if (logger.isInfoEnabled()) {
			logger.info(
					"[ThrowException:error]"
							+ jp.getSignature().getDeclaringTypeName() + "."
							+ jp.getSignature().getName() + "\n\tparams="
							+ JSON.toJSONString(jp.getArgs()) + "\n\t"
							+ ex.toString(), ex);
		}
	}
}
