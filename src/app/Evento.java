package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    // Formato BR para entrada/saída
    public static final DateTimeFormatter BR_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String nome;
    private LocalDate data;
    private String descricao;

    // NOVO: participantes (lista de nomes)
    private List<String> participantes = new ArrayList<>();

    public Evento(String nome, LocalDate data, String descricao) {
        this.nome = nome;
        this.data = data;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public LocalDate getData() { return data; }
    public String getDescricao() { return descricao; }

    // ---- Participações ----
    // Retorna true se conseguiu adicionar, false se já estava na lista ou nome inválido
    public boolean participar(String nomePessoa) {
        if (nomePessoa == null || nomePessoa.isBlank()) return false;
        if (participantes.contains(nomePessoa)) return false;
        participantes.add(nomePessoa.trim());
        return true;
    }

    // Retorna true se removeu
    public boolean cancelarParticipacao(String nomePessoa) {
        if (nomePessoa == null) return false;
        return participantes.remove(nomePessoa.trim());
    }

    public int getTotalParticipantes() {
        return participantes.size();
    }

    public List<String> getParticipantes() {
        return new ArrayList<>(participantes); // cópia de segurança
    }

    // Exibe as infos principais (resumo)
    public void exibirInfo() {
        System.out.println("Evento: " + nome);
        System.out.println("Data: " + data.format(BR_DATE));
        System.out.println("Descrição: " + descricao);
        System.out.println("Participantes: " + getTotalParticipantes());
    }
}
