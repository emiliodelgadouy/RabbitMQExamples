import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker implements Runnable {

    private static final String TASK_QUEUE_NAME = "task_queue";
    private static int COUNT = 0;
    private int number;

    public Worker() {
        this.number = COUNT++;
    }

    public void dispatch() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Worker " + this.number + " is waiting for messages");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [-] Worker " + this.number + " received '" + message + "'");
            String result = "";
            try {
                result = doWork(Integer.parseInt(message));
            } finally {
                System.out.println(" [x] Worker " + this.number + " done '" + message + "', result:" + result);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
    }

    private String doWork(int number) {
        String string = String.valueOf(PiCalculator.resolve(number * 10000));
        return String.valueOf(string.charAt(string.length() - 1));
    }

    @Override
    public void run() {
        try {
            dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
