package org.voicebot.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;
// сюда будут сохраняться все необходимые updates и им будет присваиваться id первичный ключ
//@Data - может привезти к ошибки ненахода из-за мутабельных полей и изменении хэш-кода
@Getter
@Setter
//исключаем поле id, так как оно будет меняться в процессе жизни объекта.
@EqualsAndHashCode(exclude = "id")

@Builder// позволяет внедрить паттерн builder в класс
@NoArgsConstructor
@AllArgsConstructor
//говорит приложению что нас класс будет сущностью, которая связана с таблицей в бд
@Entity
@Table(name = "raw_data")

@TypeDef( name = "jsonb", typeClass = JsonBinaryType.class)
//указываем какую информацию будем добавлять в базу
public class RawData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// позволяем базе данных самой генерить значения для первичных ключей
    private Long id;
// сохраняем наш Update в формате jsonb в нашей бд
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Update event;
}
