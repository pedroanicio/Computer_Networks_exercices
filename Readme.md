# Simple Java Client-Server Messaging System

This project implements a basic client-server messaging system in Java, where clients can send messages to each other through a server. The server manages the connection and communication between clients, handling message transmission and retrieval.

## Features

- **Client-Server Architecture**: The system uses a server to manage client connections and handle message storage and retrieval.
- **Multi-threaded Server**: The server can handle multiple client connections simultaneously using threads.
- **Message Sending**: Clients can send messages to other connected clients.
- **Message Listing**: Clients can retrieve a list of messages sent to them.

## Project Structure

The project consists of the following classes:

- `Cliente`: Represents the client side of the application. It handles the sending and receiving of messages to/from the server.
- `Servidor`: The server application that listens for client connections and handles message processing.
- `ClientHandler`: A helper class used by the server to manage individual client connections.

## Usage

### Running the Server

1. Compile and run the `Servidor` class:

   ```bash
   javac org/example/Servidor.java
   java org.example.Servidor
The server will start listening on port 5000 and will accept incoming client connections.

### Running the Client

1. Compile and run the `Cliente` class:

   ```bash
   javac org/example/Cliente.java
   java org.example.Cliente

2. Enter the server IP address and port number when prompted.

3. Enter your username to connect to the server.

4. You can now send messages to other connected clients by entering their username and message content.
5. To retrieve a list of messages sent to you, enter `list` when prompted.
6. To disconnect from the server, enter `exit`.

