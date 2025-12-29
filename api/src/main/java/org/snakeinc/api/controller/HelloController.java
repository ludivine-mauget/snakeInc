package org.snakeinc.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {
    @GetMapping
    public String hello() {
        return "Hello World!";
    }

    @PostMapping
    public String post(@RequestBody Body body) {
        return body.message();
    }

    public record Body(String message) { }
}
