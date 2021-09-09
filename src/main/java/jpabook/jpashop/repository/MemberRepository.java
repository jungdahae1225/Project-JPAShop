package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 회원 리포지토리
 **/
@Repository //스프링 빈으로 등록
@RequiredArgsConstructor // EntityManagerFactory 생성자 주입.
public class MemberRepository {
    
    @PersistenceContext // EntityManagerFactory 따로 안만들어줘도 이 어노테이션만으로 스프링데이터jpa가 알아서 자동 주입해줌.
    // ++ 이는  @Autowired 어노테이션으로 대체해도 스프링이 다 알아들음.
    private final EntityManager em;

    //==회원저장==//
    public void save(Member member) { em.persist(member); }

    //==단 건 조회==//
    public Member findOne(long id) {
        return em.find(Member.class,id);
    }

    public List<Member> findAll() {
        return em.createQuery("select  m from Member m", Member.class)
            .getResultList();
    }

    //==이름으로 회원조회==//
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
