package by.javagur.spring.mapper;


import by.javagur.spring.database.entity.UserImage;
import by.javagur.spring.dto.UserImageToDto;
import org.springframework.stereotype.Component;

@Component
public class UserImageToDtoMapper implements Mapper<UserImage, UserImageToDto> {

    @Override
    public UserImageToDto map(UserImage object) {
        return new UserImageToDto(object.getId(),
                object.getImage(),
                object.getUser());
    }

    public UserImage map(UserImageToDto userImageToDto) {
        return UserImage.builder()
                .id(userImageToDto.getId())
                .image(userImageToDto.getImage())
                .user(userImageToDto.getUser())
                .build();
    }
}
