package com.woof.mapper;

import java.util.List;

import com.woof.domain.Cart;

public interface CartMapper {
	
	public void addToCart(Cart cart) throws Exception;
	
	public List<Cart> getCart(Cart cart) throws Exception;
	
	public List<Cart> getOrder(Cart cart) throws Exception;
	
	public void changeCheckStatus(Cart cart) throws Exception;
	
	public void removeFromCart(Cart cart) throws Exception;
	
	public void removeChecked(Cart cart) throws Exception;
	
	public void modifyQuantity(Cart cart) throws Exception;
}
