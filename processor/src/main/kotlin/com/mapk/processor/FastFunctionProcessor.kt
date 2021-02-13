package com.mapk.processor

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.service.AutoService
import com.mapk.processor.processingsteps.constructor.ConstructorProcessingStep
import javax.annotation.processing.Processor
import javax.lang.model.SourceVersion

@AutoService(Processor::class)
class FastFunctionProcessor : BasicAnnotationProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedOptions(): Set<String> = setOf(KAPT_KOTLIN_GENERATED_OPTION_NAME)

    override fun initSteps(): Iterable<ProcessingStep> {
        return listOf(ConstructorProcessingStep())
    }

    companion object {
        private const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}
