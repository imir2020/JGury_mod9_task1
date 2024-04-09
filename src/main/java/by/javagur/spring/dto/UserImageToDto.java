package by.javagur.spring.dto;


import by.javagur.spring.database.entity.User;
import lombok.Value;

@Value
public class UserImageToDto {
    Long id;
    String image;
    User user;
}
