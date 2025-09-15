package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventoRepositorio {
    private List<Evento> eventos = new ArrayList<>();

    public void adicionar(Evento e) {
        eventos.add(e);
    }

    public List<Evento> listarTodos() {
        return new ArrayList<>(eventos);
    }

    public void imprimirTodos() {
        imprimirLista(eventos);
    }

    // NOVO: imprime com índice (para selecionar no menu)
    public void imprimirTodosComIndice() {
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado ainda.");
            return;
        }
        System.out.println("=== Eventos (índice - nome | data) ===");
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            System.out.println(i + " - " + e.getNome() + " | " + e.getData().format(Evento.BR_DATE));
        }
    }

    // NOVO: obter por índice (retorna null se inválido)
    public Evento obterPorIndice(int indice) {
        if (indice < 0 || indice >= eventos.size()) return null;
        return eventos.get(indice);
    }

    // ----- Ordenações / filtros -----

    public List<Evento> listarOrdenadoPorData() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getData))
                .collect(Collectors.toList());
    }

    public List<Evento> listarFuturos() {
        LocalDate hoje = LocalDate.now();
        return eventos.stream()
                .filter(e -> !e.getData().isBefore(hoje))
                .sorted(Comparator.comparing(Evento::getData))
                .collect(Collectors.toList());
    }

    public void imprimirLista(List<Evento> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("Nenhum evento para mostrar.");
            return;
        }
        System.out.println("=== Eventos ===");
        for (Evento e : lista) {
            e.exibirInfo();
            System.out.println("---------------------------");
        }
    }

    // ----- Persistência CSV (nome;data;descricao) -----

    public void carregarDeArquivo(Path caminho) {
        eventos.clear();
        if (caminho == null || !Files.exists(caminho)) return;

        try (BufferedReader br = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(";", -1);
                if (partes.length < 3) continue;

                String nome = partes[0];
                String dataStr = partes[1];
                String descricao = partes[2];

                LocalDate data = LocalDate.parse(dataStr, Evento.BR_DATE);
                eventos.add(new Evento(nome, data, descricao));
            }
        } catch (IOException e) {
            System.out.println("Aviso: não foi possível carregar os eventos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Aviso: arquivo de dados contém linhas inválidas. Alguns eventos podem não ter sido carregados.");
        }
    }

    public void salvarParaArquivo(Path caminho) {
        if (caminho == null) return;

        try {
            if (caminho.getParent() != null) {
                Files.createDirectories(caminho.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(caminho, StandardCharsets.UTF_8)) {
                for (Evento e : eventos) {
                    String nome = e.getNome().replace(";", ",");
                    String data = e.getData().format(Evento.BR_DATE);
                    String descricao = e.getDescricao().replace(";", ",");

                    bw.write(nome + ";" + data + ";" + descricao);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Aviso: não foi possível salvar os eventos: " + e.getMessage());
        }
    }
}
