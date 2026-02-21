package com.careers.backend.jobAdvert;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class JobAdvert implements Persistable<String> {

    @Id

    private String id;

    private String title;
    private String description;
    private String location;
    private LocalDate expiryDate;
    private LocalDateTime postedDateTime;

    @Enumerated(EnumType.STRING)
    private JobAdStatus jobAdStatus;

    @Transient
    private boolean isNew = true;

    public JobAdvert() {
    }

    public JobAdvert(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public JobAdvert(String id, String title, String description, String location, LocalDate expiryDate, LocalDateTime postedDateTime, JobAdStatus jobAdStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.expiryDate = expiryDate;
        this.postedDateTime = postedDateTime;
        this.jobAdStatus = jobAdStatus;
        this.isNew = true;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew(){return isNew;}

    @PostLoad
    @PostPersist
    void markNotNew() {this.isNew =false;}

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getPostedDateTime() {
        return postedDateTime;
    }

    public void setPostedDateTime(LocalDateTime postedDateTime) {
        this.postedDateTime = postedDateTime;
    }

    public JobAdStatus getJobAdStatus() {
        return jobAdStatus;
    }

    public void setJobAdStatus(JobAdStatus jobAdStatus) {
        this.jobAdStatus = jobAdStatus;
    }
}
