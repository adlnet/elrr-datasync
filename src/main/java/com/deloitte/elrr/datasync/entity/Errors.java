// PHL
package com.deloitte.elrr.datasync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ERRORS")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Errors extends Auditable<String> {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "errors_seq")
  @SequenceGenerator(name = "errors_seq", sequenceName = "errors_seq", allocationSize = 1)
  @Column(name = "ERRORSID")
  private long errorsId;
  
  @Column(name = "ERRORMSG")
  private String errorMsg;
 
}