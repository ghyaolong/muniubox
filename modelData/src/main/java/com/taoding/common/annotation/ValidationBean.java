package com.taoding.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识使用该注解的类需要进行Bean验证，
 * 具体的处理逻辑在 ResponseIntercepter.java类中
 * @author vincent
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidationBean {
}
