package com.vestahealthcare.csvdataloader.target.model.util;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractEnumSetConverter<T extends Enum<T>> implements AttributeConverter<Set<T>, String> {
  private final Class<T> enumClass;

  public AbstractEnumSetConverter(final Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public String convertToDatabaseColumn(final Set<T> list) {
    if (list == null || list.isEmpty()) {
      return null;
    }

    return String.join(
        ",",
        list.stream()
            .map(T::toString)
            .collect(Collectors.toUnmodifiableList())
    );
  }

  @Override
  public Set<T> convertToEntityAttribute(final String data) {
    if (!Optional.ofNullable(data).isPresent()) {
      return new HashSet<>();
    }

    return Arrays.asList(data.split(","))
        .stream()
        .map(value -> Enum.valueOf(this.enumClass, value.trim()))
        .collect(Collectors.toSet());
  }
}
