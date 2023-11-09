package org.spring.dev.company.service.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.dev.company.config.OpenApiUtil;
import org.spring.dev.company.dto.weather.Weather;
import org.spring.dev.company.dto.weather.WeatherApiDto;
import org.spring.dev.company.dto.weather.WeatherInfo;
import org.spring.dev.company.entity.weather.WeatherEntity;
import org.spring.dev.company.repository.weather.WeatherRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public Map<String, String> weatherSave(WeatherApiDto weatherApiDto) throws Exception {
        WeatherEntity weather = WeatherEntity.builder()
                .lat(weatherApiDto.getCoord().getLat())
                .lon(weatherApiDto.getCoord().getLon())
                .name(weatherApiDto.getName())
                .temp_max(weatherApiDto.getMain().getTemp_max())
                .temp_min(weatherApiDto.getMain().getTemp_min())
                .country(weatherApiDto.getSys().getCountry())
                .build();

        Optional<WeatherEntity> optionalWeatherEntity = weatherRepository.findByName(weather.getName());
        if (!optionalWeatherEntity.isPresent()){
            weatherRepository.save(weather);
        }

        List<Weather> weatherList = weatherApiDto.getWeather()
                .stream()
                .collect(Collectors.toList());

        for(Weather el : weatherList){
            System.out.println("List" + el);
        }

        Map<String, String> weatherMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        String weatherList2 = mapper.writeValueAsString(weatherList);
        weatherMap.put("weather",weatherList2);

        return weatherMap;
    }


    public WeatherInfo weatherList(String city) {
        DecimalFormat df = new DecimalFormat("#.#");

        String appid = "b6616c0963212986998cdd8cf346c479";

        String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + appid; // JSON ���
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-type", "application/json");

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);

        // JSON -> DB
        ObjectMapper objectMapper = new ObjectMapper();

        WeatherApiDto response = null;
        try {
            // json ���ڿ������͸� -> Ŭ������ ����
            response = objectMapper.readValue(responseBody, WeatherApiDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(" <<  WeatherApiDto " + response);

        WeatherEntity weatherEntity = null;
        if (response != null) {
            weatherEntity = WeatherEntity.builder()
                    .lat(response.getCoord().getLat())
                    .lon(response.getCoord().getLon())
                    .name(response.getName())
                    .temp(Double.parseDouble(df.format(response.getMain().getTemp() - 273.15)))
                    .temp_max(Double.parseDouble(df.format(response.getMain().getTemp_max() - 273.15)))
                    .temp_min(Double.parseDouble(df.format(response.getMain().getTemp_min() - 273.15)))
                    .country(response.getSys().getCountry())
                    .build();


            Optional<WeatherEntity> optionalWeatherEntity
                    = weatherRepository.findByName(response.getName());
            if (!optionalWeatherEntity.isPresent()) {
                weatherRepository.save(weatherEntity);

                return WeatherInfo.builder()
                        .id(weatherEntity.getId())
                        .city(weatherEntity.getName())
                        .country(weatherEntity.getCountry())
                        .lon(weatherEntity.getLon())
                        .lat(weatherEntity.getLat())
                        .temp(weatherEntity.getTemp())
                        .temp_min(Double.parseDouble(df.format(response.getMain().getTemp_min() - 273.15)))
                        .temp_max(Double.parseDouble(df.format(response.getMain().getTemp_max() - 273.15)))
                        .build();

            } else {
                return WeatherInfo.builder()
                        .id(optionalWeatherEntity.get().getId())
                        .city(optionalWeatherEntity.get().getName())
                        .country(optionalWeatherEntity.get().getCountry())
                        .lat(weatherEntity.getLat())
                        .lon(weatherEntity.getLon())
                        .temp(weatherEntity.getTemp())
                        .temp_min(Double.parseDouble(df.format(response.getMain().getTemp_min() - 273.15)))
                        .temp_max(Double.parseDouble(df.format(response.getMain().getTemp_max() - 273.15)))
                        .build();

            }
        }
        return null;
    }
}





