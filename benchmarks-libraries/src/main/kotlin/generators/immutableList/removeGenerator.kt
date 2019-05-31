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

package generators.immutableList

import generators.BenchmarkSourceGenerator
import java.io.PrintWriter

interface ListRemoveBenchmark {
    val packageName: String
    fun emptyOf(T: String): String
    fun listType(T: String): String
    val removeAtOperation: String
}

class ListRemoveBenchmarkGenerator(private val impl: ListRemoveBenchmark) : BenchmarkSourceGenerator() {
    override val benchmarkName: String = "Remove"

    override fun getPackage(): String {
        return super.getPackage() + ".immutableList." + impl.packageName
    }

    override fun generateBody(out: PrintWriter) {
        out.println("""
open class Remove {
    @Param("10000", "100000")
    var size: Int = 0

    private var persistentList = ${impl.emptyOf("String")}

    @Setup(Level.Trial)
    fun prepare() {
        persistentList = persistentListAdd(size)
    }

    @Benchmark
    fun removeLast(): ${impl.listType("String")} {
        var list = persistentList
        repeat(times = size) {
            list = list.${impl.removeAtOperation}(list.size - 1)
        }
        return list
    }
}
        """.trimIndent()
        )
    }
}