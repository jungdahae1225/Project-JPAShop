package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { // model에 data를 심어서 현재 controller에서 view로 넘길 수 있음.
        model.addAttribute("data","hello!!!");
        return "hello"; //thymeleaf viewName매핑 : 화면이름 templates에 있는 hello.html로 이동. (.html은 자동으로 붙는다.)

    }
}
