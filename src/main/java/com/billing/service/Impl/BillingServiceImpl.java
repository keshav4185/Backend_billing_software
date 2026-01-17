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

    // ---------------- COMPANY ----------------
    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    // ---------------- CUSTOMER ----------------
    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        existing.setName(customer.getName());
        existing.setPhone(customer.getPhone());
        existing.setGst(customer.getGst());
        existing.setAddress(customer.getAddress());
        return customerRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ---------------- PRODUCT ----------------
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ---------------- INVOICE ----------------
    @Override
    public Invoice saveInvoice(Invoice invoice) {

        double subTotal = 0.0;
        double totalCgst = 0.0;
        double totalSgst = 0.0;
        double totalAmount = 0.0;

        for (InvoiceItem item : invoice.getItems()) {
            item.setInvoice(invoice);

            // ✅ IF FRONTEND ALREADY CALCULATED
            if (item.getRowTotal() != null) {

                double baseAmount =
                        (item.getPrice() != null ? item.getPrice() : 0.0) *
                        (item.getQuantity() != null ? item.getQuantity() : 0.0);

                subTotal += baseAmount;
                totalCgst += item.getCgstAmount() != null ? item.getCgstAmount() : 0.0;
                totalSgst += item.getSgstAmount() != null ? item.getSgstAmount() : 0.0;
                totalAmount += item.getRowTotal();

                continue; // 🔥 DO NOT RECALCULATE
            }

            // ⛑ FALLBACK CALCULATION (SAFETY)
            double price = item.getPrice() != null ? item.getPrice() : 0.0;
            double qty = item.getQuantity() != null ? item.getQuantity() : 0.0;
            double discountPercent = item.getDiscount() != null ? item.getDiscount() : 0.0;
            double taxPercent = item.getTax() != null ? item.getTax() : 0.0;

            double baseAmount = price * qty;
            double discountAmount = baseAmount * (discountPercent / 100);
            double taxableAmount = baseAmount - discountAmount;
            double taxAmount = taxableAmount * (taxPercent / 100);

            double cgst = taxAmount / 2;
            double sgst = taxAmount / 2;
            double rowTotal = taxableAmount + taxAmount;

            item.setRowTotal(rowTotal);
            item.setCgstAmount(cgst);
            item.setSgstAmount(sgst);

            subTotal += baseAmount;
            totalCgst += cgst;
            totalSgst += sgst;
            totalAmount += rowTotal;
        }

        invoice.setSubTotal(subTotal);
        invoice.setTotalCgst(totalCgst);
        invoice.setTotalSgst(totalSgst);
        invoice.setTotalAmount(totalAmount);

        invoice.setBalanceAmount(
                invoice.getAdvanceAmount() != null
                        ? totalAmount - invoice.getAdvanceAmount()
                        : totalAmount
        );

        invoice.setInvoiceDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(Long id, Invoice invoice) {

        Invoice existing = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        existing.setInvoiceNumber(invoice.getInvoiceNumber());
        existing.setCompany(invoice.getCompany());
        existing.setCustomer(invoice.getCustomer());
        existing.setEmployee(invoice.getEmployee());
        existing.setAdvanceAmount(invoice.getAdvanceAmount());
        existing.setPaymentStatus(invoice.getPaymentStatus());

        existing.getItems().clear();

        double subTotal = 0.0;
        double totalCgst = 0.0;
        double totalSgst = 0.0;
        double totalAmount = 0.0;

        for (InvoiceItem item : invoice.getItems()) {
            item.setInvoice(existing);

            if (item.getRowTotal() != null) {

                double baseAmount =
                        (item.getPrice() != null ? item.getPrice() : 0.0) *
                        (item.getQuantity() != null ? item.getQuantity() : 0.0);

                subTotal += baseAmount;
                totalCgst += item.getCgstAmount() != null ? item.getCgstAmount() : 0.0;
                totalSgst += item.getSgstAmount() != null ? item.getSgstAmount() : 0.0;
                totalAmount += item.getRowTotal();

                existing.getItems().add(item);
                continue;
            }

            double price = item.getPrice() != null ? item.getPrice() : 0.0;
            double qty = item.getQuantity() != null ? item.getQuantity() : 0.0;
            double discountPercent = item.getDiscount() != null ? item.getDiscount() : 0.0;
            double taxPercent = item.getTax() != null ? item.getTax() : 0.0;

            double baseAmount = price * qty;
            double discountAmount = baseAmount * (discountPercent / 100);
            double taxableAmount = baseAmount - discountAmount;
            double taxAmount = taxableAmount * (taxPercent / 100);

            double cgst = taxAmount / 2;
            double sgst = taxAmount / 2;
            double rowTotal = taxableAmount + taxAmount;

            item.setRowTotal(rowTotal);
            item.setCgstAmount(cgst);
            item.setSgstAmount(sgst);

            subTotal += baseAmount;
            totalCgst += cgst;
            totalSgst += sgst;
            totalAmount += rowTotal;

            existing.getItems().add(item);
        }

        existing.setSubTotal(subTotal);
        existing.setTotalCgst(totalCgst);
        existing.setTotalSgst(totalSgst);
        existing.setTotalAmount(totalAmount);

        existing.setBalanceAmount(
                invoice.getAdvanceAmount() != null
                        ? totalAmount - invoice.getAdvanceAmount()
                        : totalAmount
        );

        return invoiceRepository.save(existing);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice existing = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoiceRepository.delete(existing);
    }

    // ---------------- PAYMENT ----------------
    @Override
    public Payment savePayment(Payment payment) {
        payment.setPaymentDate(LocalDate.now());
        return paymentRepository.save(payment);
    }
}
