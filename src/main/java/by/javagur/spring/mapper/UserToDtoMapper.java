package by.javagur.spring.mapper;

import by.javagur.spring.database.entity.User;
import by.javagur.spring.dto.CompanyToDto;
import by.javagur.spring.dto.UserToDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserToDtoMapper implements Mapper<User, UserToDto> {

    private final CompanyToDtoMapper companyReadMapper;

    @Override
    public UserToDto map(User object) {
        CompanyToDto company = Optional.ofNullable(object.getCompany())
                .map(companyReadMapper::map)
                .orElse(null);
        return new UserToDto(
                object.getId(),
                object.getUsername(),
                object.getBirthDate(),
                object.getFirstname(),
                object.getLastname(),
                object.getRole(),
                object.getImage(),
                company
        );
    }
}
