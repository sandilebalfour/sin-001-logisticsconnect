package co.wethinkcode.logisticsconnect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvCleanerTest {

    private CsvCleaner cleaner;

    @BeforeEach
    void setUp() {
        cleaner = new CsvCleaner();
    }

    @Test
    void testLoadAndCleanNotEmpty() throws Exception {
        List<HubsGlobal> hubs = cleaner.loadAndClean();
        assertFalse(hubs.isEmpty(), "Cleaned list should not be empty");
    }

    @Test
    void testTrimAndCasingAndDoubleSpace() throws Exception {
        List<HubsGlobal> hubs = cleaner.loadAndClean();

        HubsGlobal pretoria = hubs.stream()
                .filter(h -> h.getSortingCenter().equalsIgnoreCase("Pretoria North"))
                .findFirst().orElse(null);

        assertNotNull(pretoria);
        assertEquals("Gauteng", pretoria.getProvince());

        HubsGlobal capeTown = hubs.stream()
                .filter(h -> h.getSortingCenter().equalsIgnoreCase("Cape Town Port"))
                .findFirst().orElse(null);

        assertNotNull(capeTown, "Should have collapsed double space");
        assertEquals("Western Cape", capeTown.getProvince());
        assertTrue(capeTown.isActive());
    }

    @Test
    void testBooleanNormalization() throws Exception {
        List<HubsGlobal> hubs = cleaner.loadAndClean();
        // All actives should be proper boolean, not Y/N strings
        for (HubsGlobal h : hubs) {
            assertNotNull(h.isActive());
        }
    }

    @Test
    void testDeduplication_JohannesburgCentral() throws Exception {
        List<HubsGlobal> hubs = cleaner.loadAndClean();
        // H-500, H-504, H-510, H-515 all describe Johannesburg Central
        long jhbCount = hubs.stream()
                .filter(h -> h.getSortingCenter().equalsIgnoreCase("Johannesburg Central"))
                .count();

        assertEquals(1, jhbCount, "Johannesburg Central should be deduped to 1 record, got " + jhbCount);
    }

    @Test
    void testNoPlaceholderValues() throws Exception {
        List<HubsGlobal> hubs = cleaner.loadAndClean();
        for (HubsGlobal h : hubs) {
            assertFalse(h.getProvince().equalsIgnoreCase("N/A"));
            assertFalse(h.getProvince().equalsIgnoreCase("TBD"));
            assertFalse(h.getProvince().trim().isEmpty());
        }
    }

    @Test
    void testProvinceNormalization_PretoriaNorth() throws Exception {
        List<HubsGlobal> hubs = cleaner.loadAndClean();
        HubsGlobal pretoria = hubs.stream()
                .filter(h -> h.getSortingCenter().equalsIgnoreCase("Pretoria North"))
                .findFirst().orElse(null);
        if (pretoria != null) {
            assertEquals("Gauteng", pretoria.getProvince());
        }
    }
}