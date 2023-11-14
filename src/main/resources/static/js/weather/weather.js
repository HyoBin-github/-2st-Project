const search = document.querySelector('#search');
const weatherList = document.querySelector('.weather-list');
const weatherListCon = document.querySelectorAll('.weather-list .con');
const data = {city : search.value};

function weatherSerach() {
    // search.value;
    // search.val();
    weatherFn(search.value)
}

function weatherFn(cityVal) {
    weatherListCon.forEach(el => {
        el.innerText = "";
    });

    // 날씨 api - fontawesome 아이콘
    let weatherIcon = {
        '01': 'fas fa-sun',
        '02': 'fas fa-cloud-sun',
        '03': 'fas fa-cloud',
        '04': 'fas fa-cloud-meatball',
        '09': 'fas fa-cloud-sun-rain',
        '10': 'fas fa-cloud-showers-heavy',
        '11': 'fas fa-poo-storm',
        '13': 'far fa-snowflake',
        '50': 'fas fa-smog'
    };


    let appUrl = `https://api.openweathermap.org/data/2.5/weather?q=${cityVal}&appid=0e549b8ba274344294623f7f93c1cd98`;

    $.ajax({
        url: appUrl,
        dataType: "json",
        type: "GET",
        success: function (result) {
            console.log(result + " <-rs" + (typeof result));
            console.log(result);
            let lon = result.coord.lon; // 경도
            let lat = result.coord.lat; //위도
            result.main.temp.value = Math.floor(result.main.temp - 273.15);
            result.main.temp_max.value = Math.floor(result.main.temp_max - 273.15)+ 'º';
            result.main.temp_max.value = Math.floor(result.main.temp_max - 273.15)+ 'º';
            console.log(result.main.temp_max);
            weatherCreate(result);

            console.log(lon, lat);

            let $Icon = (result.weather[0].icon).substr(0, 2);
            let $weather_description = result.weather[0].main; // 현재 날씨 상태 (맑다)
            // let $weather_description = result.weather[0].description; // 현재 날씨 상태 (맑다)
            console.log(result.weather[0].description + " << 현재 날씨 세세한 ")
            let $Temp = Math.floor(result.main.temp - 273.15) + 'º';
            // 절대영도 - 273.15  -> 섭씨 0
            // let $humidity = '습도&nbsp;&nbsp;&nbsp;&nbsp;' + result.main.humidity + ' %';
            // let $wind = '바람&nbsp;&nbsp;&nbsp;&nbsp;' + result.wind.speed + ' m/s';
            let $city = cityVal;
            // let $cloud = '구름&nbsp;&nbsp;&nbsp;&nbsp;' + result.clouds.all + "%";
            // let $temp_min = '최저 온도&nbsp;&nbsp;&nbsp;&nbsp;' + Math.floor(result.main.temp_min - 273.15) + 'º';
            let $temp_max = '최고 온도&nbsp;&nbsp;&nbsp;&nbsp;' + Math.floor(result.main.temp_max - 273.15) + 'º';

            $('.weather_icon').append('<i class="' + weatherIcon[$Icon] +
                ' fa-5x" style="height : 150px; width : 150px;"></i>');
            // $('.weather_description').prepend($weather_description);
            $('.current_temp').prepend($Temp);
            // $('.humidity').prepend($humidity);
            // $('.wind').prepend($wind);
            $('.city .con').html($city);
            $('.city .description').html($weather_description);
            // $('.cloud').append($cloud);
            // $('.temp_min').append($temp_min);
            // Math.floor(result.main.temp_min - 273.15) + 'º'
            $('.temp_min .con').html(Math.floor(result.main.temp_min - 273.15) + 'º');
            // $('.temp_max .con').append($temp_max);
            $('.temp_max .con').html(Math.floor(result.main.temp_max - 273.15) + 'º');
            mapFn(lon, lat);
        }
    })

}

(
    () => {
        weatherFn("seoul")
    }
)();

function mapFn(lon, lat) {
    /// 카카오 지도 API
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = { // 위도    경도
            center: new kakao.maps.LatLng(lat, lon), // 지도의 중심좌표
            level: 10 // 지도의 확대 레벨
        };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    // 마커가 표시될 위치입니다                  // 마커 위도   마커 경도
    var markerPosition = new kakao.maps.LatLng(lat, lon);
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });
    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);
    // 아래 코드는 지도 위의 마커를 제거하는 코드입니다
    // marker.setMap(null);
}

function weatherCreate(result) {
    console.log("weatherCreate");
    console.log(result);
    console.log("weatherCreate");
    $.ajax({
        url: "/weather/weatherList",
        dataType: "json",
        type: "POST",
        data: JSON.stringify(result), // 데이터를 JSON 형식으로 변환
        contentType: "application/json", // Content-Type을 JSON으로 설정
        success: function (res) {
            console.log(res);
        }
    });
}

