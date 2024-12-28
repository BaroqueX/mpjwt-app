# Microprofile JWT Application 说明

## 生成私钥
```
openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:2048
```
## 从私钥生成公钥
```
openssl rsa -in privateKey.pem -pubout -out publicKey.pem
```

## 配置 micorprofile-config.properties 文件
- 将公钥保存到 `src/main/resources` 目录下，配置 `mp.jwt.verify.publickey.location` 为 `publicKey.pem`。
- 将私钥 `privateKey.pem` 保存到 `/temp/` 目录下。

## 注册普通用户
```
curl -X POST http://localhost:9080/mpjwt-app/api/registration \
    -H 'Content-Type: application/json' \
    -d '{
  "username": "panyun",
  "password": "123456",
  "email": "john.doe@example.com"
}'
```

## 注册admin用户
```
curl -X POST http://localhost:9080/mpjwt-app/api/registration \
    -H 'Content-Type: application/json' \
    -d '{
  "username": "admin_panyun",
  "password": "123456",
  "email": "john.doe.admin@example.com"
}'
```

## 注册manager用户
```
curl -X POST http://localhost:9080/mpjwt-app/api/registration \
    -H 'Content-Type: application/json' \
    -d '{
  "username": "manager_panyun",
  "password": "123456",
  "email": "john.doe.manager@example.com"
}'
```

## 普通用户登录
```
curl -X POST http://localhost:9080/mpjwt-app/api/jwt \
    -H 'Content-Type: application/json' \
    -d '{
  "username": "panyun",
  "password": "123456"
}'
```
返回得到JWT。

## admin用户访问受保护的资源
```
curl http://localhost:9080/mpjwt-app/api/admins \
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJjODA2MGM2My1jMDY1LTQ4ODItYjIyZC1iZmZiYjRkYTVlNzEiLCJpc3MiOiJodHRwczovL2F1dGguZXhhbXBsZS5jb20iLCJpYXQiOjE3MzQ4NTg4NzEsImV4cCI6MTczNTcyMjg3MSwidXBuIjoiYWRtaW5fcGFueXVuIiwiZ3JvdXBzIjpbImFkbWlucyIsInVzZXJzIl19.nTvSBozH9nyJ4f9oG7OPXeP091YNstN8ZSO2naWbWJkAZ-m7Uw1jGgdyYrtnTkIjFGYV0AmOHuLe774ndItnBauuo3GeqQOf2eoApzMszT-SmsxRN6hORE4HmTciBx2FOin3wtEvLic_Kw5kd_xtXtisqdQgdJlwlY9eEUwe52iyWHiCQlGcbL7Il8xNwmethDbhcMWWWdSdCoJ7KJnBW5zPh8B866HeGcmsajThEGjW4rKikLwHBJdMAXTGQrw-L4D8AprN8lWu6ohGwkIIaK9vUI-8fSiV19OpH7f8i_xHsuCllZBrHNgG2JNvwstKlS9_ebFr9MAt9vp0JMJjYg'
```