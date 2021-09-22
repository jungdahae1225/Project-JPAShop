package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    //@JsonIgnore // 엔티티를 직접 넘길 때 공개 되지 않아야 하는 정보를 뺄 수 있도록 도와준다. -> body에 Order 정보를 뺄 수 있다.
    //그러나 쓰지 않는 것이 좋다. Member엔티티를 사용하는 곳이 하나인 경우는 적기 때문에 오더 정보가 필요한 클라이언트에서는 이 어노테이션을 쓰면 안되고 어쩌고 저쩌고 하여튼 복잡해짐.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); //컬렉션은 이렇게 필드에서 이렇게 바로 초기화 하자.
}
