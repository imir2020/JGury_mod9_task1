package by.javaguru.integration.repository;

import by.javagur.spring.database.entity.Company;
import by.javagur.spring.database.repository.CompanyRepository;
import by.javaguru.annotation.IT;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class CompanyRepositoryTest {
//
//    private static final Integer APPLE_ID = 5;
//    private final EntityManager entityManager;
//    private final CompanyRepository companyRepository;
//
//
//
//    @Test
//    void checkFindByQueries() {
//        companyRepository.findByName("Google");
////        var companies = companyRepository.findAllByNameContainingIgnoreCase("a");
////        assertThat(companies).hasSize(3);
//
//    }
//
//    @Test
//    void delete() {
//        var maybeCompany = companyRepository.findById(APPLE_ID);
//        assertTrue(maybeCompany.isPresent());
//        maybeCompany.ifPresent(companyRepository::delete);
//        entityManager.flush();
//        assertTrue(companyRepository.findById(APPLE_ID).isEmpty());
//    }
//
//
//    @Test
//    void findById() {
//        var company = entityManager.find(Company.class, 1);
//        assertNotNull(company);
//        assertThat(company.getLocales()).hasSize(2);
//    }
//
//    @Test
//    void save() {
//        var company = Company.builder()
//                .name("Apple1")
//                .locales(Map.of(
//                        "ru", "Apple описание",
//                        "en", "Apple description"
//                ))
//                .build();
//        entityManager.persist(company);
//        assertNotNull(company.getId());
//    }

}
