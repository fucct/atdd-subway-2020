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
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponseAssembler;
import wooteco.subway.maps.station.application.StationService;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.application.MemberService;
import wooteco.subway.members.member.domain.LoginMember;
import wooteco.subway.members.member.domain.Member;
import wooteco.subway.members.member.dto.MemberResponse;

@Service
@Transactional
public class MapService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathService pathService;
    private final MemberService memberService;

    public MapService(LineService lineService, StationService stationService, PathService pathService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    public MapResponse findMap() {
        List<Line> lines = lineService.findLines();
        Map<Long, Station> stations = findStations(lines);

        List<LineResponse> lineResponses = lines.stream()
            .map(it -> LineResponse.of(it, extractLineStationResponses(it, stations)))
            .collect(Collectors.toList());

        return new MapResponse(lineResponses);
    }

    public PathResponse findPath(Long source, Long target, PathType type, LoginMember loginMember) {
        final List<Line> lines = lineService.findLines();
        final SubwayPath subwayPath = pathService.findPath(lines, source, target, type);
        final Map<Long, Station> stations = stationService.findStationsByIds(subwayPath.extractStationId());
        try {
            final MemberResponse member = memberService.findMember(loginMember.getId());
            int totalFare = calculateTotalFare(subwayPath, member);

            return PathResponseAssembler.assemble(subwayPath, stations, totalFare);
        } catch (RuntimeException e) {
            int totalFare = calculateTotalFare(subwayPath);

            return PathResponseAssembler.assemble(subwayPath, stations, totalFare);
        }
    }

    private int calculateTotalFare(final SubwayPath subwayPath) {
        final int extraFare = lineService.getMaxExtraFareInLines(subwayPath.extractLineId());
        return subwayPath.calculateFare(extraFare);
    }
    private int calculateTotalFare(final SubwayPath subwayPath, final MemberResponse member) {
        final int extraFare = lineService.getMaxExtraFareInLines(subwayPath.extractLineId());
        final int totalFare = subwayPath.calculateFare(extraFare);
        return subwayPath.discountFare(totalFare, member.getAge());
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
