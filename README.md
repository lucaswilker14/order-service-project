# 📦 Order Service Project

Este projeto é um serviço de pedidos escalável e eficiente, projetado para processar um grande volume de requisições com alto desempenho. Ele utiliza **Spring Boot**, **Redis**, **MongoDB**, e **Docker**, garantindo **baixa latência, resiliência e escalabilidade**.

---

## 🚀 Tecnologias Utilizadas

- **Spring Boot** - Framework principal para desenvolvimento.
- **Redis** - Utilizado como cache para otimizar a recuperação de dados e reduzir carga no banco.
- **MongoDB** - Banco NoSQL escolhido por sua flexibilidade e escalabilidade.
- **Spring Data** - Facilita a comunicação com o banco.
- **Docker & Docker Compose** - Para facilitar a orquestração do ambiente.
- **JMeter** - Usado para testes de carga.

---

## 🏗 Arquitetura

O sistema segue uma **arquitetura baseada em microsserviços**, utilizando um banco NoSQL para armazenar pedidos e um cache Redis para otimizar acessos frequentes.

---

## 📖 Configuração do Ambiente

### 🐳 Rodando com Docker Compose

1. **Clone o repositório**
   ```sh
   git clone https://github.com/seu-usuario/order-service.git
   cd order-service
   ```

2. **Suba os containers**
   ```sh
   docker-compose up --build
   ```

3. A API estará disponível em `http://localhost:8080`.

---

### 🏃‍♂️ Rodando Manualmente (Sem Docker)

1. **Suba um banco MongoDB e um Redis localmente**

    - No **MongoDB**:
      ```sh
      docker run --name mongo -d -p 27017:27017 mongo
      ```
    - No **Redis**:
      ```sh
      docker run --name redis -d -p 6379:6379 redis
      ```

2. **Configure as variáveis de ambiente**
   ```sh
   export SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/orders
   export SPRING_REDIS_HOST=localhost
   ```

3. **Execute a aplicação**
   ```sh
   ./mvnw spring-boot:run
   ```

---

## 📡 Endpoints Principais

### Criar um Pedido
```http
POST /api/orders
```
#### Request Body
```json
{
  "externalId": "ORDER-12345",
  "items": [
    {
      "productId": 1,
      "productName": "Vinho Branco",
      "quantity": 4,
      "price": 25.00
    }
  ],
  "createdAt": "2025-02-19T15:00:00"
}
```

### Buscar Pedido por ID
```http
GET /api/orders/{externalId}
```


---
## 📊 Testes de Carga com JMeter

1. **Baixe o JMeter**: [Apache JMeter](https://jmeter.apache.org/)
2. **Configure um Header dinâmico** para **Idempotency-Key** no **Thread Group**:
    - Use a função do JMeter:
      ```jmeter
      ${__UUID}
      ```
3. **Torne o `externalId` dinâmico** no body:
    - No **PreProcessor (JSR223)**, adicione:
      ```groovy
      vars.put("externalId", "ORDER-" + (10000 + new Random().nextInt(90000)));
      ```
    - E use no body da requisição:
      ```json
      { "externalId": "${externalId}", "items": "[...]" }
      ```
4. **Execute o teste e analise os resultados!** 🚀

---

## 🎯 Próximos Passos (Fator tempo)

- Implementar **mensageria com Kafka** para eventos assíncronos.
- Adicionar **rate limiting** para evitar sobrecarga.
- Melhorar logs e métricas para análise em tempo real.
- Adicionar Observabilidade com OpenTelemetry

---