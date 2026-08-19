package org.example.ss06.tool;

import org.example.ss06.model.entity.Customer;
import org.example.ss06.repository.CustomerRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerTools {

    private final CustomerRepository customerRepository;

    public CustomerTools(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Tool(
            name = "getAllCustomers",
            description = """
            Lấy danh sách toàn bộ khách hàng trong hệ thống.
            Sử dụng tool này khi cần xem danh sách, tra cứu hàng loạt.
            """
    )
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Tool(
            name = "getCustomerById",
            description = """
            Tìm kiếm khách hàng theo ID.
            Sử dụng tool này khi biết chính xác ID của khách hàng cần tìm.
            """
    )
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Tool(
            name = "createCustomer",
            description = """
            Tạo mới một khách hàng.
            Chỉ sử dụng khi có đầy đủ: tên (name), số điện thoại (phone), và email (email).
            """
    )
    public Customer createCustomer(String name, String phone, String email) {
        Customer customer = Customer.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .build();
        return customerRepository.save(customer);
    }

    @Tool(
            name = "updateCustomerPhoneOrEmail",
            description = """
            Cập nhật số điện thoại hoặc email của khách hàng.
            Cần có ID của khách hàng.
            Chỉ truyền vào phone hoặc email muốn thay đổi, nếu không đổi thì để null.
            """
    )
    public Customer updateCustomerPhoneOrEmail(Long id, String phone, String email) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (phone != null && !phone.isBlank()) {
                customer.setPhone(phone);
            }
            if (email != null && !email.isBlank()) {
                customer.setEmail(email);
            }
            return customerRepository.save(customer);
        }
        return null; // Hoặc ném Exception nếu cần thiết
    }
}
