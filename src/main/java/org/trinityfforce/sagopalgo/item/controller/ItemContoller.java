package org.trinityfforce.sagopalgo.item.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trinityfforce.sagopalgo.global.security.UserDetailsImpl;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
import org.trinityfforce.sagopalgo.item.dto.request.OptionRequest;
import org.trinityfforce.sagopalgo.item.dto.request.SearchRequest;
import org.trinityfforce.sagopalgo.item.dto.response.ItemResponse;
import org.trinityfforce.sagopalgo.item.dto.response.ResultResponse;
import org.trinityfforce.sagopalgo.item.service.ItemService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Item API", description = "상품 관리에 관련된 API")
@RequestMapping("/api/v1/items")
public class ItemContoller {

    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "상품 등록", description = "상품을 등록한다.")
    public ResponseEntity<ResultResponse> createItem(@Valid @RequestBody ItemRequest itemRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(itemService.createItem(itemRequest, userDetails.getUser()));
    }

    @GetMapping
    @Operation(summary = "추천 목록 조회(시간대)", description = "시간대에 따라 추천 목록을 조회한다.")
    public ResponseEntity<List<ItemResponse>> getItem() {
        LocalDateTime time = LocalDateTime.now();
        String condition;   //  조회 condition
        if (time.getHour() < 9) {
            condition = "before";
        } else if (time.getHour() < 18) {
            condition = "progress";
        } else {
            condition = "after";
        }
        return ResponseEntity.ok(itemService.getItem(time.toLocalDate(), condition));
    }

    @GetMapping("/search")
    @Operation(summary = "상품 검색", description = "상품을 검색한다.")
    public ResponseEntity<Page<ItemResponse>> pageItem(@RequestBody SearchRequest searchRequest,
        @ModelAttribute
        OptionRequest optionRequest) {
        return ResponseEntity.ok(itemService.pageItem(searchRequest, optionRequest));
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "상품 단일 조회", description = "상품 ID를 통해 상품을 조회한다.")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId)
        throws BadRequestException {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "상품 수정", description = "상품정보를 수정한다(경매가 시작되지 않은경우).")
    public ResponseEntity<ResultResponse> updateItem(@PathVariable Long itemId,
        @RequestBody ItemRequest itemRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws BadRequestException {
        return ResponseEntity.ok(
            itemService.updateItem(itemId, itemRequest, userDetails.getUser()));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제한다(경매가 시작되지 않은경우).")
    public ResponseEntity<ResultResponse> deleteItem(@PathVariable Long itemId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws BadRequestException {
        return ResponseEntity.ok(itemService.deleteItem(itemId, userDetails.getUser()));
    }

}
