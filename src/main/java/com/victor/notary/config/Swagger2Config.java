package com.victor.notary.config;

/**q
 * @Author Victor LIU
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration  //让Spring来加载该类配置
@EnableSwagger2  //启用Swagger2
public class Swagger2Config {

    @Value("${swagger.is.active}")
    private boolean swaggerIsActive;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerIsActive)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("公证人组的跨链")
                .description("victor_Liu")
                .contact(new Contact("NotaryGroup","http://localhost:8081","666666666@hzfc.com"))
                .version("1.0")
                .build();
    }
}
