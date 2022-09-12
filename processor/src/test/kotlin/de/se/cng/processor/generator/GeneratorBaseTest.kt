package de.se.cng.processor.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import de.se.cng.processor.ProcessorTestBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GeneratorBaseTest : ProcessorTestBase() {

    @Test
    fun `disable logging creates nothing`() {
        val generator = BasicGenerator("file1", false)

        val expected = ""

        assertSourceEquals(expected, generator.generate().toString())
    }

    @Test
    fun `single logging call is generated`() {
        val generator = BasicGenerator("file1", true)

        generator.functionBuilderScope("function1") {
            generator.addLogd(this, "My Message")
        }

        val expected = """
            import kotlin.Unit
            
            public fun function1(): Unit {
              Log.d("file1", "My Message")
            }
        """.trimIndent()

        assertSourceEquals(expected, generator.generate().toString())
    }

    @Test
    fun `multiple logging calls are generated`() {
        val generator = BasicGenerator("file1", true)

        generator.functionBuilderScope("function1") {
            generator.addLogd(this, "My first logd message")
            generator.addLogd(this, "My second logd message")
            generator.addLogi(this, "My first info message: INFO!")
            generator.addLogw(this, "My first warning message: !!!WARNING!!!")
            generator.addLoge(this, "My first error message without an exception")
            generator.addLogd(this, "My last debug message")
        }

        val expected = """
        import kotlin.Unit
                
        public fun function1(): Unit {
          Log.d("file1", "My first logd message")
          Log.d("file1", "My second logd message")
          Log.i("file1", "My first info message: INFO!")
          Log.i("file1", "My first warning message: !!!WARNING!!!")
          Log.e("file1", "My first error message without an exception")
          Log.d("file1", "My last debug message")
        }
        """.trimIndent()

        assertSourceEquals(expected, generator.generate().toString())
    }

    @Test
    fun `multiple logging calls in multiple functions are generated`() {
        val generator = BasicGenerator("file1", true)

        generator.functionBuilderScope("function1") {
            generator.addLogd(this, "My first logd message")
            generator.addLogd(this, "My second logd message")
        }

        generator.functionBuilderScope("function2") {
            generator.addLogi(this, "My first info message: INFO!")
            generator.addLogw(this, "My first warning message: !!!WARNING!!!")
        }

        generator.functionBuilderScope("function3") {
            generator.addLoge(this, "My first error message without an exception")
        }

        generator.functionBuilderScope("function4") {
            generator.addLogd(this, "My last debug message")
        }

        val expected = """
        import kotlin.Unit
               
        public fun function1(): Unit {
            Log.d("file1", "My first logd message")
            Log.d("file1", "My second logd message")
        }
        
        public fun function2(): Unit {
            Log.i("file1", "My first info message: INFO!")
            Log.i("file1", "My first warning message: !!!WARNING!!!")
        }        
        
        public fun function3(): Unit {
            Log.e("file1", "My first error message without an exception")
        }   

        public fun function4(): Unit {
            Log.d("file1", "My last debug message")
        }
        """.trimIndent()

        assertSourceEquals(expected, generator.generate().toString())
    }

    @Test
    fun `no logging calls in multiple functions are generated, if disabled`() {
        val generator = BasicGenerator("file1", false)

        generator.functionBuilderScope("function1") {
            generator.addLogd(this, "My first logd message")
            generator.addLogd(this, "My second logd message")
        }

        generator.functionBuilderScope("function2") {
            generator.addLogi(this, "My first info message: INFO!")
            generator.addLogw(this, "My first warning message: !!!WARNING!!!")
        }

        generator.functionBuilderScope("function3") {
            generator.addLoge(this, "My first error message without an exception")
        }

        generator.functionBuilderScope("function4") {
            generator.addLogd(this, "My last debug message")
        }

        val expected = """
            import kotlin.Unit
            
            public fun function1(): Unit {
            }
            
            public fun function2(): Unit {
            }
            
            public fun function3(): Unit {
            }
            
            public fun function4(): Unit {
            }
        """.trimIndent()

        assertSourceEquals(expected, generator.generate().toString())
    }

    @Test
    fun `filename is set correctly`() {
        val generator = BasicGenerator("file1", true)

        assertEquals("file1", generator.generate().name)
    }

    private class BasicGenerator(fileName: String, enableLogging: Boolean) : GeneratorBase(fileName, enableLogging) {
        private val functions = mutableSetOf<FunSpec>()

        override fun FileSpec.Builder.addImports(): FileSpec.Builder {
            TODO("Not yet implemented")
        }


        fun addLogd(builder: FunSpec.Builder, message: String) = with(builder) {
            logd(message)
        }

        fun addLogi(builder: FunSpec.Builder, message: String) = with(builder) {
            logi(message)
        }

        fun addLogw(builder: FunSpec.Builder, message: String) = with(builder) {
            logw(message)
        }

        fun addLoge(builder: FunSpec.Builder, message: String) = with(builder) {
            loge(message)
        }

        fun functionBuilderScope(name: String, scope: FunSpec.Builder.() -> Unit) {
            val builder = FunSpec.builder(name)
            scope.invoke(builder)
            functions += builder.build()
        }

        override fun generate() = with(FileSpec.builder("", fileName)) {
            functions.forEach { funSpec ->
                addFunction(funSpec)
            }
            build()
        }
    }
}