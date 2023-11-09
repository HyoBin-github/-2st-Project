package org.spring.dev.openApi.bus;

import org.spring.dev.openApi.bus.busRoute.BusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<BusEntity,Long> {
  Optional<BusEntity> findByBusRouteId(String busRouteId);

  List<BusEntity> findByBusRouteNmContaining(String name);
}
