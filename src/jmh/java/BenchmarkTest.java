package OrderBookDemo.jmh;

import com.ib.core.Engine;
import com.ib.core.SimpleEngine;
import com.messages.Order;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Fork(value = 1,warmups = 1)//,jvmArgsAppend = {"-XX:+UseZGC"})
@State(Scope.Thread)
@Warmup(iterations = 1, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 2, time=3000, timeUnit = TimeUnit.MILLISECONDS)
//@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BenchmarkTest {

    @State(Scope.Thread)
    public static class OBState
    {
        static long i = 0;
        static double price = 0.0001;
        Order o;
        Engine engine = new SimpleEngine();

        @Setup(Level.Invocation)
        public void set_up()
        {
            ++i;
            ++price;
            o= new Order();
            o.setOrderId(String.valueOf(i));
            o.setSide(i%2==0?"B":"S");
            o.setPrice(price);
            o.setQuantity(i+2);
            o.setUpdateTime(i);
        }

        @TearDown
        public void tear_down()
        {
            //engine.printStats();
        }
    }


    @Benchmark
    public void measureAddOrder(Blackhole bh, OBState obState) {
        obState.engine.addOrder(obState.o);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
