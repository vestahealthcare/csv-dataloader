package com.vestahealthcare.csvdataloader.target.model.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * For converting <tt>{@code java.sql.Time}</tt> values to/from nifty Java 8 <tt>{@code java.time.LocalDate}</tt>
 * instances.
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Timestamp> {
  @Override
  public Timestamp convertToDatabaseColumn(final LocalDate entityValue) {
    if (entityValue == null) {
      return null;
    }
    return Timestamp.valueOf(entityValue.atStartOfDay());
  }

  @Override
  public LocalDate convertToEntityAttribute(final Timestamp databaseValue) {
    if (databaseValue == null) {
      return null;
    }
    return databaseValue.toLocalDateTime().toLocalDate();
  }
}
