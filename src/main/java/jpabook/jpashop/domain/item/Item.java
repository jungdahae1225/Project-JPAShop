package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 개발 의도에 맞는 상속관계 테이블 전략을 추상클래스에 설정해주기
@DiscriminatorColumn(name = "dtype") //자식 클래스 @DiscriminatorValue와 연결되어 자식 클래스 타입이 뭘로 표시될지 정해주는 것.
@Getter @Setter
public abstract class Item { //구현체를 가지고 갈거임
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
    
    //==비지니스 로직 - 해당 엔티티가 해결해야 하는 비지니스 로직은 객체지향적으로 해당 엔티티에 로직을 추가해준다.==//
    //=>Setter로 값 변경 하지 말고 메서드를 만드는 방법으로 수정해라.
    /***
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /***
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) { // 재고가 부족하다면
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity -= restStock;
    }
    

}
