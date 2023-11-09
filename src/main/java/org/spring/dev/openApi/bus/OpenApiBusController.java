package org.spring.dev.openApi.bus;

import lombok.RequiredArgsConstructor;
import org.spring.dev.openApi.bus.busRoute.ApiExplorer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenApiBusController {

 private final BusService busService;

  @GetMapping("/busList")
  public Map<String,String> busList( @RequestParam(required = false) String strSrch)
                                                    throws IOException {
    String rs= ApiExplorer.getBusList(strSrch);

    System.out.println(rs+" busList");

    busService.insertResposeBody(rs);

    Map<String,String> map=new HashMap<>();

    map.put("rs",rs);
    return map;
  }


  @GetMapping("/busStationPost")
  public Map<String,String> busGet(@RequestParam(required = false) String busRouteId) throws IOException {


    System.out.println(busRouteId+" << busRouteId");

    String rs=ApiExplorer.getRespose(busRouteId);

    busService.busStationPostdo(rs);

    System.out.println(rs+" busRouteId");
    Map<String,String> map=new HashMap<>();
    map.put("rs",rs);
    return map;
  }
}
