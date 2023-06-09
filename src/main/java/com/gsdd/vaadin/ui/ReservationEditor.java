package com.gsdd.vaadin.ui;

import com.gsdd.vaadin.model.Reservation;
import com.gsdd.vaadin.repository.ReservationRepository;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ReservationEditor extends VerticalLayout {

  private static final long serialVersionUID = 6251462789734087062L;

  private final ReservationRepository reservationRepository;

  private Reservation reservation;

  private TextField reservationOwner = new TextField("Reservation Owner");
  private TextField restaurantName = new TextField("Restaurant");
  private DateField reservationLocalDate = new DateField("Reservation Date");
  private TextField dinersNumber = new TextField("Diners Number");
  private CheckBox confirmed = new CheckBox("Reservation Confirmed", false);

  private Button save = new Button("Save", VaadinIcons.DISC);
  private Button cancel = new Button("Cancel");
  private Button delete = new Button("Delete", VaadinIcons.TRASH);
  private CssLayout actions = new CssLayout(save, cancel, delete);

  private Binder<Reservation> binder = new Binder<>(Reservation.class);

  @Autowired
  public ReservationEditor(ReservationRepository repository) {
    this.reservationRepository = repository;

    addComponents(
        reservationOwner, restaurantName, reservationLocalDate, dinersNumber, confirmed, actions);

    binder.bindInstanceFields(this);

    // reservationOwner.setCaption("Reservation Owner: First and Last Name");

    setSpacing(true);
    actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

    save.addClickListener(e -> repository.save(reservation));
    delete.addClickListener(e -> repository.delete(reservation));
    cancel.addClickListener(e -> editReservation(reservation));
    setVisible(false);
  }

  public interface ChangeHandler {
    void onChange();
  }

  public final void editReservation(Reservation c) {
    if (c == null) {
      setVisible(false);
      return;
    }
    final boolean persisted = c.getId() != null;
    if (persisted) {
      reservation = reservationRepository.findById(c.getId()).orElse(null);
    } else {
      reservation = c;
    }
    cancel.setVisible(persisted);
    binder.setBean(reservation);
    setVisible(true);

    save.focus();
    reservationOwner.selectAll();
  }

  public void setChangeHandler(ChangeHandler h) {
    save.addClickListener(e -> h.onChange());
    delete.addClickListener(e -> h.onChange());
  }
}
