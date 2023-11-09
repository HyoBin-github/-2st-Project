package org.spring.dev.company.controller.weather;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasiController {

  @GetMapping("/weather/weatherList")
  public String weather1() {
    return "weather/index";
  }

  @GetMapping({"", "/index"})
  public String index() {
    return "index";
  }

  @GetMapping("/weather")
  public String weather() {
    return "weather/index";
  }

  @GetMapping("/movie")
  public String movie() {
    return "movie/index";
  }

//  @GetMapping("/bus")
//  public String bus() {
//    return "bus/index";
//  }


  @GetMapping("/chat")
  public String chat() {
    return "chatbot/index";
  }

}
