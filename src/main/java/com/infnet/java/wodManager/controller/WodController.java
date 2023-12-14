package com.infnet.java.wodManager.controller;
import com.infnet.java.wodManager.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.infnet.java.wodManager.model.Wod;
import com.infnet.java.wodManager.service.WodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/wod")
public class WodController {
    Logger logger = LoggerFactory.getLogger(WodController.class);

    @Autowired
    private WodService wodService;

    @GetMapping
    public ResponseEntity<List<Wod>> getAll(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "tipo", required = false) String tipo) {

        List<Wod> wods = wodService.getWodsByCategoriaETipo(categoria, tipo);
        return ResponseEntity.ok(wods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            Wod wod = wodService.getById(id);
            return ResponseEntity.ok(wod);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Wod inexistente - Você precisa treinar mais!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarById(@PathVariable int id) {
        try {
            Wod wodRemovido = wodService.deletarById(id);
            return ResponseEntity.ok(wodRemovido);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Wod inexistente.");
        }
    }

    @PostMapping("/inserirWod")
   public ResponseEntity criarWod(@Valid @RequestBody Wod wod) {
        try {
            wodService.criarWod(wod);
            return ResponseEntity.status(HttpStatus.CREATED).body(wod);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Falha na inserção de um novo Wod.");
        }
    }

    @PutMapping("/alterarWod/{id}")
    public ResponseEntity<Wod> alterarWod(@PathVariable int id, @Valid @RequestBody Wod wodDetalhes) {
        try {
            Wod wodAtualizado = wodService.alterarWod(id, wodDetalhes);
            return ResponseEntity.ok(wodAtualizado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}



