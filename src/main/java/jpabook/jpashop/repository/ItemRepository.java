package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 빈 연결을 위해
public class ItemRepository {
    private final EntityManager em;

    //아이템 저장
    public void save(Item item) {
        if(item.getId() == null) { //아이템이 없는 상태이면, 새롭게 등록
            em.persist(item); 
        }else { //원래 있었으면 업데이트
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
