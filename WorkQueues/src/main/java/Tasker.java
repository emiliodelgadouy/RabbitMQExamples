import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class Tasker implements Runnable {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public void dispatch() throws Exception {
        Scanner scanner = new Scanner(System.in);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            while (true) {
                String message = scanner.nextLine();
                channel.basicPublish("", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
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