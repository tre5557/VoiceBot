package org.voicebot.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voicebot.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// позволяем базе данных самой генерить значения для первичных ключей
    private Long id;
    private Long telegramUserId;
    //аннотация добавляет текущую дату в поле firstLoginDate
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)// говорит спринг дата, как именно этот enum будет транслироваться в базу данных
    // enum string запишет значение в бд в виде строки типа varchar255
    private UserState state;
}
