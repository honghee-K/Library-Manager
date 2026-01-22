# Library-Manager
A backend system for managing libraries, books, users, and loans.

## 1. Running Tests
Before starting the application, you can verify the system by running the automated test suite.
    
```bash
mvn verify
```


## 2. Installation & Running

Download and run latest Docker Image from this Github Repository:

```bash
docker run -p 8080:8080 ghcr.io/honghee-k/library-manager:latest
```


## 3. Usage & Testing the API
You can interact with the API using either the **Postman** or **Linux Shell**.

### Option A: Using Postman (Recommended)
We have provided a pre-configured collection file in this repository to make testing easier.

1. **Download:** Download `Library_Manager.postman_collection.json` in the **root directory** of this repository
2. **Import:** Open Postman, click the **Import** button, and select the downloaded JSON file.
3. **Get Token:** Run the `1. Login` request from the collection and **copy** the JWT token string from the response body.
4. **Set Authorization:**
    * Select any other request you want to test (e.g., *Create Libraries*).
    * Navigate to the **Authorization** tab.
    * Select **Type: Bearer Token** and **paste** the copied token into the 'Token' field.
5. **Execute:** Click **Send** to test the endpoint.

> **Note on Token Expiry:** The JWT token is valid for **8 hours**. If you receive a `401 Unauthorized` error, please re-run the Login request to refresh your token.

### Option B: Using Linux Shell (curl)
Open your terminal and follow the sequence below.

#### **Step 1: User Authentication**

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

#### **Step 2: Use Library Manager**

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

 
## Author
Honghee Kim & Gülsüm Erboga

## Credit
Developed for the Backend Systems Assignment @ THWS
