package org.trinityfforce.sagopalgo.item.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trinityfforce.sagopalgo.global.security.UserDetailsImpl;
import org.trinityfforce.sagopalgo.item.dto.request.ItemRequest;
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
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.createItem(itemRequest, userDetails.getUser()));
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "전체 상품 목록을 조회한다.")
    public ResponseEntity<List<ItemResponse>> getItem() {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.getItem());
    }

    @GetMapping("/names/{itemName}")
    @Operation(summary = "상품 검색", description = "검색어를 통해 상품을 조회한다.")
    public ResponseEntity<List<ItemResponse>> searchItem(@PathVariable String itemName) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.searchItem(itemName));
    }

    @GetMapping("/categories/{categoryName}")
    @Operation(summary = "카테고리별 상품 조회", description = "카테고리를 통해 상품을 조회한다.")
    public ResponseEntity<List<ItemResponse>> getCategoryItem(@PathVariable String categoryName) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.getCategoryItem(categoryName));
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "상품 단일 조회", description = "상품 ID를 통해 상품을 조회한다.")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.getItemById(itemId));
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "상품 수정", description = "상품정보를 수정한다(입찰자가 없는경우).")
    public ResponseEntity<ResultResponse> updateItem(@PathVariable Long itemId,
        @RequestBody ItemRequest itemRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.updateItem(itemId, itemRequest, userDetails.getUser()));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제한다(입찰자가 없는경우).")
    public ResponseEntity<ResultResponse> deleteItem(@PathVariable Long itemId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(itemService.deleteItem(itemId, userDetails.getUser()));
    }

}
