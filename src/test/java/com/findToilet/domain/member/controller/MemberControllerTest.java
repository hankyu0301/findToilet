package com.findToilet.domain.member.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.findToilet.domain.member.dto.MemberCreateRequest;
import com.findToilet.domain.member.dto.MemberDto;
import com.findToilet.domain.member.dto.MemberUpdateRequest;
import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.member.repository.MemberRepository;
import com.findToilet.domain.member.service.MemberService;
import com.findToilet.global.auth.dto.LoginDto;
import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.helper.StubData;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberControllerTest {
    private final String MEMBER_DEFAULT_URI = "/api/members";

    @Autowired
    private Gson gson;
    @MockBean
    private MemberService service;

    @Autowired
    private MemberRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String accessTokenForUser;

    @Autowired
    private JwtTokenizer jwtTokenizer;
    @BeforeAll
    public void init() {
        accessTokenForUser = StubData.MockSecurity.getValidAccessToken(jwtTokenizer.getSecretKey());
    }

    @Test
    @DisplayName("회원 가입을 한다.")
    void registerMember() throws Exception {
        //given
        MemberCreateRequest request = new MemberCreateRequest("finebears@naver.com", "123456a!", "finebears");
        String jsonData = gson.toJson(request);

        doNothing().when(service).register(request);

        //when
        mockMvc.perform(
                        post(MEMBER_DEFAULT_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonData))
                //then
                .andExpect(status().isCreated())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 가입 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Member")
                                                .description("회원 등록")
                                                .requestFields(
                                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀 번호"),
                                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"))
                                                .responseFields()
                                                .build())));
    }

    @Test
    @DisplayName("로그인을 한다.")
    void loginMember() throws Exception {
        //given
        LoginDto request = new LoginDto("finebears@naver.com", "123456a!");
        String jsonData = gson.toJson(request);

        Member member = Member.builder()
                .email("finebears@naver.com")
                .password(passwordEncoder.encode("123456a!"))
                .nickname("finebears")
                .role(Member.Role.USER)
                .build();

        repository.save(member);

        //when
        mockMvc.perform(
                        post(MEMBER_DEFAULT_URI + "/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonData))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 로그인 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Member")
                                                .description("회원 로그인")
                                                .requestFields(
                                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀 번호")
                                                )
                                                .responseHeaders(
                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .build())));
    }

    @Test
    @DisplayName("회원정보를 조회한다.")
    void readMember() throws Exception {
        MemberDto dto = MemberDto.of(Member.builder().id(1L).nickname("finebears").email("finebears@naver.com").role(Member.Role.USER).build());
        given(service.readMember(anyLong())).willReturn(dto);

        //when
        ResultActions actions = mockMvc.perform(
                        get(MEMBER_DEFAULT_URI + "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(dto.getId()))
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 조회 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Member")
                                                .description("회원 조회")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .responseFields(
                                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                                        fieldWithPath("data.role").type(JsonFieldType.STRING).description("회원 권한"))
                                                .build())));
    }

    @Test
    @DisplayName("회원정보를 수정한다.")
    void updateMember() throws Exception {
        //given
        MemberUpdateRequest request = new MemberUpdateRequest("updatedNickname");
        String jsonData = gson.toJson(request);


        doNothing().when(service).updateMember(1L, request);

        mockMvc.perform(
                        put(MEMBER_DEFAULT_URI + "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser)
                                .content(jsonData))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 수정 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Member")
                                                .description("회원 수정")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .requestFields(
                                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("수정할 닉네임"))
                                                .build())));
    }

    @Test
    @DisplayName("회원을 탈퇴한다.")
    void deleteMember() throws Exception {
        //given
        doNothing().when(service).deleteMember(anyLong());

        //when
        mockMvc.perform(
                        delete(MEMBER_DEFAULT_URI + "/{id}", 1L)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenForUser)
                                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 삭제 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Member")
                                                .description("회원 삭제")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .responseFields()
                                                .build())));
    }

    @Test
    @DisplayName("로그아웃을 한다.")
    void postLogoutMember() throws Exception {
        String logoutAccessToken = StubData.MockSecurity.getLogoutValidAccessToken(jwtTokenizer.getSecretKey());

        //when
        mockMvc.perform(
                        get(MEMBER_DEFAULT_URI + "/logout")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + logoutAccessToken))
                //then
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 로그아웃 예제",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Member")
                                                .description("회원 로그아웃")
                                                .requestHeaders(
                                                        headerWithName("Authorization").description("발급받은 인증 토큰"))
                                                .build())));
    }


}