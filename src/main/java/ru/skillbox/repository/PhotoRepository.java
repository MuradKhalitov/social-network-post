package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
