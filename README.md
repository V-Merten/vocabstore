# VocabStore

VocabStore is a full-stack learning project for building and practicing personal vocabulary.

## Features
- User registration & login (session-based authentication)
- Create, update, delete words
- Organize words into groups (optional grouping)
- Assign/remove words to/from groups
- Practice mode (training with selected words)
- Password reset via email (token link + reset form)
- Account deletion (removes user data)

## Tech Stack
**Frontend:** React + Vite  
**Backend:** Spring Boot + Spring Security + JPA (Hibernate)  
**Database:** PostgreSQL  
**Email:** Gmail SMTP (App Password)

## Running Locally
### Requirements
- Java + Maven
- Node.js + npm
- PostgreSQL

### Start
From `backend/`:
```bash
mvn spring-boot:run
The build also runs the frontend production build and copies it into:
backend/src/main/resources/static
