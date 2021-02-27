package com.hotel.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ROOM_TYPE")
@ApiModel
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoomType_SEQ")
    @SequenceGenerator(name = "RoomType_SEQ", sequenceName = "RoomType_ROOMS")
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true)
    private Long id;

    @NotNull(message = "Label mustn't be null")
    @Column(name = "LABEL", nullable = false, unique = true)
    @NotEmpty(message = "label mustn't be empty")
    private String label;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomType", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Room> rooms;

}
