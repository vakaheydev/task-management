# Notifier Service

Это сервис **Notifier** для управления уведомлениями пользователей. Он отвечает за то, чтобы задачи, требующие напоминаний, попадали в очередь Kafka.  
Далее эти сообщения обрабатываются [Telegram-ботом](https://github.com/vakaheydev/daily-tgbot), который выступает **Kafka Consumer** и отправляет пользователям уведомления в Telegram.

## Особенности

- **Генерация уведомлений**: выборка задач, срок которых подошёл.  
- **Интеграция с Kafka**: публикация сообщений в Kafka-топик.  
- **Гибкость**: можно расширять под другие каналы уведомлений.  
- **В связке с Telegram Bot**: бот слушает Kafka и отправляет уведомления пользователям.

## Проект с несколькими репозиториями

Проект состоит из связанных сервисов:

1. **[REST API](https://github.com/vakaheydev/daily-rest-api)**  
   Отвечает за CRUD операций с задачами, пользователями и прочими сущностями. Содержит логику планирования и хранение данных.

2. **[Client](https://github.com/vakaheydev/daily-rest-client)**  
   Клиент для удобной работы с REST API.

3. **[MVC](https://github.com/vakaheydev/daily-mvc)**  
   Веб-интерфейс для пользователей. Работает через клиент.

4. **[Telegram Bot](https://github.com/vakaheydev/daily-tgbot)**  
   Telegram-бот, который слушает Kafka и отправляет уведомления пользователям. Также позволяет управлять задачами через чаты.

5. **Notifier (этот репозиторий)**  
   Сервис, который анализирует задачи и кладёт сообщения об уведомлениях в Kafka.

## Установка

Сервис контейнеризован через Docker и запускается вместе с остальными сервисами проекта (REST API, MVC, Telegram Bot).

## Kafka

Notifier работает как **Kafka Producer**:  
- Формирует сообщение с данными задачи и пользователя.  
- Кладёт его в Kafka-топик.  
- Telegram Bot читает этот топик и отправляет уведомления.

## Требования для запуска приложения

- Docker  
- Docker Compose  

## Запуск приложения

Чтобы запустить все сервисы, включая Notifier, выполните:

Склонируйте репозиторий REST API (в нём содержатся Docker-файлы для запуска всего проекта):
```sh
git clone https://github.com/vakaheydev/daily-rest-api
```
или просто скопируйте/скачайте файлы [docker-compose.yaml](https://github.com/vakaheydev/daily-rest-api/blob/master/docker/remote/docker-compose.yaml) и [.env.origin](https://github.com/vakaheydev/daily-rest-api/blob/master/docker/remote/.env.origin) из папки [docker/remote](https://github.com/vakaheydev/daily-rest-api/tree/master/docker/remote)

Перейдите в папку с докером:
```sh
cd ./docker/remote
```
