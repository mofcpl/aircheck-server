package pl.aircheck.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.data")
public class Config {
    private final String originUrl;
    private final String stationEndpoint;
    private final String sensorEndpoint;
    private final String dataEndpoint;

    private final String summaryEndpoint;
    private final int expirationStations;
    private final int expirationSensors;
    private final int expirationSummary;
    private final int expirationData;

    public Config(String originUrl, String stationEndpoint, String sensorEndpoint, String dataEndpoint, String summaryEndpoint, int expirationStations, int expirationSensors, int expirationSummary, int expirationData) {
        this.originUrl = originUrl;
        this.stationEndpoint = stationEndpoint;
        this.sensorEndpoint = sensorEndpoint;
        this.dataEndpoint = dataEndpoint;
        this.summaryEndpoint = summaryEndpoint;
        this.expirationStations = expirationStations;
        this.expirationSensors = expirationSensors;
        this.expirationSummary = expirationSummary;
        this.expirationData = expirationData;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public int getExpirationStations() {
        return expirationStations;
    }

    public int getExpirationSensors() {
        return expirationSensors;
    }

    public int getExpirationSummary() {
        return expirationSummary;
    }

    public int getExpirationData() {
        return expirationData;
    }

    public String getStationEndpoint() {
        return stationEndpoint;
    }

    public String getSensorEndpoint() {
        return sensorEndpoint;
    }

    public String getDataEndpoint() {
        return dataEndpoint;
    }

    public String getSummaryEndpoint() {
        return summaryEndpoint;
    }
}
