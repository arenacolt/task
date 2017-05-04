package gptest.data;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import gptest.task.MyUI;

public class HotelForm extends FormLayout {

    private TextField name = new TextField("Hotel's name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private TextField link = new TextField("Link");
    private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
    private DateField operatesFrom = new DateField("operatesFrom");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private TextField area = new TextField("Description");

    private HotelService service = HotelService.getInstance();
    private Hotel hotel;
    private MyUI myUI;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    public HotelForm(MyUI myUI) {
        this.myUI = myUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        addComponents(name, category, rating, address, operatesFrom,link, area,buttons);

        category.setItems(HotelCategory.values());
       
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.bind(name, "name");
        binder.bind(address, "address");
        binder.bind(area, "description");
        binder.bind(link, "url");
        binder.bind(rating, "rating");
        binder.bind(operatesFrom, "operatesFrom");
        binder.bind(category,"category");

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.setBean(hotel);
        
        delete.setVisible(hotel.isPersisted());
        setVisible(true);
        name.selectAll(); 
    
    }

    private void delete() {	
        service.delete(hotel);
        myUI.updateTable();
        setVisible(false);
    }

    private void save() {
        service.save(hotel);
        myUI.updateTable();
        setVisible(false);
    }

}