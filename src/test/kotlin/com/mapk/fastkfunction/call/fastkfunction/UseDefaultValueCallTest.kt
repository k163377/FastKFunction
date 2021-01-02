package com.mapk.fastkfunction.call.fastkfunction

import com.mapk.fastkfunction.FastKFunction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor

enum class DefaultValues {
    Constructor,
    InnerConstructor,
    InstanceFunc,
    InstanceFuncWithInstance,
    CompanionObjectFunc,
    CompanionObjectFuncWithInstance,
    CompanionObjectFuncFromReflection,
    CompanionObjectFuncFromReflectionWithInstance,
    TopLevelFunc,
    TopLevelExtensionFunc,
    TopLevelExtensionFuncFromInstance,
    TopLevelExtensionFuncFromInstanceWithInstance
}

private fun topLevelFunc(
    arg1: Int,
    arg2: String,
    arg3: String = DefaultValues.TopLevelFunc.name
): UseDefaultValueCallTest.Dst = UseDefaultValueCallTest.Dst(arg1, arg2, arg3)

private fun UseDefaultValueCallTest.Class.topLevelExtensionFunc(
    arg1: Int,
    arg2: String,
    arg3: String = DefaultValues.TopLevelExtensionFunc.name
): UseDefaultValueCallTest.Dst = UseDefaultValueCallTest.Dst(arg1, arg2, arg3)

private fun UseDefaultValueCallTest.Class.topLevelExtensionFuncFromInstance(
    arg1: Int,
    arg2: String,
    arg3: String = DefaultValues.TopLevelExtensionFuncFromInstance.name
): UseDefaultValueCallTest.Dst = UseDefaultValueCallTest.Dst(arg1, arg2, arg3)

private fun UseDefaultValueCallTest.Class.topLevelExtensionFuncFromInstanceWithInstance(
    arg1: Int,
    arg2: String,
    arg3: String = DefaultValues.TopLevelExtensionFuncFromInstanceWithInstance.name
): UseDefaultValueCallTest.Dst = UseDefaultValueCallTest.Dst(arg1, arg2, arg3)

