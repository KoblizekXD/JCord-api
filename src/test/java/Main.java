import org.jcord.api.client.DiscordClient;

public class Main {
    public static void main(String[] args) {
        DiscordClient client = new DiscordClient(args[0]);
        client.get("/api/users/@me", true, System.out::println);
    }
}
