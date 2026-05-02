# Stock Market API

This repository contains the implementation of a simplified stock exchange service. The system provides a RESTful API to manage stock transactions between user wallets and a central bank. To meet the high availability requirements, the application is containerized and deployed as a multi-node cluster.

## Architecture and High Availability

The system relies on Docker and Docker Compose to ensure cross-platform compatibility across Windows, Linux, and macOS (both arm64 and x64 architectures). 

Rather than running a single process, the provided startup scripts initialize a highly available cluster. The infrastructure consists of a PostgreSQL database, three independent backend instances, and an Nginx reverse proxy. Nginx distributes incoming traffic among the available instances. If one instance crashes or is intentionally terminated, the load balancer routes traffic to the remaining healthy nodes, ensuring the product remains operational. 

You can verify this resilience using the dedicated `/chaos` endpoint, which immediately terminates the underlying process of the instance serving the request.

## Domain Assumptions

The business logic operates under a specific set of simplified rules:
- The Bank acts as the sole liquidity provider, and its account is initially empty.
- Wallets can own various numbers of different stocks, and are created automatically upon the first operation.
- The stock price is fixed at 1, with no price fluctuations.
- Wallet balances are not tracked, meaning there is no fiat fund management.
- Buy and sell operations are executed immediately at face value.
- The system maintains an audit log limited to a maximum of 10,000 successful wallet operations, excluding internal bank operations.

## Startup Instructions

The solution makes no assumptions about the host environment other than the availability of a Docker runtime. It can be started using a single command that takes the target port as a parameter.

**Linux / macOS:**
```bash
chmod +x start.sh
./start.sh 8080
```

**Windows:**
```cmd
start.bat 8080
```

The API will be exposed at `localhost:8080` (or the port specified in the command). To safely tear down the cluster and clean up the environment, execute `docker-compose down`.

## API Reference

The service exposes the following endpoints as specified in the requirements:

- `POST /wallets/{wallet_id}/stocks/{stock_name}`
  Payload: `{"type": "sell|buy"}`
  Executes a trade. Returns `200` on success. Fails with `404` if the stock does not exist, or `400` if the bank/wallet lacks sufficient stock.

- `GET /wallets/{wallet_id}`
  Retrieves the current state of a wallet.
  Response format: `{"id": "12qdsdadsa", "stocks": [{"name":"stock1", "quantity":99}]}`.

- `GET /wallets/{wallet_id}/stocks/{stock_name}`
  Returns a single number representing the exact quantity of the specified stock in the wallet.

- `GET /stocks`
  Retrieves the current state of the bank's stock inventory.
  Response format: `{"stocks": [{"name":"stock1", "quantity":99}]}`.

- `POST /stocks`
  Sets the state of the bank. Returns `200` on success.
  Payload: `{"stocks": [{"name":"stock1", "quantity":99}]}`.

- `GET /log`
  Returns the audit log of successful wallet operations in order of occurrence.
  Response format: `{"log": [{"type":"buy", "wallet_id":"23qdsadsa", "stock_name":"cbdadsa"}]}`.

- `POST /chaos`
  Kills the instance that serves this request to demonstrate system resilience.
