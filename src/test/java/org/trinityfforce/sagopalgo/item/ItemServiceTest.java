package org.trinityfforce.sagopalgo.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.trinityfforce.sagopalgo.common.TestValue.*;

import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.category.repository.CategoryRepository;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
import org.trinityfforce.sagopalgo.item.dto.request.RelistRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;
import org.trinityfforce.sagopalgo.item.dto.response.ResultResponse;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.repository.ItemRepository;
import org.trinityfforce.sagopalgo.item.service.ItemService;
import org.trinityfforce.sagopalgo.user.entity.User;
import org.trinityfforce.sagopalgo.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class ItemServiceTest {

    @InjectMocks
    ItemService itemService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    RedisTemplate<String, List<ItemResponse>> listRedisTemplate;

    private User testUser1; //유저1
    private User testUser2; //유저2
    private Category testCategory1; //카테고리1
    private Category testCategory2; //카테고리2
    private Item testItem1; //경매전 상품
    private Item testItem2; //경매중 상품
    private Item testItem3; //경매종료 상품 (낙찰)
    private Item testItem4; //경매종료 상품 (유찰)

    @BeforeEach
    public void setup() {
        testUser1 = TEST_USER1;
        testUser2 = TEST_USER2;
        testCategory1 = TEST_CATEGORY1;
        testItem1 = TEST_ITEM1;
        testItem2 = TEST_ITEM2;
        testCategory2 = TEST_CATEGORY2;
        testItem3 = TEST_ITEM3;
        testItem4 = TEST_ITEM4;
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class 상품추가테스트 {

        @Test
        @Order(1)
        @DisplayName("1. 상품 추가 성공 테스트")
        void createItemSuccess() throws BadRequestException {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME1, TEST_ITEMPRICE1,
                TEST_BIDUNIT1,
                TEST_DATE1, TEST_CATEGORY_NAME1, TEST_URL1);
            given(categoryRepository.findByName(TEST_CATEGORY_NAME1)).willReturn(
                Optional.ofNullable(testCategory1));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));
            //when
            itemService.createItem(itemRequest, testUser1);

            //then
            verify(itemRepository, times(1)).save(any(Item.class));
        }

        @Test
        @Order(2)
        @DisplayName("2. 상품 추가 실패 테스트 (유저)")
        void createItemFailure_User() {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME1, TEST_ITEMPRICE1,
                TEST_BIDUNIT1,
                TEST_DATE1, TEST_CATEGORY_NAME1, TEST_URL1);
            given(categoryRepository.findByName(TEST_CATEGORY_NAME1)).willReturn(
                Optional.ofNullable(testCategory1));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.createItem(itemRequest, testUser1);
            });

            //then
            assertEquals("해당 유저가 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @Order(3)
        @DisplayName("3. 상품 추가 실패 테스트 (카테고리)")
        void createItemFailure_Category() {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME1, TEST_ITEMPRICE1,
                TEST_BIDUNIT1,
                TEST_DATE1, TEST_CATEGORY_NAME1, TEST_URL1);

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.createItem(itemRequest, testUser1);
            });

            //then
            assertEquals("해당 카테고리가 존재하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class 상품수정테스트 {

        @Test
        @Order(1)
        @DisplayName("1. 상품 수정 성공 테스트")
        void updateItemSuccess() throws BadRequestException {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME2, TEST_ITEMPRICE2,
                TEST_BIDUNIT2,
                TEST_DATE2, TEST_CATEGORY_NAME2, TEST_URL2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));
            given(categoryRepository.findByName(TEST_CATEGORY_NAME2)).willReturn(
                Optional.ofNullable(testCategory2));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            ResultResponse result = itemService.updateItem(TEST_ITEM_ID1, itemRequest, testUser1);

            //then
            assertEquals(200, result.getCode());
            assertEquals("OK", result.getStatus());
            assertEquals("수정되었습니다.", result.getMsg());
        }

        @Test
        @Order(2)
        @DisplayName("2. 상품 수정 실패 테스트")
        void updateItemFailure() {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME2, TEST_ITEMPRICE2,
                TEST_BIDUNIT2,
                TEST_DATE2, TEST_CATEGORY_NAME2, TEST_URL2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(Optional.empty());

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.updateItem(TEST_ITEM_ID1, itemRequest, testUser1);
            });

            //then
            assertEquals("해당 상품이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @Order(3)
        @DisplayName("3. 상품 수정 실패 테스트 (권한)")
        void updateItemFailure_Unauthorized() {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME2, TEST_ITEMPRICE2,
                TEST_BIDUNIT2,
                TEST_DATE2, TEST_CATEGORY_NAME2, TEST_URL2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));
            given(categoryRepository.findByName(TEST_CATEGORY_NAME2)).willReturn(
                Optional.ofNullable(testCategory2));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser2));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.updateItem(TEST_ITEM_ID1, itemRequest, testUser1);
            });

            //then
            assertEquals("상품 등록자만 수정, 삭제가 가능합니다.", exception.getMessage());
        }

        @Test
        @Order(4)
        @DisplayName("4. 상품 수정 실패 테스트 (상태)")
        void updateItemFailure_Status() {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME2, TEST_ITEMPRICE2,
                TEST_BIDUNIT2,
                TEST_DATE2, TEST_CATEGORY_NAME2, TEST_URL2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem2));
            given(categoryRepository.findByName(TEST_CATEGORY_NAME2)).willReturn(
                Optional.ofNullable(testCategory2));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.updateItem(TEST_ITEM_ID1, itemRequest, testUser1);
            });

            //then
            assertEquals("경매전 상품만 가능합니다.", exception.getMessage());
        }

        @Test
        @Order(5)
        @DisplayName("5. 상품 수정 실패 테스트 (카테고리)")
        void updateItemFailure_Category() {
            //given
            ItemRequest itemRequest = new ItemRequest(TEST_ITEMNAME2, TEST_ITEMPRICE2,
                TEST_BIDUNIT2,
                TEST_DATE2, TEST_CATEGORY_NAME2, TEST_URL2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));
            given(categoryRepository.findByName(TEST_CATEGORY_NAME2)).willReturn(Optional.empty());

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.updateItem(TEST_ITEM_ID1, itemRequest, testUser1);
            });

            //then
            assertEquals("해당 카테고리가 존재하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class 상품재등록테스트 {

        @Test
        @Order(1)
        @DisplayName("1. 상품 재등록 성공 테스트")
        void relistItemSuccess() throws BadRequestException {
            //given
            RelistRequest relistRequest = new RelistRequest(TEST_DATE2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem4));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            ResultResponse result = itemService.relistItem(TEST_ITEM_ID1, relistRequest, testUser1);

            //then
            assertEquals(200, result.getCode());
            assertEquals("OK", result.getStatus());
            assertEquals("재등록 되었습니다.", result.getMsg());
        }

        @Test
        @Order(2)
        @DisplayName("2. 상품 재등록 실패 테스트")
        void relistItemFailure() {
            //given
            RelistRequest relistRequest = new RelistRequest(TEST_DATE2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(Optional.empty());

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.relistItem(TEST_ITEM_ID1, relistRequest, testUser1);
            });

            //then
            assertEquals("해당 상품이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @Order(3)
        @DisplayName("3. 상품 재등록 실패 테스트 (권한)")
        void relistItemFailure_Unauthorized() {
            //given
            RelistRequest relistRequest = new RelistRequest(TEST_DATE2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser2));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.relistItem(TEST_ITEM_ID1, relistRequest, testUser1);
            });

            //then
            assertEquals("상품 등록자만 수정, 삭제가 가능합니다.", exception.getMessage());
        }

        @Test
        @Order(4)
        @DisplayName("4. 상품 재등록 실패 테스트 (상태)")
        void relistItemFailure_status() {
            //given
            RelistRequest relistRequest = new RelistRequest(TEST_DATE2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem2));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.relistItem(TEST_ITEM_ID1, relistRequest, testUser1);
            });

            //then
            assertEquals("경매가 끝난 상품만 가능합니다.", exception.getMessage());
        }

        @Test
        @Order(5)
        @DisplayName("5. 상품 재등록 실패 테스트 (낙찰)")
        void relistItemFailure_Award() {
            //given
            RelistRequest relistRequest = new RelistRequest(TEST_DATE2);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem3));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.relistItem(TEST_ITEM_ID1, relistRequest, testUser1);
            });

            //then
            assertEquals("유찰된 상품만 가능합니다.", exception.getMessage());
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class 상품삭제테스트 {

        @Test
        @Order(1)
        @DisplayName("1. 상품 삭제 성공 테스트")
        void deleteItemSuccess() throws BadRequestException {
            //given
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            ResultResponse result = itemService.deleteItem(TEST_ITEM_ID1, testUser1);

            //then
            assertEquals(200, result.getCode());
            assertEquals("OK", result.getStatus());
            assertEquals("삭제되었습니다.", result.getMsg());
        }

        @Test
        @Order(2)
        @DisplayName("2. 상품 삭제 실패 테스트")
        void deleteItemFailure() {
            //given
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(Optional.empty());

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.deleteItem(TEST_ITEM_ID1, testUser1);
            });

            //then
            assertEquals("해당 상품이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @Order(3)
        @DisplayName("3. 상품 삭제 실패 테스트 (권한)")
        void deleteItemFailure_Unauthorized() {
            //given
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser2));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.deleteItem(TEST_ITEM_ID1, testUser1);
            });

            //then
            assertEquals("상품 등록자만 수정, 삭제가 가능합니다.", exception.getMessage());
        }

        @Test
        @Order(4)
        @DisplayName("4. 상품 삭제 실패 테스트 (상태)")
        void deleteItemFailure_Status() {
            //given
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem2));
            given(userRepository.findById(TEST_USER_ID1)).willReturn(
                Optional.ofNullable(testUser1));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                itemService.deleteItem(TEST_ITEM_ID1, testUser1);
            });

            //then
            assertEquals("경매전 상품만 가능합니다.", exception.getMessage());
        }
    }


}
