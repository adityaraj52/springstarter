package com.courseraexample.restful.restfulweb.controllers;

import com.courseraexample.restful.restfulweb.models.HelloWorldBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping(path = "/helloworld")
    public String hello(){
        return "HelloWorld";
    }

    @GetMapping(path = "/helloworldbean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean("Hello World Bean");
    }

    @GetMapping(path = "/helloworldbean/{name}")
    public HelloWorldBean helloWorldBean2(@PathVariable String name){
        return new HelloWorldBean(name);
    }

}
