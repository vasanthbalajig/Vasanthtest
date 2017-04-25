package com.janani.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.janani.model.Book;
import com.janani.model.Order;
import com.janani.model.OrderItem;
import com.janani.model.User;
import com.janani.service.BookService;
import com.janani.service.OrderItemService;
import com.janani.service.OrderService;

@Controller
@RequestMapping("/orderItems")
public class OrderItemController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private BookService bookService;


	@PostMapping("/addToCart")
	public String addToCart(@RequestParam("book_id") Long bookId, @RequestParam("qty") Integer qty,  HttpServletRequest request, HttpSession session) {

		User user = (User) session.getAttribute("LOGGED_IN_USER");
		
		Order order = (Order) session.getAttribute("MY_CART_ITEMS");
	
		//create order
		if ( order == null ){	
			order = new Order();
			order.setUser(user);
			order.setTotalPrice(0);
			order.setStatus("ORDERED");
		}

		//store orderItems
		List<OrderItem> orderItems = order.getOrderItems();
		
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		
		Book book = bookService.findOne(bookId);
		orderItem.setBook(book);
		orderItem.setQuantity(qty);
		orderItems.add(orderItem);
		
		order.setOrderItems(orderItems);
		
		session.setAttribute("MY_CART_ITEMS", order);
		
		//orderService.save(order);	
		
		return "redirect:../orders/cart";
	}

	@GetMapping("/list")
	public String list(HttpSession session) {
		return null;

	}
	
	@GetMapping("/remove")
	public String removeItemFromCart(@RequestParam("id") Integer id, HttpSession session) {

		
		Order order = (Order) session.getAttribute("MY_CART_ITEMS");
		
		if (order != null && order.getOrderItems().size() > 0){
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.remove(id);
			order.setOrderItems(orderItems);
			session.setAttribute("MY_CART_ITEMS", order);
		}
		
		return "redirect:../orders/cart";
	
	}
	
	@GetMapping("/emptycart")
	public String emptyCartid( HttpSession session) {

		session.removeAttribute("MY_CART_ITEMS");
		return "redirect:../orders/cart";
	
	}
	
	

}
