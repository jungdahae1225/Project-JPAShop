package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    //==저장소 불러오기==//
    @PersistenceContext
    private final ItemRepository itemRepository;

    @Transactional //클래스 전체에 @Transactional(readOnly = true) 읽기 전용 모드를 설정했기 때문에 save(쓰기)를 하려면 트랜젝션 오버라이딩 해줘야해
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
