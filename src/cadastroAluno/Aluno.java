package cadastroAluno;

import java.util.ArrayList;

public class Aluno {

	private String nome;
	private String cpf;
	private String situacao = "Reprovado";
	private float totalNota, mediaNota = 0;
	private float[][] notas = new float[4][2]; // 4 bimestres, 2 notas cada
	private int qtnota;
	

    public Aluno() {
        this.notas = new float[4][2];
        this.situacao = "Reprovado";
    }
	
    
	public Aluno(String nome, String cpf, int qtnota) {
		this.nome = nome;
		this.cpf = cpf;
		this.qtnota = qtnota;

	}

	public Aluno(String nome, String cpf) {
		this.nome = nome;
		this.cpf = cpf;
		this.notas = new float[4][2];
	}

	public float[][] getNotas() {
		return notas;
	}

	public float getNota(int bimestre, int numeroNota) {
		return notas[bimestre][numeroNota];
	}

	public void setNotas(float[][] notas) {
		this.notas = notas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public void inserirNota(int bimestre, int n, float nota) {
		notas[bimestre][n] = nota;
	}

	public void listarNotasPorBimestre() {
		for (int b = 0; b < 4; b++) {
			System.out.println("Bimestre " + (b + 1) + ": ");
			for (int n = 0; n < 2; n++) {
				System.out.println("  Nota " + (n + 1) + ": " + notas[b][n]);
			}
		}
	}
	//Aqui
	public void calcularNotaAluno() {
		float soma = 0;
		int total = 0;

		for (int b = 0; b < 4; b++) {
			for (int n = 0; n < 2; n++) {
				soma += notas[b][n];
				total++;
			}
		}

		this.totalNota = soma;
		float media = (total > 0) ? soma / total : 0;

		this.situacao = (media >= 7.0) ? "Aprovado" : "Reprovado";
	}

	public void mostraSituacao() {
		System.out.println("\nNome: " + this.nome);
		System.out.println("CPF: " + this.cpf);

		// Mostrar as notas por bimestre
		System.out.println("Notas por Bimestre:");
		for (int b = 0; b < 4; b++) {
			System.out.print("  Bimestre " + (b + 1) + ": ");
			System.out.print(notas[b][0] + " | " + notas[b][1]);
			System.out.println();
		}

		// Calcular média e total
		float soma = 0;
		int totalNotas = 0;

		for (int b = 0; b < 4; b++) {
			for (int n = 0; n < 2; n++) {
				soma += notas[b][n];
				totalNotas++;
			}
		}

		float media = soma / totalNotas;
		System.out.println("Total das Notas: " + soma);
		System.out.println("Média Final: " + media);
		System.out.println("Situação: " + this.situacao);
		
		//Alterei aqui
		mensagemDeMotivacao();
	}

	public static boolean validarCPF(String cpf) {
		if (cpf == null || !cpf.matches("\\d{11}"))
			return false;
		if (cpf.matches("(\\d)\\1{10}"))
			return false;

		try {
			int soma = 0;
			for (int i = 0; i < 9; i++) {
				soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
			}
			int dig1 = 11 - (soma % 11);
			if (dig1 >= 10)
				dig1 = 0;
			if (dig1 != Character.getNumericValue(cpf.charAt(9)))
				return false;

			soma = 0;
			for (int i = 0; i < 10; i++) {
				soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
			}
			int dig2 = 11 - (soma % 11);
			if (dig2 >= 10)
				dig2 = 0;
			return dig2 == Character.getNumericValue(cpf.charAt(10));
		} catch (Exception e) {
			return false;
		}
	}

	public void listar() {
		System.out.println("\nNome do Aluno: " + this.nome);
		System.out.println("CPF do Aluno: " + this.cpf);

		for (int b = 0; b < 4; b++) {
			System.out.println("  Bimestre " + (b + 1) + ":");
			for (int n = 0; n < 2; n++) {
				System.out.println("    Nota " + (n + 1) + ": " + notas[b][n]);
			}
		}
	}

	public float calcularMedia() {
		float soma = 0;
		int total = 0;
		for (int b = 0; b < 4; b++) {
			for (int n = 0; n < 2; n++) {
				soma += notas[b][n];
				total++;
			}
		}
		return soma / total;
	}
	
	public void mensagemDeMotivacao() {
		if("Aprovado".equalsIgnoreCase(this.situacao)){
			System.out.println("🎉 Parabéns! Você foi aprovado! Continue assim e vá além!");
			
		}else {
			System.out.println("🙁 Faltou pouco! Infelizmente não foi desta vez, mas tenho certeza que você pode chegar no próximo nível!\"");
		}
	}

}