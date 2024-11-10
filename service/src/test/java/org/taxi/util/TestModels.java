package org.taxi.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.taxi.entity.Car;
import org.taxi.entity.PayMethod;
import org.taxi.entity.Payment;
import org.taxi.entity.Review;
import org.taxi.entity.Ride;
import org.taxi.entity.RideStatus;
import org.taxi.entity.User;
import org.taxi.entity.UserCar;
import org.taxi.entity.UserRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class TestModels extends AbstractHibernateTest {

    public void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();

        User tomSoyer = saveUser(session, "Tom", "Soyer", "895454121", "toms@gmail.com", UserRole.DRIVER, "Qeexf12lk");
        User aliceJohnson = saveUser(session, "Alice", "Johnson", "895454122", "alice@gmail.com", UserRole.CLIENT, "Passw0rd1");
        User bobSmith = saveUser(session, "Bob", "Smith", "895454123", "bob@gmail.com", UserRole.DRIVER, "SecurePwd2");
        User charlieBrown = saveUser(session, "Charlie", "Brown", "895454124", "charlie@gmail.com", UserRole.CLIENT, "P@ssw0rd3");
        User dianaRoss = saveUser(session, "Diana", "Ross", "895454125", "diana@gmail.com", UserRole.DRIVER, "Secret4");
        User edwardNorton = saveUser(session, "Edward", "Norton", "895454126", "edward@gmail.com", UserRole.CLIENT, "Passw0rd5");
        User fionaApple = saveUser(session, "Fiona", "Apple", "895454127", "fiona@gmail.com", UserRole.DRIVER, "P@ssw0rd6");
        User georgeClooney = saveUser(session, "George", "Clooney", "895454128", "george@gmail.com", UserRole.CLIENT, "SecurePwd7");
        User hannahMontana = saveUser(session, "Hannah", "Montana", "895454129", "hannah@gmail.com", UserRole.ADMIN, "Qeexf12lk8");

        Ride ride1 = saveRide(session, aliceJohnson, tomSoyer, new BigDecimal("15.00"), "Home", "Work",
                LocalDateTime.of(2024, 10, 30, 8, 0), LocalDateTime.of(2024, 10, 30, 8, 30), RideStatus.COMPLETED);

        Ride ride2 = saveRide(session, charlieBrown, bobSmith, new BigDecimal("20.00"), "Airport", "Hotel",
                LocalDateTime.of(2024, 10, 30, 10, 0), LocalDateTime.of(2024, 10, 30, 10, 45), RideStatus.IN_PROGRESS);

        Ride ride3 = saveRide(session, edwardNorton, dianaRoss, new BigDecimal("12.50"), "Office", "Home",
                LocalDateTime.of(2024, 10, 30, 18, 0), LocalDateTime.of(2024, 10, 30, 18, 30), RideStatus.PLANNED);

        Ride ride4 = saveRide(session, georgeClooney, fionaApple, new BigDecimal("25.00"), "Mall", "Cinema",
                LocalDateTime.of(2024, 10, 30, 20, 0), LocalDateTime.of(2024, 10, 30, 20, 30), RideStatus.COMPLETED);

        Car camry = saveCar(session, tomSoyer, "Toyota", "Camry", "ABC123", true);
        Car civic = saveCar(session, bobSmith, "Honda", "Civic", "XYZ789", false);
        Car focus = saveCar(session, dianaRoss, "Ford", "Focus", "DEF456", true);
        Car cruze = saveCar(session, fionaApple, "Chevrolet", "Cruze", "GHI789", false);

        UserCar tomSoyerCamry = saveRelationUserCar(session, tomSoyer, camry);
        UserCar bobSmithCivic = saveRelationUserCar(session, bobSmith, civic);
        UserCar dianaRossFocus = saveRelationUserCar(session, dianaRoss, focus);
        UserCar fionaAppleCruze = saveRelationUserCar(session, fionaApple, cruze);

        Payment paymentAlice = savePayment(session, ride1, PayMethod.CASH, LocalDateTime.of(2024, 10, 30, 8, 30), new BigDecimal("15.00"));
        Payment paymentCharlie = savePayment(session, ride2, PayMethod.CARD, LocalDateTime.of(2024, 10, 30, 10, 45), new BigDecimal("20.00"));
        Payment paymentEdward = savePayment(session, ride3, PayMethod.CASH, LocalDateTime.of(2024, 10, 30, 18, 30), new BigDecimal("12.50"));
        Payment paymentGeorge = savePayment(session, ride4, PayMethod.CARD, LocalDateTime.of(2024, 10, 30, 20, 30), new BigDecimal("25.00"));

        saveReview(session, aliceJohnson, ride1, 5, "Отличная поездка!", LocalDateTime.of(2024, 10, 30, 9, 0));
        saveReview(session, charlieBrown, ride2, 4, "Хорошая поездка, но немного опоздали.", LocalDateTime.of(2024, 10, 30, 11, 0));
        saveReview(session, edwardNorton, ride3, 3, "Средненько", LocalDateTime.of(2024, 10, 30, 19, 0));
        saveReview(session, georgeClooney, ride4, 2, "Недовольны обслуживанием.", LocalDateTime.of(2024, 10, 30, 21, 0));
    }

    private Car saveCar(Session session, User driver, String firm, String model, String licence, boolean isAvailable) {
        Car car = Car.builder()
                .make(firm)
                .model(model)
                .licensePlate(licence)
                .isAvailable(isAvailable)
                .build();
        session.persist(car);
        return car;
    }

    private User saveUser(Session session, String name, String lastName, String phone, String email, UserRole role, String pswrd) {
        User user = User.builder()
                .firstName(name)
                .lastName(lastName)
                .phone(phone)
                .email(email)
                .userRole(role)
                .password(pswrd)
                .build();
        session.persist(user);
        return user;
    }

    private Payment savePayment(Session session, Ride ride, PayMethod payMethod, LocalDateTime date, BigDecimal amount) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentMethod(payMethod)
                .date(date)
                .amount(amount)
                .build();
        session.persist(payment);
        return payment;
    }

    private Review saveReview(Session session, User client, Ride ride, int rate, String comment, LocalDateTime dateReview) {
        Review review = Review.builder()
                .user(client)
                .ride(ride)
                .rating(rate)
                .comment(comment)
                .reviewDate(dateReview)
                .build();
        session.persist(review);
        return review;
    }

    private Ride saveRide(Session session, User client, User driver, BigDecimal cost, String start,
                          String finish, LocalDateTime startDate, LocalDateTime endDate, RideStatus rideStatus) {
        Ride ride = Ride.builder()
                .user(client)
                .driver(driver)
                .cost(cost)
                .status(rideStatus)
                .startLocation(start)
                .endLocation(finish)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        session.persist(ride);
        return ride;
    }

    private UserCar saveRelationUserCar(Session session, User driver, Car car) {
        UserCar userCar = UserCar.builder()
                .user(driver)
                .car(car)
                .build();
        session.persist(userCar);
        return userCar;
    }

}
