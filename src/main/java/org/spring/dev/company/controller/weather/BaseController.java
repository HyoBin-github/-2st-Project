package org.spring.dev.company.controller.weather;

// 지우지 말것

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather/weatherList")
public class BaseController {
    @GetMapping({"/",""})
    private String weatherList2() {

        return "weather/index";
    }
}
