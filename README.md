# SimplePairMaker
### Простой генератор пар из списка студентов.  
Считывает список студентов, генерирует их пары, выводит в указанное пользователем место ответ.  
Логгирует каждый свой вызов в отдельный лог файл в папке /logs.

## Содержание
- [Зависимости](#зависимости)
- [Использование](#использование)
- [Установка](#установка)
- [To do](#to-do)
- [Автор](#автор)

## Параметры
- Java 17
- Кодировка входного / выходного файла - UTF-8
- Кодировка для вывода в консоль определяется динамически.

## Зависимости
- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [SLF4J](https://www.slf4j.org/)
- [JUnit5](https://junit.org/junit5/)
- [AssertJ](https://assertj.github.io/doc/)

## Установка
### Создание исполняемого .jar файла:
Для генерации исполняемого jar файла необходимо выполнить сборку проекта
```sh
./gradlew build
```
В папке build/libs повяится 2 .jar файла. Один из содержит зависимости, другой - нет.  
Нам нужен файл PairMaker-*.jar. Он будет больше по размеру, чем файл без зависимостей.  
Данный файл генерируется плагином com.github.johnrengelman.shadow, и является исполняемым.  
На этом этапе можно остановиться.
### Создание jre для поставки весте с исполняемым файлом:
Версия установленной jre у пользователя может отличаться. Для этого можно подготовить для него jre и упаковать вместе с исполняемым файлом.  
Для этого надо: 
1. Узнать какие модули java используются для нашей программы.  
При сборке проекта автоматически запускается катомный таск copyAllDependencies, который копирует все .jar файлы зависимостей
в папку build/deps. Это позволит нам воспользоваться готовой утилитой от разработчиков java jdeps.  
Выполняем команду
```shell
jdeps --ignore-missing-deps \
    -q -recursive \
    --print-module-deps \
    --class-path 'build/deps' \
    ./build/libs/PairMaker-*.jar
```
Здесь в class-path указывается путь к нашим зависимостям, а последний аргумент - путь к нашему .jar файлу.  
Получили список модулей.
2. Собрать легковесный jre с лишь необходимыми модулями
```shell
jlink --add-modules "$deps" \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output build\runtime
```
Здесь $deps - результат выполнения предыдущего шага. Последним аргументом указываем куда сохранять наш jre.
3. Скопировать в отдельную папку исполняемый jar файл и папку с нашим jre.  
Теперь пользователь может запустить программу без самостоятельной установки jre
```shell
runtime/bin/java.exe -jar PairMaker-1.0.0.jar
```
4. Добавить вспомогательный файлы, например README.txt и т.д. в папку и заархивировать.
Дистрибутив готов.

## Использование
Запуск осуществляется командой 
```shell
java -jar PairMaker-*.jar [args]
```
### Список обязательных аргументов:
> ### -f,--inputFile <file.txt>  
> Файл, откуда будет производиться чтение списка студентов.  

> ### -q,--questionsCount <number>   
> Количество вопросов, которое задает каждый студент.

### Список необязательных аргументов:
> ### -cOut,--consoleOut
> Флаг, отвечающий за вывод результата в консоль.

> ### -fOut,--fileOut <output.txt>
> Опция, отвечающая за вывод результата в указанный файл.

> ### -s,--silent
> Флаг, отвечающий за отключение вывода логов в консоль. В файл логи также продолжат записываться.

Таким образом, необходимо выбрать куда выводить результат. При отсутствии опций, ответ не будет выведен.  
### Может парсить слегка поврежденные строки, например:  
<pre>
1.        Вася     Пупкин        -> Вася Пупкин
1 Вася   Пупкин Иванович         -> Вася Пупкин
351135 Вася Пупкин junkStr       -> Вася Пупкин
</pre>

## To-do
- [ ] Добавить пользовательский интерфейс для более удобного запуска.

## Автор
* Ощепков Антон Антонович