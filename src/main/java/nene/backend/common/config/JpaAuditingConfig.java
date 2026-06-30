package nene.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// TODO: Concept - CONFIGURATION CLASS
//  @Configuration tells Spring: "This class contains setup/config for the app."
//  @EnableJpaAuditing turns on automatic timestamp tracking (createdAt, updatedAt).

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
