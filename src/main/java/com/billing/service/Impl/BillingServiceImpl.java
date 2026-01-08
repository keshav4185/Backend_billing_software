package com.billing.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billing.entity.*;
import com.billing.repository.*;
import com.billing.service.BillingService;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public BillingServiceImpl(CustomerRepository customerRepository,
                              ProductRepository productRepository,
                              CompanyRepository companyRepository,
                              InvoiceRepository invoiceRepository,
                              PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Customer updateCustomer(Long id, Customer customer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existingCustomer.setName(customer.getName());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setGst(customer.getGst());
        existingCustomer.setAddress(customer.getAddress());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customerRepository.delete(customer);
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

//    @Override
//    public Invoice saveInvoice(Invoice invoice) {
//        invoice.setInvoiceDate(LocalDate.now());
//        return invoiceRepository.save(invoice);
//    }
    
    @Override
    public Invoice saveInvoice(Invoice invoice) {

        double subTotal = 0.0;
        double totalAmount = 0.0;

        for (InvoiceItem item : invoice.getItems()) {

            // 🔗 VERY IMPORTANT
            item.setInvoice(invoice);
            

            double price = item.getPrice() != null ? item.getPrice() : 0;
            double qty = item.getQuantity() != null ? item.getQuantity() : 0;
            double tax = item.getTax() != null ? item.getTax() : 0;
            double discount = item.getDiscount() != null ? item.getDiscount() : 0;

            double rowBase = price * qty;
            double rowTotal = rowBase + tax - discount;

            item.setRowTotal(rowTotal);

            subTotal += rowBase;
            totalAmount += rowTotal;
        }

        invoice.setSubTotal(subTotal);
        invoice.setTotalAmount(totalAmount);

        if (invoice.getAdvanceAmount() != null) {
            invoice.setBalanceAmount(totalAmount - invoice.getAdvanceAmount());
        } else {
            invoice.setBalanceAmount(totalAmount);
        }

        invoice.setInvoiceDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    
    @Override
    public Payment savePayment(Payment payment) {
        payment.setPaymentDate(LocalDate.now());
        return paymentRepository.save(payment);
    }
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    
//    to fetch  the invoices using Id 
    
    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }
    
//    for updating the invoices using the id 
    
    
    @Override
    public Invoice updateInvoice(Long id, Invoice invoice) {

        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        existingInvoice.setCustomer(invoice.getCustomer());
        existingInvoice.setCompany(invoice.getCompany());
        existingInvoice.setEmployee(invoice.getEmployee());
        existingInvoice.setAdvanceAmount(invoice.getAdvanceAmount());
        existingInvoice.setPaymentStatus(invoice.getPaymentStatus());

        // 🔴 Remove old items
        existingInvoice.getItems().clear();

        double subTotal = 0.0;
        double totalAmount = 0.0;

        // 🔵 Add updated items
        for (InvoiceItem item : invoice.getItems()) {

            item.setInvoice(existingInvoice);

            double price = item.getPrice() != null ? item.getPrice() : 0;
            double qty = item.getQuantity() != null ? item.getQuantity() : 0;
            double tax = item.getTax() != null ? item.getTax() : 0;
            double discount = item.getDiscount() != null ? item.getDiscount() : 0;

            double rowBase = price * qty;
            double rowTotal = rowBase + tax - discount;

            item.setRowTotal(rowTotal);

            subTotal += rowBase;
            totalAmount += rowTotal;

            existingInvoice.getItems().add(item);
        }

        existingInvoice.setSubTotal(subTotal);
        existingInvoice.setTotalAmount(totalAmount);
        existingInvoice.setBalanceAmount(
                invoice.getAdvanceAmount() != null
                        ? totalAmount - invoice.getAdvanceAmount()
                        : totalAmount
        );

        return invoiceRepository.save(existingInvoice);
    }

    
//    finding the company using id  
    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    
    
    

}
