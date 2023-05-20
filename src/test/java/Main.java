import org.jcord.api.websocket.Gateway;

public class Main {
    public static void main(String[] args) {
        Gateway gateway = Gateway.of(args[0]);

        gateway.runAsync(System.out::println);
    }
}
