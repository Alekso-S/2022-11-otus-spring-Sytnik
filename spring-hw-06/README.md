# Домашнее задание 6

### Примечания/вопросы:

- Пришлось принудительно переводить списки жанров внутри книг в ArrayList, так как hibernate возвращает их в виде типов, для которых не реализован корректно метод equals. Судя по гуглу это распространённый вопрос, но какого-то единственного правильного красивого решения я не нашёл
- Я что-то немного запутался с работой с контекстом jpa. При загрузке объекта в контекст через find и последующем изменение какого-либо его поля обновлённое значение автоматически уже попадает в базу. Нужно ли тогда вообще вызывать merge?
- Насколько я понимаю, тесты для методов добавления/обновление/удаления репозиториев теперь не нужны? Пока что оставил