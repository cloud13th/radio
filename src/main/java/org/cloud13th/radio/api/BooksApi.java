package org.cloud13th.radio.api;

import org.cloud13th.radio.traffic.RateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "books")
public class BooksApi {

    @GetMapping
    @RateLimiter(maxRequests = 10, limitDuration = 60)
    public ResponseEntity<String> list() {
        return ResponseEntity.ok().body("Hello World.");
    }
}
