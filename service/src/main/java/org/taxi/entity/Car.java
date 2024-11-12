package org.taxi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "licensePlate")
@ToString(exclude = "userCars")
@Builder
@Entity
@Table(name = "car")
public class Car implements Comparable<Car>, BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String make;
    private String model;
    @Column(unique = true,
            name = "license_plate")
    private String licensePlate;
    private boolean isAvailable;
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "car")
    @Builder.Default
    public List<UserCar> userCars = new ArrayList<>();

    @Override
    public int compareTo(Car o) {
        return licensePlate.compareTo(o.licensePlate);
    }
}
