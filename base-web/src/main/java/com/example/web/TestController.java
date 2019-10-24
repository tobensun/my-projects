package com.example.web;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("/test1")
    public void test(@Valid TestBean b){

    }
}
@Data
class Page{
    int row;
    int index;
}
