package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    //ManyToOne은 기본 fetch가 EAGER로 되어있기 때문에, 우리가 LAZY로 다시 바꿔줘야함 -> ~TOMANY는 기본이 LAZY.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    //cascade란? 상위위 엔티티를 persist하거나 delete하면 원래는 각각 엔티티마다 날려야 하는 코드들이 전파되어 한 번에 실행된다.
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //==1:1관계인 경우 어느쪽에 외래키를 두던 상관은 없다. 다만, 상대적으로 접근할 일이 많은 곳에 외래키를(=연관관계 주인으로 설정) 두면 된다.==//
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;//주문시간

    private OrderStatus status;//주문상태 [ORDER, CANCEL]

    /*protected Order(){
    }//롬복으로 설정 완료
    //다른 곳에서 new로 생성 못하게. 생성메서드를 필수로 거쳐서 생성하도록 유도.
    */

    //==연관관계 편의 메서드 - 양방향 매핑일 때 어플리케이션 돌릴 때 양방향 중 한쪽을 실수?로 데이터를 넣지 않는 상황을 고려하여, 둘 사이를 원자적으로
    // 필수로 이어질 수 있도록 셋팅해주는 것.두쪽 중 한 쪽에만 해주면 된다.==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==주문 생성 메서드 - 주문 관련해서 메서드를 수정해야 할 때 이 메서드만 접근하면 되므로 이렇게 통합하는 메서드를 작성하는 것이 편리하다.==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItmes) {
        Order order = new Order();
        order.setMember(member); //주문 고객 셋팅
        order.setDelivery(delivery);//배달 관련 셋팅

        for(OrderItem orderItem : orderItmes) { //주문 물품들 셋팅
            order.addOrderItem(orderItem); 
        }

        order.setStatus(OrderStatus.ORDER); //주문 상태 셋팅
        order.setOrderDate(LocalDateTime.now()); //주문 날짜 셋팅
        return order;
    }
    /***
     * 이렇게 도메인 모델 안에 핵심 비지니스 로직을 넣는 것이 ORM에서 추구하는 도메인 모델 패턴을 사용한다.
     * cf.트랜젝션 스크립트 모델 패턴
     * **/
    //==비지니스 로직==//
    /**
     * 주문 취소
     **/
    public void cancel() {
        //==배송이 시작되었으면 취소 안되도록==//
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능 합니다.");
        }

        //==배송이 시작되지 않았으면 배송 상태 '취소로' 변경 해주기==//
        this.setStatus(OrderStatus.CANCEL);

        //==배송을 취소했으니까 재고 복구해주기=//
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /***
     * 전체 주문 가격 조회
     **/
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
