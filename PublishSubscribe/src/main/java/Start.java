public class Start {
    private static final int workers = 5;
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        for (int i = 0; i < workers; i++) {
            new Thread(new ReciveLogs(EXCHANGE_NAME)).start();
        }
        new Thread(new EmitLog(EXCHANGE_NAME)).start();
    }
}
