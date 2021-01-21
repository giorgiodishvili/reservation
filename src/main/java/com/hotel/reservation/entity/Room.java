package com.hotel.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Entity
@DynamicUpdate
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private String label;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RoomType roomType;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private Set<Orders> orders;

    public Room() {
    }

    public Room(Long id, String label, RoomType roomType, String description) {
        this.id = id;
        this.label = label;
        this.roomType = roomType;
        this.description = description;
    }

    public Room(String label, RoomType roomType, String description) {
        this.label = label;
        this.roomType = roomType;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", label='" + label + '\'' +
//                ", roomType=" + roomType +
                ", description='" + description + '\'' +
                ", orders=" + orders +
                '}';
    }
}
