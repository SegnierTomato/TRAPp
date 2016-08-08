# TRAPp rus
Это приложение разрабатывается в процессе изучения мобильной платформы Android.            
На данный момент времени оно ***всё ещё находится в разработке***.

### Суть приложения                                     
Приложение позволяет пользователю заносить английские слова, привязывать к ним изображение и звуковой файл.            
Пользователь к каждому слову может загрузить его перевод через Yandex Translate API, Google Translate API или добавить свою версия перевода.
Английские слова можно разбивать по каталогам, к которым так же можно прикрепить изображение.        
В дальнейшем пользователь может иметь возможность лицезреть слова в виде карточек с прикрепренными к нему изображением,
звуковым файлом и переводами.


Для хранения всей информации о пользовательских каталогах, добавленных в них словах и их переводов используется база данных SQLite.
Ниже приведена схема БД:


Как можно увидеть из схемы, прикрепляемые изображения и звуковые файлы не хранятся в базе данных.
Вместо этого сохраняются только пути к файлам.

Так же в приложении реализован кэш с загружаемыми из памяти изображениями.  
Для реализации загрузки изображения, по неопытности, использовался объект `AsyncTask`.   
Однако автор осознал ошибку и собирается её исправить, как только "дойдут руки".

### Пример частичной работы приложения:
