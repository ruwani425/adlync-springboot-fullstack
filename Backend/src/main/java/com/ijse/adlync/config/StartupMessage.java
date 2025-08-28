package com.ijse.adlync.config;

import com.ijse.adlync.entity.CategoryEntity;
import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import com.ijse.adlync.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupMessage implements CommandLineRunner {


    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeCategories();

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

    private void initializeCategories() {
        try {
            System.out.println("🔧 Initializing categories...");

            long existingCategoriesCount = categoryRepository.count();

            if (existingCategoriesCount == 0) {
                for (CategoryEntityNameEnum categoryName : CategoryEntityNameEnum.values()) {
                    CategoryEntity category = new CategoryEntity();
                    category.setName(categoryName);
                    categoryRepository.save(category);
                    System.out.println("✅ Created category: " + categoryName);
                }
                System.out.println("🎉 Successfully initialized " + CategoryEntityNameEnum.values().length + " categories!");
            } else {
                System.out.println("ℹ️  Categories already exist (" + existingCategoriesCount + " found). Skipping initialization.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error initializing categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

}