
# SpringBoot-Project-SoleManager
스프링 부트 -> 프리랜서 중개 에이전시 그룹웨어
<br>

## 🖥️ 프로젝트 소개
1. 프리랜서와 회사 간의 적합한 프로젝트를 찾아 매칭하는 서비스 제공
2. 계약서 작성, 결제 처리 및 금융 관련 서비스를 제공
3. 프리랜서와 회사 간의 원활한 커뮤니케이션을 제공
다음과 같은 역할을 하는 그룹웨어에 서비스를 추가한 프로젝트입니다.
<br>

### ⌛️ 개발 기간
2차 Project
- 23.09.26 ~ 23.10.25

<br/>
3차 Project
- 23.10.26 ~ 23.11.16
<br/>

### 🏃‍♀️ 맴버 구성 <br/>

* 김★진(팀장) : 근무/근태(R), 급여(C,R), BaseLayout디자인, 모달디자인, PPT, 영화 API <br/>
* 김★현 : 로그인, 이메일 인증, 비밀번호 재설정, 권한별 LIST, 로그인 디자인, 날씨 API <br/>
* 박★현 : 게시판(CRUD), 댓글, 파일, FUllCalendar일정추가, 웹소캣 알림 챗봇, 메인페이지디자인, PPT, 버스 API <br/>
* 방★빈 : 회원(CRUD), 회원가입, 회원페이지 디자인, 날씨 API <br/>
* 안★기 : 결재(CRUD), 버스 API <br/>
* 이★훈 : 근무/근태(CUD), FUllCalendar(근무,프리랜서일정), 네이버웍스 구현, CI/CD, 영화 API <br/>

### 🛠️ 개발 환경
<img width="846" alt="스크린샷 2023-10-30 오후 4 18 31" src="https://github.com/anna1843/TechForge/assets/133622218/1797ae7e-bdd1-4826-92fd-b91f76223c86">

### ⚙️ DB 구성
![DB](https://github.com/anna1843/TechForge/assets/133622218/5d4b2626-1fb2-4da2-9040-16d827fc5511)

<br/>
<br/>

# 2차 Project

## 회원가입
회원의 DB를 하나만 설정하여 권한별로 회원가입의 페이지가 다르게 설정

<details>
  <summary>회원별 로그인페이지</summary>

  ````
 @GetMapping("/join")
    public String adminJoin() {
        // 관리자 회원가입페이지
        return "member/join";
    }
 @GetMapping("/companyJoin")
    public String companyJoin() {
        // 회사 회원가입
        return "company/join";
    }

@GetMapping("/freeJoin")
    public String freeJoin() {
        // 프리렌서 회원가입
        return "freelancer/join";
    }
````

  
</details>

## 회원 마이페이지
본인의 마이페이지가 아닌경우 접속불가능하게 설정

<details>
  <summary>마이페이지 권한제한</summary>

````
@GetMapping("/detail/{id}")
    public String detailMember(@PathVariable("id") Long memberId, Model model) {
        // 로그인 정보 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        MemberDto memberDto = memberService.detailMember(memberId);
        System.out.println(auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().matches(memberDto.getName())));
        System.out.println(auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().matches(memberDto.getEmail())));

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))) {

            if (isUserAuthorized(auth, memberDto) == 1) {
                model.addAttribute("memberDto", memberDto);
                return "member/staffDetail";
            } else if (isUserAuthorized(auth, memberDto) == 2) {
                model.addAttribute("memberDto", memberDto);
                return "member/staffDetail";
            }
            return "member/authorityError";
        }
        return "member/authorityError";
    }

private int isUserAuthorized(Authentication auth, MemberDto memberDto) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if (memberDto.getGrade().toString().equals("STAFF")) {
                return 1;
            }
            return 0;
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))) {
            if (memberDto.getGrade().toString().equals("STAFF") || memberDto.getGrade().toString().equals("ADMIN")) {
                return 0;
            } else {
                return 2;
            }

        }
        return 0;
    }


````
  
</details>
<br/>
<br/>

## 회원수정
이메일을 변경시 이메일인증을 한 후 수정버튼활성화 및 기존의 이메일확인

