// package name here

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.caproject.model.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {

}
