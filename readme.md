dependency -> jpa - ����� jdcb ��� ������ � ����� ������ (jpa ������ �� spring �������)
jdcb, mysqlconnector - java - ��� ������ � ������ ������
����������� ������ ���� �� ���� �� �������� ��� ������� �����
���� ���� ����� - ���� application.properties: server.port = 8081

https://spring.io/guides

jdcb - ����������� java ���������� ��� ������ � ��. ����� ������������ � ��.
���� application.properties - ��������� ������� � ��:
����
�������� �� (������� � utf8_general_ci)
�����
������= ���� ����� - ����� ��� ���������


������� mysql ������� ->������� ����� �����-������
������ �������� �� ������ � ����� ������

��� ����������� dependencies - ����� ������ ����������, �� ����� ������ @Entity

@Entity+import > ����� ���������� �������. ������ ������� ����� ��������, ���� �� �� ����������.
import javax.persistence.Id;

alt+insert - generate getters, setters

����� ����� ������� ����� �������������� �������� � ��
��� ������ ������ - ����� �����������
������ ����������� - ���������

public interface PostRepository extends CrudRepository {
}

������ - ������� ����� �� �������� ���� ���������
Consider defining a bean of type 'repo.PostRepository' in your configuration.


��������� ������
blogcontroller>new mapping
templates> blog-add.html 
��������� ������ �� �����
blogcontroller

����������� �����:����
� ������ ������� �����������

maincontroller - / and /about
blogcontroller - /blog and /blog/add - post & get mapping
controller 'blogcontroller' >model 'post'>postrepository> interface 'postrepository'