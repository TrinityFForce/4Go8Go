package org.trinityfforce.sagopalgo.item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.bid.repository.BidRepository;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.category.repository.CategoryRepository;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
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
        List<ItemResponse> itemResponseList = new ArrayList<>();
        for (Item item : itemList)
                itemResponseList.add(new ItemResponse(item));


        return itemResponseList;
//        return itemList.stream().map(item -> new ItemResponse(item)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> searchItem(String itemName) {
        List<Item> itemList = itemRepository.findAll();
        List<ItemResponse> itemResponseList = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getName().contains(itemName)) {
                itemResponseList.add(new ItemResponse(item));
            }
        }

        return itemResponseList;
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getCategoryItem(String categoryName) {
        Category category = getCategory(categoryName);
        List<Item> itemList = itemRepository.findAllByCategory(category);
        List<ItemResponse> itemResponseList = new ArrayList<>();

        for (Item item : itemList) {
            itemResponseList.add(new ItemResponse(item));
        }

        return itemResponseList;
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long itemId) throws BadRequestException {
        Item item = getItem(itemId);

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
        isBidding(item);

        item.update(itemRequest, category);

        return new ResultResponse(200, "OK", "수정되었습니다.");
    }

    @Transactional
    @CacheEvict(value = "item", allEntries = true)
    public ResultResponse deleteItem(Long itemId, User user) throws BadRequestException {
        Item item = getItem(itemId);
        User owner = getUser(user.getId());
        isAuthorized(item, owner.getId());
        isBidding(item);

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

    private void isBidding(Item item) throws BadRequestException {
        if (item.getBidCount()>0) {
            throw new BadRequestException("해당 상품에 입찰자가 존재합니다.");
        }
    }
}
