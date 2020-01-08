package com.scsoft.wlyz.common.tag;

/**
 * @title: BeetlTagName
 * @Description:
 * @Author: zhaopengfei
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @CreateDate: 2019/5/28 11:28
 * @Version: 1.0
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation - Beetl自定义标签命名
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeetlTagName {
    /**
     * 标签名称
     */
    String value() default "";
}
