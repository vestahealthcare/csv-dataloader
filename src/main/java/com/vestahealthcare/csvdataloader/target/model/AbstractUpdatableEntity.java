package com.vestahealthcare.csvdataloader.target.model;

import com.vestahealthcare.csvdataloader.target.model.util.ZonedDateTimeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractUpdatableEntity extends AbstractTimestampedEntity {

    @Column(
            name = "updated_at",
            columnDefinition = COLDEF_TIMESTAMP_NOT_NULL_DEFAULT,
            nullable = false
    )
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime updatedAt = ZonedDateTime.now(ZoneOffset.UTC);

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    public void setUpdatedAt(final ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

