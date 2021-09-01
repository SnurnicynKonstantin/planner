package xrm.extrim.planner.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.AbstractMap;
import java.util.Map;

@Configuration
@Data
public class ConfluenceConfig {
    @Value("${confluence.login}")
    private String confluenceLogin;
    @Value("${confluence.password}")
    private String confluencePassword;
    @Value("${confluence.url}")
    private String confluenceUrl;

    private final Map<String, Integer> monthNumbers  = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("январь",1),
            new AbstractMap.SimpleEntry<>("февраль",2),
            new AbstractMap.SimpleEntry<>("март",3),
            new AbstractMap.SimpleEntry<>("апрель",4),
            new AbstractMap.SimpleEntry<>("май",5),
            new AbstractMap.SimpleEntry<>("июнь",6),
            new AbstractMap.SimpleEntry<>("июль",7),
            new AbstractMap.SimpleEntry<>("август",8),
            new AbstractMap.SimpleEntry<>("сентябрь",9),
            new AbstractMap.SimpleEntry<>("октябрь",10),
            new AbstractMap.SimpleEntry<>("ноябрь",11),
            new AbstractMap.SimpleEntry<>("декабрь",12)

    );

}
