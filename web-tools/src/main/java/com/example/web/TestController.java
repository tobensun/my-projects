package com.example.web;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("/test1")
    public void test(HttpServletRequest request, String a , Integer b,String fff,Page page){
        System.out.println("1"+a);
        System.out.println("2"+b);
    }
}
@Data
class Page{
    int row;
    int index;
}
