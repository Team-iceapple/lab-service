package wisoft.labservice.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private List<T> data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, Collections.singletonList(data));
    }

    public static <T> ApiResponse<T> success(List<T> data) {
        return new ApiResponse<>(true, data);
    }

    public static <T> ApiResponse<T> successEmpty() {
        return new ApiResponse<>(true, Collections.emptyList());
    }

    public static ApiResponse<Map<String, String>> error (String message) {
        return new ApiResponse<>(false, Collections.singletonList(Map.of("message", message)));
    }
}
