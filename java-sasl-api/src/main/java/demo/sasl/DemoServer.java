package demo.sasl;

public class DemoServer {
    public static void main(String[] args) throws Exception{
        Util.loginAndDoAction("DemoServer", new SaslServerAction());
    }
}
