package wooteco.subway.maps.map.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.maps.line.application.LineService;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.line.dto.LineResponse;
import wooteco.subway.maps.line.dto.LineStationResponse;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.map.dto.CalculatedPathResponse;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponseAssembler;
import wooteco.subway.maps.station.application.StationService;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;

@Service
@Transactional
public class MapService {
    private LineService lineService;
    private StationService stationService;
    private PathService pathService;

    public MapService(LineService lineService, StationService stationService, PathService pathService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public MapResponse findMap() {
        List<Line> lines = lineService.findLines();
        Map<Long, Station> stations = findStations(lines);

        List<LineResponse> lineResponses = lines.stream()
            .map(it -> LineResponse.of(it, extractLineStationResponses(it, stations)))
            .collect(Collectors.toList());

        return new MapResponse(lineResponses);
    }

    public CalculatedPathResponse findPath(Long source, Long target, PathType type) {
        List<Line> lines = lineService.findLines();
        SubwayPath subwayPath = pathService.findPath(lines, source, target, type);
        Map<Long, Station> stations = stationService.findStationsByIds(subwayPath.extractStationId());
        int extraFare = lineService.getMaxExtraFareInLines(subwayPath.extractLineId());

        int totalFare = calculateFare(subwayPath, extraFare);

        return PathResponseAssembler.assemble(subwayPath, stations, totalFare);
    }

    private int calculateFare(final SubwayPath subwayPath, final int extraFare) {
        final int distance = subwayPath.calculateDistance();
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

    private Map<Long, Station> findStations(List<Line> lines) {
        List<Long> stationIds = lines.stream()
            .flatMap(it -> it.getStationInOrder().stream())
            .map(it -> it.getStationId())
            .collect(Collectors.toList());

        return stationService.findStationsByIds(stationIds);
    }

    private List<LineStationResponse> extractLineStationResponses(Line line, Map<Long, Station> stations) {
        return line.getStationInOrder().stream()
            .map(it -> LineStationResponse.of(line.getId(), it, StationResponse.of(stations.get(it.getStationId()))))
            .collect(Collectors.toList());
    }
}
