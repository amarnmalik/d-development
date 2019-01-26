/******************************************************************************
Online Java Compiler. Code, Compile, Run and Debug java program online.
*******************************************************************************/
package com.test.fab;

import java.util.List;
import java.util.ArrayList;
import java.time.Period;
import java.time.LocalDate;

public class RetailShop
{
	public static void main(String[] args) {
		System.out.println("Hello World");
    	Customer custObj = new Customer(true, false, LocalDate.of(2017, 4, 23));
    	custObj.placeOrder();
	}
}

class Product {
	int productId = 0;
    String name = "";
    int quantity = 0;
    double price = 0.0;
    double productCost = 0.0;
	String categoryName = "";
	
	Product(int productId, String name, int quantity, Double price, String categoryName) {
	    this.productId = productId;
	    this.name = name;
	    this.quantity = quantity;
	    this.price = price;
	    this.categoryName = categoryName;
	}

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setPrice(double price) {
        this.price = price;
    }    
    public double getPrice() {
        return price;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public Double getProductCost() {
        return quantity * price;
    }
}

class Products {
    private final List<Product> products = new ArrayList<Product>();

    public Products () {
        this.initStoreItems();
    }
    public List<Product> getProducts() {
        return products;
    }
    public void initStoreItems() {
        this.products.add(new Product(1, "bakery", 1, 10.0, "glosary"));
        this.products.add(new Product(2, "fishmongers", 1, 9.0, "glosary"));
        this.products.add(new Product(3, "curtains", 1, 50.0, "furniture"));
        this.products.add(new Product(4, "sofas", 1, 110.0, "furniture"));        
    }
}

class ShoppingCart {
    List<Product> cartItems = new ArrayList<Product>();
    
    public void addProductToCartByPID(int pid) {
        Product product = getProductByProductID(pid);
        addToCart(product);
    }

    public void addToCart(Product product) {
        cartItems.add(product);
    }
    private Product getProductByProductID(int pid) {
        List<Product> products = new Products().getProducts();
        for (Product prod: products) {
            return prod;
        }
        return null;
    }    
    public List<Product> getShoppingCart() {
        cartItems = new Products().getProducts();
        return cartItems;
    }
    
    public void checkOutCart(Customer custObj) {
    	new Bill().getBillDetails(getShoppingCart(), custObj);
    }    
}

class Bill {
    double totalBill=0, totalDiscount=0;
    
    public void getBillDetails( List<Product> cartObj, Customer customerObj) {
    	calculateBillAmount(cartObj, customerObj);
        System.out.println("Net bill : " + totalBill + "\n Discount : " + totalDiscount + 
        ", \nTotal bill after discount :" + (totalBill - totalDiscount));
    }

	private void calculateBillAmount(List<Product> products, Customer customerObj) {
		double subTotalGlosaryBill = 0, subTotalBill = 0, percentageDiscount = 0;
	    int plandiscount = 0;
        for(Product prod : products) {
            if(prod.getCategoryName().equals("glosary")) {
                subTotalGlosaryBill += prod.getProductCost();
            } else if(prod.getCategoryName().equals("furniture")) {
                subTotalBill += prod.getProductCost();
            }
        }
        
        totalBill = subTotalBill + subTotalGlosaryBill;
        percentageDiscount = Discount.customerPercentageDiscount(customerObj, subTotalBill);
        plandiscount = Discount.customerPlaneDiscount(totalBill, percentageDiscount); 
        totalDiscount = percentageDiscount + plandiscount;
	}
}

class Discount {
	final static double employee = 0.3;
	final static double affiliate = 0.1;
	final static double registration = 0.05;
	final static int planDiscount = 5;
	final static int noDiscount = 0;

	// 	6.	A	user	can	get	only	one	of	the	percentage	based	discounts	on	a	bill    
    public static double customerPercentageDiscount(Customer custObj, double totalBill) {
        if (custObj.isEmployee()) { //1. user	is	an	employee	of	the	store,	he	gets	a	30%	discount
            return totalBill * employee;
        } else if (custObj.isAffiliate()) { //2. 	user	is	an	affiliate	of	the	store,	he	gets	a	10%	discount
            return totalBill * affiliate;
        } else if (UtilityClass.getYearDifference(custObj.getRegistrationDate()) >= 2) { //3. 	user	has	been	a	customer	for	over	2	years,	he	gets	a	5%	discount
            return totalBill * registration;
        } else {
            return totalBill * noDiscount;
        }
    }
    
    //4. For	every	$100	on	the	bill,	there	would	be	a	$	5	discount	(e.g.	for	$	990,	you	get	$	45	
    // as	a	discount)
    public static int customerPlaneDiscount(double totalBill, double percentageDiscount) {
        return (int)((totalBill - percentageDiscount) / 100) * planDiscount;
    }	
}

class Customer {
    boolean employee;
    boolean affiliate;
    LocalDate registrationDate;
    ShoppingCart cartObj = null;

    Customer(boolean employee, boolean affiliate, LocalDate registrationDate) {
        this.employee = employee; this.affiliate = affiliate; this.registrationDate = registrationDate;
    }
    public void setEmployee(boolean employee) {
        this.employee = employee;
    }
    public boolean isEmployee() {
        return employee;
    }
    public void setAffiliate(boolean affiliate) {
        this.affiliate = affiliate;
    }
    public boolean isAffiliate() {
        return affiliate;
    }
    public void setRegistrationDate(int year, int month, int date) {
        registrationDate = LocalDate.of(year, month, date);
    }
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void placeOrder() {
    	new ShoppingCart().checkOutCart(new Customer(employee, affiliate, registrationDate));
    }
}

class UtilityClass {
    public static int getYearDifference(LocalDate registrationDate) {
        return Period.between(registrationDate, LocalDate.now()).getYears();
    }
}