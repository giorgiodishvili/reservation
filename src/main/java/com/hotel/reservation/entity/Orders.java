package com.hotel.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@DynamicUpdate
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    @JsonIgnore
    private String uuid;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodBegin;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEnd;

    private String description;

    public Orders() {
    }

    public Orders(Long id, String uuid, Room room, LocalDate periodBegin, LocalDate periodEnd, String description) {
        this.id = id;
        this.uuid = uuid;
        this.room = room;
        this.periodBegin = periodBegin;
        this.periodEnd = periodEnd;
        this.description = description;
    }

    public Orders(String uuid, Room room, LocalDate periodBegin, LocalDate periodEnd, String description) {
        this.uuid = uuid;
        this.room = room;
        this.periodBegin = periodBegin;
        this.periodEnd = periodEnd;
        this.description = description;
    }

    @PrePersist
    public void initializeUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String key) {
        this.uuid = key;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(LocalDate periodBegin) {
        this.periodBegin = periodBegin;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate endDateTime) {
        this.periodEnd = endDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", key='" + uuid + '\'' +
//                ", room=" + room +
                ", periodBegin=" + periodBegin +
                ", periodEnd=" + periodEnd +
                ", description='" + description + '\'' +
                '}';
    }
}
