package sg.edu.nus.cart.interfacemethods;
import sg.edu.nus.cart.model.Product;   
import java.util.List; 

//Goh Ching Tard
public interface ProductInterface {

	public boolean saveProduct(Product product); 
	public boolean createProduct(Product product); 
	public List<Product> retrieveProducts(); 
	public Product findProductById(Long id); 
	public void deleteProduct(Product product); 
} 

