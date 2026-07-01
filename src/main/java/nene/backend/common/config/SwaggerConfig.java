package nene.backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("A simple REST API for managing tasks — Code Along Project")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nene Backend")
                                .email("hilarymwendia97@gmail.com")));
    }
}
