package com.hotel.reservation.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableOpenApi
@Data
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hotel.reservation.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInformation());
//                .useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.GET, getCustomizedResponseMessages()
//                );
    }

    private ApiInfo getApiInformation(){
        return new ApiInfo("Room Reservation API",
                "Book room anywhere, anytime",
                "1.0",
                "API Terms of Service URL",
                new Contact("Giorgi Odishvili", "localhost:8080/", "odishvili.giorgi@gmail.com"),
                "API License",
                "API License URL",
                Collections.emptyList()
        );
    }

//    private List<ResponseMessage> getCustomizedResponseMessages(){
//        List<ResponseMessage> responseMessages = new ArrayList<>();
//        responseMessages.add(new ResponseMessageBuilder().code(500).message("Server has crashed!!").responseModel(new ModelRef("Error")).build());
//        responseMessages.add(new ResponseMessageBuilder().code(403).message("You shall not pass!!").build());
//        return responseMessages;
//    }


}
