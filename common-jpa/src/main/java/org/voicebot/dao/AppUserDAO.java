package org.voicebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voicebot.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    //метод проверяет есть ли пользователь уже в нашей базе. Реализацию метода берет на себя спринг
    AppUser findAppUserByTelegramUserId(Long id);
}
