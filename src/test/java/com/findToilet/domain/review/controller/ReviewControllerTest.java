package com.findToilet.domain.review.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.review.dto.ReviewCreateRequest;
import com.findToilet.domain.review.dto.ReviewDto;
import com.findToilet.domain.review.entity.Review;
import com.findToilet.domain.review.service.ReviewService;
import com.findToilet.domain.toilet.entity.Toilet;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewControllerTest {

    private final String REVIEW_DEFAULT_URI = "/api/reviews";

    @Autowired
    private Gson gson;

    @MockBean
    private ReviewService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("리뷰를 작성한다.")
    void createReview() throws Exception {
        //given
        ReviewCreateRequest request = new ReviewCreateRequest("good", 5D, 1L);
        String jsonData = gson.toJson(request);

        doNothing().when(service).create(request);

        //when
        mockMvc.perform(
                        post(REVIEW_DEFAULT_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonData))
                //then
                .andExpect(status().isCreated())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("리뷰 등록 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Review")
                                                .description("리뷰 등록")
                                                .requestFields(
                                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                                        fieldWithPath("toiletId").type(JsonFieldType.NUMBER).description("화장실 id"))
                                                .responseFields()
                                                .build())));
    }

    @Test
    @DisplayName("리뷰를 조회한다.")
    void readAllReview() throws Exception {
        ReviewDto dto = ReviewDto.of(Review.builder().id(1L).content("content1").score(5d).toilet(Toilet.builder().id(1L).build()).member(Member.builder().nickname("finebears").build()).build());
        dto.setCreatedAt(LocalDateTime.now());
        given(service.readAll(1L)).willReturn(List.of(dto));

        //when
        mockMvc.perform(
                        get(REVIEW_DEFAULT_URI + "/{id}", 1L))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("리뷰 조회 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Review")
                                                .description("리뷰 조회")
                                                .requestFields()
                                                .responseFields(
                                                        List.of(fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("리뷰 식별자"),
                                                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("리뷰 내용"),
                                                                fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("리뷰 별점"),
                                                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("리뷰 생성 일자"),
                                                                fieldWithPath("data[].toiletId").type(JsonFieldType.NUMBER).description("화장실 식별자"),
                                                                fieldWithPath("data[].memberNickname").type(JsonFieldType.STRING).description("회원 닉네임")))
                                                .build())));
    }

    @Test
    @DisplayName("리뷰를 삭제한다.")
    void deleteReview() throws Exception {
        //given
        doNothing().when(service).delete(1L);

        //when
        mockMvc.perform(
                        delete(REVIEW_DEFAULT_URI + "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("리뷰 삭제 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Review")
                                                .description("리뷰 삭제")
                                                .requestFields()
                                                .responseFields()
                                                .build())));
    }


}
