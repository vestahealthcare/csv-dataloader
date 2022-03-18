package com.vestahealthcare.csvdataloader.target.model.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * For converting timestamp values to/from nifty Java 8 <tt>{@code java.time.ZonedDateTime}</tt> instances.
 */
@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {
  @Override
  public Timestamp convertToDatabaseColumn(final ZonedDateTime entityValue) {
    if (entityValue == null) {
      return null;
    }
    return new Timestamp(entityValue.toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli());
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(final Timestamp databaseValue) {
    if (databaseValue == null) {
      return null;
    }
    return ZonedDateTime.ofInstant(databaseValue.toInstant(), ZoneOffset.UTC);
  }
}
