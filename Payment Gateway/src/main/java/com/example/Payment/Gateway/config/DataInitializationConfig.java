package com.example.Payment.Gateway.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Payment.Gateway.entity.Merchant;
import com.example.Payment.Gateway.repository.MerchantRepository;

@Configuration
public class DataInitializationConfig {

    @Bean
    CommandLineRunner init(MerchantRepository repo) {
        return args -> {
            if (!repo.existsById("merchant1")) {
                Merchant m = new Merchant();
                m.setApiKey("merchant1");
                m.setKycApproved(true);
                m.setActive(true);
                repo.save(m);
            }
        };
    }
}
