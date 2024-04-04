package org.trinityfforce.sagopalgo.item.service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.category.repository.CategoryRepository;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;
import org.trinityfforce.sagopalgo.item.dto.response.ResultResponse;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.repository.ItemRepository;
import org.trinityfforce.sagopalgo.user.entity.User;
import org.trinityfforce.sagopalgo.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final RedisTemplate<String, HashMap<String, Object>> hashMapRedisTemplate;

    @Transactional
    @CacheEvict(value = "item", allEntries = true)
    public ResultResponse createItem(ItemRequest itemRequest, User user) {
        Category category = getCategory(itemRequest.getCategory());
        User owner = getUser(user.getId());

        itemRepository.save(new Item(itemRequest, category, owner));
        return new ResultResponse(200, "OK", "등록되었습니다.");
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "item", key = "#root.methodName", cacheManager = "cacheManager", unless = "#result == null")
    public List<ItemResponse> getItem() {
        List<Item> itemList = itemRepository.findAll();

        return itemList.stream().map(item -> new ItemResponse(item)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "item", key = "#searchRequest", cacheManager = "cacheManager", unless = "#result == null")
    public List<ItemResponse> searchItem(SearchRequest searchRequest) {
        List<Item> itemList = itemRepository.searchItem(searchRequest);
        return itemList.stream().map(item -> new ItemResponse(item)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long itemId) throws BadRequestException {
        Item item = getItem(itemId);
        cacheCheck(item);
        return new ItemResponse(item);
    }

    @Transactional
    @CacheEvict(value = "item", allEntries = true)
    public ResultResponse updateItem(Long itemId, ItemRequest itemRequest, User user)
        throws BadRequestException {
        Item item = getItem(itemId);
        Category category = getCategory(itemRequest.getCategory());
        User owner = getUser(user.getId());
        isAuthorized(item, owner.getId());
        isUpdatable(item);

        item.update(itemRequest, category);

        return new ResultResponse(200, "OK", "수정되었습니다.");
    }

    @Transactional
    @CacheEvict(value = "item", allEntries = true)
    public ResultResponse deleteItem(Long itemId, User user) throws BadRequestException {
        Item item = getItem(itemId);
        User owner = getUser(user.getId());
        isAuthorized(item, owner.getId());
        isUpdatable(item);

        itemRepository.deleteById(item.getId());

        return new ResultResponse(200, "OK", "삭제되었습니다.");
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NullPointerException("해당 유저가 존재하지 않습니다.")
        );
        return user;
    }

    private Category getCategory(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(
            () -> new NullPointerException("해당 카테고리가 존재하지 않습니다.")
        );
        return category;
    }

    private Item getItem(Long itemId) throws BadRequestException {
        Item item = itemRepository.findById(itemId).orElseThrow(
            () -> new BadRequestException("해당 상품이 존재하지 않습니다.")
        );
        return item;
    }

    private void isAuthorized(Item item, Long userId) throws BadRequestException {
        if (!item.getUser().getId().equals(userId)) {
            throw new BadRequestException("상품 등록자만 수정, 삭제가 가능합니다.");
        }
    }

    private void isUpdatable(Item item) throws BadRequestException {
        if (item.getStatus().getLabel().equals("PENDING")) {
            throw new BadRequestException("경매전 상품만 가능합니다.");
        }
    }

    private void cacheCheck(Item item) {
        String itemKey = "Item:" + item.getId();
        HashMap<String, Object> bidInfo = hashMapRedisTemplate.opsForValue().get(itemKey);
        if (bidInfo != null) {
            Integer currentPrice = (Integer) bidInfo.get("price");
            item.updateBidItem(currentPrice);
        }
    }


}
