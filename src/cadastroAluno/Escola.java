package cadastroAluno;

import java.util.ArrayList;

public class Escola {

    private String nome;
    private ArrayList<Sala> salas;

    // Construtor padrão
    public Escola() {
        this.salas = new ArrayList<>();
    }

    // Construtor com nome da escola
    public Escola(String nome) {
        this.nome = nome;
        this.salas = new ArrayList<>();
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public void setSalas(ArrayList<Sala> salas) {
        this.salas = salas;
    }

    // Adiciona uma nova sala
    public void adicionarSala(Sala novaSala) {
        salas.add(novaSala);
    }

    // Busca uma sala pelo nome
    public Sala buscarSala(String nomeSala) {
        for (Sala sala : salas) {
            if (sala.getNome().equalsIgnoreCase(nomeSala)) {
                return sala;
            }
        }
        return null;
    }
    
    //Validar nome da sala
    public static boolean validarNomeSala(String nome) {
        return nome.matches("\\d{1,2}[A-Za-z]");
    }

    // Lista todas as salas da escola
    public void listarSalas() {
        System.out.println("\n=== Lista de Salas ===");
        if (salas.isEmpty()) {
            System.out.println("Nenhuma sala cadastrada.");
            return;
        }

        for (Sala sala : salas) {
            System.out.println("Sala: " + sala.getNome());
        }
    }

    // Lista todos os alunos por sala
    public void listarTodosAlunos() {
        System.out.println("\n=== Lista de Alunos por Sala ===");

        if (salas.isEmpty()) {
            System.out.println("Nenhuma sala cadastrada.");
            return;
        }

        for (Sala sala : salas) {
            System.out.println("\n📁 Sala: " + sala.getNome());
            sala.listarAlunos();
        }
    }
}
