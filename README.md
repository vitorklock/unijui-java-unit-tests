# Blockbuster Avaliação

Pequena aplicação de locadora de filmes (estilo Blockbuster) feita em Java com Spring Boot, usando repositórios em memória e uma página HTML simples para testar o fluxo E2E (end-to-end).

## Visão geral

A aplicação permite:

- Cadastrar filmes
- Listar / buscar filmes por nome
- Alugar filmes (cria um `Rental` e deixa o filme indisponível)
- Listar / buscar locações por nome do filme
- Devolver um filme alugado (gera multa se atrasado e deixa o filme disponível novamente)
- Marcar multa como paga

Tudo é guardado em memória (collections em repositórios in-memory). Ao reiniciar a aplicação, os dados são resetados.

---

## Desenvolvimento guiado por testes (TDD)

Esta aplicação foi desenvolvida utilizando TDD (Red → Green → Refactor) para as regras de domínio de filmes e locações.  

### Ciclo TDD – Movies

- **Red**: [6ac855](https://github.com/vitorklock/unijui-java-unit-tests/commit/6ac855e12d4d8cb0db796c12f7c82e83c20c03e6)
- **Green**: [8f3011](https://github.com/vitorklock/unijui-java-unit-tests/commit/8f30116a3f0cad4e20a2f75d669dd632af21e911)
- **Refactor**: [32624d](https://github.com/vitorklock/unijui-java-unit-tests/commit/32624d62d4bbbba1c2ed9cd380f716932ebb3897)

### Ciclo TDD – Rentals

- **Red**: [431bbd](https://github.com/vitorklock/unijui-java-unit-tests/commit/431bbd24d5b2cc6094a89c3ebb927e2fc5cc4656)
- **Green**: [3ecb15](https://github.com/vitorklock/unijui-java-unit-tests/commit/3ecb154d0c4e3616d74356cacd46c8a66ef256d2)
- **Refactor**: [22b22c](https://github.com/vitorklock/unijui-java-unit-tests/commit/22b22c2bac0046582224ce844f05794bff4843fb)


---

## Tecnologias

- Java (JDK 23 na sua máquina)
- Spring Boot (Web)
- Repositórios em memória (sem banco de dados)
- NetBeans (forma recomendada de rodar o projeto)
- Front-end: `index.html` estático consumindo a API com `fetch`

---

## Seed de dados (dados iniciais)

Ao iniciar a aplicação, o `Main` cria automaticamente **3 filmes**:

- `Garfield` – disponível
- `Dune` – disponível
- `Matrix` – **já alugado**

Para `Matrix` é criado um `Rental` com datas de início/fim configuradas e o filme marcado como `available = false`.

Você consegue ver isso assim que o app sobe:

- `GET /api/movies` → 3 filmes, com `Matrix` indisponível
- `GET /api/rentals` → 1 locação referente a `Matrix`

Pela tela `index.html` você consegue testar isso sem precisar chamar a API manualmente.

---

## Como rodar o projeto

> **Forma suportada / recomendada**: usar NetBeans.

1. Abra o projeto **blockbuster-avaliacao** no NetBeans.
2. Localize a classe `Main.java` em:
   - `src/main/java/application/main/Main.java`
3. Clique com o **botão direito** em `Main.java` e escolha **“Run File”**.
4. Aguarde o Spring Boot subir (você verá logs no console do NetBeans).
5. Com o servidor rodando, acesse em um navegador:
   - 👉 `http://localhost:8080/`

A página `index.html` será carregada e você poderá testar todo o fluxo da aplicação.

---

## Como a aplicação funciona (back-end resumido)

### Entidades principais

- `Movie`
  - `id`
  - `name`
  - `available` (true/false)
- `Rental`
  - `id`
  - `rentedMovie` (um `Movie`)
  - `startDate` (data de início do aluguel)
  - `endDate` (data prevista de devolução, hoje + 7 dias)
  - `returnDate` (data de devolução real)
  - `lateFee` (multa, hoje fixo em 20 se devolver atrasado)
  - `paidFee` (true/false)

### Services

- `MovieService`
  - Salva e busca filmes.
  - `rentMovie(movie)`: garante que o filme está disponível, marca como indisponível e salva.
- `RentalService`
  - Salva e busca locações.
  - `createRentalForMovie(movieId)`:
    - busca o filme
    - chama `MovieService.rentMovie(...)`
    - cria um `Rental`, define datas de início/fim e persiste
  - `returnMovie(rental, returnDate)`:
    - atualiza `returnDate` e calcula multa se estiver atrasado
    - marca o filme relacionado como `available = true` e salva
  - `payLateFee(rental)`:
    - marca a multa como paga (`paidFee = true`)

### Controllers (API REST)

- `MovieController` (`/api/movies`)
  - `POST /api/movies` – cria filme
  - `GET /api/movies` – lista filmes (ou busca por `?name=...`)
  - `GET /api/movies/{id}` – busca filme por ID
  - `POST /api/movies/{id}/rent` – **aluga o filme** (cria `Rental` e marca indisponível)

- `RentalController` (`/api/rentals`)
  - `POST /api/rentals` – cria locação para um filme (`{ "movieId": ... }`) usando `createRentalForMovie`
  - `GET /api/rentals` – lista locações (ou busca por `?movieName=...`)
  - `GET /api/rentals/{id}` – busca locação por ID
  - `POST /api/rentals/{id}/return?returnDate=yyyy-MM-dd` – devolve o filme, calcula multa e marca filme como disponível
  - `POST /api/rentals/{id}/pay-late-fee` – marca multa como paga

---

## Como usar a tela `index.html` (passo a passo)

A página `index.html` é servida automaticamente em `http://localhost:8080/` e usa `fetch` para chamar a API.

### 1. Ver os dados iniciais

1. Com o app rodando, acesse `http://localhost:8080/`.
2. Vá até **“Search Movies”**.
3. Deixe o campo de busca vazio e clique em **“Search”**.
   - Você verá:
     - `Garfield` (available = true)
     - `Dune` (available = true)
     - `Matrix` (available = false, já está alugado pelo seed)

4. Vá até **“Search Rentals by Movie Name”**.
5. Deixe vazio e clique em **“Search”**.
   - Você verá uma locação para `Matrix`.

---

### 2. Criar um novo filme

1. Na seção **“Create Movie”**:
   - Digite um nome (ex.: `Barbie Movie`) em **Movie name**.
   - Clique em **“Create”**.
2. Abaixo aparecerá o JSON do filme criado, incluindo o `id`.

Você pode confirmar em **“Search Movies”** que o filme foi mesmo criado.

---

### 3. Alugar um filme (criando uma Rental automaticamente)

> Aqui usamos o novo fluxo: **alugar = criar Rental + marcar filme como indisponível**.

1. Descubra o `id` do filme que você quer alugar (via “Search Movies”).
2. Na seção **“Rent Movie (creates Rental and marks movie as unavailable)”**:
   - Informe o `Movie ID`.
   - Clique em **“Rent”**.
3. Abaixo aparecerá o JSON de uma **Rental** recém criada:
   - `rentedMovie` com o filme (agora `available = false`)
   - `startDate` (hoje)
   - `endDate` (hoje + 7 dias)

Você pode:

- Voltar em **“Search Movies”** e ver que esse filme agora está `available = false`.
- Usar **“Search Rentals by Movie Name”** e ver a locação que acabou de ser criada.

---

### 4. Buscar locações

1. Na seção **“Search Rentals by Movie Name”**:
   - Se quiser listar todas, deixe vazio e clique em **“Search”**.
   - Se quiser filtrar, preencha por exemplo `Matrix` ou `Barbie`.
2. Abaixo aparecerá a lista de locações em JSON.

Guarde um `rental.id` para os próximos passos.

---

### 5. Devolver um filme

> Ao devolver, o Rental é atualizado e o filme volta a ficar disponível.  
> Se a devolução for depois de `endDate`, uma multa fixa de `20.0` é aplicada.

1. Pegue o `id` da locação (`rental.id`) na listagem anterior.
2. Vá até **“Return Rental (generate late fee if late)”**.
3. Preencha:
   - **Rental ID**: o `id` da locação
   - **Date**: uma data (formato `yyyy-MM-dd`)
     - Se escolher uma data **após** o `endDate`, haverá multa (`lateFee = 20.0`).
4. Clique em **“Return”**.
5. Abaixo aparecerá o JSON atualizado do `Rental`:
   - `returnDate` preenchido
   - `lateFee` = 0 ou 20.0
6. Se você buscar o filme novamente em **“Search Movies”**, verá que ele agora está `available = true`.

---

### 6. Pagar multa de atraso

1. Ainda olhando a locação (Rental) devolvida, se tiver `lateFee = 20.0` e `paidFee = false`:
2. Vá até **“Pay Late Fee”**.
3. Preencha o **Rental ID**.
4. Clique em **“Pay Fee”**.
5. Abaixo aparecerá o Rental atualizado com:
   - `paidFee = true`.

---

## Resumo rápido do fluxo típico

1. **Rodar o projeto** pelo NetBeans (botão direito em `Main.java` → *Run File*).
2. Abrir `http://localhost:8080/`.
3. Ver os filmes iniciais (Garfield, Dune, Matrix já alugado).
4. Criar um novo filme (opcional).
5. Alugar um filme pela seção **“Rent Movie”**.
6. Listar locações com **“Search Rentals”**.
7. Devolver com data (talvez gerando multa).
8. Pagar a multa se existir.

Pronto: com isso você testa toda a lógica de `Movie`, `Rental`, Services, Controllers e a página `index.html` de ponta a ponta.
