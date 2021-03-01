package com.hotel.reservation.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@ApiModel
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Orders_SEQ")
    @SequenceGenerator(name = "Orders_SEQ", sequenceName = "Orders_ROOMS")
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true)
    private Long id;

    @Column(name = "UUID", updatable = false, nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", nullable = false)
    @NotNull(message = "Room mustn't be null")
    private Room room;

    @Column(name = "PERIOD_BEGIN", nullable = false)
    @NotNull(message = "Begin Period mustn't be null")
    @FutureOrPresent
    private LocalDate periodBegin;

    @Column(name = "PERIOD_END", nullable = false)
    @NotNull(message = "End Period mustn't be null")
    @FutureOrPresent
    private LocalDate periodEnd;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "APP_USER_ID", nullable = false)
    @NotNull(message = "uSER FIELD SHOULD NOT BE EMPTY")
    private AppUser appUser;


    @PrePersist
    public void initializeUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
