package co.wethinkcode.logisticsconnect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HubsGlobalTest {

    @Test
    void testConstructorAndGetters() {
        HubsGlobal hub = new HubsGlobal("H-502", "Gauteng", "Pretoria North", true);

        assertEquals("H-502", hub.getHubId());
        assertEquals("Gauteng", hub.getProvince());
        assertEquals("Pretoria North", hub.getSortingCenter());
        assertTrue(hub.isActive());
    }

    @Test
    void testSetters() {
        HubsGlobal hub = new HubsGlobal("H-502", "Gauteng", "Pretoria North", false);

        hub.setHubId("H-999");
        hub.setProvince("Western Cape");
        hub.setSortingCenter("Cape Town Port");
        hub.setActive(true);

        assertEquals("H-999", hub.getHubId());
        assertEquals("Western Cape", hub.getProvince());
        assertEquals("Cape Town Port", hub.getSortingCenter());
        assertTrue(hub.isActive());
    }

    @Test
    void testFalseActiveCase() {
        // This is your H-502 ,gauteng,Pretoria North,0 case
        HubsGlobal hub = new HubsGlobal("H-502", "Gauteng", "Pretoria North", false);
        assertFalse(hub.isActive());
    }

    @Test
    void testToStringContainsFields() {
        HubsGlobal hub = new HubsGlobal("H-505", "Western Cape", "Cape Town Port", true);
        String str = hub.toString();

        // toString should contain all important data for debugging
        assertTrue(str.contains("H-505"));
        assertTrue(str.contains("Western Cape"));
        assertTrue(str.contains("Cape Town Port"));
        assertTrue(str.contains("true"));
    }

    @Test
    void testModelWithCleanedData() {
        // Simulates what CsvCleaner should output after cleaning
        HubsGlobal cleaned = new HubsGlobal(
            "H-502", 
            "Gauteng", // was "gauteng"
            "Pretoria North", 
            false // was "0"
        );

        assertDoesNotThrow(() -> {
            cleaned.getHubId();
            cleaned.getProvince();
            cleaned.getSortingCenter();
            cleaned.isActive();
        });

        assertNotNull(cleaned.getHubId());
        assertNotNull(cleaned.getProvince());
        assertNotNull(cleaned.getSortingCenter());
    }
}