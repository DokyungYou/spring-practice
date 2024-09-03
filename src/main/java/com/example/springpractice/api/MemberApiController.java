package com.example.springpractice.api;

import com.example.springpractice.domain.Member;
import com.example.springpractice.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController  // @ResponseBody + @Controller
public class MemberApiController {

    private final MemberService memberService;


    @PostMapping("/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){ //json 으로 온 body를 member에 매핑
        Long joinId = memberService.join(member);
        return new CreateMemberResponse(joinId);
    }

    /**
     * API 스펙을 위한 별도의 Data Transfer Object 인 DTO를 만듬
     *
     * - 엔티티와 프레젠테이션 계층 위한 로직 분리
     * - 엔티티와 API 스펙을 명확하게 분리
     * - 엔티티가 변해도 API 스펙이 변하지 않음
     */
    @PostMapping("/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Long joinId = memberService.join(request.toMember());
        return new CreateMemberResponse(joinId);
    }

    /**
     * 해당 실습에서는 PUT 방식을 썻으나
     * 부분 업데이트 시에는 PATCH 나 POST 사용하는 것이 REST 스타일에 적절
     */
    @PutMapping("/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        // 데이터 수정 시에는 가극적 변경 감지 활용

        /*단순하게 pk 하나 찍어서 조회하는 것 정도는 특별하게 트래픽이 많은 api가 아니면 이슈가 안되기 때문에,
         커맨드와 쿼리를 아래와 같이 분리하는 스타일로 개발하면
         유지보수성이 많이 증대가 된다고 한다.*/
        memberService.updateMember(id, request.getName());
        Member updateMember = memberService.findOne(id);

        return new UpdateMemberResponse(updateMember.getId(), updateMember.getName());
    }


    @Getter @Setter
    @NoArgsConstructor
    static class CreateMemberRequest {

        @NotBlank
        private String name;

        public Member toMember(){
            Member member = new Member();
            member.setName(name);
            return member;
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }


    @Getter @Setter
    @NoArgsConstructor
    static class UpdateMemberRequest {
        @NotBlank
        private String name;
    }

    @Getter @Setter
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
