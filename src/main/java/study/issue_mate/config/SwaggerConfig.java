package study.issue_mate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(info);
    }
    Info info = new Info()
            .title("ISSUE MATE")
            .version("1.0")
            .description("이슈 관리 API");
}
