package com.example.jsonparam;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/demo")
public class Controller {

    @RequestMapping("/test1")
    @PostMapping
    public String test1(@JsonArg("name") String name) {
        log.info("接收到参数：{}", name);
        return name;
    }

    @RequestMapping("/test2")
    @GetMapping
    public String test2(@JsonArg("id") Integer id,@JsonArg("name") String name,@JsonArg("flag") Boolean flag) {
        log.info("接收到参数：{},{},{}", id,name,flag);
        return name;
    }


    @RequestMapping("/test3")
    @GetMapping
    public String test3(@RequestBody Dto dto) {
        log.info("接收到参数：{}", dto);
        return dto.toString();
    }
    @Data
    static class Dto{
        private Integer id;
        private String name;
        private Boolean flag;
    }

}
