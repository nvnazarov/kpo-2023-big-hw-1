# Описание

Консольное приложение для работников кинотеатра с единственным
зрительным залом. В приложении есть возможность:

1. фиксации продажи посетителям кинотеатра билетов на сеанс с выбором мест;
2. возврата проданных билетов посетителям до начала сеанса;
3. отображения свободных и проданных мест для выбранного сеанса;
4. редактирования данных о фильмах и расписания сеансов их показа;
5. отметки занятых мест в зале посетителями конкретного сеанса.

- Код можно найти тут: https://github.com/nvnazarov/kpo-2023-big-hw-1
- Диаграммы тут: https://drive.google.com/drive/folders/1EACjstb_CB9TYjUIlUkFLRuOd3GU9BSG?usp=sharing

# Использование

Для осуществления какого-либо действия необходимо вызвать
соответствующую команду:

| Команда            | Действие                                                                                                                                |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| films              | Выводит таблицу со всеми фильмами.                                                                                                      |
| add film           | Добавляет фильм в таблицу фильмов. Программа попросит ввести название фильма и его длительность.                                        |
| edit film          | Изменяет данные о фильме. Программа попросит ввести номер изменяемого фильма, новые название и длительность.                            |
| tickets            | Выводит таблицу со всеми билетами.                                                                                                      |
| add ticket         | Добавляет билет в таблицу билетов. Программа попросит ввести номер сеанса (к которому относится билет), ряд и место в зрительском зале. |
| cancel ticket      | Отменяет покупку билета (возврат билета). Программа попросит ввести номер билета.                                                       |
| confirm ticket     | Помечает, что билет был использован (зритель заходил в зал). Программа попросит ввести номер билета.                                    |
| sessions           | Выводит таблицу со всеми сеансами.                                                                                                      |
| add session        | Добавляет сеанс в таблицу сеансов. Программа попросит ввести номер фильма (который будет показываться на сеансе), время начала сеанса.  |
| reschedule session | Переносит сеанс на другое время. Программа попросит ввести номер переносимого сеанса и новое время начала.                              |
| exit               | Завершает работу программы.                                                                                                             |

# Внутреннее устройство

Данные о фильмах, билетах и сеансах хранятся соответственно в таблицах Films.csv, Tickets.csv и Sessions.csv.
Формат csv был выбран, поскольку данные имеют реляционную (табличную) структуру.
