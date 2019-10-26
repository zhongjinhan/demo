package demo.sasl;

import com.sun.security.auth.callback.TextCallbackHandler;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivilegedAction;

public class Util {
    public static void loginAndDoAction(String name, PrivilegedAction action) throws LoginException {
        LoginContext lc = new LoginContext(name, new TextCallbackHandler());
        lc.login();
        Subject subject = lc.getSubject();
        Subject.doAs(subject, action);
    }

    public static void writeByte(DataOutputStream outStream,
                                 byte[] out) throws IOException {
        if (out.length == 0) {
            outStream.writeInt(out.length);
            outStream.flush();
        } else {
            outStream.writeInt(out.length);
            outStream.write(out);
            outStream.flush();
        }
    }

    public static void writeByte2(DataOutputStream outStream,
                                           byte[] out,
                                           int result) throws IOException {

        if (out == null){
            out = new byte[0];
        }
        if (out.length == 0) {
            outStream.writeInt(out.length);
            outStream.writeInt(result);
            outStream.flush();
        } else {
            outStream.writeInt(out.length);
            outStream.write(out);
            outStream.writeInt(result);
            outStream.flush();
        }
    }
}
