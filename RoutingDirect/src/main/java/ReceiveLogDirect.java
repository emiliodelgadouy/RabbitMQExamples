import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogDirect implements Runnable {

    private String EXCHANGE_NAME;
    private String LOG_LEVEL;
    private static int COUNT = 0;
    private int number;

    public ReceiveLogDirect(String EXCHANGE_NAME, String LOG_LEVEL) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        this.LOG_LEVEL = LOG_LEVEL;
        this.number = COUNT++;
    }

    private void dispatch() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();


        for (String severity : this.LOG_LEVEL.split(" ")) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println(" [*] Reciver " + number + " is waiting for messages type: " + LOG_LEVEL);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [*] Reciver " + number + " receive '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    @Override
    public void run() {
        try {
            dispatch();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
