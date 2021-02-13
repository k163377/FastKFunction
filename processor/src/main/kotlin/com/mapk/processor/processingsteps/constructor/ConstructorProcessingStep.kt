package com.mapk.processor.processingsteps.constructor

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.mapk.annotations.FastFunction
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.metadata.readKotlinClassMetadata
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import com.squareup.kotlinpoet.metadata.toImmutableKmClass
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class ConstructorProcessingStep : BasicAnnotationProcessor.ProcessingStep {
    private fun generateCode(constructorElement: ExecutableElement) {
        val clazzElement = constructorElement.enclosingElement as TypeElement
        val typeMetadata = constructorElement.returnType.getAnnotation(Metadata::class.java)!!

        typeMetadata
        clazzElement.qualifiedName

        println(typeMetadata)

        // val callFunction: FunSpec = FunSpec.builder("call").apply {
        //     Any::class.asTypeName().copy(nullable = true)
        //         .let { Array::class.asTypeName().parameterizedBy(it) }
        //         .let { addParameter("args", it) } // 引数の型をArray<Any?>でセット
//
        //     val typeMetadata = constructorElement.returnType.getAnnotation(Metadata::class.java)!!
//
        //     returns(constructorElement.returnType) // 戻り値の型のセット
//
        //     val callStatement = function.parameters
        //         .mapIndexed { i, param ->
        //             "args[${i}] as ${param.type.asTypeName()}"
        //         }
        //         .joinToString(prefix = "return Sample(", separator = ", ", postfix = ")")
//
        //     addStatement(callStatement)
        // }.build()
    }

    override fun annotations() = setOf(FastFunction::class.java)

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): Set<Element> {
        println("-----------------------")

        annotations()
            .flatMap { elementsByAnnotation[it] }
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .forEach {
                generateCode(it as ExecutableElement)
            }

        return emptySet()
    }
}
