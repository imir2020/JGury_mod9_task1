package by.javagur.spring.mapper;

import by.javagur.spring.database.entity.User;
import by.javagur.spring.database.entity.UserImage;
import by.javagur.spring.database.repository.UserRepository;
import by.javagur.spring.dto.DtoToUserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;


@Component
@RequiredArgsConstructor
public class DtoToUserImageMapper implements Mapper<DtoToUserImage, UserImage> {

    private  final UserRepository userRepository;

    @Override
    public UserImage map(DtoToUserImage object) {
        UserImage  userImage = new UserImage();
        copy(object,userImage);
        return userImage;
    }

    @Override
    public UserImage map(DtoToUserImage fromObject, UserImage toObject) {
        copy(fromObject,toObject);
        return Mapper.super.map(fromObject,toObject);
    }
    private void copy(DtoToUserImage object, UserImage userImage) {
        User user = getUser(object.getUserId());

        Optional.ofNullable(object.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image ->
                        userImage.setImage(image.getOriginalFilename())
                );
        userImage.setUser(user);
    }

    private User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}
