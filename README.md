# SQL Chatbot with Ollama

![Imagem chat bot sql](src/main/resources/static/img/sql-chat-bot.png)

## Description
This project implements a chatbot that utilizes AI focused on SQL. Users can ask questions in natural language, and the chatbot retrieves results from the database. It uses LangChain4J to simplify connections to the Ollama server, utilizing downloaded duckdb-nsql models.

## Features

- Option to select any local database.
- Option to choose language models downloaded on your machine through Ollama.
- GUI designed for ease of use.

## Technologies Used

- Java
- Ollama
- LangChain4J
- Lombok
- MySQL
- Java Swing (for the GUI)
- Tests conducted with duckdb-nsql models: 7b-q3_K_S and 7b

## Prerequisites

- Java Development Kit (JDK)
- Any database (in this case, MySQL)
- Ollama API installed and configured
- Duckdb-nsql language models available

## Installation

1. Clone the repository:
```
git clone https://github.com/username/sql-chatbot.git
```
2. Set up project dependencies with Maven.
3. Run the `Main` class to start the chatbot.
