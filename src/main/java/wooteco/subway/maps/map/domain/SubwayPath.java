package wooteco.subway.maps.map.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    private List<LineStationEdge> lineStationEdges;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        this.lineStationEdges = lineStationEdges;
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists.newArrayList(lineStationEdges.get(0).getLineStation().getPreStationId());
        stationIds.addAll(lineStationEdges.stream()
                .map(it -> it.getLineStation().getStationId())
                .collect(Collectors.toList()));

        return stationIds;
    }

    public List<Long> extractLineId() {
        List<Long> lineIds = lineStationEdges.stream()
            .map(LineStationEdge::getLineId)
            .collect(Collectors.toList());

        return lineIds;
    }

    public int calculateDuration() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    public int calculateFare(final int extraFare) {
        final int distance = this.calculateDistance();
        if (distance == 0) {
            return 0;
        }
        if (distance <= 10) {
            return 1250 + extraFare;
        }
        if (distance <= 50) {
            return (int)((Math.ceil((distance - 1) / 5) + 1) * 100) + extraFare;
        }
        return (int)((Math.ceil((distance - 1) / 8) + 1) * 100) + extraFare;
    }

    public int discountFare(final int fare, final int memberAge) {
        if (memberAge >= 13 && memberAge < 19) {
            return (int)((fare - 350) * 0.8);
        }
        if (memberAge >= 6 && memberAge < 13) {
            return (int)((fare - 350) * 0.5);
        }
        return fare;
    }
}
