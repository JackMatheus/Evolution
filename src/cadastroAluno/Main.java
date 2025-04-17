package cadastroAluno;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		int option;
		String nome, cpf;
		float nota;
		Aluno novoAluno;
		Sala novaSala = new Sala();
		Scanner teclado = new Scanner(System.in);

		do {
			System.out.println("\n-----------------------");
			System.out.println("     Menu");
			System.out.println("Escolha uma opção");
			System.out.println("1 - Inserir Aluno");
			System.out.println("2 - Listar Alunos");
			System.out.println("3 - Situações");
			System.out.println("4 - Excluir um Aluno");
			System.out.println("5 - Encontrar um Aluno");
			System.out.println("6 - Atualizar notas de um aluno");
			System.out.println("7-  Imprimir documento");
			System.out.println("8-  Importar notas de um documento");

			while (!teclado.hasNextInt()) {
				System.out.print("Digite um número válido: ");
				teclado.next();
			}
			option = teclado.nextInt();
			teclado.nextLine(); // limpar buffer

			switch (option) {
			// case 1:
			case 1:
				System.out.print("\nInforme o nome: ");
				nome = teclado.nextLine();

				do {
					System.out.print("Informe o CPF (11 dígitos): ");
					cpf = teclado.nextLine();

					if (!Aluno.validarCPF(cpf)) {
						System.out.println("CPF inválido!");
					}
				} while (!Aluno.validarCPF(cpf));

				novoAluno = new Aluno(nome, cpf);

				boolean continuar = true;
				while (continuar) {
					int bimestre;

					// Pergunta qual bimestre deseja preencher
					do {
						System.out.print("Qual bimestre deseja preencher (1 a 4)? ");
						while (!teclado.hasNextInt()) {
							System.out.print("Digite um número de 1 a 4: ");
							teclado.next();
						}
						bimestre = teclado.nextInt();
						teclado.nextLine(); // limpar \n

						if (bimestre < 1 || bimestre > 4) {
							System.out.println("Bimestre inválido. Tente novamente.");
						}
					} while (bimestre < 1 || bimestre > 4);

					// Corrige índice (0 a 3)
					int b = bimestre - 1;

					// Preenche 2 notas
					for (int n = 0; n < 2; n++) {
						float notas;
						do {
							System.out.print("  Nota " + (n + 1) + ": ");
							while (!teclado.hasNextFloat()) {
								System.out.print("  Valor inválido! Digite uma nota válida: ");
								teclado.next();
							}
							notas = teclado.nextFloat();

							if (notas < 0 || notas > 10) {
								System.out.println("⚠️ A nota deve estar entre 0 e 10.");
							}
						} while (notas < 0 || notas > 10);

						novoAluno.inserirNota(b, n, notas);
					}

					// Pergunta se deseja preencher outro bimestre
					System.out.print("Deseja preencher outro bimestre? (s/n): ");
					String resposta = teclado.next().trim().toLowerCase();
					continuar = resposta.equals("s");
					teclado.nextLine(); // limpar buffer
				}

				novoAluno.calcularNotaAluno(); // calcula a situação com as notas inseridas
				novaSala.insereAluno(novoAluno);
				System.out.println("✅ Aluno cadastrado com sucesso!");
				break;

			// case 1:
			case 2:
				novaSala.listarAlunos();
				break;

			case 3:
				novaSala.listarSituacoes();
				break;

			case 4:
				System.out.print("Digite o CPF do aluno que deseja remover: ");
				String cpfParaRemover = teclado.nextLine();

				boolean removido = novaSala.removerAluno(cpfParaRemover);

				if (removido) {
					System.out.println("Aluno removido com sucesso.");
				} else {
					System.out.println("Aluno com CPF " + cpfParaRemover + " não encontrado.");
				}
				break;

			case 5:
				System.out.print("Digite o CPF do aluno que deseja encontrar: ");
				String cpfBuscado = teclado.nextLine().trim();

				Aluno alunoEncontrado = novaSala.encontrarAluno(cpfBuscado);

				if (alunoEncontrado == null) {
					System.out.println("Aluno com CPF " + cpfBuscado + " não encontrado.");
				} else {
					alunoEncontrado.listar();
				}
				break;

			case 6:
				System.out.print("Digite o CPF do aluno que deseja atualizar: ");
				String cpfAtualiza = teclado.nextLine().trim();

				Aluno alunoEncontrad = novaSala.encontrarAluno(cpfAtualiza);

				if (alunoEncontrad == null) {
					System.out.println("Aluno não encontrado.");
				} else {
					System.out.print("Digite o número do bimestre que deseja atualizar (1 a 4): ");
					int batual;

					do {
						while (!teclado.hasNextInt()) {
							System.out.print("Valor inválido! Digite um número de 1 a 4: ");
							teclado.next();
						}
						batual = teclado.nextInt();
						if (batual < 1 || batual > 4) {
							System.out.println("Bimestre inválido! Tente novamente.");
						}
					} while (batual < 1 || batual > 4);

					batual -= 1; // índice para array

					for (int n = 0; n < 2; n++) {
						float notaAtualizada;
						do {
							System.out.print("  Nova nota " + (n + 1) + ": ");
							while (!teclado.hasNextFloat()) {
								System.out.print("  Valor inválido! Digite uma nota numérica: ");
								teclado.next();
							}
							notaAtualizada = teclado.nextFloat();

							if (notaAtualizada < 0 || notaAtualizada > 10) {
								System.out.println("  A nota deve estar entre 0 e 10.");
							}
						} while (notaAtualizada < 0 || notaAtualizada > 10);

						alunoEncontrad.inserirNota(batual, n, notaAtualizada);
					}

					alunoEncontrad.calcularNotaAluno(); // Atualiza a situação
					System.out.println("Notas atualizadas com sucesso!");
				}

				break;

			case 7:
				System.out.println("7 - Exportar alunos para arquivo");
				novaSala.exportarParaArquivo();
				break;

			case 8:
				System.out.println("8 - Importar notas de um arquivo");
				novaSala.importarDeArquivo();
				break;

			case 0:
				System.out.println("\nSaindo...");
				break;

			default:
				System.out.println("\nOpção inválida. Tente novamente.");
			}

		} while (option != 0);

		teclado.close();
	}
}
