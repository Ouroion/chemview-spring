package com.chemview.controller;

import com.chemview.service.PeriodicTableService;
import com.chemview.service.PubChemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chemistry")
@CrossOrigin(origins = "*") // Configure this for your React app's URL in production
public class ChemistryController {

    @Autowired
    private PeriodicTableService periodicTableService;

    @Autowired
    private PubChemService pubChemService;

    @GetMapping("/element/{name}")
    public ResponseEntity<Map<String, Object>> getElementInfo(@PathVariable String name) {
        try {
            Map<String, Object> result = periodicTableService.getElementInfo(name);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/chemical/{name}")
    public ResponseEntity<Map<String, Object>> getChemicalInfo(@PathVariable String name) {
        try {
            Map<String, Object> result = pubChemService.getChemicalInformation(name);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}