package gptest.task;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import gptest.data.Hotel;
import gptest.data.HotelForm;
import gptest.data.HotelService;
import gptest.data.SearchParametrs;

@Theme("mytheme")
public class MyUI extends UI {

	private HotelService service = HotelService.getInstance();
	private Grid<Hotel> grid = new Grid<>(Hotel.class);	
    private TextField filterName = new TextField();
    private TextField filterAddress = new TextField();
    private HotelForm form = new HotelForm(this);
    private TextArea area = new TextArea();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        filterName.setPlaceholder("filter by name...");
        filterName.addValueChangeListener(e -> search(filterName,filterAddress));
        filterName.setValueChangeMode(ValueChangeMode.LAZY);

        filterAddress.setPlaceholder("filter by address...");
        filterAddress.addValueChangeListener(e -> search(filterName,filterAddress));
        filterAddress.setValueChangeMode(ValueChangeMode.LAZY);

        area.setEnabled(false);
        area.setWidth(300, UNITS_PIXELS);
        
        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterName.clear());

        Button clearFilterTextBtn2 = new Button(FontAwesome.TIMES);
        clearFilterTextBtn2.setDescription("Clear the current filter");
        clearFilterTextBtn2.addClickListener(e -> filterAddress.clear());
        
        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterName, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        CssLayout filteringAddr = new CssLayout();
        filteringAddr.addComponents(filterAddress, clearFilterTextBtn2);
        filteringAddr.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        
        Button addHotelBtn = new Button("Add new hotel");
        addHotelBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setHotel(new Hotel());
        });

        HorizontalLayout toolbar1 = new HorizontalLayout(filtering);
        HorizontalLayout toolbar2 = new HorizontalLayout(filteringAddr,addHotelBtn);
        grid.setColumns("name", "category","rating","address","operatesFrom");
        
        grid.addColumn(hotel ->
        "<a href='" + hotel.getUrl() + "' target='_top'>info</a>",
        new HtmlRenderer()).setCaption("URL");

        
        
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        layout.addComponents(toolbar1, toolbar2,main,area);

       
        updateTable();

        setContent(layout);

        form.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
         
        	if (event.getValue() == null) {
                form.setVisible(false);
              //  area.setVisible(false);
            } else {
                form.setHotel(event.getValue());
                area.setValue(event.getValue().getDescription());
            }
     
        });

  
        
        
        
    }
       

    public void updateTable()
    {
    	List<Hotel>  hotels = service.findAll();
    	grid.setItems(hotels);
    }
    
    public void search(TextField tf,TextField tf2)
    {
    	List<Hotel>  hotels = service.findByParam(tf.getValue(),tf2.getValue());
    	grid.setItems(hotels);
    }
    
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
