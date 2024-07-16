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
//генерирует методы equals и hashCode, исключая поле id, потому что он может меняться в процессе, а это приведет к ошибке
@EqualsAndHashCode(exclude = "id")

@Builder// позволяет внедрить паттерн builder в класс
@NoArgsConstructor
@AllArgsConstructor
//говорит приложению что наш класс будет сущностью, которая связана с таблицей в бд
@Entity
@Table(name = "raw_data")
//регистрирует тип jsonb для использования в аннотациях @Type.
@TypeDef( name = "jsonb", typeClass = JsonBinaryType.class)
//указываем какую информацию будем добавлять в базу
public class RawData {
    @Id//указывает, что поле id является первичным ключом.

    @GeneratedValue(strategy = GenerationType.IDENTITY)// позволяем базе данных самой генерить значения для первичных ключей
    private Long id;
// сохраняем наш Update в формате jsonb в нашей бд
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Update event;
}
