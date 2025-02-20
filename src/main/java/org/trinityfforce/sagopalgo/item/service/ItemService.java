package org.trinityfforce.sagopalgo.item.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.category.repository.CategoryRepository;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
import org.trinityfforce.sagopalgo.item.dto.request.RelistRequest;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemInfoResponse;
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
    private final RedisTemplate<String, Page<ItemResponse>> listRedisTemplate;


    @Transactional
    public ResultResponse createItem(ItemRequest itemRequest, User user)
        throws BadRequestException {
        Category category = getCategory(itemRequest.getCategory());
        User owner = getUser(user.getId());

        itemRepository.save(new Item(itemRequest, category, owner));
        return new ResultResponse(200, "OK", "등록되었습니다.");
    }

    @Transactional(readOnly = true)
    public Page<ItemResponse> getItem(Pageable pageable) {
        String key = getDate() + getCondition() + pageable;
        Page<ItemResponse> itemResponseList = listRedisTemplate.opsForValue().get(key);
        if (itemResponseList != null) {
            return itemResponseList;
        }
        Page<ItemResponse> itemList = itemRepository.getItem(getDate(), getCondition(),
            pageable); // 조회 condition으로 찾아온 상품
        listRedisTemplate.opsForValue().set(key, itemList, 5, TimeUnit.SECONDS);
        return itemList;
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getSales(User user) {
        List<Item> itemList = itemRepository.findAllByUserId(user.getId());
        return itemList.stream().map(item -> new ItemResponse(item)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ItemResponse> pageItem(SearchRequest searchRequest, Pageable pageable) {
        String key = searchRequest.toString() + pageable;
        Page<ItemResponse> itemResponseList = listRedisTemplate.opsForValue().get(key);
        if (itemResponseList != null) {
            return itemResponseList;
        }
        Page<ItemResponse> itemList = itemRepository.pageItem(searchRequest, pageable);
        listRedisTemplate.opsForValue().set(key, itemList, 5, TimeUnit.SECONDS);
        return itemList;
    }

    @Transactional
    public ItemInfoResponse getItemById(Long itemId) throws BadRequestException {
        Item item = getItem(itemId);
        item.addViewCount();
        return new ItemInfoResponse(item);
    }

    @Transactional
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
    public ResultResponse relistItem(Long itemId, RelistRequest relistRequest, User user)
        throws BadRequestException {
        Item item = getItem(itemId);
        User owner = getUser(user.getId());
        isAuthorized(item, owner.getId());
        isRelistable(item);
        item.relist(relistRequest);
        return new ResultResponse(200, "OK", "재등록 되었습니다.");
    }

    @Transactional
    public ResultResponse deleteItem(Long itemId, User user) throws BadRequestException {
        Item item = getItem(itemId);
        User owner = getUser(user.getId());
        isAuthorized(item, owner.getId());
        isUpdatable(item);
        itemRepository.deleteById(item.getId());
        return new ResultResponse(200, "OK", "삭제되었습니다.");
    }

    private User getUser(Long userId) throws BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new BadRequestException("해당 유저가 존재하지 않습니다.")
        );
        return user;
    }

    private Category getCategory(String name) throws BadRequestException {
        Category category = categoryRepository.findByName(name).orElseThrow(
            () -> new BadRequestException("해당 카테고리가 존재하지 않습니다.")
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
        if (!item.getStatus().getLabel().equals("PENDING")) {
            throw new BadRequestException("경매전 상품만 가능합니다.");
        }
    }

    private void isRelistable(Item item) throws BadRequestException {
        if (item.getBidCount() != 0) {
            throw new BadRequestException("유찰된 상품만 가능합니다.");
        }
        if (!item.getStatus().getLabel().equals("COMPLETED")) {
            throw new BadRequestException("경매가 끝난 상품만 가능합니다.");
        }
    }

    private LocalDate getDate() {
        return LocalDate.now();
    }

    private String getCondition() {
        LocalDateTime time = LocalDateTime.now();
        if (time.getHour() < 9) {
            return "progress";
        } else if (time.getHour() < 15) {
            return "after";
        } else {
            return "before";
        }
    }

}
