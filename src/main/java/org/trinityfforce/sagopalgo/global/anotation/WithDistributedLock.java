package org.trinityfforce.sagopalgo.global.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithDistributedLock {

    String lockName();

    long waitTime() default 10;

    long leaseTime() default 60;
}
