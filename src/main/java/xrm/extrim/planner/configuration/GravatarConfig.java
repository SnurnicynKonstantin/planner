package xrm.extrim.planner.configuration;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

// TODO: Нужен рефакторинг
@Configuration
@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.FieldNamingConventions", "PMD.MutableStaticState"})
public class GravatarConfig {

    @Value("${gravatar.url}")
    public String gravatarUrl;

    public static String GRAVATAR_URL;

    @Value("${gravatar.url}")
    public void setNameStatic(String name){
        GravatarConfig.GRAVATAR_URL = gravatarUrl;
    }
}
