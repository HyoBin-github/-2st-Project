package org.spring.dev.openApi.bus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<BusEntity,Long> {
  Optional<BusEntity> findByBusRouteId(String busRouteId);
}
