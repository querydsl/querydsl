/*
 * Copyright 2012, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.testutil;

public final class Runner {
    
    public static void run(String label, Benchmark benchmark) throws Exception {
        // warmup
        benchmark.run(50000);
        System.err.print("- ");

        // run garbage collection
        System.gc();
        System.err.print("- ");
        
        // perform timing
        long start = System.nanoTime();
        benchmark.run(1000000);
        long end = System.nanoTime();
        System.err.println(label + " " + ((end-start) / 1000000));
    }
    
    private Runner() {}

}
