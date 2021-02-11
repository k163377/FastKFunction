package com.mapk.annotations

/**
 * 高速呼び出しのコード生成対象
 * 将来的にはContextとしてKClassを持たせ、返却される型が同じでも区別できるようにしたい
 */
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FastFunction
