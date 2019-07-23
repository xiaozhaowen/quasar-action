package com.xiaozhao.office;

import co.paralleluniverse.fibers.*;
import co.paralleluniverse.fibers.io.FiberFileChannel;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardOpenOption.*;

/**
 * @author xiaozhao
 * @date 2019/7/11:54 PM
 */
public class Demo {
    public static void main(String[] args) {
//        new Fiber<Void>(){
//            @Override
//            protected Void run() throws SuspendExecution, InterruptedException {
//                System.out.println("Hello World");
//                return super.run();
//            }
//        }.start();

        ExecutorService singel =  Executors.newSingleThreadExecutor();
        FiberScheduler fiberScheduler = new FiberExecutorScheduler("test",singel);
        new Fiber<Void>(fiberScheduler,new SuspendableRunnable() {
            @Override
            public void run() throws SuspendExecution, InterruptedException {
                // your code
                System.out.println("Hello World " + Thread.currentThread().getName());
            }
        }).start();


//        try {
//            testFiberAsyncFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }



    private static final Charset charset = Charset.forName("UTF-8");
    private static final CharsetEncoder encoder = charset.newEncoder();
    private static final CharsetDecoder decoder = charset.newDecoder();


    public static void testFiberAsyncFile() throws Exception {

        FiberScheduler scheduler = new FiberForkJoinScheduler("test", 4, null, false);

        new Fiber(scheduler, new SuspendableRunnable() {
            @Override
            public void run() throws SuspendExecution {
                try (FiberFileChannel ch = FiberFileChannel.open(Paths.get(System.getProperty("user.home"), "fibertest.bin"), READ, WRITE, CREATE, TRUNCATE_EXISTING)) {
                    ByteBuffer buf = ByteBuffer.allocateDirect(1024);

                    String text = "this is my text blahblah";
                    ch.write(encoder.encode(CharBuffer.wrap(text)));

                    ch.position(0);
                    ch.read(buf);

                    buf.flip();
                    String read = decoder.decode(buf).toString();

//                    assertThat(read, equalTo(text));

                    buf.clear();

                    ch.position(5);
                    ch.read(buf);

                    buf.flip();
                    read = decoder.decode(buf).toString();

//                    assertThat(read, equalTo(text.substring(5)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start().join();
    }
}
