package org.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.taxi.entity.enums.Role;

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
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder.Default
    @OneToMany(mappedBy = "client")
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

