//package name here 

import java.util.List;    
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional; 
import sg.edu.nus.cart.interfacemethods.ProductInterface; 
import sg.edu.nus.cart.model.Product;
import sg.edu.nus.cart.repository.ProductRepository; 

//Goh Ching Tard
@Service 
@Transactional 
public class ProductImplementation implements ProductInterface { 

	@Autowired 
	ProductRepository productRepo; 

	@Override 
	@Transactional 
	public boolean saveProduct(Product product) { 
		if (productRepo.save(product) != null) 
			return true; 
		else 
			return false; 
	} 

	@Override 
	@Transactional 
	public List<Product> retrieveProducts() { 
		return productRepo.ShowProducts(); 
	} 
	
/*
	@Override 
	@Transactional 
	public List<Product> SearchProductByCategory(String category) { 
		return prepo.SearchProductByCategory(category); 
	} 
*/	
    @Override 
    @Transactional 
    public Product findProductById(Long id) { 
        return productRepo.findById(id).get(); 
    } 
   
    @Override 
    @Transactional 
    public void deleteProduct(Product product) { 
        productRepo.delete(product); 
    }
    
	@Override 
	@Transactional 
	public boolean createProduct(Product product) { 
		if (productRepo.save(product) != null) 
			return true; 
		else 
			return false; 
	} 
}
