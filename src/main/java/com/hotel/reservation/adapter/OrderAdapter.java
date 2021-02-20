package com.hotel.reservation.adapter;

import com.hotel.reservation.entity.Order;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class OrderAdapter {
    private final Order order;

    public OrderAdapter(Order order) {
        this.order = order;
    }

    public OrderAdapter() {
        this(new Order());
    }

    public Order toOrder() {
        return order;
    }

    /*
      getters
         */
    @ApiModelProperty(readOnly = true)
    public Long getId() {
        return order.getId();
    }

    @ApiModelProperty(readOnly = true)
    public String getUuid() {
        return order.getUuid();
    }

    public @NotNull(message = "Begin Period mustn't be null") LocalDate getPeriodBegin() {
        return order.getPeriodBegin();
    }

    public @NotNull(message = "End Period mustn't be null") LocalDate getPeriodEnd() {
        return order.getPeriodEnd();
    }

    public String getDescription() {
        return order.getDescription();
    }


    /*
    setters
     */
    public void setId(Long id) {
    }

    public void setPeriodBegin(@NotNull(message = "Begin Period mustn't be null") LocalDate periodBegin) {
        order.setPeriodBegin(periodBegin);
    }

    public void setPeriodEnd(@NotNull(message = "End Period mustn't be null") LocalDate periodEnd) {
        order.setPeriodEnd(periodEnd);
    }

    public void setDescription(String description) {
        order.setDescription(description);
    }

    public void setUuid(String uuid) {
    }

}
