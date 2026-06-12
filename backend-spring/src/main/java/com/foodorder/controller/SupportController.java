package com.foodorder.controller;

import com.foodorder.dto.SupportCreateRequest;
import com.foodorder.model.SupportRequest;
import com.foodorder.service.SupportService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    private final SupportService support;

    public SupportController(SupportService support) {
        this.support = support;
    }

    @GetMapping
    public List<SupportRequest> getAll() {
        return support.getAllSupportRequests();
    }

    @PostMapping
    public SupportRequest create(@RequestBody SupportCreateRequest request) {
        return support.createSupportRequest(request);
    }
}
