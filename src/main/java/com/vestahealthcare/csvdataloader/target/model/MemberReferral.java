package com.vestahealthcare.csvdataloader.target.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "member_referrals", schema = "referrals")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberReferral extends AbstractUpdatableEntity{
  @Id
  @SequenceGenerator(name = "member_referrals_id_seq", sequenceName = "member_referrals_id_seq", schema = "referrals", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_referrals_id_seq")
  private Long id;

  @Column(nullable = false)
  private Long memberId;

  @Column(name="organization_id", nullable = false)
  private Long organizationId;

  @Column(name = "external_id", nullable = false)
  private String externalId;

  @Column(name = "enrollment_start", nullable = false)
  private LocalDate enrollmentStart;

  @Column(name = "enrollment_end")
  private LocalDate enrollmentEnd;

  @Column
  private Long createdBy;

  @Column
  private Long updatedBy;

  @Column
  private OffsetDateTime discontinuedAt;
}
