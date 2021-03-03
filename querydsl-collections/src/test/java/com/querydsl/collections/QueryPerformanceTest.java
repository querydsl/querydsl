package com.querydsl.collections;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.Performance;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@Ignore
@Category(Performance.class)
public class QueryPerformanceTest {

    private static final int size = 1000;

    private static List<Cat> cats = new ArrayList<Cat>(size);

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException {
        for (int i = 0; i < size; i++) {
            cats.add(new Cat(String.valueOf(i), i));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void benchmark1() {
        // 15857
        QCat cat = QCat.cat;
        CollQueryFactory.from(cat, cats).where(cat.id.eq(ThreadLocalRandom.current().nextInt(100) % size)).select(cat).fetch();
    }

    @Test
    public void launchBenchmark() throws Exception {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(3)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(3)
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

}
