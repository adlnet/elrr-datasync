package com.deloitte.elrr.datasync.entity;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Auditable<U> {

    @Column(name = "inserteddate", updatable = false)
    @CreationTimestamp
    private ZonedDateTime inserteddate;

    @Column(name = "updatedby")
    @LastModifiedBy
    private U updatedBy;

    @Column(name = "lastmodified")
    @UpdateTimestamp
    private ZonedDateTime lastmodified;
}
