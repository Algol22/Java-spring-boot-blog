<h2>#Deployment:</h2><br>
<b>https://jsprblog.herokuapp.com/</b>
<br>
<br>
***

database generated by Post.java


dependency -> jpa - кроме jdcb для работы с базой данных (jpa только со spring связана)
jdcb, mysqlconnector - java - для работы с базами данных
контроллеры должны быть не выше по иерархии чем главный класс
если порт занят - файл application.properties: server.port = 8081

https://spring.io/guides

jdcb - стандартная java технология для работы с БД. Можем подключаться к БД.
файл application.properties - настрокий доступа к БД:
порт
название БД (создать в utf8_general_ci)
Логин
Пароль= если пусто - ничег оне указывать


создать mysql таблицу ->создать новый класс-модель
Модель отвечает за работу с базой данных

при подключение dependencies - нужно делать обновления, не будет видеть @Entity

@Entity+import > класс становится моделью. Модель создает новую табличку, если ее не существует.
import javax.persistence.Id;

alt+insert - generate getters, setters

через класс интфейс можем манипулировать таблицей в БД
для каждой модели - новый репозиторий
каждый репозиторий - интерфейс

public interface PostRepository extends CrudRepository {
}

ошибка - главный класс по иерархии выше остальных
Consider defining a bean of type 'repo.PostRepository' in your configuration.


добавляем статье
blogcontroller>new mapping
templates> blog-add.html 
обработка данных их формы
blogcontroller

отслеживаем адрес:пост
в моделе создаем конструктор

maincontroller - / and /about
blogcontroller - /blog and /blog/add - post & get mapping
controller 'blogcontroller' >model 'post'>postrepository> interface 'postrepository'
