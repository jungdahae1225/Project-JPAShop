package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

//기본적으로 api와 화면 구성 코드들은 공통처리 부분이 상이하기 때문에 패키지 단위로 분리 해주는 것이 좋다
@RestController //@Controller@ResponseBody 이 둘을 합친 어노테이션- @ResponseBody는 데이터를 바로 json이나 xml로 보내고자 할때 씀.
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1(){ //엔티티를 직접 노출하는 방법(@JsonIgnore 어노테이션으로 해결. -> 궁극적인 해결책이 아니고, 기타 다른 문제를 야기한다.)
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){ //엔티티를 직접 노출하는 방법(@JsonIgnore 어노테이션으로 해결. -> 궁극적인 해결책이 아니고, 기타 다른 문제를 야기한다.)
        List<Member> findMembers = memberService.findMembers();

        /**..JAVA8문법..**/
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name; //단순하게 name만 넘겨 받는 api를 원한다고 가정하자. (body에 name만 노출시키기)
    }

    /***
     * API를 만들 때 스펙에 맞는 DTO를 따로 만들고 활용할 것.
     * */
    @Data
    @AllArgsConstructor
    static class Result<T> { //제네릭으로 작성하여 body의 데이터에 대한 유연성을 살릴 수 있다.
        private T data;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) { //json으로 온 body를 파라미터인 member에 넣어준다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2 (@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberResponseV2(
            @PathVariable("id")Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());

    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    /***
     * API를 만들 때 스펙에 맞는 DTO를 따로 만들고 활용할 것.
     * */
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
