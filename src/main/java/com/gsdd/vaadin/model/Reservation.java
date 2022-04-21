package com.gsdd.vaadin.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reservation_owner")
    private String reservationOwner;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @Column(name = "diners_number")
    private String dinersNumber;

    @Column(name = "confirmed")
    private Boolean confirmed;

    public Reservation(
            String reservationOwner,
            String restaurantName,
            LocalDate reservationLocalDate,
            String dinersNumber,
            Boolean confirmed) {
        this.reservationOwner = reservationOwner;
        this.restaurantName = restaurantName;
        if (reservationLocalDate != null) {
            this.reservationDate =
                    Date.from(
                            reservationLocalDate
                                    .atStartOfDay()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
        }
        this.dinersNumber = dinersNumber;
        this.confirmed = confirmed;
    }
}
