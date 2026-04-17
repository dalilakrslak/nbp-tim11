package com.nbp.cinemaapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cinemaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cinema App API")
                        .version("1.0")
                        .description("API dokumentacija za upravljanje filmovima, projekcijama, korisnicima, kartama i kinima.")
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8443")
                                .description("Local development server")
                ));
    }
}