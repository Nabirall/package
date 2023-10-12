Cервис, который обращается к сервису курсов валют, и отображает gif:
если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную отсюда https HYPERLINK "https://giphy.com/search/rich" если ниже - отсюда https HYPERLINK "https://giphy.com/search/broke" Ссылки
REST API курсов валют - https HYPERLINK "https://docs.openexchangerates.org/
Must Have
Сервис на Spring Boot 2 + Java 
Запросы приходят на HTTP endpoint (должен быть написан в соответствии с rest conventions), туда передается код валюты по отношению с которой сравнивается USD
Для взаимодействия с внешними сервисами используется Feign
На сервис написаны тесты ( @mockbean )
Для сборки должен использоваться Gradle
Результатом выполнения должен быть репо на GitHub с инструкцией по запуску
Nice to Have
Сборка и запуск Docker контейнера с этим сервисом


Endpoints:
---  
Получить список кодов для валют:
```
GET /gg/codes
```  
Получить гифку  
(пример ответа: https://api.giphy.com/v1/gifs/random?api_key=hQd1Hn82MGjcevVdqxUj21VCYbijsy8b&tag=rich):
```
GET /gg/gif/{Char Code}
```  
Простой html+js доступен по localhost:8080/

Запуск .jar:
---
```
java -jar package-0.0.1.jar
```
Docker:

```  
docker build package_image:gg .  
```
Запуск:
```
docker run -p 8080:8080 package_image:gg 
``` 

Запуск:
``` 
docker run -p 8080:8080 --name package drozd/package:latest
```
