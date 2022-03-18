package com.vestahealthcare.csvdataloader.target.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "organizations", schema = "referrals")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Organization extends AbstractUpdatableEntity {

  @Id
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "abbr")
  private String abbr;
}
