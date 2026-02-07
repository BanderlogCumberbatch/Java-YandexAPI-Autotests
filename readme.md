# Инструкция
В директории src/test/resources создать файл secrets.properties со следующими параметрами:
* oauth.token - Oauth токен для авторизации YandexAPI
* yd.login - Логин аккаунта Яндекс
* yd.display.name - Отображаемое имя в аккаунте Яндекс

# Тест-сьют Yandex API https://cloud-api.yandex.net

## 1. Тест-кейсы REST API v1/disk GET

### 1.1. Авторизация с корректным Oauth токеном

Предусловия: В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен. {yd_login} - логин аккаунта Яндекс, {yd_name} - отображаемое имя в аккаунте Яндекса.

Шаги:

Выполнить GET запрос:
https://cloud-api.yandex.net/v1/disk

Ожидаемый результат: HTTP 200, JSON

    {
        "user": { "login": {yd_login},
        “display_name”: {yd_name}}
    }

### 1.2. Авторизация без Oauth токена

Шаги:

Выполнить GET запрос:
https://cloud-api.yandex.net/v1/disk

Ожидаемый результат: HTTP 401, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }


## 2. Тест-кейсы REST API v1/disk/resources

### 2.1. Эндпоинт PUT

### 2.1.1. Создание папки:

Предусловия: В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/resources?path=/{folder_name}

Ожидаемый результат: HTTP 201, JSON

    {
        "method": string,
        "href": string,
        "templated": boolean
    }

Таблица тестирования:

| **folder_name** |
|:----------------|
| .folder         |
| Папка           |
| 123             |
| “456”           |
| ‘789’           |
| ё.txt           |
| *?<>\|\         |
| ~!@#$%^-+*;№)(  |
| 🔵              |



### 2.1.2. Создание папки без Oauth токена:

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/resources?path=/folder

Ожидаемый результат: HTTP 401, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.1.3. Создание уже существующей папки:

Предусловия: Создана папка /folder. В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/resources?path=/folder

Ожидаемый результат: HTTP 409, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.1.4. Создание папки с несуществующим path в запросе:

Предусловия: В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/resources?path=/

Ожидаемый результат: HTTP 409, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.1.5. Создание папки с некорректным path в запросе:

Предусловия: В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
[https://cloud-api.yandex.net/v1/disk/resources?path=/:](https://cloud-api.yandex.net/v1/disk/resources?path=/:)

Ожидаемый результат: HTTP 400, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.1.6. Создание папки без path в запросе:

Предусловия: В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/resources

Ожидаемый результат: HTTP 400, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }


### 2.2. Эндпоинт DELETE

### 2.2.1. Удаление папки:

Предусловия: Создана папка /folder. В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить DELETE запрос: https://cloud-api.yandex.net/v1/disk/resources?path=/folder

Ожидаемый результат: HTTP 204, JSON { }

### 2.2.2. Удаление папки без Oauth токена:

Шаги:

Выполнить DELETE запрос: https://cloud-api.yandex.net/v1/disk/resources?path=/folder

Ожидаемый результат: HTTP 401, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.2.3. Удаление несуществующей папки:

Предусловия: Не существует папки /folder0. В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить DELETE запрос: https://cloud-api.yandex.net/v1/disk/resources?path=/folder

Ожидаемый результат: HTTP 404, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.2.4. Удаление папки без path в запросе:

Предусловия: В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить DELETE запрос:
https://cloud-api.yandex.net/v1/disk/resources

Ожидаемый результат: HTTP 400, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 2.3. Эндпоинт /files GET

### 2.3.1 Получения списка файлов

Предусловия: Создана папка /files и в неё загружен файл file.txt. В заголовке запроса указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен. {yd_login} - логин аккаунта Яндекс, {yd_name} - отображаемое имя в аккаунте Яндекса.

Шаги:

Выполнить GET запрос:
https://cloud-api.yandex.net/v1/disk/resources/files

Ожидаемый результат:
1) HTTP 200.
2) JSON соответствует схеме get_files_schema.json в папке resources.


## 3. Тест-кейсы REST API v1/disk/trash/resources/restore

### 3.1. Эндпоинт PUT

### 3.1.1. Восстановление папки:

Предусловия: Папка /folder0 создана, а затем перемещена в корзину. В заголовке запросов указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

1) Выполнить GET запрос:
   https://cloud-api.yandex.net/v1/disk/trash/resources с телом ответа: JSON

       {
       "_embedded": { "items": [
               {
               "path": {trash_path},
               "origin_path": "disk:/folder0"
               }
           ] }
       }


2) Выполнить PUT запрос:
   https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path={trash_path}

Ожидаемый результат: HTTP 201, JSON

    {
        "method": string,
        "href": string,
        "templated": boolean
    }

### 3.1.2. Восстановление уже существующей папки:

Предусловия: Папка /folder1 перемещена в корзину, а затем была создана ещё одна папка  /folder1. В заголовке запросов указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

1) Выполнить GET запрос:
   https://cloud-api.yandex.net/v1/disk/trash/resources с телом ответа: JSON

       {
       "_embedded": { "items": [
               {
               "path": {trash_path},
               "origin_path": "disk:/folder1"
               }
           ] }
       }

2) Выполнить PUT запрос:
   https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path={trash_path}

Ожидаемый результат: HTTP 201, JSON

    {
        "method": string,
        "href": string,
        "templated": boolean
    }

### 3.1.3. Восстановление папки без Oauth токена:

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path=/folder

Ожидаемый результат: HTTP 401, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }

### 3.1.4. Восстановление несуществующей папки:

Предусловия: Папка /folder не существует в корзине. В заголовке запросов указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/trash/resources/restore?path=/folder

Ожидаемый результат: HTTP 404, JSON

    {
        "method": string,
        "href": string,
        "templated": boolean
    }

### 3.1.5. Восстановление папки без path в запросе:

Предусловия: В заголовке запросов указано {Authorization: OAuth {auth_token}} где {auth_token} - корректный OAuth токен.

Шаги:

Выполнить PUT запрос:
https://cloud-api.yandex.net/v1/disk/trash/resources/restore

Ожидаемый результат: HTTP 400, JSON

    {
        "error": string,
        "description": string,
        "message": string
    }


