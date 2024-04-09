package by.javagur.spring.mapper;

import by.javagur.spring.database.entity.Company;
import by.javagur.spring.database.entity.User;
import by.javagur.spring.database.repository.CompanyRepository;
import by.javagur.spring.dto.DtoToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class DtoToUserMapper implements Mapper<DtoToUser, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User map(DtoToUser fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(DtoToUser object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    private void copy(DtoToUser object, User user) {
        user.setUsername(object.getUsername());
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setBirthDate(object.getBirthDate());
        user.setRole(object.getRole());
        user.setCompany(getCompany(object.getCompanyId()));

        Optional.ofNullable(object.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
    }

    private Company getCompany(Integer companyId) {
        return Optional.ofNullable(companyId)
                .flatMap(companyRepository::findById)
                .orElse(null);
    }
}
