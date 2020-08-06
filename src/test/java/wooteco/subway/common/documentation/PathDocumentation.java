package wooteco.subway.common.documentation;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static wooteco.subway.common.documentation.Documentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class PathDocumentation {
    public static RestDocumentationResultHandler findPath() {
        return document("paths/find",
            getDocumentRequest(),
            getDocumentResponse(),
            requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("최단경로 계산 타입")
            ),
            responseFields(
                fieldWithPath("stations").type(ARRAY).description("경로 역 정보"),
                fieldWithPath("stations[].id").type(NUMBER).description("역 ID"),
                fieldWithPath("stations[].name").type(STRING).description("역 이름"),
                fieldWithPath("duration").type(NUMBER).description("총 시간"),
                fieldWithPath("distance").type(NUMBER).description("총 거리"),
                fieldWithPath("fare").type(NUMBER).description("총 운임")
            )
        );
    }

    public static RestDocumentationResultHandler findPathWithLogin() {
        return document("paths/find-with-login",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description("유저 토큰 정보")
            ),
            requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("최단경로 계산 타입")
            ),
            responseFields(
                fieldWithPath("stations").type(ARRAY).description("경로 역 정보"),
                fieldWithPath("stations[].id").type(NUMBER).description("역 ID"),
                fieldWithPath("stations[].name").type(STRING).description("역 이름"),
                fieldWithPath("duration").type(NUMBER).description("총 시간"),
                fieldWithPath("distance").type(NUMBER).description("총 거리"),
                fieldWithPath("fare").type(NUMBER).description("총 운임")
            )
        );
    }
}
