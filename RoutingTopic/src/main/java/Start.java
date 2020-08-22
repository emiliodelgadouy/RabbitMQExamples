public class Start {
    private static final int workers = 5;
    private static final String EXCHANGE_NAME = "logs_topic";

    public static void main(String[] args) {

        new Thread(new ReceiveLogTopic(EXCHANGE_NAME,"#")).start();
        new Thread(new ReceiveLogTopic(EXCHANGE_NAME,"info")).start();
        new Thread(new ReceiveLogTopic(EXCHANGE_NAME,"info.* error.*")).start();
        new Thread(new ReceiveLogTopic(EXCHANGE_NAME,"error")).start();


        new Thread(new EmitLogTopic(EXCHANGE_NAME)).start();
    }
}
