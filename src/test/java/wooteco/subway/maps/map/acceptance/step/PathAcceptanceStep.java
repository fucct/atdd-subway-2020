package wooteco.subway.maps.map.acceptance.step;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.members.member.acceptance.step.MemberAcceptanceStep.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.security.core.TokenResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.station.dto.StationResponse;

public class PathAcceptanceStep {
    public static ExtractableResponse<Response> 거리_경로_조회_요청(String type, long source, long target) {
        return RestAssured.given().log().all().
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type).
            then().
            log().all().
            extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청2(String type, long source, long target, String email, String password, int age) {
        final ExtractableResponse<Response> createResponse = 회원_등록되어_있음(email, password, age);
        final TokenResponse token = 로그인_되어_있음(email, password);

        return RestAssured.given().log().all().
            auth().oauth2(token.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type).
            then().
            log().all().
            extract();
    }

    public static ExtractableResponse<Response> 빠른_도착_경로_조회_요청(String type, long source, long target, String time) {
        return RestAssured.given().log().all().
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}&time={time}", source, target, type, time).
            then().
            log().all().
            extract();
    }

   public static void 적절한_경로를_응답(ExtractableResponse<Response> response, ArrayList<Long> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> stationIds = pathResponse.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPath);
    }

    public static void 총_거리와_소요_시간을_함께_응답함(ExtractableResponse<Response> response, int totalDistance, int totalDuration,
        int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
        assertThat(pathResponse.getDuration()).isEqualTo(totalDuration);
        assertThat(pathResponse.getFare()).isEqualTo(totalFare);

    }
}

