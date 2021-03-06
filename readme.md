dependency -> jpa - кроме jdcb дл€ работы с базой данных (jpa только со spring св€зана)
jdcb, mysqlconnector - java - дл€ работы с базами данных
контроллеры должны быть не выше по иерархии чем главный класс
если порт зан€т - файл application.properties: server.port = 8081

https://spring.io/guides

jdcb - стандартна€ java технологи€ дл€ работы с Ѕƒ. ћожем подключатьс€ к Ѕƒ.
файл application.properties - настрокий доступа к Ѕƒ:
порт
название Ѕƒ (создать в utf8_general_ci)
Ћогин
ѕароль= если пусто - ничег оне указывать


создать mysql таблицу ->создать новый класс-модель
ћодель отвечает за работу с базой данных

при подключение dependencies - нужно делать обновлени€, не будет видеть @Entity

@Entity+import > класс становитс€ моделью. ћодель создает новую табличку, если ее не существует.
import javax.persistence.Id;

alt+insert - generate getters, setters

через класс интфейс можем манипулировать таблицей в Ѕƒ
дл€ каждой модели - новый репозиторий
каждый репозиторий - интерфейс

public interface PostRepository extends CrudRepository {
}

ошибка - главный класс по иерархии выше остальных
Consider defining a bean of type 'repo.PostRepository' in your configuration.


добавл€ем статье
blogcontroller>new mapping
templates> blog-add.html 
обработка данных их формы
blogcontroller

отслеживаем адрес:пост
в моделе создаем конструктор

maincontroller - / and /about
blogcontroller - /blog and /blog/add - post & get mapping
controller 'blogcontroller' >model 'post'>postrepository> interface 'postrepository'