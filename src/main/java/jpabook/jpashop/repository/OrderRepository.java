package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save (Order order) {
        em.persist(order);
    }

    //==주문 단 건 조회==//
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


    //==복잡한 쿼리들과 동적 쿼리 처리법 => Queryds로!!==//
    public List<Order> findAll (OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m" +
                " where o.status = :status " +
                " and m.name like :name",
                Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) //몇 개까지 검색 건을 찾아줄 건지
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery() { //jpa로 fetch조인으로 order를 가져오는 쿼리 짜면 됨.
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    /** order에서 OrderRepository는 Repository만의 일만 할 수 있도록 dto를 떼어주어 따로 패키지를 만들어준다.
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }**/
}
