package com.hotel.reservation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ApiIgnore
@RestController
public class MainController {

    @GetMapping("/")
    public void swagger(HttpServletResponse httpResponse) throws IOException {
        httpResponse.sendRedirect("/swagger-ui/#/");
    }


    @GetMapping("/ping")
    public String getOrderById() {
        return "pong";
    }
}
