package com.vestahealthcare.csvdataloader.migrator;

import com.vestahealthcare.csvdataloader.enums.TransactionType;
import com.vestahealthcare.csvdataloader.source.model.ElaraReferralTX;
import com.vestahealthcare.csvdataloader.source.repository.ElaraReferralTXRepository;
import com.vestahealthcare.csvdataloader.target.model.MemberReferral;
import com.vestahealthcare.csvdataloader.target.model.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ElaraTexasAddMigrator extends Migrator<ElaraReferralTX> {

  private Map<String,Long> organizations;
  public ElaraTexasAddMigrator(
          @Qualifier("targetEntityManager") final EntityManager targetEntityManager
          ) {
    super(targetEntityManager,new ElaraReferralTXRepository(), TransactionType.INSERT);
  }

  @Override
  public void init() {
    organizations = findAll(targetEntityManager, Organization.class)
            .stream().filter(organization -> organization.getAbbr() != null).collect(Collectors.toMap(Organization::getAbbr, Organization::getId));;
  }

  @Override
  public void finish() {
    organizations = null;
  }

  @Override
  public List<Object> transform(final ElaraReferralTX elaraReferralTX) {

    if (elaraReferralTX.getChildOrgName() == null || elaraReferralTX.getChildOrgName().isEmpty()) {
      throw new IllegalStateException(String.format("The member %s doesnt have a valid Elara Texas child referral source",elaraReferralTX.getMemberId()));
    }
    final MemberReferral memberReferral = new MemberReferral();
    memberReferral.setMemberId(Long.parseLong(elaraReferralTX.getMemberId()));
    memberReferral.setOrganizationId(organizations.get(elaraReferralTX.getChildOrgName()));
    memberReferral.setExternalId(elaraReferralTX.getExternalId());
    memberReferral.setEnrollmentStart(LocalDate.now(ZoneOffset.UTC));

    return List.of(memberReferral);
  }
}
