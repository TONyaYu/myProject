package org.taxi.util;

import lombok.Builder;
import lombok.Value;
import org.taxi.entity.User;
import org.taxi.entity.enums.RideStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class RideFilter {

    LocalDateTime startDate;
    RideStatus status;
    BigDecimal cost;
    User driver;
    User client;
    String startLocation;
    String endLocation;

}
