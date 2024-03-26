package by.javagur.spring.service;

import by.javagur.spring.database.entity.UserImage;
import by.javagur.spring.database.repository.ImageRepository;
import by.javagur.spring.dto.DtoToUserImage;
import by.javagur.spring.dto.UserImageToDto;
import by.javagur.spring.mapper.DtoToUserImageMapper;
import by.javagur.spring.mapper.UserImageToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserImageService {
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final UserImageToDtoMapper userImageToDtoMapper;
    private final DtoToUserImageMapper dtoToUserImageMapper;

    public Optional<UserImageToDto> findById(Long id){
        return imageRepository.findById(id)
                .map(userImageToDtoMapper::map);
    }

    public List<UserImageToDto> findAllByUserId(Long userId){
        return imageRepository.findAllByUserId(userId)
                .stream().map(userImageToDtoMapper::map)
                .toList();
    }

    @Transactional
    public UserImageToDto create(DtoToUserImage dtoToUserImage){
        return Optional.of(dtoToUserImage)
                .map(dtoToUserImageMapper::map)
                .map(imageRepository::saveAndFlush)
                .map(userImageToDtoMapper::map)
                .orElseThrow();
    }

    public Optional<byte[]> findAvatar(Long id){
        return imageRepository.findById(id)
                .map(UserImage::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public Optional<UserImageToDto> update(Long id, DtoToUserImage dtoToUserImage){
        return imageRepository.findById(id)
                .map(entity-> dtoToUserImageMapper.map(dtoToUserImage, entity))
                .map(imageRepository::saveAndFlush)
                .map(userImageToDtoMapper::map);
    }

    @Transactional
    public boolean deleteById(Long id){
        return imageRepository.findById(id)
                .map(entity->{
                    imageRepository.delete(entity);
                    imageRepository.flush();
                    return true;
                })
                .orElse(false);
    }

}
