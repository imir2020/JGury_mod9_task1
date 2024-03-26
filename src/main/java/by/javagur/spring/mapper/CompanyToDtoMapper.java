package by.javagur.spring.mapper;

import by.javagur.spring.database.entity.Company;
import by.javagur.spring.dto.CompanyToDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyToDtoMapper implements Mapper<Company, CompanyToDto>{
    @Override
    public CompanyToDto map(Company object) {
        return new CompanyToDto(
                object.getId(),
                object.getName()
        );
    }
}
