package com.aisales.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReadController {

    @GetMapping("/read")
    public ResponseEntity<Map<String, String>> read() {
        return ResponseEntity.ok(Map.of("status", "read-access-granted"));
    }
}
