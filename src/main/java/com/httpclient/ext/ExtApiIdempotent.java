package com.httpclient.ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解决接口幂等性问题，支持网络延迟和表单重复提交
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ExtApiIdempotent {
    String type();
}
