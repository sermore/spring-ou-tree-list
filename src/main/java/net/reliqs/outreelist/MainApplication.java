package net.reliqs.outreelist;

import net.reliqs.outreelist.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    @Autowired
    DataService dataService;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {
            if (args.length > 0) {
                int len = Integer.parseInt(args[0]);
                dataService.generate(len);
            }
        };
    }
}