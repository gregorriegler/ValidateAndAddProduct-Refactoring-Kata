package codingdojo;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Received from the client with all the details of the new product to be added to the catalogue.
 */
public class ProductFormData {
    private final String name;
    private String type;
    private final double weight;
    private final double suggestedPrice;
    private final boolean packagingRecyclable;

    public ProductFormData(String name, String type, double weight, double suggestedPrice, boolean packagingRecyclable) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.suggestedPrice = suggestedPrice;
        this.packagingRecyclable = packagingRecyclable;
    }

    Optional<Response> getValidationFailure() {
        if ("".equals(name)) {
            return Optional.of(new Response(0, -2, "Missing Name"));
        } else if (isType("")) {
            return Optional.of(new Response(0, -2, "Missing Type"));
        } else if (weight < 0) {
            return Optional.of(new Response(0, -3, "Weight error"));
        } else if (isType("Blusher") && weight > 10) {
            return Optional.of(new Response(0, -3, "Error - weight too high"));
        } else if (isType("Lipstick") && suggestedPrice > 20 && (weight > 0 && weight < 10 || !packagingRecyclable)) {
            return Optional.of(new Response(0, -1, "Error - failed quality check for Queen Range"));
        } else if (isType("Unknown")) {
            return Optional.of(new Response(0, -1, "Unknown product type " + type));
        } else {
            return Optional.empty();
        }
    }

    Product toProduct() {
        Product product = new Product(name);
        product.setFamily(getProductFamily());
        product.setType(getProductType());
        product.setRange(getProductRange());
        product.setWeight(weight);
        return product;
    }

    private boolean isType(String type) {
        return type.equals(this.type);
    }

    private ProductFamily getProductFamily() {
        if (isType("Eyeshadow")) {
            return ProductFamily.EYES;
        } else if (isType("Lipstick")) {
            return ProductFamily.LIPS;
        } else if (isType("Mascara")) {
            return ProductFamily.LASHES;
        } else if (isType("Blusher") || isType("Foundation")) {
            return ProductFamily.SKIN;
        }
        return null;
    }

    private String getProductType() {
        if (Stream.of("Eyeshadow", "Mascara", "Lipstick", "Blusher", "Foundation").anyMatch(s -> isType(s))) {
            return type;
        } else {
            return "Unknown";
        }
    }

    private ProductRange getProductRange() {
        if (isType("Lipstick") && suggestedPrice > 20
            || isType("Mascara") && suggestedPrice > 25 && packagingRecyclable) {
            return ProductRange.QUEEN;
        } else if (packagingRecyclable
            || isType("Foundation") && suggestedPrice > 10
            || isType("Lipstick") && suggestedPrice > 10
            || isType("Mascara") && suggestedPrice > 15) {
            return ProductRange.PROFESSIONAL;
        } else {
            return ProductRange.BUDGET;
        }
    }
}
