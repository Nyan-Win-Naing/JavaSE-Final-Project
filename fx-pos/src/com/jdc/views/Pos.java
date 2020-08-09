package com.jdc.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jdc.entities.Item;
import com.jdc.entities.Sale;
import com.jdc.entities.SalesDetails;
import com.jdc.repo.ItemRepo;
import com.jdc.repo.SaleRepo;
import com.jdc.util.MessageHandler;
import com.jdc.util.TaxRateSetting;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class Pos implements Initializable {

    @FXML
    private TextField id;

    @FXML
    private TextField item;

    @FXML
    private TableView<Item> items;

    @FXML
    private Label headTotal;

    @FXML
    private TableView<SalesDetails> saleItems;

    @FXML
    private TableColumn<SalesDetails, Integer> quentity;

    @FXML
    private Label subTotal;

    @FXML
    private Label tax;

    @FXML
    private Label total;
    
    private ItemRepo itemRepo;
    private SaleRepo saleRepo;

    @FXML
    void addToCart(MouseEvent event) {
    	if (event.getClickCount() == 2) {
			Item item = items.getSelectionModel().getSelectedItem();
			
			if (null != item) {
				SalesDetails sale = saleItems.getItems().stream().filter(a -> 
											a.getItemId() == item.getId()).findFirst().orElse(null);
				
				
				if (null == sale) {
					sale = new SalesDetails();
					sale.setItemId(item.getId());
					sale.setQuantity(1);
					sale.setUnitPrice(item.getPrice());
					sale.setItemName(item.getName());
					sale.setCategory(item.getCategory());
					
					saleItems.getItems().add(sale);
				} else {
					// update existing saleDetails item
					sale.setQuantity(sale.getQuantity() + 1);
					saleItems.refresh();
					calculateValue();
				}
				
				sale.setTax(sale.getSubTotal() / 100 *
						TaxRateSetting.getTaxRate());
				
				
			}
		}
    }

    private void calculateValue() {
		subTotal.setText(String.valueOf(saleItems.getItems().stream()
				.mapToInt(detail -> detail.getSubTotal()).sum()));
		
		tax.setText(String.valueOf(saleItems.getItems().stream().mapToInt(detail -> 
		detail.getTax()).sum()));
		
		total.setText(String.valueOf(saleItems.getItems().stream()
				.mapToInt(detail -> detail.getTotal()).sum()));
	}

	@FXML
    void clear() {
		id.clear();
		item.clear();
    	saleItems.getItems().clear();
    }

    @FXML
    void paid() {
    	
    	try {
    		// get sale records from table
    		Sale sale = new Sale();
        	sale.setDetails(new ArrayList<SalesDetails>(saleItems.getItems()));
        	
        	// save in database
        	saleRepo.create(sale);
        	
        	// clear records from table
        	clear();
        	items.getItems().clear();
        	
		} catch (Exception e) {
			MessageHandler.show(e);
		}
    
    }

    @FXML
    void clearCart() {
    	saleItems.getItems().clear();
    }

    @FXML
    void remove() {
    	SalesDetails detail = saleItems.getSelectionModel().getSelectedItem();
    	
    	if (detail != null) {
			saleItems.getItems().remove(detail);
		} else {
			MessageHandler.show("Please select item from cart !");
		}
    	
    	calculateValue();
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		itemRepo = ItemRepo.getInstance();
		saleRepo = SaleRepo.getInstance();
		id.textProperty().addListener((a, b, c) -> search());
		item.textProperty().addListener((a, b, c) -> search());
		
		// show headTotal values from Total TextField
		headTotal.textProperty().bind(total.textProperty());
		
		saleItems.getItems().addListener(new ListChangeListener<SalesDetails>() {

			@Override
			public void onChanged(Change<? extends SalesDetails> c) {
				calculateValue();
			}
		});
		
		quentity.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

			@Override
			public String toString(Integer object) {
				if (null != object) {
					return object.toString();
				}
				return null;
			}

			@Override
			public Integer fromString(String string) {
				try {
					if (!string.isEmpty() && null != string) {
						return Integer.parseInt(string);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		}));
		
		saleItems.setEditable(true);
		
		// change value after editing quantity TableColumn
		quentity.setOnEditCommit(event -> {
			SalesDetails detail = event.getRowValue();
			detail.setQuantity(event.getNewValue());
			
			saleItems.refresh();
			
			calculateValue();
		});
		
	}

	private void search() {
		List<Item> itemList = itemRepo.find(id.getText(), item.getText());
		items.getItems().clear();
		items.getItems().addAll(itemList);
	}

}