<details>
  <summary>회원수정Script</summary>

````
$(document).ready(function () {
    // 초기에는 버튼 비활성화
    $('#submit-button').prop('disabled', false);

    var phoneValidated = true;
    var addressValidated = true;
    var nameValidated = true;
    var emailValidated = true;
    var emailCheckValidated = true;
    var birthValidated = true;

    var phoneInput = $('#phone');
    var addressInput = $('#address');
    var detailAddressInput = $('#detailAddress');
    var nameInput = $('#name');
    var emailInput = $('#mail');
    var emailClick = $('#emailCheck');
    var cerNumInput = $('#certificationNumber');
    var carClick = $('#certificationBtn');
    var birthInput = $('#birth');
    // th:data-birth 속성에서 날짜 값을 가져옵니다.
        var dateString = birthInput.data('birth');
        // 날짜 값을 "YYYYMMDD"에서 "YYYY-MM-DD" 형식으로 변환합니다.
        var formattedDate = dateString.replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
        // 변환된 날짜를 input 요소의 value 속성에 설정합니다.
        birthInput.val(formattedDate);

    // 초기 값 저장
    var initialValues = {
        email: emailInput.val(),
        phone: phoneInput.val(),
        address: addressInput.val(),
        detailAddress: detailAddressInput.val(),
        name: nameInput.val(),
        birth: birthInput.val()
    };

    emailInput.on('input', function () {
        if (emailInput.val() !== initialValues.email) {
            emailValidated = false;
            emailCheckValidated = false;
            checkAllFields();
            emailChange();
        } else {
            emailValidated = true;
            emailCheckValidated = true;
            checkAllFields();
        }
    });
    emailClick.on('click', function () {
        if (emailInput.val() !== initialValues.email) {
            emailValidated = false;
            emailCheckValidated = false;
            checkAllFields();
            emailSend2();
        } else {
            emailValidated = true;
            emailCheckValidated = true;
            checkAllFields();
        }
    });
    carClick.on('click', function () {
        if (emailInput.val() !== initialValues.email) {
            emailValidated = false;
            emailCheckValidated = false;
            checkAllFields();
            emailCertification();
        } else {
            emailValidated = true;
            emailCheckValidated = true;
            checkAllFields();
        }
    });

    phoneInput.on('input', function () {
        if (phoneInput.val() !== initialValues.phone) {
            phoneValidated = false;
            checkAllFields();
            phoneCheck();
        } else {
            phoneValidated = true;
            checkAllFields();
        }
    });

    addressInput.on('input', function () {
        if (addressInput.val() !== initialValues.address) {
            addressValidated = false;
            addressCheck();
        } else {
            addressValidated = true;
            checkAllFields();
        }
    });

    detailAddressInput.on('input', function () {
        if (detailAddressInput.val() !== initialValues.detailAddress) {
            addressValidated = false;
            addressCheck();
        } else {
            addressValidated = true;
            checkAllFields();
        }
    });

    nameInput.on('input', function () {
        if (nameInput.val() !== initialValues.name) {
            nameValidated = false;
            nameCheck();
        } else {
            nameValidated = true;
            checkAllFields();
        }
    });
    birthInput.on('input', function () {
            if (birthInput.val() !== initialValue.birth) {
                birthValidated = false;
                birtCheck();
            } else {
                birthValidated = true;
                checkAllFields();
            }
        });



    // 이메일 유효성 검사
    function emailChange() {
        var email = emailInput.val();
        const regExpEm = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        // 이메일 유효성 검사 로직을 추가
        if (!regExpEm.test(email)) {
            $('.email_expression').css("display", "inline-block");
            $('.member_ok').css("display", "none");
            $('.member_already').css("display", "none");
            emailCheckValidated = false;
            checkAllFields();
        } else {
            $('.email_expression').css("display", "none");
            $.ajax({
                url: "/join/emailCheck",
                type: 'post',
                data: { "email": email },
                success: function (cnt) {
                    if (cnt != 0) {
                        $('.member_already').css("display", "inline-block");
                        $('.member_ok').css("display", "none");
                        emailValidated = false;
                        checkAllFields();
                    } else {
                        $('.member_ok').css("display", "inline-block");
                        $('.member_already').css("display", "none");
                        emailValidated = true;
                        checkAllFields();
                    }
                },
                error: function () {
                    alert("에러입니다");
                }
            });
        }
    }

    function emailSend2() {

        const clientEmail = emailInput.val();
        $.ajax({
            type: "post",
            url: "/member/check",
            data: { email: clientEmail },
            success: function (data) {
                alert('인증번호를 보냈습니다.');
            }, error: function (e) {
                alert('오류입니다. 잠시 후 다시 시도해주세요.');
            }
        });
    }
    function emailCertification() {

        let clientEmail = emailInput.val();
        let inputCode = cerNumInput.val();

        $.ajax({
            type: "post",
            url: "/member/emailCheck",
            data: { email: clientEmail, inputCode: inputCode },
            success: function (result) {
                console.log(result);
                if (result == true) {
                    alert("인증완료");
                    emailValidated = true;
                    emailCheckValidated = true;
                    checkAllFields();

                } else {
                    alert('인증번호가 틀립니다.');
                    emailCheckValidated = false;
                    checkAllFields();
                }
            }
        }
        );
    }


    function phoneCheck() {
        var phone = phoneInput.val();
        var validatePone = /^01([0|1|6|7|8|9])-?([0-9]{4})-?([0-9]{4})$/;
        if (!validatePone.test(phone)) {
            $('.phone_expression').css("display", "inline-block");
            $('.phone_already').css("display", "none");
            $('.phone_ok').css("display", "none");
            phoneValidated = false
            checkAllFields();
        } else {
            $('.phone_expression').css("display", "none");
            $.ajax({
                url: "/join/phoneCheck",
                type: 'post',
                data: { "phone": phone },
                success: function (cnt) {
                    if (cnt != 0) {
                        $('.phone_already').css("display", "inline-block");
                        $('.phone_ok').css("display", "none");
                        phoneValidated = false;
                        checkAllFields();
                    } else {
                        phoneValidated = true;
                        checkAllFields();
                        $('.phone_ok').css("display", "inline-block");
                        $('.phone_already').css("display", "none");
                    }
                },
                error: function () {
                    alert("에러입니다");
                }
            });
        }
    }

    function addressCheck() {
        var address = addressInput.val();
        var detailAddress = detailAddressInput.val();

        $('.post_already').css("display", "none");
        if (address === "") {
            $('.address_already').css("display", "inline-block");
            addressValidated = false;
            checkAllFields();
        } else {
            $('.address_already').css("display", "none");
            if (detailAddress === "") {
                $('.detail_already').css("display", "inline-block");
                addressValidated = false;
                checkAllFields();
            } else {
                $('.detail_already').css("display", "none");
                addressValidated = true;
                checkAllFields();
            }
        }
    }
    function nameCheck() {
        if (nameInput.val().trim() === "") {
            nameValidated = false;
            checkAllFields();
        } else {
            nameValidated = true;
            checkAllFields();
        }
    }
    function birtCheck() {
            if (birthInput.val().trim() === "") {
                birthValidated = false;
                checkAllFields();
            } else {
                birthValidated = true;
                checkAllFields();
            }
        }

    function checkAllFields() {
        if (emailValidated && emailCheckValidated && phoneValidated && addressValidated && nameValidated && birthValidated) {
            $('#submit-button').prop('disabled', false);
        } else {
            $('#submit-button').prop('disabled', true);
        }
    }
});
````

</details>



<br/>
<br/>
<br/>

# 3차 Project

 ## ☀️날씨 API <br> 
  OpenWeatherMap Api를 활용하여 데이터 저장 및 표출, 카카오 지도를 활용하여 위치표출

<details>
  <summary>페이지 표출 JavaScript</summary>

````
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


    let appUrl = `https://api.openweathermap.org/data/2.5/weather?q=${cityVal}&appid=Apikey`;

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

````

</details>

<details>
  <summary>챗봇을 활용한 날씨 API</summary>

````
    public WeatherInfo weatherList(String city) {
        DecimalFormat df = new DecimalFormat("#.#");

        String appid = "OpenWeatherMap ApiKey";

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

````

  
</details>


