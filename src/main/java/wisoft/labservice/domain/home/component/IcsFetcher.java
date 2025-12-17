package wisoft.labservice.domain.home.component;

import java.io.InputStream;
import java.net.URL;
import net.fortuna.ical4j.data.CalendarBuilder;
import org.springframework.stereotype.Component;

@Component
public class IcsFetcher {

    public net.fortuna.ical4j.model.Calendar fetch(String icsUrl) throws Exception {
        try (InputStream in = new URL(icsUrl).openStream()) {
            CalendarBuilder builder = new CalendarBuilder();
            return builder.build(in);
        }
    }
}
