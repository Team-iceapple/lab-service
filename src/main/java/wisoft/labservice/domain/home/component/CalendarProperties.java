package wisoft.labservice.domain.home.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "calendar")
@Getter
@Setter
public class CalendarProperties {
    private String icsUrl;
}
