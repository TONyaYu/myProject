package org.taxi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Column(unique = true)
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
