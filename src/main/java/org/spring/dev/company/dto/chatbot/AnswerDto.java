package org.spring.dev.company.dto.chatbot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dev.company.dto.weather.WeatherInfo;
import org.spring.dev.openApi.bus.BusDto;
import org.spring.dev.openApi.bus.BusInfo;
import org.spring.dev.openApi.movie.dto.MovieDto;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnswerDto {

    private Long id;

    private String content;

    private String keyword;

    private MemberInfo info;
    private WeatherInfo weatherInfo;
    private MovieDto movieInfo;
    private BusInfo busInfo;

    private List<MemberInfo> memberInfoList;
    private List<WeatherInfo> weatherInfoList;
    private List<MovieDto> movieInfoList;
    private List<BusInfo> busInfoList;


    public AnswerDto info(MemberInfo info){
        this.info=info;
        return this;
    }

    public AnswerDto weatherInfo(WeatherInfo weatherInfo){
        this.weatherInfo=weatherInfo;
        return this;
    }

    public AnswerDto movieInfoList(List<MovieDto> movieInfo){
        this.movieInfoList=movieInfo;
        return this;
    }

    public AnswerDto busInfoList(List<BusInfo> busInfo){
        this.busInfoList = busInfo;
        return this;
    }


}
