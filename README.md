# Restaurant Ordering System

Sistema SaaS de atendimento para restaurantes e bares de grande porte, que substitui o
atendimento em papel por um fluxo digital via **QR Code nas mesas**.

Cada mesa possui um QR Code fixo com o número da mesa. Ao escanear, o cliente tem acesso a três ações:

1. **Cardápio** — visualização dos itens disponíveis
2. **Chamar garçom** — aciona o atendimento em tempo real
3. **Fechar a conta** — soma automática do total pedido

O cliente não precisa se cadastrar; apenas **garçons** e o **dono/administrador** possuem login no sistema.

## ✨ Funcionalidades

- Geração de QR Code por mesa (via ZXing)
- Cardápio digital
- Chamada de garçom e fechamento de conta em tempo real (WebSocket)
- Autenticação e controle de acesso para garçons e administrador (Spring Security)
- Cálculo automático do total da conta — o cliente só visualiza itens e total, sem poder editar o pedido
- Documentação de API via Swagger/OpenAPI

## 🛠️ Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1 |
| Persistência | Spring Data JPA + PostgreSQL |
| Segurança | Spring Security |
| Comunicação em tempo real | Spring WebSocket |
| Geração de QR Code | ZXing (`core` + `javase`) |
| Mapeamento de objetos | MapStruct |
| Boilerplate | Lombok |
| Documentação de API | springdoc-openapi (Swagger UI) |
| Build | Maven |

## 📁 Estrutura do projeto

```
Restaurant-ordering-system/
├── .mvn/wrapper/       # Maven Wrapper
├── qrcodes/            # QR Codes gerados para as mesas
├── src/                # Código-fonte da aplicação (Spring Boot)
├── mvnw / mvnw.cmd      # Scripts do Maven Wrapper
└── pom.xml             # Dependências e build do projeto
```

## ✅ Pré-requisitos

- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)

Não é necessário ter Java ou Maven instalados localmente — tudo roda dentro dos containers.

## 🚀 Como rodar com Docker

1. Clone o repositório:

   ```bash
   git clone https://github.com/HENRIQUE-PYTH/Restaurant-ordering-system.git
   cd Restaurant-ordering-system
   ```

2. Suba a aplicação e o banco de dados:

   ```bash
   docker compose up --build
   ```

3. A aplicação estará disponível em:

   ```
   http://localhost:8080
   ```

4. Documentação da API (Swagger):

   ```
   http://localhost:8080/swagger-ui.html
   ```

Para rodar em segundo plano:

```bash
docker compose up --build -d
```

Para parar os containers:

```bash
docker compose down
```

Para parar e apagar também os dados do banco:

```bash
docker compose down -v
```

### 🐳 Alternativa: rodando com Docker puro (sem compose)

Se preferir não usar `docker compose`, dá pra fazer o equivalente na mão com `docker run`:

1. Crie uma rede para os containers conversarem entre si:

   ```bash
   docker network create restaurant-net
   ```

2. Suba o banco PostgreSQL:

   ```bash
   docker run -d \
     --name restaurant-db \
     --network restaurant-net \
     -e POSTGRES_DB=restaurant_db \
     -e POSTGRES_USER=restaurant_user \
     -e POSTGRES_PASSWORD=restaurant_pass \
     -v restaurant_db_data:/var/lib/postgresql/data \
     -p 5432:5432 \
     postgres:16-alpine
   ```

3. Gere a imagem da aplicação (usa o `Dockerfile` da raiz do projeto):

   ```bash
   docker build -t restaurant-app .
   ```

4. Suba a aplicação, ligada à mesma rede do banco:

   ```bash
   docker run -d \
     --name restaurant-app \
     --network restaurant-net \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://restaurant-db:5432/restaurant_db \
     -e SPRING_DATASOURCE_USERNAME=restaurant_user \
     -e SPRING_DATASOURCE_PASSWORD=restaurant_pass \
     -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
     -v "$(pwd)/qrcodes:/app/qrcodes" \
     -p 8080:8080 \
     restaurant-app
   ```

5. Acesse em `http://localhost:8080` (Swagger em `/swagger-ui.html`).

Para parar e remover tudo:

```bash
docker stop restaurant-app restaurant-db
docker rm restaurant-app restaurant-db
docker network rm restaurant-net
```

> O ponto-chave do `docker run` puro é o `--network`: sem ele os dois containers não se enxergam, e é exatamente isso que o `docker compose` cria automaticamente pra você — por isso vale aprender compose mais pra frente.

## 🔧 Variáveis de ambiente

Configuradas no `docker-compose.yml`, podem ser sobrescritas conforme o ambiente:

| Variável | Descrição | Padrão |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexão com o PostgreSQL | `jdbc:postgresql://db:5432/restaurant_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `restaurant_user` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `restaurant_pass` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estratégia de schema do Hibernate | `update` |

> ⚠️ Ajuste esses valores (principalmente usuário/senha) antes de usar em produção.

## 📦 Volumes

- `restaurant_db_data`: persiste os dados do PostgreSQL entre reinicializações
- `./qrcodes`: mapeia a pasta local `qrcodes/` para dentro do container, preservando os QR Codes gerados

## 📄 Licença

Defina aqui a licença do projeto (ex: MIT, propriedade privada, etc).
