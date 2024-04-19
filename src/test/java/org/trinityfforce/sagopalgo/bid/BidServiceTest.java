package org.trinityfforce.sagopalgo.bid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_BIDUNIT1;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_ITEM1;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_ITEM2;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_ITEMPRICE1;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_ITEM_ID1;
import static org.trinityfforce.sagopalgo.common.TestValue.TEST_USER1;

import java.util.HashMap;
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
import org.springframework.data.redis.core.ValueOperations;
import org.trinityfforce.sagopalgo.bid.dto.BidRequestDto;
import org.trinityfforce.sagopalgo.bid.entity.Bid;
import org.trinityfforce.sagopalgo.bid.repository.BidRepository;
import org.trinityfforce.sagopalgo.bid.service.BidService;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.repository.ItemRepository;
import org.trinityfforce.sagopalgo.item.service.ItemService;
import org.trinityfforce.sagopalgo.user.entity.User;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class BidServiceTest {

    @InjectMocks
    BidService bidService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BidRepository bidRepository;

    @Mock
    RedisTemplate<String, HashMap<String, Object>> hashMapRedisTemplate;

    @Mock
    RedisTemplate<String, List<ItemResponse>> listRedisTemplate;

    @Mock
    ItemService itemService;

    @Mock
    ValueOperations valueOperations;


    private User testUser1;
    private Item testItem1; //경매전 상품
    private Item testItem2; //경매중 상품

    @BeforeEach
    public void setup() {
        testUser1 = TEST_USER1;
        testItem1 = TEST_ITEM1;
        testItem2 = TEST_ITEM2;
    }

    @Nested
    @Order(1)
    @DisplayName("1. 입찰 테스트")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class 입찰테스트 {

        @Test
        @Order(1)
        @DisplayName("1-1. 입찰 성공 테스트")
        void placeBidSuccess() throws BadRequestException {
            //given
            BidRequestDto bidRequestDto = new BidRequestDto(TEST_ITEMPRICE1 + TEST_BIDUNIT1);
            given(hashMapRedisTemplate.opsForValue()).willReturn(valueOperations);
            given(hashMapRedisTemplate.opsForValue().get("Item:" + TEST_ITEM_ID1)).willReturn(null);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem2));

            //when
            bidService.placeBid(TEST_ITEM_ID1, testUser1, bidRequestDto);

            //then
            verify(bidRepository, times(1)).save(any(Bid.class));
        }

        @Test
        @Order(2)
        @DisplayName("1-2. 입찰 실패 테스트 (상태)")
        void placeBidFailure_Status() {
            //given
            BidRequestDto bidRequestDto = new BidRequestDto(TEST_ITEMPRICE1 + TEST_BIDUNIT1);
            given(hashMapRedisTemplate.opsForValue()).willReturn(valueOperations);
            given(hashMapRedisTemplate.opsForValue().get("Item:" + TEST_ITEM_ID1)).willReturn(null);
            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
                Optional.ofNullable(testItem1));

            //when
            Exception exception = assertThrows(BadRequestException.class, () -> {
                bidService.placeBid(TEST_ITEM_ID1, testUser1, bidRequestDto);
            });

            //then
            assertEquals("경매중인 상품만 입찰이 가능합니다.", exception.getMessage());
        }

//        @Test
//        @Order(3)
//        @DisplayName("1-3. 입찰 실패 테스트 (금액)")
//        void placeBidFailure_price() {
//            //given
//            BidRequestDto bidRequestDto = new BidRequestDto(TEST_ITEMPRICE1);
//            given(hashMapRedisTemplate.opsForValue()).willReturn(valueOperations);
//            given(hashMapRedisTemplate.opsForValue().get("Item:" + TEST_ITEM_ID1)).willReturn(null);
//            given(itemRepository.findById(TEST_ITEM_ID1)).willReturn(
//                Optional.ofNullable(testItem2));
//
//            //when
//            Exception exception = assertThrows(BadRequestException.class, () -> {
//                bidService.placeBid(TEST_ITEM_ID1, testUser1, bidRequestDto);
//            });
//
//            //then
//            assertEquals("입찰가는 " + (TEST_ITEMPRICE1 + TEST_BIDUNIT1) + "원 이상이어야 합니다.",
//                exception.getMessage());
//        }
    }

}
