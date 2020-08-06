package wooteco.subway.maps.map.dto;

import java.util.List;

import wooteco.subway.maps.station.dto.StationResponse;

public class CalculatedPathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;
    private int fare;

    public CalculatedPathResponse() {
    }

    public CalculatedPathResponse(List<StationResponse> stations, int duration, int distance, int fare) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
