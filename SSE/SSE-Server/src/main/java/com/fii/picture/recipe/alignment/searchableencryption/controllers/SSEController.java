package com.fii.picture.recipe.alignment.searchableencryption.controllers;

import com.fii.picture.recipe.alignment.searchableencryption.Dto.SSEResponse;
import com.fii.picture.recipe.alignment.searchableencryption.service.SSEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by Ariana on 1/14/2019.
 */
@RestController
@RequestMapping("v1/sse")
public class SSEController {

    @Autowired
    private SSEService sseService;

    @RequestMapping(value = "/recipe/{id}", method = RequestMethod.GET)
    public ResponseEntity<SSEResponse> getRecipes(@PathVariable("id") String id) throws Exception {
        String picture = this.sseService.getAll(id);

        if (picture == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        SSEResponse response = new SSEResponse();
        response.setImage(picture);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
