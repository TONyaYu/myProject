package org.taxi.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntity.class)
public abstract class AuditingEntity<T extends Serializable> implements BaseEntity<T> {

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant modifiedAt;

}
