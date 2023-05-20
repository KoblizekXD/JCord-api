import com.google.gson.Gson;
import org.jcord.api.util.Payload;
import org.jcord.api.websocket.Gateway;
import org.jcord.api.websocket.identify.IdentifyJson;

public class Main {
    public static void main(String[] args) {
        Gateway gateway = Gateway.of(args[0]);

        gateway.runAsync(content -> {
            System.out.println(content);
        });
        Payload.send(new IdentifyJson(args[0]).toString());
    }
}
