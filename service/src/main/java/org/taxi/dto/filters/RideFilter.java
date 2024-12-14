package org.taxi.dto.filters;

import lombok.Builder;
import lombok.Value;
import org.taxi.entity.enums.RideStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class RideFilter {

    LocalDateTime startDate;
    RideStatus status;
    BigDecimal cost;
    Long driverId;
    Long clientId;
    String startLocation;
    String endLocation;

}
