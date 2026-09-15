package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

import java.io.IOException;

public class IngestionServiceApp {

    public static void main(String[] args) throws IOException {
        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO: read and clean src/main/resources/hubs-global.csv (hubs, sorting centers, regional districts data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.
        CsvCleaner csvCleaner = new CsvCleaner();
        app.get("/Hubs", ctx -> ctx.json(csvCleaner.loadAndClean()));

    }
}
