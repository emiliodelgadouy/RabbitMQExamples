public class Start {
    private static final int workers = 5;
    private static final String EXCHANGE_NAME = "logs_direct";

    public static void main(String[] args) {

        new Thread(new ReceiveLogDirect(EXCHANGE_NAME,"info error warning")).start();
        new Thread(new ReceiveLogDirect(EXCHANGE_NAME,"info")).start();
        new Thread(new ReceiveLogDirect(EXCHANGE_NAME,"info error")).start();
        new Thread(new ReceiveLogDirect(EXCHANGE_NAME,"error")).start();
        new Thread(new ReceiveLogDirect(EXCHANGE_NAME,"#")).start(); //match routing key exactly equals to '#', not working as wildcard while exchange type is Direct


        new Thread(new EmitLogDirect(EXCHANGE_NAME)).start();
    }
}
