package org.lemon.springmicroservices.controllers;

import lombok.extern.slf4j.Slf4j;
import org.lemon.springmicroservices.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("v1")
public class ARestController {

    @Value("${org.lemon.server.url}")
    private String url;

    @GetMapping("")
    public String defaultResponse() {
        return "default";
    }

    @GetMapping("find")
    public String findBook(@RequestParam String author) {
        log.info("author: " + author);

        RestTemplate restClient = new RestTemplate();
        return restClient
                .exchange(url, HttpMethod.GET, null, String.class)
                .getBody();
    }

}
