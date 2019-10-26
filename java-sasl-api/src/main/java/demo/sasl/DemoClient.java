package demo.sasl;

public class DemoClient {

    public static void main(String[] args) throws Exception{
        Util.loginAndDoAction("DemoClient", new SaslClientAction());
    }
}
