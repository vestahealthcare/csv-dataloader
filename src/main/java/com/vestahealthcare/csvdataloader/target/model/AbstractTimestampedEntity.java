package com.vestahealthcare.csvdataloader.target.model;

import com.vestahealthcare.csvdataloader.target.model.util.ZonedDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Setter
@Getter
@ToString
@MappedSuperclass
public abstract class AbstractTimestampedEntity implements Serializable {

    protected static final String COLDEF_BOOLEAN_TRUE = "boolean default true";
    protected static final String COLDEF_BOOLEAN_FALSE = "boolean default false";
    protected static final String COLDEF_SMALLINT = "smallint default 1";
    protected static final String COLDEF_TIMESTAMP_NOT_NULL = "timestamp with time zone not null";
    protected static final String COLDEF_TIMESTAMP_NOT_NULL_DEFAULT =
            "timestamp with time zone not null default ('now'::text)::timestamp(0) with time zone";
    protected static final String COLDEF_TIMESTAMP_NULL = "timestamp with time zone";

    @Column(
            columnDefinition = COLDEF_TIMESTAMP_NOT_NULL_DEFAULT,
            updatable = false
    )
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC);
}

