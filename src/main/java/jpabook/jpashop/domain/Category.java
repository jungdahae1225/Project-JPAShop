package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    @ManyToMany
    //==객체테이블과 달리, DB테이블에서는 다:다 집접 매핑이 안된다. 따라서 임의로 새로운 중간 테이블을 만들어 다:다 를 일:다 & 다:일 매핑관계로 다 풀어줘야 한다.
    // 다:다 매핑은 실전에서는 쓰지 않는 것이 좋다. 운영하기 어렵다,,
    // ==//
    @JoinTable(name = "cateory_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))

    private List<Item> items = new ArrayList<>();

    /**
     * parent,child로 부모 자식 카테고리를 연결하려고 함. (자기자신 엔티티를 매핑)
     * 개발방안: 그냥 자기자신 엔티티라고 생각하지 말고 다른 테이블이라고 생각하고 하던대로 짜면 된다.
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 편의 메소드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
