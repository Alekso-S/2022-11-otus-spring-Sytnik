# Домашнее задание 14

### Примечания/вопросы:

- MongoItemReader не умеет сбрасывать позицию для чтения. Для возможности перезапуска джоба сделал версию с такой функцией
- Сделал две версии джоба: с использованием стандартного JpaItemWriter и с кастомными райтерами для пакетной записи
- Не придумал какие-то другие полезные виды тестов для джоба, кроме общей сверки всех данных до и после работы
- @SpringBatchTest не пригодился, так как два джоба он не поддерживает, да и вообще весь контекст поднят, доступны полноценные лаунчер и оператор