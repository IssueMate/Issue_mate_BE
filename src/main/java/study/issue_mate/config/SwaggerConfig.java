package study.issue_mate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth", createSecurityScheme()))
            .info(info())
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    private Info info() {
        return new Info()
            .title("ISSUE MATE")
            .version("1.0")
            .description("이슈 관리 API");
    }

    /**
     * JWT 인증을 위한 SecurityScheme 설정
     */
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP) // HTTP 인증 방식
            .scheme("bearer") // Bearer 토큰 방식
            .bearerFormat("JWT") // JWT 토큰 형식
            .description("JWT Bearer 토큰을 입력하세요");
    }
}
