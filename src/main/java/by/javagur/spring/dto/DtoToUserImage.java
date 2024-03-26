package by.javagur.spring.dto;


import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class DtoToUserImage {
    Long userId;
    MultipartFile image;
}
