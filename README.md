# Agenda de Eventos (Java - Console)

Projeto em Java para gerenciamento de eventos via linha de comando.  
Permite cadastrar, listar, participar, cancelar participação e ver detalhes de eventos.

## Funcionalidades
- Cadastrar eventos com **nome, data e descrição**
- Listar eventos (todos, apenas **futuros**, ou **ordenados por data**)
- Participar e cancelar participação em um evento
- Ver detalhes, incluindo **lista de participantes**
- Persistência em arquivo **CSV** (`dados_eventos.csv`)

## Como executar

### Opção A — Pelo VS Code (recomendada)
1. Abra a pasta do projeto no VS Code.
2. Abra o arquivo `src/app/Main.java`.
3. Clique em **Run** (ou **Run | Debug** acima do método `main`).
4. Use o **menu no terminal** para interagir com o sistema.

### Opção B — Pelo Terminal (alternativa)
1. Clonar este repositório:
   ```bash
   git clone https://github.com/Bieelow/agenda-eventos-java.git
   cd agenda-eventos-java
