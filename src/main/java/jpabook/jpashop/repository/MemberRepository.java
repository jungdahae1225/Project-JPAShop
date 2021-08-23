package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 회원 리포지토리
 **/
@Repository //스프링 빈으로 등록
public class MemberRepository {
    
    @PersistenceContext // jpa EntityManager 스프링이 자동 주입
    private EntityManager em;

    public void save(Member member) { em.persist(member); }

    public Member findOne(long id) {
        return em.find(Member.class,id);
    }

    public List<Member> findAll() {
        return em.createQuery("select  m from Member m", Member.class)
            .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
