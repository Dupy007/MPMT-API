package com.dupy.MPMT;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class MpmtApplication implements CommandLineRunner {

    private final DataSource dataSource;

    public MpmtApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(MpmtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Connexion à la base de données réussie !");
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion à la base de données !");
            System.err.println(e.getMessage());
            System.exit(1); // Arrête l'application si la BDD est inaccessible
        }
    }
}
