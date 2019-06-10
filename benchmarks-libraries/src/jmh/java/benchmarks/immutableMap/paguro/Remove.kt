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

// Auto-generated file. DO NOT EDIT!

package benchmarks.immutableMap.paguro

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit
import benchmarks.*

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
open class Remove {
    @Param("10000", "100000")
    var size: Int = 0

    @Param(ASCENDING_HASH_CODE, RANDOM_HASH_CODE, COLLISION_HASH_CODE, NON_EXISTING_HASH_CODE)
    var hashCodeType = ""

    private var keys = listOf<IntWrapper>()
    private var persistentMap = org.organicdesign.fp.collections.PersistentHashMap.empty<IntWrapper, String>()

    @Setup(Level.Trial)
    fun prepare() {
        keys = generateKeys(hashCodeType, size)
        persistentMap = persistentMapPut(keys)

        if (hashCodeType == NON_EXISTING_HASH_CODE)
            keys = generateKeys(hashCodeType, size)
    }

    @Benchmark
    fun remove(): org.organicdesign.fp.collections.PersistentHashMap<IntWrapper, String> {
        var map = persistentMap
        repeat(times = size) { index ->
            map = map.without(keys[index])
        }
        return map
    }
}
