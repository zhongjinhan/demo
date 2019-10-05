package demo.hadoop.rpc;

import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

interface SomeProtocol extends VersionedProtocol {
    long versionID = 1L;

    String echo(String value) throws IOException;
}
