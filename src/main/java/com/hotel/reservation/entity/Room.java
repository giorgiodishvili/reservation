package com.hotel.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@Table(name = "ROOM")
@ApiModel
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true)
    private Long id;

    @NotNull(message = "label mustn't be null")
    @Column(name = "LABEL")
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
}
