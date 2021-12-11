package com.gsdd.vaadin.ui;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.gsdd.vaadin.model.Reservation;
import com.gsdd.vaadin.repository.ReservationRepository;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;

@SpringUI
public class ReservationUI extends UI {

  private static final long serialVersionUID = -158427528697976789L;

  private final ReservationRepository reservationRepository;

  private final ReservationEditor reservationEditor;

  private final Grid<Reservation> reservationGrid;

  private final TextField reservationFilter;

  private final Button addNewReservationBtn;

  @Autowired
  public ReservationUI(ReservationRepository repo, ReservationEditor editor) {
    this.reservationRepository = repo;
    this.reservationEditor = editor;
    this.reservationGrid = new Grid<>(Reservation.class);
    this.reservationFilter = new TextField();
    this.addNewReservationBtn = new Button("New Reservation", VaadinIcons.PLUS);
    this.addNewReservationBtn.setId("newReservationButton");
  }

  @Override
  protected void init(VaadinRequest request) {
    HorizontalLayout filterBtnLayout =
        new HorizontalLayout(reservationFilter, addNewReservationBtn);
    VerticalLayout leftLayout = new VerticalLayout(filterBtnLayout, reservationGrid);
    VerticalLayout rightLayout = new VerticalLayout(reservationEditor);
    HorizontalLayout mainLayout = new HorizontalLayout(leftLayout, rightLayout);
    setContent(mainLayout);

    reservationGrid.setHeight(400, Unit.PIXELS);
    reservationGrid.setWidth(900, Unit.PIXELS);
    reservationGrid.setColumns("id", "reservationOwner", "restaurantName", "dinersNumber",
        "confirmed");
    Column<Reservation, Date> reservationDateColumn =
        reservationGrid.addColumn(Reservation::getReservationDate,
            new DateRenderer("%1$tb %1$td, %1$tY", Locale.getDefault()));
    reservationDateColumn.setCaption("Reservation Date");

    reservationFilter.setWidth(350, Unit.PIXELS);
    reservationFilter.setPlaceholder("Filter by Reservation Owner");

    reservationFilter.setValueChangeMode(ValueChangeMode.LAZY);
    reservationFilter.addValueChangeListener(e -> listReservations(e.getValue()));

    reservationGrid.asSingleSelect().addValueChangeListener(e -> {
      reservationEditor.editReservation(e.getValue());
    });

    addNewReservationBtn.addClickListener(e -> reservationEditor.editReservation(new Reservation("",
        "", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "", false)));

    reservationEditor.setChangeHandler(() -> {
      reservationEditor.setVisible(false);
      listReservations(reservationFilter.getValue());
    });

    listReservations(null);
  }

  void listReservations(String filterText) {
    if (StringUtils.hasText(filterText)) {
      reservationGrid.setItems((Collection<Reservation>) reservationRepository.findAll());
    } else {
      reservationGrid
          .setItems(reservationRepository.findByReservationOwnerContainsIgnoreCase(filterText));
    }
  }

}
