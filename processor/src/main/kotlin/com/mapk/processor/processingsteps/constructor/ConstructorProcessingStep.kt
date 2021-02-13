package com.mapk.processor.processingsteps.constructor

import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.SetMultimap
import com.mapk.annotations.FastFunction
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement

class ConstructorProcessingStep : BasicAnnotationProcessor.ProcessingStep {
    override fun annotations() = setOf(FastFunction::class.java)

    override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): Set<Element> {
        annotations()
            .flatMap { elementsByAnnotation[it] }
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .forEach {
                it as ExecutableElement

                // TODO: 生成処理
                println(it.simpleName)
            }

        return emptySet()
    }
}