/**
 * 網羅しているパターン
 * - コンストラクタ
 * - インナークラスのコンストラクタ
 * - インナークラスのコンストラクタ + インスタンス
 * - リフレクションで取得したインナークラスのコンストラクタ + インスタンス
 * - インスタンスメソッド
 * - インスタンスメソッド + インスタンス
 * - コンパニオンオブジェクトに定義したメソッド
 * - コンパニオンオブジェクトに定義したメソッド + コンパニオンオブジェクトインスタンス
 * - リフレクションで取得したコンパニオンオブジェクトに定義したメソッド
 * - リフレクションで取得したコンパニオンオブジェクトに定義したメソッド + コンパニオンオブジェクトインスタンス
 * - トップレベル関数
 * - クラスから取得したトップレベル拡張関数 + レシーバインスタンス
 * - インスタンスから取得したトップレベル拡張関数
 * - インスタンスから取得したトップレベル拡張関数 + インスタンス
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
private class UseDefaultValueCallTest {
    interface ToResult {
        fun toResult(): Triple<Int, String, String>
    }

    class Class

    data class Dst(val arg1: Int, val arg2: String, val arg3: String = DefaultValues.Constructor.name) : ToResult {
        override fun toResult() = Triple(arg1, arg2, arg3)

        companion object {
            fun of(arg1: Int, arg2: String, arg3: String = DefaultValues.CompanionObjectFunc.name) =
                Dst(arg1, arg2, arg3)

            fun ofWithInstance(
                arg1: Int,
                arg2: String,
                arg3: String = DefaultValues.CompanionObjectFuncWithInstance.name
            ) = Dst(arg1, arg2, arg3)

            fun ofFromReflection(
                arg1: Int,
                arg2: String,
                arg3: String = DefaultValues.CompanionObjectFuncFromReflection.name
            ) = Dst(arg1, arg2, arg3)

            fun ofFromReflectionWithInstance(
                arg1: Int,
                arg2: String,
                arg3: String = DefaultValues.CompanionObjectFuncFromReflectionWithInstance.name
            ) = Dst(arg1, arg2, arg3)
        }
    }

    inner class InnerDst(
        val arg1: Int,
        val arg2: String,
        val arg3: String = DefaultValues.InnerConstructor.name
    ) : ToResult {
        override fun toResult() = Triple(arg1, arg2, arg3)
    }

    private fun instanceFunction(arg1: Int, arg2: String, arg3: String = DefaultValues.InstanceFunc.name) =
        Dst(arg1, arg2, arg3)

    private fun instanceFunctionWithInstance(
        arg1: Int,
        arg2: String,
        arg3: String = DefaultValues.InstanceFuncWithInstance.name
    ) = Dst(arg1, arg2, arg3)

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    fun test(target: KFunction<ToResult>, instance: Any?, default: DefaultValues) {
        val sut = FastKFunction.of(target, instance)
        val bucket = sut.generateBucket().apply {
            val params = sut.valueParameters

            set(params[0], 100)
            set(params[1], "txt")
        }

        assertDoesNotThrow("Fail ${default.name}") {
            assertEquals(Triple(100, "txt", default.name), sut.callBy(bucket).toResult(), default.name)
        }
    }

    init {
        val hoge = ::InnerDst
        val fuga = FastKFunction.of(hoge)
        val piyo = fuga.generateBucket()
        val temp = hoge.parameters

        val result1 = hoge.parameters.subList(0, 2)
            .associateWith { if (it.index == 0) 100 else "txt" }
            .let { hoge.callBy(it) }
        val result2 = piyo.apply {
            this[0] = 100
            this[1] = "txt"
        }.let { fuga.callBy(it) }

        println()
    }

    fun argumentsProvider(): Stream<Arguments> {
        val innerDstRawFunc = InnerDst::class.primaryConstructor!!
        val ofFromReflection = Dst::class.companionObject!!.functions.first { it.name == "ofFromReflection" }
        val ofFromReflectionWithInstance =
            Dst::class.companionObject!!.functions.first { it.name == "ofFromReflectionWithInstance" }

        return listOf(
            Arguments.of(UseDefaultValueCallTest::Dst, null, DefaultValues.Constructor),
            Arguments.of(::InnerDst, null, DefaultValues.InnerConstructor),
            Arguments.of(::InnerDst, this, DefaultValues.InnerConstructor),
            Arguments.of(innerDstRawFunc, this, DefaultValues.InnerConstructor),
            Arguments.of(::instanceFunction, null, DefaultValues.InstanceFunc),
            Arguments.of(::instanceFunctionWithInstance, this, DefaultValues.InstanceFuncWithInstance),
            Arguments.of((Dst)::of, null, DefaultValues.CompanionObjectFunc),
            Arguments.of(
                (Dst)::ofWithInstance,
                Dst::class.companionObjectInstance,
                DefaultValues.CompanionObjectFuncWithInstance
            ),
            Arguments.of(ofFromReflection, null, DefaultValues.CompanionObjectFuncFromReflection),
            Arguments.of(
                ofFromReflectionWithInstance,
                Dst::class.companionObjectInstance,
                DefaultValues.CompanionObjectFuncFromReflectionWithInstance
            ),
            Arguments.of(::topLevelFunc, null, DefaultValues.TopLevelFunc),
            Arguments.of(Class::topLevelExtensionFunc, Class(), DefaultValues.TopLevelExtensionFunc),
            Class().let {
                Arguments.of(
                    it::topLevelExtensionFuncFromInstance,
                    null,
                    DefaultValues.TopLevelExtensionFuncFromInstance
                )
            },
            Class().let {
                Arguments.of(
                    it::topLevelExtensionFuncFromInstanceWithInstance,
                    it,
                    DefaultValues.TopLevelExtensionFuncFromInstanceWithInstance
                )
            }
        ).stream()
    }
}
