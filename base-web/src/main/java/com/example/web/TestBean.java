package com.example.web;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TestBean {
    @NotNull(message = "不能为空")
    private String name;
}
