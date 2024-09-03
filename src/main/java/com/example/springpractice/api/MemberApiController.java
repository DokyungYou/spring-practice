package com.example.springpractice.api;

import com.example.springpractice.domain.Member;
import com.example.springpractice.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
