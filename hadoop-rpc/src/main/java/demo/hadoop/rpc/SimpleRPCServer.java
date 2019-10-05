package demo.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

public class SimpleRPCServer {
    public static void main(String[] args) throws IOException{
        Configuration conf = new Configuration();
        RPC.Server server = new RPC.Builder(conf)
                .setProtocol(SomeProtocol.class)
                .setInstance(new SomeProtocolImpl())
                .setBindAddress("0.0.0.0")
                .setPort(8889)
                .setNumHandlers(2)
                .build();
        server.start();
    }
}
