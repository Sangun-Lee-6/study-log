package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Controller 애노테이션
 * - 이 클래스가 컨트롤러(웹 요청을 처리하는 요소)임을 스프링에게 알림
 */
@Controller
public class HelloController {
    /**
     * @GetMapping 애노테이션
     * - GET 요청을 처리하는 메서드임을 스프링에게 알림
     * - GET, /hello 로 요청이 들어오면 아래 메서드를 실행
     */
    @GetMapping("hello")
    /**
     * Model : 스프링이 제공하는 객체, 뷰에 데이터를 넘겨주는 역할
     */
    public String hello(Model model) {
        /**
         * - model 객체에 data 라는 이름으로 spring 값을 담음
         * - data의 값은 템플릿 엔진에서 ${data}로 꺼내져서 사용됨
         */
        model.addAttribute("data", "spring");
        /**
         * 컨트롤러에서 문자열을 반환하면, 스프링은 이를 View 이름으로 간주
         * ∴ hello.html을 찾아서 렌더링
         */
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-api")
    @ResponseBody
    public String helloApi(@RequestParam("name") String name) {
        return "hello-api: " + name;
    }

    @GetMapping("hello-api2")
    @ResponseBody
    public HelloData helloApi2(@RequestParam("name") String name) {
        HelloData helloData = new HelloData();
        helloData.setData(name);
        return helloData;
    }

    static class HelloData {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
