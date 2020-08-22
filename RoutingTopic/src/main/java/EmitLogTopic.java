import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic implements Runnable {

    private String EXCHANGE_NAME;

    public EmitLogTopic(String EXCHANGE_NAME) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
    }

    private void dispatch() throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            while (true) {
                String string = scanner.nextLine();
                String routingKey = string.split(" ").length > 1 ? string.split(" ")[0] : "info";

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, string.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + string + "'");
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
