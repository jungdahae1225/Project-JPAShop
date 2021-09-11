package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

@Service //스프링빈 등록
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    //==저장소 불러오기==//
    //@PersistenceContext
    private final ItemRepository itemRepository;

    //==준영속 엔티티를 수정-merge사용 //실무에서 머지 사용하지 말 것,,==//
    @Transactional //클래스 전체에 @Transactional(readOnly = true) 읽기 전용 모드를 설정했기 때문에 save(쓰기)를 하려면 트랜젝션 오버라이딩 해줘야해
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //==준영속 엔티티를 수정-변경감지기능==//
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity, int quantity) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        Item findItem = itemRepository.findOne(itemId); //id를 기반으로 영속 상태의 엔티티 가져오기
        findItem.setName(name); //데이터를 수정한다.
        findItem.setPrice(price); //데이터를 수정한다.
        findItem.setStockQuantity(stockQuantity); //데이터를 수정한다.

        /*데이터 수정이 끝나면 트랜젝션의 변경 감지에 의해 엔티티 정보가 수정된다.*/
    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
