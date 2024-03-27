package org.trinityfforce.sagopalgo.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    /**
     * HTTP 응답 상태 코드
     */
    private boolean success;
    /**
     * 응답 메시지
     */
    private String message;
    /**
     * 응답 데이터
     */
    private T data;

    /**
     * 성공 응답을 생성 함수
     *
     * @param <T>    응답 데이터의 타입
     * @param status 응답 상태 코드
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> success() {
        return new ResponseDto<>(true, null, null);
    }

    /**
     * 성공 응답을 생성
     *
     * @param <T>    응답 데이터의 타입
     * @param status 응답 상태 코드
     * @param data   응답 데이터
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, null, data);
    }

    /**
     * 실패 응답을 생성
     *
     * @param <T>     응답 데이터의 타입
     * @param status  응답 상태 코드
     * @param message 실패 메시지
     * @return ResponseDto
     */
    public static <T> ResponseDto fail(int status, String message) {
        return new ResponseDto<>(false, message, null);
    }

    public static <T> ResponseDto<T> fail(int status, T data) {
        return new ResponseDto<>(false, null, data);
    }


}
