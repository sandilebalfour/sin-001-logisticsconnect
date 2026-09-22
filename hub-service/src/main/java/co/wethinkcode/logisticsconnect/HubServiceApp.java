package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HubServiceApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Javalin app = Javalin.create().start(7051);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO (Serves provinces and sorting centers (place-name source of truth).)
        // Add domain endpoints for hub-service here.
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:7050/hubs")).build();
        String json = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        ObjectMapper mapper = new ObjectMapper();
        List<HubsGlobal> hubs = mapper.readValue(json, new TypeReference<>(){});
        System.out.println("HubService loaded " + hubs.size() + " Hubs from ingestion");

        app.get("hubs", ctx -> ctx.json(hubs));


        app.get("/provinces", ctx -> {
            var provinces = hubs.stream().map(HubsGlobal::getProvince).distinct().sorted().toList();
            ctx.json(provinces);
        });

        app.get("/hubs/{id}", ctx -> {
            String id = ctx.pathParam("id");
            hubs.stream().filter(h -> h.getHubId().equalsIgnoreCase(id)).findFirst()
                    .ifPresentOrElse(ctx::json, () -> ctx.status(404).result("Not found"));
        });

        app.get("/provinces/{province}/hubs", ctx -> {
            String prov = ctx.pathParam("province");
            var filtered = hubs.stream()
                    .filter(h -> h.getProvince().equalsIgnoreCase(prov)).toList();
            ctx.json(filtered);
        });
    }
}
