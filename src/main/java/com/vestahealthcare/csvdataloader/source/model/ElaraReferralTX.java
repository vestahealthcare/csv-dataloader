package com.vestahealthcare.csvdataloader.source.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElaraReferralTX {

  private String memberId;
  private String externalId;
  private String memberStatus;
  private String orgName;
  private String branch;
  private String childOrgName;

}
