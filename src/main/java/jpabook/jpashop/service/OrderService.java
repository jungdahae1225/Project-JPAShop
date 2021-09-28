package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    //@PersistenceContext
    private final OrderRepository orderRepository;
    //@PersistenceContext
    private final MemberRepository memberRepository;
    //@PersistenceContext
    private final ItemRepository itemRepository;

    //==주문하기==//
    @Transactional
    public Long order(Long memberId, Long itemId, int count) { //넘어올 때 ID만 넘어온다. 따라서 정보를 까볼라면 저장소 가져와야한다.
        //==엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //==배송 정보 생성==//
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); 

        //==주문 상품 생성==//
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);

        //==주문 생성==//
        Order order = Order.createOrder(member,delivery,orderItem);

        //==주문 저장- 현재 order엔티티에서 cascade로 하위 orderItem과 delivery를 뿌리내려 함께 가므로 order객체 하나만 저장해줘도 3가지 모두 저장되는 효과가 있다.==//
        orderRepository.save(order);
        return order.getId(); //order의 식별자값만 반환
    }

    //==주문 취소==//
    @Transactional
    public void cancelOrder(Long orderId) { //사용자가 취소를 누르면 orderid만 넘어옴.
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();

        //스프링 jpa가 아니라면,, 따로 업데이트 쿼리도 날려줘야함.
    }
    

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
