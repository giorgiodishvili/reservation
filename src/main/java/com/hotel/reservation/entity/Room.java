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
@Table(name = "ROOM")
@ApiModel
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOMS_SEQ")
    @SequenceGenerator(name = "ROOMS_SEQ", sequenceName = "SEQUENCE_ROOMS")
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "label mustn't be null")
    @Column(name = "LABEL", nullable = false, unique = true)
    @NotEmpty(message = "label mustn't be empty")
    private String label;

    @ManyToOne
    @JoinColumn(name = "ROOM_TYPE_ID", nullable = false)
    @NotNull(message = "Room Type should not be empty")
    private RoomType roomType;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Order> orders;

}
