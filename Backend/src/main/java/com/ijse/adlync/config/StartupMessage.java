package com.ijse.adlync.config;

import com.ijse.adlync.entity.Advertisement_typeEntity;
import com.ijse.adlync.entity.CategoryEntity;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import com.ijse.adlync.entity.enums.UserEntityRoleEnum;
import com.ijse.adlync.repository.Advertisement_typeRepository;
import com.ijse.adlync.repository.CategoryRepository;
import com.ijse.adlync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupMessage implements CommandLineRunner {


    @Value("${system.initialize.username}")
    String username;
    @Value("${system.initialize.password}")
    String password;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Advertisement_typeRepository advertisement_typeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeCategories();
        initializeAdvertisementTypes();
        initializeAdmin();

        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║                    SPRING BOOT APPLICATION                   ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║                                                              ║\n" +
                "║  🚀 Application is running on: http://localhost:8080        ║\n" +
                "║  📚 Swagger UI: http://localhost:8080/swagger-ui.html       ║\n" +
                "║  📖 API Docs: http://localhost:8080/api-docs                ║\n" +
                "║                                                              ║\n" +
                "║  💡 You can test your APIs using Swagger UI!               ║\n" +
                "║                                                              ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");
    }

    private void initializeAdmin() {
        try {
            System.out.println("Initializing Admin");
            boolean ifExists = userRepository.existsByRole(UserEntityRoleEnum.ADMIN);
            if (!ifExists) {
                UserEntity admin = UserEntity.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(UserEntityRoleEnum.ADMIN)
                        .name("System Admin")
                        .email("admin@example.com")
                        .build();

                userRepository.save(admin);
            } else {
                System.out.println("Admin already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void initializeCategories() {
        try {
            System.out.println("Initializing categories...");

            long existingCategoriesCount = categoryRepository.count();

            if (existingCategoriesCount == 0) {
                for (CategoryEntityNameEnum categoryName : CategoryEntityNameEnum.values()) {
                    CategoryEntity category = new CategoryEntity();
                    category.setName(categoryName);
                    categoryRepository.save(category);
                    System.out.println("Created category: " + categoryName);
                }
                System.out.println("Successfully initialized " + CategoryEntityNameEnum.values().length + " categories!");
            } else {
                System.out.println("Categories already exist (" + existingCategoriesCount + " found). Skipping initialization.");
            }

        } catch (Exception e) {
            System.err.println("Error initializing categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeAdvertisementTypes() {
        try {
            System.out.println("Initializing advertisement-types...");
            long exitingAdvertisementTypesCount = advertisement_typeRepository.count();
            if (exitingAdvertisementTypesCount == 0) {
                for (Advertisement_typeEntityTypeEnum type : Advertisement_typeEntityTypeEnum.values()) {
                    Advertisement_typeEntity advertisementType = new Advertisement_typeEntity();
                    advertisementType.setType(type);
                    advertisement_typeRepository.save(advertisementType);
                    System.out.println("Created advertisement type: " + type);
                }
                System.out.println("Successfully initialized advertisement-types!" + Advertisement_typeEntityTypeEnum.values().length + "advertisement types !");
            }
        } catch (Exception e) {
            System.err.println("Error initializing advertisement-types: " + e.getMessage());
            e.printStackTrace();
        }
    }

}