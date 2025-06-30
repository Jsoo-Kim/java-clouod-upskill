package com.sparta.java_02.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // Loggable이라는 어노테이션은 메소드에만 붙일 수 있다
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

}
