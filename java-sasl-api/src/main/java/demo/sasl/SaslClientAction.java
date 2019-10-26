package demo.sasl;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class SaslClientAction implements PrivilegedAction {
    public String run() {

        try {

            Map<String, String> props = new HashMap<String, String>();
            props.put(Sasl.QOP, "auth-conf");
            String[] mechanisms = new String[]{"GSSAPI"};
            SaslClient sc = Sasl.createSaslClient(mechanisms, null, "test",
                    "myserver", props, null);

            Socket socket = new Socket("localhost", 8888);
            DataInputStream inStream =
                    new DataInputStream(socket.getInputStream());
            DataOutputStream outStream =
                    new DataOutputStream(socket.getOutputStream());


            int result = 0;

            byte[] challenge = new byte[]{};
            byte[] response;

            while (!sc.isComplete()) {
                response = sc.evaluateChallenge(challenge);

                if (result == 1) {
                    if (response != null) {
                        throw new IOException(
                                "Protocol error: attempting to send response after completion");
                    }
                    break;
                } else {

                    Util.writeByte(outStream, response);
                    challenge = new byte[inStream.readInt()];
                    inStream.readFully(challenge);
                    result = inStream.readInt();

                }

            }


            System.out.println("authentication is complete");

            byte[] content = "Hellllo".getBytes();
            byte[] out = sc.wrap(content, 0, content.length);
            Util.writeByte(outStream, out);

            byte[] resp = new byte[inStream.readInt()];
            inStream.readFully(resp);

            byte[] unwrapped = sc.unwrap(resp, 0, resp.length);
            System.out.println("got from server:" + new String(unwrapped));
            socket.close();
            sc.dispose();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

}
