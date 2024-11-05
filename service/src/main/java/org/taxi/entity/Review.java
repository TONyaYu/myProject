package org.taxi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "review")
public class Review implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating; // 1-5
    private String comment;
    @Column(name = "review_date")
    private LocalDateTime reviewDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ride ride;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Override
    public Long getId() {
        return this.id;
    }
}
