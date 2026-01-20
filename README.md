# Library-Manager
Backend System

## Install and Run

Download and run latest Docker Image from this Github Repository:

```bash
docker run -p 8080:8080 ghcr.io/honghee-k/library-manager:latest
```

## User Authentication

Generate JWT Token:

```bash
curl -X 'GET' \
  'http://localhost:8080/auth/login?name=admin' \
  -H 'accept: application/json'
```

Or set directly in Environment Variable:

```bash
LM_TOKEN=$(curl "http://localhost:8080/auth/login?name=admin")
```

Set in Auth Bearer Header for every following request.

## Use Library Manager

1. Create Library:

```bash
curl -X 'POST' \
  'http://localhost:8080/libraries' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $LM_TOKEN" \
  -d '{
  "name": "Würzburg Unibib",
  "location": "Würzburg"
}'
```

2. Add 2 Books to Library:

```bash
curl -X 'POST' \
  'http://localhost:8080/books' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $LM_TOKEN" \
  -d '{
  "isbn": 1234,
  "title": "Backend System 1",
  "author": "Author A",
  "genre": "Computer Science",
  "libraryId": 1
}'

curl -X 'POST' \
  'http://localhost:8080/books' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $LM_TOKEN" \
  -d '{
  "isbn": 5678,
  "title": "Frontend System 1",
  "author": "Author B",
  "genre": "Computer Science",
  "libraryId": 1
}'
```

3. Create User

```bash
curl -X 'POST' \
  'http://localhost:8080/users' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $LM_TOKEN" \
  -d '{
  "name": "Hong",
  "email": "hong@example.com"
}'
```

4. Start Loan

```bash
curl -X 'POST' \
  'http://localhost:8080/loans' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $LM_TOKEN" \
  -d '{
  "userId": 1,
  "isbn": 1234,
  "loanDate": "2026-01-10",
  "dueDate": "2026-01-20",
  "status": "ACTIVE"
}'
```
