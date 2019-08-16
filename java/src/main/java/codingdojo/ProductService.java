package codingdojo;

public class ProductService {

    private DatabaseAccess db;

    public ProductService(DatabaseAccess db) {
        this.db = db;
    }

    /**
     * Processes new product form data, validates that it is correct.
     * If it is correct, add the new product to the database.
     */
    public Response validateAndAdd(ProductFormData productData) {
        return productData.getValidationFailure()
            .orElseGet(() -> storeProduct(productData.toProduct()));
    }

    private Response storeProduct(Product product) {
        int productId = db.storeProduct(product);
        return new Response(productId, 0, "Product Successfully Added");
    }

}
