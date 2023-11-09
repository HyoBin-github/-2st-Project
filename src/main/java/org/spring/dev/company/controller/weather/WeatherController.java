package org.spring.dev.company.controller.weather;

import lombok.RequiredArgsConstructor;
import org.spring.dev.company.dto.weather.WeatherApiDto;
import org.spring.dev.company.repository.weather.WeatherRepository;
import org.spring.dev.company.service.weather.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;




    @PostMapping("/weatherList")
    private Map<String, String> weatherList(@RequestBody WeatherApiDto weatherApiDto) throws Exception {

        System.out.println(" <<  weatherApiDto >>  " + weatherApiDto);

        Map<String, String> weatherMap = weatherService.weatherSave(weatherApiDto);

        return weatherMap;


    }


}
