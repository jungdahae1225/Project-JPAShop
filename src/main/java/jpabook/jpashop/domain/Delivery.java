package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    //==Order를 연관관계 주인으로 설정했다.==//
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) //DB에서 string으로 인식하기
    private DeliveryStatus status; //READY, COMP

}
