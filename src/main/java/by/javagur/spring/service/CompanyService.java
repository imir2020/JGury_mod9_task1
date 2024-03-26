package by.javagur.spring.service;

import by.javagur.spring.database.repository.CompanyRepository;
import by.javagur.spring.dto.CompanyToDto;
import by.javagur.spring.listener.AccessType;
import by.javagur.spring.listener.EntityEvent;
import by.javagur.spring.mapper.CompanyToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CompanyToDtoMapper companyReadMapper;

    public Optional<CompanyToDto> findById(Integer id) {
        return companyRepository.findById(id).map(entity -> {
            applicationEventPublisher.publishEvent(new EntityEvent(entity, AccessType.DELETE));
            return new CompanyToDto(entity.getId(), null);
        });
    }

    public List<CompanyToDto> findAll() {
        return companyRepository.findAll().stream()
                .map(companyReadMapper::map)
                .toList();
    }
}
