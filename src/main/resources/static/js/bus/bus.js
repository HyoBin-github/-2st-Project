// 버스 노선
const url = 'https://cors-anywhere.herokuapp.com/http://ws.bus.go.kr/api/rest/';
const serviceKey='NVJloZDa5SGDsI8Fh%2FRFvsncmFGpdbR2XZwJvhXquRiyzanDEWAHfz1LWFl4rhZV%2BSAttNUPMUmnVcuuk7AfxQ%3D%3D';

const busDetail=document.querySelector('.bus-detail')
let tbodyTag=document.querySelector('#bus1');


function busSearch(){

  let html1="";

  let search=document.querySelector('#search')
  let type='busRouteInfo/getBusRouteList?';
  let strSrch=search.value;
  console.log(strSrch +' <== strSrch? ')

  let apiUrl2 = `/api/busList?strSrch=${encodeURIComponent(strSrch)}`;

  fetch(apiUrl2, {
    method: 'GET',  // GET 메서드 사용
    headers: {
      'Content-Type': 'application/json',  // 요청 헤더 설정
    }
  })
    .then(response => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error('서버 오류');
      }
    })
    .then(data => {
      // 서버 응답 처리
      console.log('서버 응답:', data);
    })
    .catch(error => {
      console.error('오류 발생:', error);
    });

  let apiUrl=`${url}busRouteInfo/getBusRouteList?serviceKey=${serviceKey}&strSrch=${strSrch}&resultType=json`;


     fetch(apiUrl)
    .then(response => response.json())
     .then(function (msg) { //아래부터는 html로 가져오기 위한 코드-->
              console.log(msg);

              function formatBusTime(time) {
                  const hours = time.substring(0, 2);
                  const minutes = time.substring(2, 4);
                  return `${hours}:${minutes}`;
              }

               msg.msgBody.itemList.forEach(el => {
                   html1 += "<tr>";
                   const firstBusTime = formatBusTime(el.firstBusTm);
                   const lastBusTime = formatBusTime(el.lastBusTm);
                   html1 += `
                       <td>${el.busRouteNm}</td>
                       <td>${el.edStationNm}</td>
                       <td>${el.stStationNm}</td>
                       <td>${firstBusTime}</td>
                       <td>${lastBusTime}</td>
                       <td>${el.term} <span>분</span></td>
                       <td onclick='stationPost(event.target.innerText)' class="td-station">${el.busRouteId}</td>
                       <td>${el.routeType}</td>
                   `;
                   html1 += "</tr>";
               });

               tbodyTag.innerHTML=html1;
    });


}

// 버스 정류장 조회
const stationNm=document.querySelector('.bus-station');

function stationPost(busId){

  let html1="";

  let type='busRouteInfo/getStaionByRoute?';
  let busRouteId=busId;

   let apiUrl3 = `/api/busStationPost?busRouteId=${encodeURIComponent(busId)}`;

    fetch(apiUrl3, {
      method: 'GET',  // GET 메서드 사용
      headers: {
        'Content-Type': 'application/json',  // 요청 헤더 설정
      }
    })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('서버 오류');
        }
      })
      .then(data => {
        // 서버 응답 처리
        console.log('서버 응답:', data);
      })
      .catch(error => {
        console.error('오류 발생:', error);
      });



  let apiUrl=`${url}busRouteInfo/getStaionByRoute?serviceKey=${serviceKey}&busRouteId=${busRouteId}&resultType=json`;

 fetch(apiUrl)
    .then(response => response.json())
     .then(function (msg) { //아래부터는 html로 가져오기 위한 코드-->

              console.log(msg)
              console.log(msg.msgBody)
              console.log(msg.msgBody.itemList)

               msg.msgBody.itemList.forEach(el=>{
                    console.log(el.gpsX, el.gpsY,el.stationNm); // kakao map 표시
                    html1+=`<div>${el.stationNm}</div>`;
               })

               stationNm.innerHTML=html1;

                positionFn(msg.msgBody.itemList);

    });

}




   function positionFn(dataVal) {

     let positions = [];

     let lat = 0;
     let lng = 0;

     dataVal.forEach((el, idx) => {
       lat = el.gpsY;
       lng = el.gpsX;

       let result = {
         title: el.stationNm,
         latlng: new kakao.maps.LatLng(parseFloat(lat), parseFloat(lng))
       };
       positions.push(result);
     });


     let mapContainer = document.getElementById('map'), // 지도를 표시할 div
       mapOption = {
         center: new kakao.maps.LatLng(dataVal[0].gpsY, dataVal[0].gpsX), // 지도의 중심좌표
         level: 5 // 지도의 확대 레벨
       };

     // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
     let map = new kakao.maps.Map(mapContainer, mapOption);
     // 주소-좌표 변환 객체를 생성합니다
     let geocoder = new kakao.maps.services.Geocoder();
     // 마커 이미지의 이미지 주소입니다
      let imageSrc = "/images/bus/marker.png";

     for (let i = 0; i < dataVal.length; i++) {
       // 마커 이미지의 이미지 크기 입니다
       let imageSize = new kakao.maps.Size(24, 24);
       // 마커 이미지를 생성합니다
       let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
       // 마커를 생성합니다
       let marker = new kakao.maps.Marker({
         map: map, // 마커를 표시할 지도
         position: positions[i].latlng, // 마커를 표시할 위치
         title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
         image: markerImage // 마커 이미지
       });
     } //for

     map.setCenter(positions[0].latlng); //기점 을 중심좌표
   }

