package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SubwayService;
import subway.dto.AddStationRequest;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.utils.TestUtils.toJson;

@WebMvcTest
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubwayService subwayService;

    @Test
    @DisplayName("createStation은 AddStationRequest를 요청받으면 역을 노선에 추가하면 CREATED 응답코드를 반환한다.")
    void createStation() throws Exception {
        AddStationRequest addStationRequest = new AddStationRequest("신림", "2호선", "잠실나루", "잠실", 5);

        doReturn(1L).when(subwayService.addStation(addStationRequest));

        mockMvc.perform(post("/line/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(addStationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/line/stations/1"));
    }
}