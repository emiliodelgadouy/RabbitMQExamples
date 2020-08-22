import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class EmitLog implements Runnable {

    private String EXCHANGE_NAME;

    public EmitLog(String EXCHANGE_NAME) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
    }

    public void dispatch() throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            while (true) {
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
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
