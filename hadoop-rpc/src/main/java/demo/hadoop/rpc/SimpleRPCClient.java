package demo.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleRPCClient {
    public static void main(String[] args) throws IOException{
        Configuration conf = new Configuration();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8889);
        SomeProtocol proxy = RPC.getProxy(SomeProtocol.class, SomeProtocol.versionID,address, conf);
        System.out.println(proxy.echo("hello"));
    }
}
