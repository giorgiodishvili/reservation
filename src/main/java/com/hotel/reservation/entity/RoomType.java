package com.hotel.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DynamicUpdate
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    @NotNull(message = "label mustn't be null")
    private String label;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomType")
    @JsonIgnore
    private Set<Room> rooms;

    public RoomType() {
    }

    public RoomType(Long id, String label, String description) {
        this.id = id;
        this.label = label;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
//                ", rooms=" + rooms +
                '}';
    }
}
