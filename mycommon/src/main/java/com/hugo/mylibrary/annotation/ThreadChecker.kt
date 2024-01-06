package com.hugo.mylibrary.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ThreadChecker(val threadName: String)
