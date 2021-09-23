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
    public List<Order> findAllByString (OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m" +
                " where o.status = :status " +
                " and m.name like :name",
                Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) //몇 개까지 검색 건을 찾아줄 건지
                .getResultList();
    }

    /**
     * V3. "엔티티"를 조회해서 DTO로 변환(fetch join 사용O) (N:1의 경우)
     * - fetch join으로 쿼리 1번 호출 (V2의 단점을 보완)
     * 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     * 에 관함
     */
    
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

    /**
     * V3. "엔티티"를 조회해서 DTO로 변환(fetch join 사용O) (N:N의 경우)
     * - fetch join으로 쿼리 1번 호출 (V2의 단점을 보완)
     * 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     * 에 관함
     *
     * ++distinct 는 DB의 distinct기능 + DB에서는 data 값이 하나라도 다르면 중복 제거가 안되는데,
     * JPA는 id의 동일성 유무를 기준으로 중복을 제거해 준다.
     */
    public List<Order> findAllWithItem() { //id값을 기준으로 중복을 제거하여 List<Order>에 넣어준다.
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member m" + //order와 toOne관계이므로 fetch조인
                                " join fetch o.delivery d" +//order와 toOne관계이므로 fetch조인
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class) //item은 중간에 orderItem테이블이 껴있으므로 toOne관계가 아니다.
                .getResultList();
    }
    /** V3.1 **/
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
