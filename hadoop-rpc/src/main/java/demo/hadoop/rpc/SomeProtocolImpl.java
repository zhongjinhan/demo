package demo.hadoop.rpc;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

public class SomeProtocolImpl implements SomeProtocol {
    public long getProtocolVersion(String protocol, long clientVersion) throws IOException {
        return SomeProtocol.versionID;
    }

    public ProtocolSignature getProtocolSignature(String protocol, long clientVersion, int clientMethodsHash) throws IOException {
        return new ProtocolSignature(SomeProtocol.versionID, null);
    }

    public String echo(String value) throws IOException {
        return "echo: " + value;
    }
}
