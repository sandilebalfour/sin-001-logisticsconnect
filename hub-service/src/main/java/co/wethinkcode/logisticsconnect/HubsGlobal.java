package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HubsGlobal {
    private String hubId;
    private String province;
    private String sortingCenter;
    private boolean active;

    public HubsGlobal() {}

    public HubsGlobal(
            @JsonProperty("hubId") String hubId,
            @JsonProperty("province") String province,
            @JsonProperty("sortingCenter") String sortingCenter,
            @JsonProperty("active") boolean active) {
        this.hubId = hubId;
        this.province = province;
        this.sortingCenter = sortingCenter;
        this.active = active;
    }

    public String getHubId() { return hubId; }
    public void setHubId(String hubId) { this.hubId = hubId; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getSortingCenter() { return sortingCenter; }
    public void setSortingCenter(String sortingCenter) { this.sortingCenter = sortingCenter; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}