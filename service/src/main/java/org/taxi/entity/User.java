package org.taxi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"rides", "reviews", "userCars"})
@EqualsAndHashCode(of = {"email", "phone", "lastName", "firstName"})
@Entity
@Table(name = "users")
public class User implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Ride> rides = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserCar> userCars = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @Override
    public Long getId() {
        return this.id;
    }
}

