package wooteco.subway.maps.map.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.common.documentation.PathDocumentation;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.station.dto.StationResponse;

@WebMvcTest(controllers = MapController.class)
@ExtendWith(RestDocumentationExtension.class)
public class MapControllerTest extends Documentation {

    @MockBean
    private MapService mapService;

    private MockMvc mockMvc;

    protected TokenResponse tokenResponse;

    private List<StationResponse> stations;
    private int duration;
    private int distance;
    private int fare;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
        tokenResponse = new TokenResponse("token");
        stations = Lists.newArrayList(new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()), new StationResponse(2L, "판교역", LocalDateTime.now(), LocalDateTime.now()));
        duration = 15;
        distance = 3;
        fare = 1850;
    }

    @DisplayName("로그인 하지 않은 사용자가 Path를 조회한다.")
    @Test
    void findPath() throws Exception {
        when(mapService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(new PathResponse(stations, duration, distance, fare));

        mockMvc.perform(get("/paths")
            .queryParam("source", "1")
            .queryParam("target", "2")
            .queryParam("type", "DISTANCE"))
            .andExpect(status().isOk())
            .andDo(PathDocumentation.findPath());
    }

    @DisplayName("로그인 한 사용자가 Path를 조회한다.")
    @Test
    void findPath2() throws Exception {
        when(mapService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(new PathResponse(stations, duration, distance, fare));

        mockMvc.perform(get("/paths")
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .queryParam("source", "1")
            .queryParam("target", "1")
            .queryParam("type", "DISTANCE"))
            .andExpect(status().isOk())
            .andDo(PathDocumentation.findPathWithLogin());
    }

    @Test
    void findMap() {
        MapService mapService = mock(MapService.class);
        MapController controller = new MapController(mapService);

        ResponseEntity entity = controller.findMap();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(mapService).findMap();
    }
}
