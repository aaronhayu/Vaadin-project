package com.example.shop.backend.configurations;

import com.example.shop.backend.models.Transaction;
import com.example.shop.backend.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@AllArgsConstructor
public class DatabaseSeeder {

    private final TransactionRepository transactionRepository;

    /**
     * Bean definition for seeding the database with initial data.
     * @return CommandLineRunner instance to seed the database
     */
    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            // Check if database is already seeded
            if (transactionRepository.count() == 0) {
                seedPaymentsFromCsv();
            }
        };
    }

    /**
     * Seed payments data from a CSV file located in the classpath.
     */
    private void seedPaymentsFromCsv() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("payments.csv").getInputStream()))) {

            String line;
            // Skip the header line
            br.readLine();

            // Read each line from the CSV and save transactions to the database
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Transaction transaction = Transaction.builder()
                        .transactionId(data[0])
                        .product(data[1])
                        .amount(new BigDecimal(data[2]))
                        .buyerName(data[3])
                        .buyerEmail(data[4])
                        .status(Transaction.TransactionStatus.valueOf(data[5]))
                        .date(LocalDateTime.parse(data[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build();

                transactionRepository.save(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
