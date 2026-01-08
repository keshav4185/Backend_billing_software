package com.billing.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.billing.entity.*;
import com.billing.service.BillingService;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "http://localhost:5173")

public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }
    
    @PostMapping("/products")
    public Product saveProduct(@RequestBody Product product) {
        return billingService.saveProduct(product);
    }

    @PostMapping("/company")
    public Company saveCompany(@RequestBody Company company) {
        return billingService.saveCompany(company);
    }
    @GetMapping("/company/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return billingService.getCompanyById(id);
    }

    
    
    @PostMapping("/customer")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return billingService.saveCustomer(customer);
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return billingService.getAllCustomers();
    }
    
    @PutMapping("/customer/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return billingService.updateCustomer(id, customer);
    }

    @DeleteMapping("/customer/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        billingService.deleteCustomer(id);
        return "Customer deleted successfully";
    }


    @GetMapping("/products")
    public List<Product> getProducts() {
        return billingService.getAllProducts();
    }

    @PostMapping("/invoice")
    public Invoice saveInvoice(@RequestBody Invoice invoice) {
        return billingService.saveInvoice(invoice);
    }
    @GetMapping("/invoices")
    public List<Invoice> getInvoices() {
        return billingService.getAllInvoices();
        
    }
    
    
    @GetMapping("/invoice/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) {
        return billingService.getInvoiceById(id);
    }

    @PutMapping("/invoice/{id}")
    public Invoice updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        return billingService.updateInvoice(id, invoice);
    }

    


    @PostMapping("/payment")
    public Payment savePayment(@RequestBody Payment payment) {
        return billingService.savePayment(payment);
    }
}
