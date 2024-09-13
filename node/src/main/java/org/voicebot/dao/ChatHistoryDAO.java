package org.voicebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voicebot.entity.ChatHistoryEntity;

import java.util.List;

public interface ChatHistoryDAO extends JpaRepository<ChatHistoryEntity, Long> {
    List<ChatHistoryEntity> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}

