package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) //강의에서 junit4 기준으로 설명 => 나는 junit5를 쓸 것임.
@SpringBootTest
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false) // test코드에서도 롤백으로 쿼리 안날리고 그냥 실행하도록 하는 것.
    public void 회원가입() throws Exception {
        //given 이러한 상황이 주어졌을 떄
        Member member = new Member();
        member.setName("kim");
        
        //when 이렇게 하면
        Long savedId = memberService.join(member);

        //then 결과가 이렇게 나와야 한다.
        em.flush(); //DB에 강제로 쿼리 날리기 위함
        Assert.assertEquals(member,memberRepository.findOne(savedId));
    }

    @Test
    public void 중복회원예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        try {
            memberService.join(member2);
        }catch (IllegalStateException e){
            return;
        }

        //then
        fail("예외가 발생해야 합니다.");
    }




}