package wisoft.labservice.domain.home.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageIdsRequest {
    private List<String> imageIds;
}
