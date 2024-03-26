package by.javagur.spring.database.repository;


import by.javagur.spring.database.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<UserImage, Long> {

    List<UserImage> findAllByUserId(Long userId);
}
