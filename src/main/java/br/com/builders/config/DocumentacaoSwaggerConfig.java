package br.com.builders.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class DocumentacaoSwaggerConfig {

    @Bean
    public Docket apiCliente() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .directModelSubstitute(Object.class, java.lang.Void.class)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Cliente - Gerenciamento de clientes")
                .description("API responsável gerenciar as informações dos clientes")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("Lindomar Alves", "", "lindomar.piloto.alves@gmail.com"))
                .build();
    }
}
