public class Start {
    private static final int recivers = 1000;

    public static void main(String[] args) {
        for (int i = 0; i < recivers; i++) {
            new Reciver("hello").run();
        }
        Sender sender = new Sender("hello");
    }
}
