package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 서비스 개발
 **/

@Service //스프링빈 등록
@Transactional(readOnly = true) //기본은 읽기모드로 두고, 쓰기에 해당하는 회원가입만 새로 재정의
//@RequiredArgsConstructor //롬복-final이 붙은 애들만 생성자를 만들어줌.
//@RequiredArgsConstructor //롬복-final이 붙은 애들만 생성자를 만들어줌.
public class MemberService {

    //저장소 불러오기.
    private final MemberRepository memberRepository; //생성자 주입 방법으로 작성하자. -> final로 외부셋팅을 한 번 더 막자.

    @Autowired //생성자 주입 방법으로 작성하자. (생성자가 하나일 경우는 @Autowired 없어도 된다.)
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //==회원가입(등록)==//
    @Transactional //기본은 읽기모드로 두고, 쓰기에 해당하는 회원가입만 새로 재정의(기본 셋팅이 읽기모드 false임)
    public Long join(Member member) {
        validateDuplicateMember(member);//중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    //==중볻회원 검증==//
    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //==회원 전체 조회==//
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //==회원 한 데이터만 조회==//
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
