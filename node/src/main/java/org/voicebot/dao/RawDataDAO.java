package org.voicebot.dao;
// слой dao для работы с бд
import org.springframework.data.jpa.repository.JpaRepository;
import org.voicebot.entity.RawData;
// JpaRepository - механизм который предоставляет методы для работы с бд
public interface RawDataDAO extends JpaRepository <RawData, Long> {
}
