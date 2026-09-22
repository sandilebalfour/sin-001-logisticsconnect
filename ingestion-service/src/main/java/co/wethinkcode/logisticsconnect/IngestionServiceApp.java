package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;
import java.util.List;

public class IngestionServiceApp {

    public static void main(String[] args) {

        List<HubsGlobal> cleanedHubs;
        try {
            CsvCleaner cleaner = new CsvCleaner();
            cleanedHubs = cleaner.loadAndClean();
            System.out.println("Loaded " + cleanedHubs.size() + " unique hubs after cleaning/dedup");
        } catch (Exception e) {
            System.err.println("Failed to load hubs-global.csv: " + e.getMessage());
            e.printStackTrace();
            return; // don't start server with bad data
        }

        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/hubs", ctx -> ctx.json(cleanedHubs));

        app.get("/hubs/{id}", ctx -> {
            String id = ctx.pathParam("id").toUpperCase().trim();
            cleanedHubs.stream()
                    .filter(h -> h.getHubId().equalsIgnoreCase(id))
                    .findFirst()
                    .ifPresentOrElse(
                            ctx::json,
                            () -> ctx.status(404).result("Hub not found: " + id)
                    );
        });
    }
}