package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") //싱글테이블 전략을 선택했으므로 DB가 현재 무엇이 들어왔는지 알려주기 위함
@Getter @Setter
public class Movie extends Item{
    private String director;
    private String actor;
}
