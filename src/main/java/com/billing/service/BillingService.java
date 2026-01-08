package com.billing.service;

import java.util.List;
import com.billing.entity.*;

public interface BillingService {

    Company saveCompany(Company company);
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
    List<Customer> getAllCustomers();
    List<Product> getAllProducts();
    Invoice saveInvoice(Invoice invoice);
    Payment savePayment(Payment payment);
    Product saveProduct(Product product);
	List<Invoice> getAllInvoices();	
	Invoice getInvoiceById(Long id);
//	finding the  Invoice using Id 
	Invoice updateInvoice(Long id, Invoice invoice);
//	finding the company using id 
	Company getCompanyById(Long id);
	

}
