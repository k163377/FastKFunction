package com.mapk.fastkfunction.argumentBucket

import com.mapk.fastkfunction.argumentbucket.ArgumentBucket
import com.mapk.fastkfunction.argumentbucket.BucketGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private class ArgumentBucketTest {
    private val parameters = ::sample.parameters
    private val bucket: ArgumentBucket = BucketGenerator(parameters, null).generateBucket()

    private fun sample(arg1: Short, arg2: Int, arg3: Long): Long = arg1.toLong() + arg2.toLong() + arg3

    @Nested
    inner class SetAndGetTest {
        val index = 1
        val firstValue = 10
        val secondValue = 20

        @Test
        fun byKParameter() {
            val param = parameters[index]

            assertNull(bucket.set(param, firstValue))
            assertEquals(firstValue, bucket[param])
            assertEquals(firstValue, bucket.set(param, secondValue))
            assertEquals(secondValue, bucket[param])
        }

        @Test
        fun byIndex() {
            assertNull(bucket.set(index, firstValue))
            assertEquals(firstValue, bucket[index])
            assertEquals(firstValue, bucket.set(index, secondValue))
            assertEquals(secondValue, bucket[index])
        }
    }

    @Nested
    inner class SetIfAbsentTest {
        val index = 1
        val firstValue = 10
        val secondValue = 20

        @Nested
        inner class ByKParameter {
            val param = parameters[index]

            @Test
            fun isNull() {
                assertNull(bucket.setIfAbsent(param, null))
                assertTrue(bucket.containsKey(param))
            }

            @Test
            fun byKParameter() {
                assertNull(bucket.setIfAbsent(param, firstValue))
                assertEquals(firstValue, bucket[param])
                assertEquals(firstValue, bucket.setIfAbsent(param, secondValue))
                assertEquals(firstValue, bucket[param])
            }
        }

        @Nested
        inner class ByIndex {
            @Test
            fun isNull() {
                assertNull(bucket.setIfAbsent(index, null))
                assertTrue(bucket.containsKey(parameters[index]))
            }

            @Test
            fun byKParameter() {
                assertNull(bucket.setIfAbsent(index, firstValue))
                assertEquals(firstValue, bucket[index])
                assertEquals(firstValue, bucket.setIfAbsent(index, secondValue))
                assertEquals(firstValue, bucket[index])
            }
        }
    }
}
