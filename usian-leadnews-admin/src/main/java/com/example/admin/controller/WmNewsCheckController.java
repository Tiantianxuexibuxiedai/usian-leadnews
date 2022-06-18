package com.example.admin.controller;

import com.example.admin.service.WnNewsCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/check")
public class WmNewsCheckController {
    @Autowired
    private WnNewsCheckService wnNewsCheckService;

    @GetMapping("/check")
    public Boolean check(@RequestParam Integer id) {
        return wnNewsCheckService.check(id);
    }

}
