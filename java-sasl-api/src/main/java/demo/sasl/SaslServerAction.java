package demo.sasl;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class SaslServerAction implements PrivilegedAction<String> {
    public String run() {
        try {
            Map<String, String> props = new HashMap<String, String>();
            props.put(Sasl.QOP, "auth-conf");


            int port = 8888;
            ServerSocket socketServer = new ServerSocket(port);
            System.out.println("listen on port: " + port);


            while (true) {
                Socket s = socketServer.accept();
                System.out.println("accept from " + s.getInetAddress().getHostAddress());
                DataInputStream inputStream = new DataInputStream(s.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
                SaslServer ss = Sasl.createSaslServer("GSSAPI", "test",
                        "myserver", props, new ServerCallbackHandler());

                byte[] response = new byte[inputStream.readInt()];
                inputStream.readFully(response);
                byte[] challenge = null;
                System.out.println("start evaluate response");
                while (!ss.isComplete()) {

                    challenge = ss.evaluateResponse(response);

                    if (ss.isComplete()) {
                        Util.writeByte2(outputStream, challenge, 1);
                    } else {
                        Util.writeByte2(outputStream, challenge, 0);
                        response = new byte[inputStream.readInt()];
                        inputStream.readFully(response);
                    }
                }
                System.out.println("authentication is complete");
                byte[] resp = new byte[inputStream.readInt()];
                inputStream.readFully(resp);

                byte[] unwrapped = ss.unwrap(resp, 0, resp.length);
                String received = new String(unwrapped);
                System.out.println("got from client:" + received);
                String send = received + " - 0";
                byte[] content = send.getBytes();
                byte[] out = ss.wrap(content, 0, content.length);
                Util.writeByte(outputStream, out);

                ss.dispose();
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    static class ServerCallbackHandler implements CallbackHandler {
        public void handle(Callback[] cbs) throws IOException, UnsupportedCallbackException {
            AuthorizeCallback ac = null;
            for (Callback callback : cbs) {
                if (callback instanceof AuthorizeCallback) {
                    ac = (AuthorizeCallback) callback;
                } else {
                    throw new UnsupportedCallbackException(callback,
                            "Unrecognized SASL GSSAPI Callback");
                }
            }
            if (ac != null) {
                String authid = ac.getAuthenticationID();
                String authzid = ac.getAuthorizationID();
                if (authid.equals(authzid)) {
                    ac.setAuthorized(true);
                } else {
                    ac.setAuthorized(false);
                }
                if (ac.isAuthorized()) {
                    ac.setAuthorizedID(authzid);
                }
            }

        }
    }

}


