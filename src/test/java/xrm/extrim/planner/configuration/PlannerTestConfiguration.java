package xrm.extrim.planner.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"xrm.extrim.planner.repository"})
@EntityScan(basePackages = {"xrm.extrim.planner.domain"})
@ComponentScan(basePackages = {
        "xrm.extrim.planner.common",
        "xrm.extrim.planner.converter",
        "xrm.extrim.planner.domain",
        "xrm.extrim.planner.mapper",
        "xrm.extrim.planner.parser",
        "xrm.extrim.planner.service",
})
public class PlannerTestConfiguration {
}
