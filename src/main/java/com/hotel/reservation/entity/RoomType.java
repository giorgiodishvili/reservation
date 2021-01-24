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
@Table(name = "ROOM_TYPE")
@ApiModel
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoomType_SEQ")
    @SequenceGenerator(name = "RoomType_SEQ", sequenceName = "RoomType_ROOMS")
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Label mustn't be null")
    @Column(name = "LABEL", nullable = false)
    @NotNull
    private String label;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomType" , fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Room> rooms;

}
