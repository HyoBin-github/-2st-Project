package org.spring.dev.openApi.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.dev.openApi.bus.busRoute.BusEntity;
import org.spring.dev.openApi.bus.busRoute.BusResponse;
import org.spring.dev.openApi.bus.busRoute.ItemList;
import org.spring.dev.openApi.bus.busStation.BusDetailEntity;
import org.spring.dev.openApi.bus.busStation.BusDetailRepository;
import org.spring.dev.openApi.bus.busStation.BusStationResponse;
import org.spring.dev.openApi.bus.busStation.ItemStationList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusService {

  private final BusRepository busRepository;
  private final BusDetailRepository busDetailRepository;

  public List<BusInfo> getBusList(String next) {
    List<BusInfo> busDtoList = new ArrayList<>();
    List<BusEntity> busEntityList = busRepository.findByBusRouteNmContaining(next);

    for (BusEntity busEntity : busEntityList) {
      busDtoList.add(
              BusInfo.builder()
                      .busRouteNm(busEntity.getBusRouteNm())
                      .firstBusTm(busEntity.getFirstBusTm())
                      .lastLowTm(busEntity.getLastLowTm())
                      .busRouteId(busEntity.getBusRouteId())
                      .build()
      );
    }

    return busDtoList;
  }

  public void insertResposeBody(String rs) {

    ObjectMapper objectMapper = new ObjectMapper();

    BusResponse response = null;
    try {
      // json 문자열데이터를 -> 클래스에 매핑
      response = objectMapper.readValue(rs, BusResponse.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<ItemList> busItemList = response.getMsgBody().getItemList()
            .stream()
            .collect(Collectors.toList());

    System.out.println(" <<  busItemList " + busItemList);

    for (ItemList item : busItemList) {
      System.out.println("busRouteId " + item.getBusRouteId());
      BusEntity busEntity = BusEntity.builder()
              .busRouteId(item.getBusRouteId())
              .busRouteNm(item.getBusRouteNm())
              .firstBusTm(item.getFirstBusTm())
              .lastLowTm(item.getLastLowTm())
              .term(item.getTerm())
              .corpNm(item.getCorpNm())
              .routeType(item.getRouteType())
              .edStationNm(item.getEdStationNm())
              .stStationNm(item.getStStationNm())
              .build();

      Optional<BusEntity> optionalBusEntity
              = busRepository.findByBusRouteId(item.getBusRouteId());

      if (!optionalBusEntity.isPresent()) {
        busRepository.save(busEntity);
      }
    }
  }

  public void busStationPostdo(String rs) {

    System.out.println(rs+" rs2");
    ObjectMapper objectMapper = new ObjectMapper();
    BusStationResponse response = null;

    try {
      // json 문자열데이터를 -> 클래스에 매핑
      response = objectMapper.readValue(rs, BusStationResponse.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(" <<  BusStationResponse " + response);

    List<ItemStationList> busStationItemList = response.getMsgBody().getItemList()
            .stream()
            .collect(Collectors.toList());

    System.out.println(" <<  busStationItemList " + busStationItemList);

    for (ItemStationList item : busStationItemList) {
      System.out.println("  getStationNm " + item.getStationNm());

      BusDetailEntity busDetailEntity = BusDetailEntity.builder()
              .busRouteId(item.getBusRouteId())
              .beginTm(item.getBeginTm())
              .lastTm(item.getLastTm())
              .busRouteNm(item.getBusRouteNm())
              .routeType(item.getRouteType())
              .gpsX(item.getGpsX())
              .gpsY(item.getGpsY())
              .posX(item.getPosX())
              .posY(item.getPosY())
              .stationNm(item.getStationNm())
              .stationNo(item.getStationNo())
              .build();

      Optional<BusDetailEntity> optionalBusDetailEntity
              = busDetailRepository.findByBusRouteId(item.getBusRouteId());

      if (!optionalBusDetailEntity.isPresent()) {

        System.out.println(" save+");
        busDetailRepository.save(busDetailEntity);

      }
    }
  }
}
