package wisoft.labservice.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    // 400 BAD_REQUEST - 잘못된 요청
    INVALID_INPUT_VALUE("E400-001", "잘못된 입력값입니다", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_PARAMETER("E400-002", "필수 파라미터가 누락되었습니다", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_VALUE("E400-003", "잘못된 타입의 값입니다", HttpStatus.BAD_REQUEST),
    INVALID_FILE("E400-004", "유효하지 않은 파일입니다", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY("E400-005", "유효하지 않은 카테고리입니다", HttpStatus.BAD_REQUEST),

    // 401 UNAUTHORIZED - 인증 실패
    UNAUTHORIZED("E401-001", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("E401-002", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("E401-003", "만료된 토큰입니다", HttpStatus.UNAUTHORIZED),

    //403 FORBIDDEN - 권한 없음
    FORBIDDEN("E403-001", "접근 권한이 없습니다", HttpStatus.FORBIDDEN),

    // 404 NOT_FOUND - 리소스 없음
    RESOURCE_NOT_FOUND("E404-001", "요청한 리소스를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("E404-002", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND("E404-003", "파일을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    NEWS_NOT_FOUND("E404-004", "뉴스를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PAPER_NOT_FOUND("E404-005", "논문을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PROJECT_NOT_FOUND("E404-006", "프로젝트를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PATENT_NOT_FOUND("E404-007", "특허를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    AWARD_NOT_FOUND("E404-008", "수상 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    IMAGE_NOT_FOUND("E404-009", "이미지를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    SLIDE_NOT_FOUND("E404-010", "슬라이드를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 409 CONFLICT - 충돌
    DUPLICATE_RESOURCE("E409-001", "이미 존재하는 리소스입니다", HttpStatus.CONFLICT),
    DUPLICATE_EMAIL("E409-002", "이미 존재하는 이메일입니다", HttpStatus.CONFLICT),
    DUPLICATE_USER("E409-003", "이미 존재하는 사용자입니다", HttpStatus.CONFLICT),
    DATA_INTEGRITY_VIOLATION("E409-004", "데이터 중복 또는 제약 조건 위반", HttpStatus.CONFLICT),

    // 500 INTERNAL_SERVER_ERROR - 서버 에러
    INTERNAL_SERVER_ERROR("E500-001", "서버 내부 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
