package jbasicform.demo;

import jbasicform.form.JBasicForm;

import javax.swing.*;

public class ExampleForm extends JBasicForm implements ExampleFormView {
    private final static String[] CATEGORIES = {"Electronic", "Book"};
    private JTextField productNameTextField;
    private JTextField productIdTextField;
    private JComboBox<String> categoryComboBox;
    private JSlider quantitySlider;
    private JSlider discountSlider;

    public ExampleForm(){
        productNameTextField = addTextField("Product Name", "", 15);
        productIdTextField = addTextField("Product ID", "1234",  5);
        categoryComboBox = addComboBoxField("Category", CATEGORIES);
        quantitySlider = addSliderField("Quantity", 1, 1000, 250);
        discountSlider = addSliderField("Discount", 0.0, 1.0, 0.0);
        setButtonText("Submit");
    }

    @Override
    public String getProductName() {
        return productNameTextField.getText();
    }

    @Override
    public int getProductId() {
        return Integer.parseInt(productIdTextField.getText());
    }

    @Override
    public String getCategory() {
        return (String) categoryComboBox.getSelectedItem();
    }

    @Override
    public int getQuantity() {
        return quantitySlider.getValue();
    }

    @Override
    public double getDiscount() {
        return discountSlider.getValue();
    }
}
