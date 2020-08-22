public class StartRPC {
    public static void main(String[] args) {
        new Thread(new RPCServer()).start();
        new Thread(new RPCClient()).start();
    }
}
