package wooteco.subway.maps.map.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {
    private static final int DEFAULT_FARE = 1250;
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
        final DistanceType distanceType = DistanceType.of(distance);
        return distanceType.calculate(distance, extraFare);
    }

    public int discountFare(final int fare, final int memberAge) {
        final AgeType ageType = AgeType.of(memberAge);

        return ageType.calculate(fare);
    }
}
