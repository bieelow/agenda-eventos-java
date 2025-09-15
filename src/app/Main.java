package app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Path ARQUIVO_DADOS = Paths.get("dados_eventos.csv");

    public static void main(String[] args) {
        System.out.println("Agenda de Eventos - v1.0 (console)");

        EventoRepositorio repo = new EventoRepositorio();
        repo.carregarDeArquivo(ARQUIVO_DADOS);
        System.out.println("Eventos carregados de: " + ARQUIVO_DADOS.toAbsolutePath());

        try (Scanner sc = new Scanner(System.in)) {
            int opcao = -1;
            while (opcao != 0) {
                mostrarMenu();
                System.out.print("Escolha: ");
                String entrada = sc.nextLine().trim();

                try {
                    opcao = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um número válido.");
                    continue;
                }

                switch (opcao) {
                    case 1:
                        cadastrarEvento(sc, repo);
                        break;
                    case 2:
                        repo.imprimirTodos();
                        break;
                    case 3:
                        List<Evento> futuros = repo.listarFuturos();
                        repo.imprimirLista(futuros);
                        break;
                    case 4:
                        List<Evento> ordenados = repo.listarOrdenadoPorData();
                        repo.imprimirLista(ordenados);
                        break;
                    case 5:
                        participarEmEvento(sc, repo);
                        break;
                    case 6:
                        cancelarParticipacao(sc, repo);
                        break;
                    case 7:
                        verDetalhes(sc, repo);
                        break;
                    case 0:
                        repo.salvarParaArquivo(ARQUIVO_DADOS);
                        System.out.println("Dados salvos em: " + ARQUIVO_DADOS.toAbsolutePath());
                        System.out.println("Saindo... Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== MENU ===");
        System.out.println("1 - Cadastrar evento");
        System.out.println("2 - Listar todos");
        System.out.println("3 - Listar FUTUROS (de hoje em diante)");
        System.out.println("4 - Listar TODOS ordenados por data");
        System.out.println("5 - Participar de um evento");
        System.out.println("6 - Cancelar participação");
        System.out.println("7 - Ver detalhes de um evento");
        System.out.println("0 - Salvar e sair");
    }

    private static void cadastrarEvento(Scanner sc, EventoRepositorio repo) {
        System.out.println();
        System.out.println("Cadastro de evento");

        System.out.print("Nome do evento: ");
        String nome = sc.nextLine().trim();

        LocalDate data = null;
        while (data == null) {
            System.out.print("Data (dd/MM/yyyy): ");
            String dataStr = sc.nextLine().trim();
            try {
                data = LocalDate.parse(dataStr, Evento.BR_DATE);
            } catch (Exception ex) {
                System.out.println("Data inválida. Tente de novo no formato dd/MM/yyyy.");
            }
        }

        System.out.print("Descrição: ");
        String descricao = sc.nextLine().trim();

        Evento e = new Evento(nome, data, descricao);
        repo.adicionar(e);

        System.out.println("✅ Evento cadastrado com sucesso!");
    }

    // ---- Novas operações ----

    private static void participarEmEvento(Scanner sc, EventoRepositorio repo) {
        System.out.println();
        System.out.println("Participar de um evento");
        repo.imprimirTodosComIndice();
        System.out.print("Informe o índice do evento: ");
        Integer idx = lerInteiro(sc);
        if (idx == null) return;

        Evento escolhido = repo.obterPorIndice(idx);
        if (escolhido == null) {
            System.out.println("Índice inválido.");
            return;
        }

        System.out.print("Seu nome: ");
        String nome = sc.nextLine().trim();

        boolean ok = escolhido.participar(nome);
        if (ok) {
            System.out.println("✅ Participação confirmada!");
        } else {
            System.out.println("Não foi possível confirmar (nome vazio ou já inscrito).");
        }
    }

    private static void cancelarParticipacao(Scanner sc, EventoRepositorio repo) {
        System.out.println();
        System.out.println("Cancelar participação");
        repo.imprimirTodosComIndice();
        System.out.print("Informe o índice do evento: ");
        Integer idx = lerInteiro(sc);
        if (idx == null) return;

        Evento escolhido = repo.obterPorIndice(idx);
        if (escolhido == null) {
            System.out.println("Índice inválido.");
            return;
        }

        System.out.print("Seu nome (exatamente como foi cadastrado): ");
        String nome = sc.nextLine().trim();

        boolean ok = escolhido.cancelarParticipacao(nome);
        if (ok) {
            System.out.println("✅ Participação cancelada!");
        } else {
            System.out.println("Nome não encontrado na lista de participantes.");
        }
    }

    private static void verDetalhes(Scanner sc, EventoRepositorio repo) {
        System.out.println();
        System.out.println("Detalhes do evento");
        repo.imprimirTodosComIndice();
        System.out.print("Informe o índice do evento: ");
        Integer idx = lerInteiro(sc);
        if (idx == null) return;

        Evento e = repo.obterPorIndice(idx);
        if (e == null) {
            System.out.println("Índice inválido.");
            return;
        }

        System.out.println();
        e.exibirInfo();
        System.out.println("Lista de participantes (" + e.getTotalParticipantes() + "):");
        for (String p : e.getParticipantes()) {
            System.out.println(" - " + p);
        }
    }

    // Utilitário: tenta ler um inteiro, senão informa e volta ao menu
    private static Integer lerInteiro(Scanner sc) {
        String s = sc.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            System.out.println("Valor inválido. Operação cancelada.");
            return null;
        }
    }
}
//