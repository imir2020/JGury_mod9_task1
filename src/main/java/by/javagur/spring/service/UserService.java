package by.javagur.spring.service;

import by.javagur.spring.database.entity.User;
import by.javagur.spring.database.entity.UserImage;
import by.javagur.spring.database.repository.UserRepository;
import by.javagur.spring.dto.*;
import by.javagur.spring.mapper.DtoToUserMapper;
import by.javagur.spring.mapper.UserImageToDtoMapper;
import by.javagur.spring.mapper.UserToDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.javagur.spring.database.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserToDtoMapper userReadMapper;
    private final DtoToUserMapper userCreateEditMapper;
    private final ImageService imageService;
    private final UserImageService userImageService;
    private final UserImageToDtoMapper userImageToDtoMapper;

    public Page<UserToDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .build();

        return userRepository.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }


    public List<UserToDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Optional<UserToDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public UserToDto create(DtoToUser userDto) {
        return Optional.of(userDto)
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return userCreateEditMapper.map(dto);
                })
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }


    @Transactional
    public Optional<UserToDto> update(Long id, DtoToUser userDto) {
        return userRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDto.getImage());
                    return userCreateEditMapper.map(userDto, entity);
                })
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    @SneakyThrows
    public Long addUserImage(Long userId, MultipartFile image) {
        uploadImage(image);
        UserImageToDto userImageToDto = userImageService.create(new DtoToUserImage(userId, image));
        UserImage map = userImageToDtoMapper.map(userImageToDto);
        userRepository.findById(userId)
                .map(user -> {
                    user.addUserImage(map);
                    return userRepository.saveAndFlush(user);
                });
        return map.getId();
    }

    @Transactional
    public Optional<UserToDto> removeUserImage(Long userId, Long userImageId) {
        var userImage =
                userImageService.findById(userImageId)
                        .map(userImageToDtoMapper::map).orElseThrow();

        return userRepository.findById(userId)
                .map(user -> {
                    user.removeUserImage(userImage);
                    userImageService.deleteById(userImageId);
                    userRepository.saveAndFlush(user);
                    return user;
                })
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));

    }
}
