package com.woof.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.woof.domain.Cart;
import com.woof.domain.Item;
import com.woof.service.CartService;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("/addToCart")
	public String addToCart(@ModelAttribute("item") Item item, @RequestParam("itemQuantity") int itemQuantity, Principal principal, Model model) throws Exception {
		String username = principal.getName();
		log.info("/addToCart POST: " + item + ", itemQuantity: " + itemQuantity + ", username: " + username);
		String itemNo = String.valueOf(item.getItemNo()); 
		cartService.addToCart(item, username, itemQuantity);
		cartService.deleteDuplicateRows(itemNo, username);
		return "redirect:/item/itemList";
	}
	
	@GetMapping("/myCart")
	public String getCart(Principal principal, Model model) throws Exception {
		String username = principal.getName();
		log.info("/myCart GET: username: " + username);
		// for test
		List<Cart> cartList = cartService.getCart(username);
		log.info("/myCart GET: cartList: " + cartList.toString());
		model.addAttribute("cartList", cartList);
		return "account/myCart/myCart";
	}
		
	@PostMapping("/getOrder")
	public String getOrder(@RequestParam("selectedItems") List<String> selectedItems, Principal principal, Cart cart, Model model) throws Exception {
		String username = principal.getName();
		log.info("/getOrder POST selectedItems: " + selectedItems.toString() + ", username: " + username);
		List<Cart> cartList = cartService.getOrder(selectedItems, username);
		int totalPrice = calculateTotalPrice(cartList);
		
		model.addAttribute("cartList", cartList);
		log.info("/getOrder cartList: " + cartList);
		model.addAttribute("totalPrice", totalPrice);
		log.info("/getOrder totalPrice: " + totalPrice);
		return "account/myCart/myOrder";
	}
	
	private int calculateTotalPrice(List<Cart> cartList) {
		int totalPrice = 0;
		for(Cart cart : cartList) {
			int itemQuantity = cart.getItemQuantity();
			int itemPrice = cart.getItemPrice();
			totalPrice += itemQuantity * itemPrice;
		}
		return totalPrice;
	}

	@PostMapping("/removeChecked")
	public ResponseEntity<String> removeChecked(@RequestBody Map<String, Object> requestData) throws Exception {
		log.info("/removeChecked POST requestBody: " + requestData.toString());
		
		List<String> selectedItems = (List<String>) requestData.get("selectedItems");
		String username = (String) requestData.get("username");
		
		log.info("/removeChecked POST selectedItems: " + selectedItems.toString() + ", username: " + username);
		cartService.removeChecked(selectedItems, username);
		return ResponseEntity.ok("Items removed successfully");
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity<String> removeFromCart(@RequestBody Map<String, Object> requestData) throws Exception {
		log.info("/removeFromCart POST requestBody: " + requestData.toString());
		
		String itemNo = (String) requestData.get("itemNo");
		String username = (String) requestData.get("username");
		
		log.info("/removeFromCart POST itemNo: " + itemNo + ", username: " + username);
		cartService.removeFromCart(itemNo, username);
		return ResponseEntity.ok("Item removed successfully");
	}
	
//	@RequestMapping("/changeCheckStatus")
//	public void changeCheckStatus(Cart cart) throws Exception {
//		cartService.changeCheckStatus(cart);
//	}
	
	@PostMapping("/modifyQuantity")
	public ResponseEntity<String> modifyQuantity(@RequestBody Map<String, Object> requestData) throws Exception {
		log.info("/modifyQuantity: requestData: " + requestData.toString());
		
		Cart cart = new Cart();
		cart.setItemNo(Integer.parseInt(requestData.get("itemNo").toString()));
		cart.setUsername(requestData.get("username").toString());
		cart.setNewQuantity(Integer.parseInt(requestData.get("newQuantity").toString()));
		cartService.modifyQuantity(cart);
		
		return ResponseEntity.ok("Quantity updated");
	}
}
