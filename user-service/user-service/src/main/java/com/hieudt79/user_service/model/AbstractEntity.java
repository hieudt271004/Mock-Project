package com.hieudt79.user_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
@MappedSuperclass
@Data
public abstract class AbstractEntity<T extends Serializable> implements Serializable {

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDate updatedAt;
}
