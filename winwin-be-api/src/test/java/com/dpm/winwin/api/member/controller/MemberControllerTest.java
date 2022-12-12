package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.dto.response.RanksResponse;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import java.util.List;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.dpm.winwin.api.member.controller.MemberControllerTest.MEMBER_ID;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = MEMBER_ID, authorities = {"ROLE_USER"})
public class MemberControllerTest extends RestDocsTestSupport {

    public static final String MEMBER_ID = "1";
    @MockBean
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @Test
    void member_닉네임을_설정한다() throws Exception {

        Long memberId = Long.parseLong(MEMBER_ID);
        String nickname = "김감자";

        MemberNicknameRequest request = new MemberNicknameRequest(
                nickname
        );

        MemberNicknameResponse response = new MemberNicknameResponse(
                nickname
        );

        // when
        given(memberCommandService.updateMemberNickname(memberId, request))
                .willReturn(response);

        ResultActions result = mockMvc.perform(
                patch("/api/v1/members/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("수정하려는 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("성공 여부"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("수정된 닉네임")
                        )
                ));
    }

    @Test
    void member를_수정한다() throws Exception {

        Long memberId = Long.parseLong(MEMBER_ID);
        String nickname = "김감자";

        MemberUpdateRequest request = new MemberUpdateRequest(
                nickname,
                "depromeet/team2.png",
                "안녕하세요 김감자입니다.",
                "www.depromeet.com",
                emptyList(),
                emptyList()
        );

        MemberUpdateResponse response = new MemberUpdateResponse(
                nickname,
                "depromeet/team2.png",
                "안녕하세요 김감자입니다.",
                "www.depromeet.com",
                emptyList(),
                emptyList()
        );

        // when
        given(memberCommandService.updateMember(memberId, request))
                .willReturn(response);

        ResultActions result = mockMvc.perform(
                put("/api/v1/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("수정하려는 닉네임"),
                                fieldWithPath("image").type(JsonFieldType.STRING)
                                        .description("수정할 회원 프로필 사진"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .description("수정할 회원 소개"),
                                fieldWithPath("profileLink").type(JsonFieldType.STRING)
                                        .description("수정할 회원의 프로필 링크"),
                                fieldWithPath("givenTalents").type(JsonFieldType.ARRAY)
                                        .description("수정할 회원이 가진 재능"),
                                fieldWithPath("takenTalents").type(JsonFieldType.ARRAY)
                                        .description("수정할 회원이 줄 수 있는 재능")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("성공 여부"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("data.image").type(JsonFieldType.STRING)
                                        .description("회원 프로필 사진"),
                                fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                        .description("회원 소개"),
                                fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                        .description("회원의 프로필 링크"),
                                fieldWithPath("data.givenTalents").type(JsonFieldType.ARRAY)
                                        .description("회원이 가진 재능"),
                                fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY)
                                        .description("회원이 줄 수 있는 재능")
                        )
                ));
    }

    @Test
    void member를_조회한다() throws Exception {

        when(memberQueryService.readMemberInfo(any())).thenReturn(new MemberRankReadResponse(
                1L,
                "김감자",
                "depromeet/team2.png",
                "안녕하세요 김감자입니다.",
                "루키",
                "ranksImage",
                "1000",
                "www.depromeet.com",
                emptyList(),
                emptyList()
        ));

        mockMvc.perform(
                        get("/api/v1/members/{memberId}", 1L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                responseFields(
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("성공 여부"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                                .description("저장된 회원 id"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("닉네임"),
                                        fieldWithPath("data.image").type(JsonFieldType.STRING)
                                                .description("회원 프로필 사진"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                                .description("회원 소개"),
                                        fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                                                .description("회원 등급"),
                                        fieldWithPath("data.ranksImage").type(JsonFieldType.STRING)
                                                .description("회원 등급의 사진"),
                                        fieldWithPath("data.likeCount").type(JsonFieldType.STRING)
                                                .description("회원이 보유한 총 좋아요"),
                                        fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                                .description("회원의 프로필 링크"),
                                        fieldWithPath("data.givenTalents").type(JsonFieldType.ARRAY)
                                                .description("회원이 가진 재능"),
                                        fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY)
                                                .description("회원이 줄 수 있는 재능")
                                )
                        )
                )
        ;
    }

    @Test
    void 랭크_목록을_조회한다() throws Exception {
        // given
        Ranks rank = Ranks.ROOKIE;
        Ranks rank2 = Ranks.BEGINNER;

        RanksListResponse response = RanksListResponse.from(List.of(
            new RanksResponse(rank.getName(), rank.getImage(), rank.getCondition()),
            new RanksResponse(rank2.getName(), rank2.getImage(), rank2.getCondition())
        ));

        // when
        given(memberQueryService.getRankList())
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/v1/members/ranks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.ranks[].name").type(JsonFieldType.STRING).description("랭크 이름"),
                    fieldWithPath("data.ranks[].image").type(JsonFieldType.STRING).description("랭크 이미지"),
                    fieldWithPath("data.ranks[].condition").type(JsonFieldType.STRING).description("랭크 설명문")
                )
            ))
        ;
    }
}
