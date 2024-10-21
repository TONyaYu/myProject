package org.taxi.util;

import org.taxi.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestObjectsUtils {

    public static User getClient(String name, String email) {
        return User.builder()
                .firstName(name)
                .lastName("Ivanov")
                .email(email)
                .password("client1")
                .phone("16379872")
                .userRole(UserRole.USER)
                .build();
    }

    public static User getDriver(String name, String email) {
        return User.builder()
                .firstName(name)
                .lastName("Ivanov")
                .email(email)
                .password("client1")
                .phone("16379872")
                .userRole(UserRole.DRIVER)
                .build();
    }

    public static Car getCar(String model, String license) {
        return Car.builder()
                .make("BMW")
                .model(model)
                .licensePlate(license)
                .isAvailable(true)
                .build();
    }

    public static Ride getRide(String start, String end){
        return Ride.builder()
                .startLocation(start)
                .endLocation(end)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(1).plusMinutes(30))
                .status(RideStatus.COMPLETED)
                .cost(BigDecimal.valueOf(2.45))
                .build();
    }

    public static Review getReview(Ride ride, User client, String comment){
        return Review.builder()
                .rating(5)
                .comment(comment)
                .reviewDate(LocalDateTime.now())
                .ride(ride)
                .reviewDate(LocalDateTime.now())
                .client(client)
                .build();
    }

    public static Payment getPayment(Ride ride) {
        return Payment.builder()
                .paymentMethod(PayMethod.CASH)
                .amount(BigDecimal.valueOf(1.17))
                .date(LocalDateTime.now())
                .ride(ride)
                .build();
    }
}
