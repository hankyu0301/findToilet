package com.findToilet.domain.toilet.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.findToilet.domain.toilet.dto.*;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import com.findToilet.domain.toilet.service.ToiletService;
import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.helper.StubData;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ToiletControllerTest {

    private final String TOILET_DEFAULT_URI = "/api/toilets";

    @Autowired
    private Gson gson;

    @MockBean
    private ToiletService service;

    @MockBean
    private ToiletRepository toiletRepository;

    @Autowired
    private MockMvc mockMvc;

    private String accessTokenForUser;

    @Autowired
    private JwtTokenizer jwtTokenizer;
    @BeforeAll
    public void init() {
        accessTokenForUser = StubData.MockSecurity.getValidAdminAccessToken(jwtTokenizer.getSecretKey());
    }

    @Test
    @DisplayName("화장실을 등록한다.")
    void createToilet() throws Exception {

        //given
        ToiletCreateRequest request = ToiletCreateRequest.builder()
                .name("testToilet")
                .road_address("test_Road_Address")
                .address("test_Address")
                .latitude(123.000)
                .longitude(30.000)
                .male_disabled(false)
                .female_disabled(false)
                .male_kids(false)
                .female_kids(false)
                .diaper(false)
                .operation_time("everyday")
                .build();

        String jsonData = gson.toJson(request);

        doNothing().when(service).create(request);

        //when
        mockMvc.perform(
                        post(TOILET_DEFAULT_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser)
                                .content(jsonData))
                //then
                .andExpect(status().isCreated())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("화장실 등록 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Toilet")
                                                .description("화장실 등록")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .requestFields(
                                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 id"),
                                                        fieldWithPath("road_address").type(JsonFieldType.STRING).description("도로명 주소"),
                                                        fieldWithPath("address").type(JsonFieldType.STRING).description("지번 주소"),
                                                        fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                                        fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                                        fieldWithPath("male_disabled").type(JsonFieldType.BOOLEAN).description("남성 장애인용 변기 유무"),
                                                        fieldWithPath("female_disabled").type(JsonFieldType.BOOLEAN).description("여성 장애인용 변기 유무"),
                                                        fieldWithPath("male_kids").type(JsonFieldType.BOOLEAN).description("남성 유아용 변기 유무"),
                                                        fieldWithPath("female_kids").type(JsonFieldType.BOOLEAN).description("여성 유아용 변기 유무"),
                                                        fieldWithPath("diaper").type(JsonFieldType.BOOLEAN).description("기저귀 교환대 유무"),
                                                        fieldWithPath("operation_time").type(JsonFieldType.STRING).description("화장실 운영 시간"))
                                                .responseFields()
                                                .build())));
    }

    @Test
    @DisplayName("화장실을 조회한다.")
    void readToilet() throws Exception {

        ToiletReadResponseDto result = ToiletReadResponseDto.builder()
                .id(1L)
                .name("testName")
                .road_address("우리집")
                .male_disabled(false)
                .female_disabled(false)
                .male_kids(false)
                .female_kids(false)
                .diaper(false)
                .operation_time("everyday")
                .score(4.5)
                .scoreCount(99L)
                .build();


        given(service.read(1L)).willReturn(result);

        //when
        mockMvc.perform(
                        get(TOILET_DEFAULT_URI + "/{id}", 1L)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser))

                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("화장실 조회 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Toilet")
                                                .description("화장실 조회")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .responseFields(
                                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 id"),
                                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원 이름"),
                                                        fieldWithPath("data.road_address").type(JsonFieldType.STRING).description("도로명 주소"),
                                                        fieldWithPath("data.disabled").type(JsonFieldType.BOOLEAN).description("장애인용 변기 유무"),
                                                        fieldWithPath("data.kids").type(JsonFieldType.BOOLEAN).description("유아용 변기 유무"),
                                                        fieldWithPath("data.diaper").type(JsonFieldType.BOOLEAN).description("기저귀 교환대 유무"),
                                                        fieldWithPath("data.score").type(JsonFieldType.NUMBER).description("화장실 평점"),
                                                        fieldWithPath("data.scoreCount").type(JsonFieldType.NUMBER).description("평점 갯수"),
                                                        fieldWithPath("data.operation_time").type(JsonFieldType.STRING).description("화장실 운영 시간"))
                                        .build())));
    }

    @Test
    @DisplayName("화장실 목록을 조회한다.")
    void findAllByCondition() throws Exception {
        //given
        ToiletSearchCondition condition = ToiletSearchCondition.builder()
                .page(0)
                .size(20)
                .userLatitude(37.445620228619)
                .userLongitude(126.65182310263)
                .limit(10000.0) // m
                .disabled(false)
                .kids(false)
                .diaper(false)
                .build();

        Page<ToiletDto> toiletDtoPage = new PageImpl<>(List.of(ToiletDto.builder()
                .id(1L)
                .name("testName")
                .road_address("우리집")
                .distance(0.0)
                .male_disabled(false)
                .female_disabled(false)
                .male_kids(false)
                .female_kids(false)
                .diaper(false)
                .operation_time("everyday")
                .score(4.5)
                .scoreCount(99L)
                .build()), Pageable.ofSize(condition.getSize()), 1L);

        ToiletListDto result = ToiletListDto.toDto(toiletDtoPage);

        given(service.findAllByCondition(any(ToiletSearchCondition.class))).willReturn(result);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(condition.getPage()));
        params.add("size", String.valueOf(condition.getSize()));
        params.add("userLatitude", String.valueOf(condition.getUserLatitude()));
        params.add("userLongitude", String.valueOf(condition.getUserLongitude()));
        params.add("limit", String.valueOf(condition.getLimit()));
        params.add("disabled", String.valueOf(condition.getDisabled()));
        params.add("kids", String.valueOf(condition.getKids()));
        params.add("diaper", String.valueOf(condition.getDiaper()));

        mockMvc.perform(
                get(TOILET_DEFAULT_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser)
                        .accept(MediaType.APPLICATION_JSON)
                        .params(params))

                .andExpect(status().isOk()).andDo(
                        MockMvcRestDocumentationWrapper.document("화장실 목록 조회 예제",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Toilet")
                                        .description("화장실 목록 조회")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("발급받은 인증 토큰"))
                                        .requestParameters(
                                            parameterWithName("page").description("Page 번호"),
                                            parameterWithName("size").description("Page Size"),
                                            parameterWithName("userLatitude").description("사용자 위도, ex)37.566826004661"),
                                            parameterWithName("userLongitude").description("사용자 경도, ex)126.978652258309"),
                                            parameterWithName("limit").description("탐색하는 거리"),
                                            parameterWithName("disabled").description("장애인용 변기 유무").optional(),
                                            parameterWithName("kids").description("유아용 변기 유무").optional(),
                                            parameterWithName("diaper").description("기저귀 교환대 유무").optional()
                                        )
                                        .responseFields(
                                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 화장실 갯수"),
                                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                                fieldWithPath("data.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                                                fieldWithPath("data.toiletDtoList[].id").type(JsonFieldType.NUMBER).description("화장실 id"),
                                                fieldWithPath("data.toiletDtoList[].name").type(JsonFieldType.STRING).description("화장실 이름"),
                                                fieldWithPath("data.toiletDtoList[].road_address").type(JsonFieldType.STRING).description("도로명 주소"),
                                                fieldWithPath("data.toiletDtoList[].distance").type(JsonFieldType.NUMBER).description("화장실 까지의 거리"),
                                                fieldWithPath("data.toiletDtoList[].disabled").type(JsonFieldType.BOOLEAN).description("장애인용 변기 유무"),
                                                fieldWithPath("data.toiletDtoList[].kids").type(JsonFieldType.BOOLEAN).description("유아용 변기 유무"),
                                                fieldWithPath("data.toiletDtoList[].diaper").type(JsonFieldType.BOOLEAN).description("기저귀 교환대 유무"),
                                                fieldWithPath("data.toiletDtoList[].operation_time").type(JsonFieldType.STRING).description("화장실 운영 시간"),
                                                fieldWithPath("data.toiletDtoList[].score").type(JsonFieldType.NUMBER).description("화장실 평점"),
                                                fieldWithPath("data.toiletDtoList[].scoreCount").type(JsonFieldType.NUMBER).description("평점 갯수"))
                                        .build())));
    }

    @Test
    @DisplayName("화장실을 수정한다.")
    void updateToilet() throws Exception {

        ToiletUpdateRequest request = ToiletUpdateRequest.builder()
                .name("우리집")
                .road_address("우리집")
                .address("우리집")
                .operation_time("상시")
                .build();

        String jsonData = gson.toJson(request);

        doNothing().when(service).update(1L, request);

        //when
        mockMvc.perform(
                        put(TOILET_DEFAULT_URI + "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser)
                                .content(jsonData))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("화장실 조회 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Toilet")
                                                .description("화장실 조회")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .requestFields(
                                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                                        fieldWithPath("road_address").type(JsonFieldType.STRING).description("도로명 주소"),
                                                        fieldWithPath("address").type(JsonFieldType.STRING).description("지번 주소"),
                                                        fieldWithPath("operation_time").type(JsonFieldType.STRING).description("화장실 운영 시간"))
                                                .build())));
    }

    @Test
    @DisplayName("화장실을 삭제한다.")
    void deleteToilet() throws Exception {

        doNothing().when(service).delete(1L);

        //when
        mockMvc.perform(
                        delete(TOILET_DEFAULT_URI + "/{id}", 1L)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("화장실 삭제 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Toilet")
                                                .description("화장실 삭제")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .build())));
    }
}
