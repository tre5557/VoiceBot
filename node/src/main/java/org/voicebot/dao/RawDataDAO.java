package org.voicebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voicebot.entity.RawData;

public interface RawDataDAO extends JpaRepository <RawData, Long> {
}
