package org.taxi.util;

import lombok.Builder;
import lombok.Value;
import org.taxi.entity.Ride;
import org.taxi.entity.enums.PayMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class PaymentFilter {
    Ride ride;
    LocalDateTime date;
    BigDecimal amount;
    PayMethod payMethod;
}
