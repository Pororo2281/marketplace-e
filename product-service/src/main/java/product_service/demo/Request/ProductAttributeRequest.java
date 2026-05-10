package product_service.demo.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductAttributeRequest{

    @NotBlank(message = "Attribute name cannot be empty")
    @Size(max = 50, message = "Attribute name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "Attribute value cannot be empty")
    @Size(max = 100, message = "Attribute value cannot exceed 100 characters")
    private String value;


    public ProductAttributeRequest() {
    }

    public ProductAttributeRequest(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
