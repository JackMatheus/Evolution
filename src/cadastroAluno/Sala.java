package cadastroAluno;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class Sala {
	private String nome;
	private ArrayList<Aluno> listaDeAlunos;

	public Sala(String nome) {
		this.nome = nome;
		this.listaDeAlunos = new ArrayList<>();
	}

	public String getNome() {
		return nome;
	}

	public Sala() {
		this.listaDeAlunos = new ArrayList<>();
	}

	public void insereAluno(Aluno novoAluno) {
		novoAluno.calcularNotaAluno();
		listaDeAlunos.add(novoAluno);
	}

	// 2-encontrar
	public Aluno encontrarAluno(String cpf) {
		// Encontrar o aluno pelo cpf
		for (Aluno aluno : listaDeAlunos) {
			if (aluno.getCpf().equals(cpf.trim())) {
				return aluno;
			}
		}
		return null;
	}

	// 3-excluir
	public boolean removerAluno(String cpf) {
		// Encontrar o aluno pelo cpf
		for (Aluno aluno : listaDeAlunos) {
			if (aluno.getCpf().equals(cpf)) {
				listaDeAlunos.remove(aluno);
				return true;
			}
		}
		return false;
	}

	// 3-Atualizar

	public void listarSituacoes() {
		System.out.println("\n=== Situação dos Alunos ===");
		for (Aluno aluno : listaDeAlunos) {
			aluno.mostraSituacao();
		}
	}

	public void listarAlunos() {
		System.out.println("\n=== Lista de Alunos ===");
		if (listaDeAlunos.isEmpty()) {
			System.out.println("Nenhum aluno cadastrado.");
			return;
		}

		for (Aluno aluno : listaDeAlunos) {
			aluno.listar();
		}
	}

	public void exportarParaArquivo() {
		String caminhoPasta = "C:\\workspace\\evolution\\cadastroAluno\\extracao";
		String caminhoArquivo = caminhoPasta + "\\turma.txt";

		try {
			// Criar a pasta se não existir
			File pasta = new File(caminhoPasta);
			if (!pasta.exists()) {
				pasta.mkdirs(); // cria todas as pastas no caminho
			}

			// Escreve no arquivo
			BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo));

			if (listaDeAlunos.isEmpty()) {
				writer.write("Nenhum aluno cadastrado.");
			} else {
				for (Aluno aluno : listaDeAlunos) {
					writer.write("Nome: " + aluno.getNome());
					writer.newLine();

					writer.write("CPF: " + aluno.getCpf());
					writer.newLine();

					writer.write("Notas por Bimestre:");
					writer.newLine();

					for (int b = 0; b < 4; b++) {
						writer.write(
								"  Bimestre " + (b + 1) + ": " + aluno.getNota(b, 0) + " | " + aluno.getNota(b, 1));
						writer.newLine();
					}

					float media = aluno.calcularMedia();
					writer.write("Média Final: " + media);
					writer.newLine();

					writer.write("Situação: " + aluno.getSituacao());
					writer.newLine();

					writer.write("--------------------------------------------------");
					writer.newLine();
				}
			}

			writer.close();
			System.out.println("✅ Arquivo exportado com sucesso para: " + caminhoArquivo);

		} catch (IOException e) {
			System.out.println("❌ Erro ao exportar o arquivo: " + e.getMessage());
		}
	}

	// atualizado
	public void importarDeArquivo() {
		String caminhoArquivo = "C:\\workspace\\evolution\\cadastroAluno\\dadosDeEntrada\\dadosDeEntrada.txt";
		File arquivo = new File(caminhoArquivo);

		if (!arquivo.exists()) {
			System.out.println("🔍 Arquivo não encontrado em: " + caminhoArquivo);
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
			String linha;
			Aluno alunoAtual = null;
			int bimestre = 0;
			// Aqui
			while ((linha = reader.readLine()) != null) {
				if (linha.startsWith("Nome: ")) {
					if (alunoAtual != null) {
						alunoAtual.calcularNotaAluno();
						this.insereAluno(alunoAtual);
					}

					// Novo aluno
					String nome = linha.substring(6).trim();
					alunoAtual = new Aluno();
					alunoAtual.setNome(nome);

				} else if (linha.startsWith("CPF: ")) {
					String cpf = linha.substring(5).trim();
					if (alunoAtual != null)
						alunoAtual.setCpf(cpf);

				} else if (linha.startsWith("  Bimestre") && linha.contains(":")) {
					try {
						String[] partes = linha.split(":");
						if (partes.length > 1) {
							bimestre = Integer.parseInt(partes[0].replaceAll("\\D+", "")) - 1;

							String[] notasTxt = partes[1].trim().split("\\|");
							if (notasTxt.length == 2) {
								float nota1 = Float.parseFloat(notasTxt[0].trim());
								float nota2 = Float.parseFloat(notasTxt[1].trim());

								if (alunoAtual != null) {
									alunoAtual.inserirNota(bimestre, 0, nota1);
									alunoAtual.inserirNota(bimestre, 1, nota2);
								}
							}
						}
					} catch (Exception e) {
						System.out.println("⚠️ Erro ao processar linha: " + linha);
					}

				} else if (linha.startsWith("Situação:")) {
					// Situação será calculada abaixo, após fim do loop
					// Mantido para compatibilidade com arquivos atuais
				}
			}

			// Após sair do loop, garante que o último aluno seja salvo
			if (alunoAtual != null) {
				alunoAtual.calcularNotaAluno();
				this.insereAluno(alunoAtual);
			}

			System.out.println("✅ Alunos importados com sucesso de: " + caminhoArquivo);

		} catch (IOException e) {
			System.out.println("❌ Erro ao ler o arquivo: " + e.getMessage());
		} catch (Exception ex) {
			System.out.println("❌ Erro inesperado: " + ex.getMessage());
		}
	}

}
