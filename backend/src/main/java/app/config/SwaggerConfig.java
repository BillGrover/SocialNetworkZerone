package app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.singletonList;

/**
 * Файл кнфигурации Swagger документации.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${jwt.token.test}")
    private String testToken;

    @Bean
    public Docket makeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("app"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(
                        singletonList(
                                new ParameterBuilder()
                                        .name("Authorization")
                                        .modelRef(new ModelRef("string"))
                                        .parameterType("header")
                                        .required(false)
                                        .hidden(false)
                                        .defaultValue(testToken)
                                        .build()
                        )
                )
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo() {
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("Документация API")
                .version("1.0")
                .license("© Group Copyright")
                .description("Список всех API нашего приложения (Swagger2)")
                .contact(new Contact("Skillbox", "https://app.swaggerhub.com/apis/a92196/Dokwu/1.0", "java10skillbox@yandex.ru"));
        return builder.build();
    }

}