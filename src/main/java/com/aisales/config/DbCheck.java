package com.aisales.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class DbCheck implements CommandLineRunner {

    private final DataSource dataSource;

    public DbCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        log.info("=== ПРОВЕРКА ПОДКЛЮЧЕНИЯ К БАЗЕ ДАННЫХ ===");

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            log.info("База данных: {}", metaData.getDatabaseProductName());
            log.info("Версия: {}", metaData.getDatabaseProductVersion());
            log.info("URL: {}", metaData.getURL());

            // Получаем список таблиц
            log.info("--- ТАБЛИЦЫ В БАЗЕ ДАННЫХ ---");
            ResultSet tables = metaData.getTables(null, "public", "%", new String[]{"TABLE"});

            int count = 0;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                log.info("  [{}] {}", ++count, tableName);
            }

            if (count == 0) {
                log.warn("Таблиц не найдено! Flyway миграции еще не применились.");
            } else {
                log.info("Всего таблиц: {}", count);
            }

            log.info("=== ПОДКЛЮЧЕНИЕ УСПЕШНО ===");

        } catch (SQLException e) {
            log.error("=== ОШИБКА ПОДКЛЮЧЕНИЯ ===");
            log.error("Сообщение: {}", e.getMessage());
            log.error("SQL State: {}", e.getSQLState());
        }
    }
}
