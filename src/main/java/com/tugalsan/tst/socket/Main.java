package com.tugalsan.tst.socket;

import com.tugalsan.api.log.server.*;
import com.tugalsan.api.socket.server.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncWait;
import com.tugalsan.api.thread.server.sync.*;
import java.util.stream.IntStream;

public class Main {

    private static final TS_Log d = TS_Log.of(Main.class);

    //cd C:\me\codes\com.tugalsan\tst\com.tugalsan.tst.socket
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.tst.socket-1.0-SNAPSHOT-jar-with-dependencies.jar
    //java -jar target/com.tugalsan.tst.socket-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String... s) {
        var killTrigger = TS_ThreadSyncTrigger.of("main");
        var port = 8282;
        if (!TS_SocketUtils.available(8282)) {
            d.ce("main", "ERROR: Port in use already!", port);
            return;
        }
        var server = TS_SocketServer
                .of(killTrigger, port, forEachReceivedLine -> forEachReceivedLine.toUpperCase())
                .start();
        var client = TS_SocketClient
                .of(killTrigger, port, forEachReceivedLine -> d.cr("client", "server sent", forEachReceivedLine))
                .start();

        IntStream.range(0, 10).forEachOrdered(i -> {
            TS_ThreadSyncWait.milliseconds100();
            client.add("ĞÜğüŞİşiÖçöçIıı " + i);
        });
        TS_ThreadSyncWait.seconds(null, killTrigger, 15);
    }

}
