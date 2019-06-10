/*
 * Copyright 2016-2019 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package generators.immutableListBuilder

import generators.BenchmarkSourceGenerator
import java.io.PrintWriter

interface ListBuilderGetBenchmark {
    val packageName: String
    fun emptyOf(T: String): String
}

class ListBuilderGetBenchmarkGenerator(private val impl: ListBuilderGetBenchmark) : BenchmarkSourceGenerator() {
    override val outputFileName: String = "Get"

    override fun getPackage(): String {
        return super.getPackage() + ".immutableList." + impl.packageName
    }

    override val imports: Set<String> = super.imports + "org.openjdk.jmh.infra.Blackhole"

    override fun generateBody(out: PrintWriter) {
        out.println("""
open class Get {
    @Param("10000", "100000")
    var size: Int = 0

    @Param("0.0", "50.0")
    var immutablePercentage: Double = 0.0

    private var builder = ${impl.emptyOf("String")}

    @Setup(Level.Trial)
    fun prepare() {
        builder = persistentListBuilderAdd(size, immutablePercentage)
    }

    @Benchmark
    fun getByIndex(bh: Blackhole) {
        for (i in 0 until builder.size) {
            bh.consume(builder[i])
        }
    }
}
        """.trimIndent()
        )
    }
}