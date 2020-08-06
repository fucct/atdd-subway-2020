package wooteco.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.subway.maps.line.domain.LineStation;

class SubwayPathTest {
    private SubwayPath subwayPath1;
    private SubwayPath subwayPath2;

    @BeforeEach
    void setUp() {
        LineStationEdge lineStation1 = new LineStationEdge(new LineStation(1L, null, 0, 0), 1L);
        LineStationEdge lineStation2 = new LineStationEdge(new LineStation(2L, 1L, 3, 5), 1L);
        LineStationEdge lineStation3 = new LineStationEdge(new LineStation(3L, 2L, 5, 5), 1L);
        LineStationEdge lineStation4 = new LineStationEdge(new LineStation(4L, 3L, 5, 5), 1L);
        LineStationEdge lineStation5 = new LineStationEdge(new LineStation(2L, null, 0, 0), 2L);
        LineStationEdge lineStation6 = new LineStationEdge(new LineStation(5L, 2L, 0, 0), 2L);

        subwayPath1 = new SubwayPath(Lists.newArrayList(lineStation2, lineStation6));
        subwayPath2 = new SubwayPath(Lists.newArrayList(lineStation2, lineStation3, lineStation4));
    }

    @Test
    void extractStationId() {
        assertThat(subwayPath1.extractStationId()).contains(1L, 2L, 5L);
        assertThat(subwayPath2.extractStationId()).contains(1L, 2L, 3L, 4L);
    }

    @Test
    void extractLineId() {
        assertThat(subwayPath1.extractLineId()).contains(1L, 2L);
        assertThat(subwayPath1.extractLineId()).contains(1L);
    }

    @Test
    void calculateDistance() {
        assertThat(subwayPath1.calculateDistance()).isEqualTo(3);
        assertThat(subwayPath2.calculateDistance()).isEqualTo(13);
    }

    @Test
    void calculateDuration() {
        assertThat(subwayPath1.calculateDuration()).isEqualTo(5);
        assertThat(subwayPath2.calculateDuration()).isEqualTo(15);
    }

    @Test
    void calculateFare() {
        assertThat(subwayPath1.calculateFare(0)).isEqualTo(1250);
        assertThat(subwayPath2.calculateFare(0)).isEqualTo(1550);
    }
}
