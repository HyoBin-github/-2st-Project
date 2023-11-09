package org.spring.dev.openApi.bus;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusInfo {

    private String busRouteNm;
    private String firstBusTm;
    private String lastLowTm;
    private String term;
    private String busRouteId;

    public static BusInfo of(BusEntity bus) {
        return BusInfo.builder()
                .busRouteNm(bus.getBusRouteNm())
                .firstBusTm(bus.getFirstBusTm())
                .lastLowTm(bus.getLastLowTm())
                .term(bus.getTerm())
                .busRouteId(bus.getBusRouteId())
                .build();
    }
}
