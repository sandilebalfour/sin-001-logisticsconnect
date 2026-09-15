package co.wethinkcode.logisticsconnect;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvCleaner {

    private boolean isActive(String check){

       switch (check.toLowerCase()) {
           case ("y"), ("yes") ,("true"), ("1") -> {
               return true;
           }
           case ("n"), ("no"), ("false"), ("0") -> {
               return false;
           }
       }
        return false;
    }

    public List<HubsGlobal> loadAndClean() throws IOException {
        List<HubsGlobal> hubs = new ArrayList<>();
        InputStream is = getClass().getResourceAsStream("/hubs-global.csv");
        if(is == null) throw new FileNotFoundException("Hubs global csv not in resources.");

        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        br.readLine();
        String line;

        int rowNum = 1;
        while((line = br.readLine())!= null){
            rowNum++;
            if(line.trim().isEmpty()) continue;
            String[] parts = line.split(",", -1);
            if (parts.length < 4) continue;

            String hubId = titleCase(parts[0]);
            String province = titleCase(parts[1].trim());
            String sorting_hub = parts[2];
            if(sorting_hub.equalsIgnoreCase("Pretoria North")){
                province = "Gauteng";
            }
            boolean isActive = isActive(parts[3]);

            HubsGlobal hubsGlobal = new HubsGlobal(hubId, province, sorting_hub, isActive);
            hubs.add(hubsGlobal);
        }

        return hubs;

    }

    private String titleCase(String s){
        if (s.isEmpty()) return "Not provided";
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
