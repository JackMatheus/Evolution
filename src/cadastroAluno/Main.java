package cadastroAluno;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		int option;
		String nome, cpf;
		Sala novaSala = new Sala();
		Scanner teclado = new Scanner(System.in);
		Escola escola = new Escola();

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
			System.out.println("9 - Listar Alunos Aprovados");
			System.out.println("10 - Listar alunos de uma sala por ano");

			while (!teclado.hasNextInt()) {
				System.out.print("Digite um número válido: ");
				teclado.next();
			}
			option = teclado.nextInt();
			teclado.nextLine(); // limpar buffer

			switch (option) {

			case 1:
				System.out.print("\nInforme o nome do aluno: ");
				nome = teclado.nextLine();
				
				System.out.print("Informe o ano da sala (ex: 2024): ");
				int anoSala = teclado.nextInt();
				teclado.nextLine();
				
				// Validação do CPF
				do {
					System.out.print("Informe o CPF (11 dígitos): ");
					cpf = teclado.nextLine();
					
					if (escola.existeCpf(cpf)) {
					    System.out.println("❌ Já existe um aluno cadastrado com esse CPF. Cadastro cancelado!");
					    break; // volta para o menu
					}

					if (!Aluno.validarCPF(cpf)) {
						System.out.println("CPF inválido!");
					}
				} while (!Aluno.validarCPF(cpf));

				// 🔷 Escolher ou criar sala
				//System.out.print("Informe o nome da sala: ");
				//String nomeSala = teclado.nextLine();
				
				String nomeSala;
				do {
					System.out.println("Informe o nome da sala (ex: 10A): ");
					nomeSala = teclado.nextLine();
					
					if(!Escola.validarNomeSala(nomeSala)) {
						 System.out.println("⚠️ Nome inválido! Use o formato: número + letra (ex: 9A, 10B).");
					}
					
				}while(!Escola.validarNomeSala(nomeSala));

				Sala sala = escola.buscarSala(nomeSala, anoSala); // busca dentro da escola
				if (sala == null) {
					sala = new Sala(nomeSala, anoSala);
					escola.adicionarSala(sala);
					System.out.println("📁 Sala criada com sucesso!");
				}

				// Criar aluno
				Aluno novoAluno = new Aluno(nome, cpf);

				// 🔢 Inserção de notas por bimestre
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

					int b = bimestre - 1;

					// Notas do bimestre
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

					System.out.print("Deseja preencher outro bimestre? (s/n): ");
					String resposta = teclado.next().trim().toLowerCase();
					continuar = resposta.equals("s");
					teclado.nextLine(); // limpar buffer
				}

				novoAluno.calcularNotaAluno();
				sala.insereAluno(novoAluno);

				System.out.println("✅ Aluno cadastrado com sucesso na sala \"" + nomeSala + "\"!");
				break;

			case 2:
				System.out.print("Informe o nome da sala: ");
				String nomeSalaListada = teclado.nextLine();
				System.out.print("Informe o ano da sala: ");
				int anoSalaListada = teclado.nextInt();
				teclado.nextLine();
				//
				Sala salaSelecionada = escola.buscarSala(nomeSalaListada,anoSalaListada);
				if (salaSelecionada != null) {
					salaSelecionada.listarSituacoes();
				} else {
					System.out.println("❌ Sala não encontrada.");
				}

				break;

			case 3:
				System.out.print("Informe o nome da sala: ");
				String nomeSalaSituacao = teclado.nextLine();
				System.out.print("Informe o ano da sala: ");
				int anoSalaSituacao= teclado.nextInt();
				teclado.nextLine();
				//
				Sala salaSituacao = escola.buscarSala(nomeSalaSituacao,anoSalaSituacao);
				if (salaSituacao != null) {
					salaSituacao.listarSituacoes();
				} else {
					System.out.println("Sala não encontrada.");
				}
				break;

			case 4:
				System.out.print("Informe o nome da sala do aluno: ");
				String nomeSalaRemover = teclado.nextLine();
				System.out.print("Informe o ano da sala: ");
				int anoSalaRemover= teclado.nextInt();
				teclado.nextLine();

				Sala salaRemover = escola.buscarSala(nomeSalaRemover,anoSalaRemover);
				if (salaRemover == null) {
					System.out.println("❌ Sala não encontrada.");
				} else {
					System.out.print("Digite o CPF do aluno que deseja remover: ");
					String cpfParaRemover = teclado.nextLine();

					boolean removido = salaRemover.removerAluno(cpfParaRemover);

					if (removido) {
						System.out.println("✅ Aluno removido com sucesso.");
					} else {
						System.out.println("❌ Aluno com CPF " + cpfParaRemover + " não encontrado.");
					}
				}
				break;

			case 5:
				System.out.print("Informe o nome da sala do aluno: ");
				String nomeSalaBusca = teclado.nextLine();
				System.out.print("Informe o ano da sala: ");
				int anoSalaBusca= teclado.nextInt();
				teclado.nextLine();

				Sala salaBusca = escola.buscarSala(nomeSalaBusca,anoSalaBusca);
				if (salaBusca == null) {
					System.out.println("❌ Sala não encontrada.");
				} else {
					System.out.print("Digite o CPF do aluno que deseja encontrar: ");
					String cpfBuscado = teclado.nextLine().trim();

					Aluno alunoEncontrado = salaBusca.encontrarAluno(cpfBuscado);

					if (alunoEncontrado == null) {
						System.out.println("❌ Aluno com CPF " + cpfBuscado + " não encontrado.");
					} else {
						alunoEncontrado.listar();
					}
				}
				break;

			case 6:
				System.out.print("Informe o nome da sala do aluno: ");
				String nomeSalaAtualiza = teclado.nextLine();
				System.out.print("Informe o ano da sala: ");
				int anoSalaAtualiza= teclado.nextInt();
				teclado.nextLine();

				Sala salaAtualiza = escola.buscarSala(nomeSalaAtualiza, anoSalaAtualiza);
				if (salaAtualiza == null) {
					System.out.println("❌ Sala não encontrada.");
				} else {
					System.out.print("Digite o CPF do aluno que deseja atualizar: ");
					String cpfAtualiza = teclado.nextLine().trim();

					Aluno alunoAtualizar = salaAtualiza.encontrarAluno(cpfAtualiza);

					if (alunoAtualizar == null) {
						System.out.println("❌ Aluno não encontrado.");
					} else {
						System.out.print("Digite o número do bimestre que deseja atualizar (1 a 4): ");
						int batual;

						do {
							while (!teclado.hasNextInt()) {
								System.out.print("Valor inválido! Digite um número de 1 a 4: ");
								teclado.next();
							}
							batual = teclado.nextInt();
							teclado.nextLine(); // limpa quebra de linha
							if (batual < 1 || batual > 4) {
								System.out.println("Bimestre inválido! Tente novamente.");
							}
						} while (batual < 1 || batual > 4);

						batual -= 1; // índice começa do zero

						for (int n = 0; n < 2; n++) {
							float notaAtualizada;
							do {
								System.out.print("  Nova nota " + (n + 1) + ": ");
								while (!teclado.hasNextFloat()) {
									System.out.print("  Valor inválido! Digite uma nota numérica: ");
									teclado.next();
								}
								notaAtualizada = teclado.nextFloat();
								teclado.nextLine(); // limpar buffer

								if (notaAtualizada < 0 || notaAtualizada > 10) {
									System.out.println("  A nota deve estar entre 0 e 10.");
								}
							} while (notaAtualizada < 0 || notaAtualizada > 10);

							alunoAtualizar.inserirNota(batual, n, notaAtualizada);
						}

						alunoAtualizar.calcularNotaAluno();
						System.out.println("✅ Notas atualizadas com sucesso!");
					}
				}
				break;

			case 7:
				System.out.print("Informe o nome da sala que deseja exportar: ");
				String nomeSalaExportar = teclado.nextLine();
				
				System.out.print("Informe o ano da sala: ");
				int anoSalaExportar= teclado.nextInt();
				teclado.nextLine();
				

				Sala salaExportar = escola.buscarSala(nomeSalaExportar, anoSalaExportar);
				if (salaExportar != null) {
					salaExportar.exportarParaArquivo();
				} else {
					System.out.println("❌ Sala não encontrada.");
				}
				break;

			case 8:
				System.out.print("Informe o nome da sala para importar os alunos: ");
				String nomeSalaImportar = teclado.nextLine();
				
				System.out.print("Informe o ano da sala: ");
				int anoSalaImportar= teclado.nextInt();
				teclado.nextLine();

				Sala salaImportar = escola.buscarSala(nomeSalaImportar, anoSalaImportar);
				if (salaImportar == null) {
					salaImportar = new Sala(nomeSalaImportar, anoSalaImportar);
					escola.adicionarSala(salaImportar);
					System.out.println("📁 Sala criada com sucesso.");
				}

				salaImportar.importarDeArquivo(); // importa alunos para esta sala
				break;
				
			case 9:
				 System.out.print("Informe o nome da sala: ");
				 String nomeSalaAprovados = teclado.nextLine();
				 
					System.out.print("Informe o ano da sala: ");
					int anoSalaAprovados= teclado.nextInt();
					teclado.nextLine();
					
				 Sala salaAprovados = escola.buscarSala(nomeSalaAprovados, anoSalaAprovados);
				 
				 if (salaAprovados != null) {
				        salaAprovados.listarAlunosAprovados();
				    } else {
				        System.out.println("Sala não encontrada.");
				    }
				    break;
				    
			case 11:
			    System.out.print("Informe o nome da sala: ");
			    String salaConsulta = teclado.nextLine();

			    System.out.print("Informe o ano da sala: ");
			    int anoConsulta = teclado.nextInt();
			    teclado.nextLine();

			    Sala salaFiltrada = escola.buscarSala(salaConsulta, anoConsulta);
			    if(salaFiltrada!=null) {
			    	salaFiltrada.listarAlunos();
			    }else {
			    	System.out.println("❌ Sala com esse nome e ano não encontrada.");
			    }

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
