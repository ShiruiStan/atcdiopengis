package com.atcdi.opengis.controller;


import com.atcdi.opengis.reponse.StandardResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@Api(tags = "测试接口集合")
public class TestController {

    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public String getHello() {
        return "hello";
    }

    @GetMapping("/hello")
    @ApiOperation(value = "测试")
    public String getHello2() {
        return "hello";
    }

    @GetMapping("/example")
    @ApiOperation(value = "标准返回示例")
    public StandardResponse response(){
        return new StandardResponse();
    }
}
