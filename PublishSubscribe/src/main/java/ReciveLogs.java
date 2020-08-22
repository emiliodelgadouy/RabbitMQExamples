import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReciveLogs implements Runnable {

    String EXCHANGE_NAME;
    private static int COUNT = 0;
    private int number;

    public ReciveLogs(String EXCHANGE_NAME) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        number = COUNT++;
    }

    private void dispatch() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Reciver " + number + " is waiting for messages");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[*] Reciver " + number + "  recive '" + message + "' in queue " + queueName);
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
