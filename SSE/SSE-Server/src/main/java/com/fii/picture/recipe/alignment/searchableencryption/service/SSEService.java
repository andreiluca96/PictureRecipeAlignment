package com.fii.picture.recipe.alignment.searchableencryption.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SSEService {
    @RequestMapping(value = "/search")
    public ResponseEntity<Object> getProduct() {
        return new ResponseEntity<>("Search!", HttpStatus.OK);
    }
}
