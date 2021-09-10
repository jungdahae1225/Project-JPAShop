package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량

    /*protected OrderItem() {
    }//롬복으로 설정해주었다.
    //현재 order를 생성 메서드를 사용하여 생성하도록 했기 때문에 다른 생성법들과 혼용되지 않도록 다른 곳에서 new로 생성 못하게. 기본 생성메서드를 필수로 거쳐서 생성하도록 유도.
     */

    //==생성 메서드 - 얼마에 몇 개 샀는지==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //샀으니까 산만큼 재고 빼주기.
        return orderItem;
    }
    //==비지니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    } //재고상태를 복구해준다.

    //==주문 상품 전체 가격 조회를 위한 해당 아이템 갯수만큼 금액 뽑아주기.==//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
