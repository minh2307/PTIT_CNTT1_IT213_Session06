package org.example.ss06.seeder;

import lombok.RequiredArgsConstructor;
import org.example.ss06.model.entity.Customer;
import org.example.ss06.model.entity.DentalService;
import org.example.ss06.model.entity.Doctor;
import org.example.ss06.repository.CustomerRepository;
import org.example.ss06.repository.DentalServiceRepository;
import org.example.ss06.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final DentalServiceRepository dentalServiceRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (doctorRepository.count() == 0) {
            doctorRepository.saveAll(List.of(
                    Doctor.builder().name("Nguyễn Văn A").specialty("Nhổ răng").phone("0123456789").description("Chuyên gia nhổ răng").active(true).build(),
                    Doctor.builder().name("Trần Thị B").specialty("Niềng răng").phone("0987654321").description("Chuyên gia niềng răng").active(true).build(),
                    Doctor.builder().name("Lê Văn C").specialty("Trám răng").phone("0345678912").description("Chuyên gia trám răng").active(true).build()
            ));
        }

        if (dentalServiceRepository.count() == 0) {
            dentalServiceRepository.saveAll(List.of(
                    DentalService.builder().name("Nhổ răng khôn").description("Nhổ răng khôn không đau").price(new BigDecimal("1000000")).durationMinutes(30).active(true).build(),
                    DentalService.builder().name("Niềng răng Invisalign").description("Niềng răng trong suốt").price(new BigDecimal("50000000")).durationMinutes(60).active(true).build(),
                    DentalService.builder().name("Trám răng thẩm mỹ").description("Trám răng bằng Composite").price(new BigDecimal("300000")).durationMinutes(30).active(true).build(),
                    DentalService.builder().name("Cạo vôi răng").description("Cạo vôi răng và đánh bóng").price(new BigDecimal("200000")).durationMinutes(30).active(true).build()
            ));
        }

        if (customerRepository.count() == 0) {
            customerRepository.saveAll(List.of(
                    Customer.builder().name("Phạm Văn D").phone("0111222333").email("phamd@example.com").build(),
                    Customer.builder().name("Hoàng Thị E").phone("0444555666").email("hoange@example.com").build()
            ));
        }
    }
}
