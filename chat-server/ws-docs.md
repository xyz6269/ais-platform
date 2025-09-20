
# 📡 Chat Service WebSocket API

This document describes the WebSocket events and authentication flow for the **Chat Service** implemented with `netty-socketio`.

---

## 🔐 Authentication

Clients **must** provide a valid **JWT token** during the handshake.  
The token should be passed as a **URL parameter**:

- If the token is invalid or missing, the server will **disconnect** the client.
- On successful authentication, the server extracts the user's **email** from the JWT and associates it with the socket session.

---

## 🔌 Connection Lifecycle

### ✅ On Connect
- Event: `connect`
- Action:
    - Validates the JWT.
    - Extracts `clientEmail`.
    - Stores the client in `SocketStateService`.
    - Logs:
      ```
      User Connected: <clientEmail>
      ```

### ❌ On Disconnect
- Event: `disconnect`
- Action:
    - Removes the client from `SocketStateService`.
    - Logs:
      ```
      User Disconnected: <clientEmail>
      ```

---

## 📩 Events

### 1. `send_message`
**Client → Server**

Send a chat message to the server.

- **Event Name**: `send_message`
- **Payload**: `ChatMessageDTO`

#### Example Payload
```json
{
  "senderId": "user1@example.com",
  "receiversIds": ["user2@example.com", "user3@example.com"],
  "sentAt": "2025-09-13T18:30:00Z",
  "content": "Hello, everyone!",
  "attachmentType": "image",
  "attachmentData": "BASE64_STRING",
  "room": "general"
}
