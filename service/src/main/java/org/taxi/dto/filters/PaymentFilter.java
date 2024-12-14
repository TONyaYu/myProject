package org.taxi.dto.filters;

import lombok.Builder;
import lombok.Value;
import org.taxi.entity.enums.PayMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class PaymentFilter {
    Long rideId;
    LocalDateTime date;
    BigDecimal amount;
    PayMethod payMethod;
}
