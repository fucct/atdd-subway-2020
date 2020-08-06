package wooteco.subway.maps.map.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import wooteco.subway.common.TestObjectUtils;
import wooteco.subway.maps.line.application.LineService;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.line.domain.LineStation;
import wooteco.subway.maps.map.domain.LineStationEdge;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.station.application.StationService;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.members.member.application.MemberService;
import wooteco.subway.members.member.domain.LoginMember;
import wooteco.subway.members.member.domain.Member;
import wooteco.subway.members.member.dto.MemberResponse;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {
    private MapService mapService;
    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;
    @Mock
    private PathService pathService;
    @Mock
    private MemberService memberService;

    private Map<Long, Station> stations;
    private List<Line> lines;
    private List<LoginMember> loginMembers;
    private List<Member> members;

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        line1.addLineStation(new LineStation(1L, null, 0, 0));
        LineStation lineStation2 = new LineStation(2L, 1L, 2, 2);
        line1.addLineStation(new LineStation(2L, 1L, 2, 2));

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 900);
        line2.addLineStation(new LineStation(2L, null, 0, 0));
        line2.addLineStation(new LineStation(3L, 2L, 2, 1));

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 600);
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        LineStation lineStation6 = new LineStation(4L, 1L, 1, 2);
        LineStation lineStation7 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation6);
        line3.addLineStation(lineStation7);

        lines = Lists.newArrayList(line1, line2, line3);

        List<LineStationEdge> lineStations = Lists.newArrayList(
            new LineStationEdge(lineStation6, line3.getId()),
            new LineStationEdge(lineStation7, line3.getId())
        );
        subwayPath = new SubwayPath(lineStations);

        mapService = new MapService(lineService, stationService, pathService, memberService);

        Member member1 = new Member("abc@abc.com", "123", 6);
        Member member2 = new Member("abc@abc.com", "123", 13);
        Member member3 = new Member( "abc@abc.com", "123", 19);
        members = Lists.newArrayList(member1, member2, member3);

        LoginMember loginMember1 = new LoginMember(1L,"abc@abc.com", "123", 6);
        LoginMember loginMember2 = new LoginMember(2L,"abc@abc.com", "123", 13);
        LoginMember loginMember3 = new LoginMember(3L, "abc@abc.com", "123", 19);
        loginMembers = Lists.newArrayList(loginMember1, loginMember2, loginMember3);
    }

    @Test
    void findPath() {
        when(lineService.findLines()).thenReturn(lines);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(subwayPath);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);

        PathResponse pathResponse = mapService.findPath(1L, 3L, PathType.DISTANCE, null);

        assertThat(pathResponse.getStations()).isNotEmpty();
        assertThat(pathResponse.getDuration()).isNotZero();
        assertThat(pathResponse.getDistance()).isNotZero();
        assertThat(pathResponse.getFare()).isNotZero();
    }

    @DisplayName("사용자의 나이에 따라 할인율이 적용됨을 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:750", "1:1200", "2:1850"}, delimiter = ':')
    void findPath2(int index, int fare) {
        when(lineService.findLines()).thenReturn(lines);
        when(lineService.getMaxExtraFareInLines(anyList())).thenReturn(600);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(subwayPath);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);
        when(memberService.findMember(anyLong())).thenReturn(MemberResponse.of(members.get(index)));

        final PathResponse path = mapService.findPath(1L, 3L, PathType.DISTANCE, loginMembers.get(index));
        assertThat(path.getFare()).isEqualTo(fare);
    }

    @Test
    void findMap() {
        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);

        MapResponse mapResponse = mapService.findMap();

        assertThat(mapResponse.getLineResponses()).hasSize(3);
    }
}
