package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); // 컨트롤러에서 뷰로 넘어갈 때 이 데이터를 심어서 넘긴다.
        return "members/createMemberForm";
    }
    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm form, BindingResult result) { //BindingResult result -> 오류가 있으면 원래 에러 코드들이 뜨지만, 이건 스프링에서 result에 오류를 담아 아래 코드 실행 하게 됨.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(),
                form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/"; //저장 성공하면 홈으로 돌아가기.
    }

    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers(); //모든 멤버 조회
        model.addAttribute("members", members); //조회 한 거를 모델에 담아서
        return "members/memberList"; //화면에 넘긴다.
    }
}