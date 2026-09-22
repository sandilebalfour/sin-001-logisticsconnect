package co.wethinkcode.logisticsconnect;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvCleaner {

    public List<HubsGlobal> loadAndClean() throws IOException {
        List<HubsGlobal> hubs = new ArrayList<>();
        InputStream is = getClass().getResourceAsStream("/hubs-global.csv");
        if (is == null) throw new FileNotFoundException("hubs-global.csv not in resources");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            Map<String, HubsGlobal> deduped = new java.util.LinkedHashMap<>();

            while ((line = br.readLine())!= null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                String hubId = cleanId(parts[0]);
                if (isPlaceholder(hubId)) continue;

                String province = cleanName(parts[1]);
                String sortingHub = cleanName(parts[2]);

                // Normalize known variant
                if (sortingHub.equalsIgnoreCase("Pretoria North")) {
                    province = "Gauteng";
                }

                boolean active = parseBoolean(parts[3]);

                // Dedupe key: normalized sorting_center + province
                // Johannesburg Central will have same key even with different IDs
                String dedupeKey = (sortingHub + "|" + province).toLowerCase();

                // Keep first seen, or active=true wins if conflict
                if (!deduped.containsKey(dedupeKey) || active) {
                    deduped.put(dedupeKey, new HubsGlobal(hubId, province, sortingHub, active));
                }
            }
            hubs.addAll(deduped.values());
        }
        return hubs;
    }

    private String cleanId(String s) {
        if (s == null) return "";
        return s.trim().toUpperCase().replaceAll("\\s+", "");
    }

    private String cleanName(String s) {
        if (s == null) return "Not provided";
        s = s.trim().replaceAll("\\s+", " "); // collapse double spaces
        if (isPlaceholder(s)) return "Not provided";

        // Title Case each word
        String[] words = s.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) sb.append(Character.toUpperCase(w.charAt(0)))
                    .append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    private boolean isPlaceholder(String s) {
        if (s == null) return true;
        String low = s.trim().toLowerCase();
        return low.isEmpty() || List.of("n/a", "na", "tbd", "unknown", "-", "nan", "null").contains(low);
    }

    private boolean parseBoolean(String check) {
        if (check == null) return false;
        String c = check.trim().toLowerCase();
        return switch (c) {
            case "y", "yes", "true", "1" -> true;
            case "n", "no", "false", "0" -> false;
            default -> false;
        };
    }
}