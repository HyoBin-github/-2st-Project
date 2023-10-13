package org.spring.dev.company.service.approval;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.dev.company.controller.approval.request.ApprovalCreate;
import org.spring.dev.company.entity.approval.ApprovalEntity;
import org.spring.dev.company.entity.approval.ApprovalStatus;
import org.spring.dev.company.entity.approval.ProjectStatus;
import org.spring.dev.company.entity.member.MemberEntity;
import org.spring.dev.company.entity.util.ApproType;
import org.spring.dev.company.entity.util.GenderEntity;
import org.spring.dev.company.repository.approval.ApprovalRepository;
import org.spring.dev.company.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApprovalServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ApprovalRepository approvalRepository;

    @AfterEach
    public void clean() {
        approvalRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회사가 프로젝트에 필요한 프리랜서를 구한다.")
    public void test1() {
        // given
        MemberEntity member = memberSave("김회사", "0418", "010-0000-0000", "주소1", ApproType.COMPANY, GenderEntity.M);



        MemberEntity memberEntity = memberRepository.save(member);


        ApprovalCreate request = ApprovalCreate.builder()
                .title("프리랜서 구합니다.")
                .content("프로젝트 관련")
                .build();

        // when
        approvalService.create(memberEntity.getId(), request.toServiceRequest());

        // then
        ApprovalEntity response = approvalRepository.findAll().get(0);
        assertEquals("프리랜서 구합니다.", response.getTitle());
        assertEquals("프로젝트 관련", response.getContent());
        assertEquals(ApproType.FREELANCER, response.getType());
    }

    @Test
    @DisplayName("회사가 프리랜서를 구하는 문서에 프리랜서가 승인한다.")
    public void test2() {
        // given
        LocalDateTime now = LocalDateTime.now();

        MemberEntity company = memberSave("김회사", "0418", "010-0000-0000", "주소1", ApproType.COMPANY, GenderEntity.M);

        MemberEntity freelancer = memberSave("김프리랜서", "0418", "010-1111-1111", "주소2", ApproType.FREELANCER, GenderEntity.M);

        List<MemberEntity> memberEntities = memberRepository.saveAll(List.of(company, freelancer));


        ApprovalEntity approval = ApprovalEntity.builder()
                .title("프리랜서 구합니다.")
                .content("프로젝트 관련")
                .type(ApproType.FREELANCER)
                .memberEntity(memberEntities.get(0))
                .build();

        approvalRepository.save(approval);
        String request = "APPROVAL";
        // when
        approvalService.approval(memberEntities.get(1).getId(), approval.getId(), request, now);

        // then
        ApprovalEntity response = approvalRepository.findAll().get(0);

        assertEquals("프리랜서 구합니다.", response.getTitle());
        assertEquals("프로젝트 관련", response.getContent());
        assertEquals(ApproType.FREELANCER, response.getType());
        assertEquals(ApprovalStatus.APPROVAL, response.getStatus());
    }

    @Test
    @DisplayName("허가받지 않는 사람이 결재를 요청할 시 에러가 난다.")
    public void test3() {
        // given
        LocalDateTime now = LocalDateTime.now();

        MemberEntity company = memberSave("김회사", "0418", "010-0000-0000", "주소1", ApproType.COMPANY, GenderEntity.M);

        MemberEntity freelancer = memberSave("이회사", "0418", "010-1111-1111", "주소2", ApproType.COMPANY, GenderEntity.M);

        List<MemberEntity> memberEntities = memberRepository.saveAll(List.of(company, freelancer));

        ApprovalEntity approval = ApprovalEntity.builder()
                .title("프리랜서 구합니다.")
                .content("프로젝트 관련")
                .type(ApproType.FREELANCER)
                .memberEntity(memberEntities.get(0))
                .build();

        approvalRepository.save(approval);

        String request = "APPROVAL";

        // expect
        assertThrows(IllegalArgumentException.class, () -> approvalService.approval(memberEntities.get(0).getId(), approval.getId(), request, now));

    }

    @Test
    @DisplayName("부장이 인턴의 결재를 반려한다.")
    public void test4() {
        // given
        LocalDateTime now = LocalDateTime.now();
        MemberEntity company = memberSave("김회사", "0418", "010-0000-0000", "주소1", ApproType.COMPANY, GenderEntity.M);

        MemberEntity freelancer = memberSave("김프리랜서", "0418", "010-1111-1111", "주소2", ApproType.FREELANCER, GenderEntity.M);

        List<MemberEntity> memberEntities = memberRepository.saveAll(List.of(company, freelancer));

        ApprovalEntity approval = ApprovalEntity.builder()
                .title("프리랜서 구합니다.")
                .content("프로젝트 관련")
                .type(ApproType.FREELANCER)
                .memberEntity(memberEntities.get(0))
                .build();

        approvalRepository.save(approval);

        String request = "UNAUTHORIZED";
        // when
        approvalService.approval(memberEntities.get(1).getId(), approval.getId(), request, now);

        // then
        ApprovalEntity response = approvalRepository.findAll().get(0);
        assertEquals(ApprovalStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    @DisplayName("공문을 올릴 때 ProjectStatus이 Null이면  ProjectStatus는 미완성 상태가 된다..")
    public void test5() {
        // given
        MemberEntity member = memberSave("김회사", "0418", "010-0000-0000", "주소1", ApproType.COMPANY, GenderEntity.M);



        MemberEntity memberEntity = memberRepository.save(member);


        ApprovalCreate request = ApprovalCreate.builder()
                .title("프리랜서 구합니다.")
                .content("프로젝트 관련")
                .build();

        // when
        approvalService.create(memberEntity.getId(), request.toServiceRequest());

        // then
        ApprovalEntity response = approvalRepository.findAll().get(0);
        assertEquals(ProjectStatus.INCOMPLETE, response.getProjectStatus());
    }

    @Test
    @Transactional
    @DisplayName("프리랜서가 결재버튼을 눌렀을 때 결재 시간이 등록된다.")
    public void test6() {
        LocalDateTime now = LocalDateTime.now();

        MemberEntity company = memberSave("김회사", "0418", "010-0000-0000", "주소1", ApproType.COMPANY, GenderEntity.M);

        MemberEntity freelancer = memberSave("김프리랜서", "0418", "010-1111-1111", "주소2", ApproType.FREELANCER, GenderEntity.M);

        List<MemberEntity> memberEntities = memberRepository.saveAll(List.of(company, freelancer));


        ApprovalEntity approval = ApprovalEntity.builder()
                .title("프리랜서 구합니다.")
                .content("프로젝트 관련")
                .type(ApproType.FREELANCER)
                .memberEntity(memberEntities.get(0))
                .build();

        approvalRepository.save(approval);
        String request = "APPROVAL";

        // when
        approvalService.approval(memberEntities.get(1).getId(), approval.getId(), request, now);

        // then
        ApprovalEntity response = approvalRepository.findAll().get(0);

        assertEquals(now, approval.getStart());
    }


    private static MemberEntity memberSave(String name, String birth, String phone, String address, ApproType type, GenderEntity gender) {
        return MemberEntity.builder()
                .name(name)
                .birth(birth)
                .phone(phone)
                .address(address)
                .grade(type)
                .gender(gender)
                .build();
    }
}