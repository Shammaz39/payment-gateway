# Payment Gateway - Quick Start Guide

This guide will help you run the Payment Gateway system on any machine with Docker installed. You do NOT need to clone the source code or build any Java projects.

## Prerequisites

1.  **Install Docker Desktop**:
    *   [Download for Windows/Mac/Linux](https://www.docker.com/products/docker-desktop/)
    *   Ensure Docker is running.

## How to Run

1.  **Download** the `docker-compose.yml` file to a folder on your computer (e.g., `payment-gateway`).
2.  Open a terminal (Command Prompt or PowerShell) in that folder.
3.  Run the following command:

    ```bash
    docker-compose up -d
    ```

    *   This will download all necessary images and start the system in the background.
    *   *Note: The first run might take a few minutes depending on your internet speed.*

4.  **Verify** the services are running:

    ```bash
    docker-compose ps
    ```

## Accessing the Services

| Service | URL | Credentials (if any) |
| :--- | :--- | :--- |
| **Merchant Service** | `http://localhost:8081` | - |
| **Payment Service** | `http://localhost:8082` | - |
| **Processor Service** | `http://localhost:8083` | - |
| **Mock Bank** | `http://localhost:8084` | - |
| **Notification** | `http://localhost:8085` | - |
| **PgAdmin (DB UI)** | `http://localhost:8088` | `admin@example.com` / `admin` |
| **Adminer (DB UI)** | `http://localhost:8089` | System: `PostgreSQL`, Server: `postgres`, User: `postgres`, Pass: `postgres`, DB: `payment_gateway` |

## Stopping the System

To stop all services and remove the containers:

```bash
docker-compose down
```
