package com.okworo.cards.controllers;

import com.okworo.cards.dao.request.CardRequest;
import com.okworo.cards.dao.request.CardSearchModel;
import com.okworo.cards.dao.response.ResponseObject;
import com.okworo.cards.exceptions.CustomValidationException;
import com.okworo.cards.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;


    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody CardRequest cardRequest) {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(cardService.addCard(cardRequest))
                    .success(true)
                    .message("Added Successfully")
                    .build());
        } catch (CustomValidationException ex) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .build());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCard(@RequestBody CardRequest cardRequest) {
        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(cardService.updateCard(cardRequest))
                    .success(true)
                    .message("Updated Successfully")
                    .build());
        } catch (CustomValidationException ex) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseObject.builder()
                .data(cardService.findById(id))
                .success(true)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCards() {
        return ResponseEntity.ok(ResponseObject.builder()
                .data(cardService.getAllCards())
                .success(true)
                .build());
    }

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody CardSearchModel cardSearchModel) {
        return ResponseEntity.ok(ResponseObject.builder()
                .data(cardService.search(cardSearchModel))
                .success(true)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .success(true)
                .message("Card Deleted Successfully")
                .build());
    }
}
