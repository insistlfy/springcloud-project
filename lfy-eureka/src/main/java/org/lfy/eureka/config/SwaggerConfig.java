package org.lfy.eureka.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerConfig
 *
 * @author lfy
 * @date 19-3-21
 **/
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        List<springfox.documentation.service.Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("EUREKA SERVER")
                .description("EUREKA SERVER API")
                .termsOfServiceUrl("https://www.baidu.com/")
                .version("version 1.0")
                .build();
    }
}
