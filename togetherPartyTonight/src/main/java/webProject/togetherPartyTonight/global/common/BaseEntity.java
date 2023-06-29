package webProject.togetherPartyTonight.global.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Column (name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column (name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
