package com.xact.assessment.annotations;

import io.micronaut.aop.Around;
import io.micronaut.aop.Introduction;
import io.micronaut.context.annotation.Type;
import io.micronaut.core.annotation.Internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Introduction
@Target({TYPE,METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Internal
@Around
@Type(ContributorAuthInterceptor.class)

public @interface ContributorAuth {
}
