package cadastroAluno;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


public class Sala {
	private String nome;
	private int ano;
	private ArrayList<Aluno> listaDeAlunos;

	public Sala(String nome, int ano) {
		this.nome = nome;
		this.ano = ano;
		this.listaDeAlunos = new ArrayList<>();
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
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

	public void listarAlunosAprovados() {
		System.out.println("\n=== Alunos Aprovados ===");

		boolean encontrouAprovado = false;

		for (Aluno aluno : listaDeAlunos) {
			if ("Aprovado".equalsIgnoreCase(aluno.getSituacao())) {
				aluno.listar();
				encontrouAprovado = true;
			}

		}
		if (!encontrouAprovado) {
			System.out.println("Nenhum aluno aprovado encontrado.");
		}
	}
	
	public void gerarBoletim(String cpf) throws IOException {
	    Aluno aluno = encontrarAluno(cpf);
	    if (aluno == null) {
	        System.out.println("❌ Aluno não encontrado.");
	        return;
	    }

	    String nomeArquivo = "boletim_" + aluno.getNome().replaceAll(" ", "_") + ".pdf";
	    String caminho = "C:\\workspace\\evolution\\cadastroAluno\\boletins\\" + nomeArquivo;

	    PDDocument doc = new PDDocument();
	    PDPage page = new PDPage(PDRectangle.A4);
	    doc.addPage(page);

	    PDPageContentStream content = new PDPageContentStream(doc, page);
	    content.setFont(PDType1Font.HELVETICA, 12);
	    content.beginText();
	    content.setLeading(16f);
	    content.newLineAtOffset(50, 750);
	    content.showText("=== BOLETIM ESCOLAR ===");
	    content.newLine();
	    content.newLine();
	    content.showText("Nome: " + aluno.getNome());
	    content.newLine();
	    content.showText("CPF: " + aluno.getCpf());
	    content.newLine();
	    content.newLine();
	    content.showText("Notas por Bimestre:");
	    content.newLine();

	    float[][] notas = aluno.getNotas();
	    for (int b = 0; b < 4; b++) {
	        content.showText("  Bimestre " + (b + 1) + ": " + notas[b][0] + " | " + notas[b][1]);
	        content.newLine();
	    }

	    content.newLine();
	    content.showText("Média Final: " + aluno.calcularMedia());
	    content.newLine();
	    content.showText("Situação: " + aluno.getSituacao());
	    content.newLine();

	    if ("Aprovado".equalsIgnoreCase(aluno.getSituacao())) {
	        content.showText("Mensagem: Parabéns! Você foi aprovado!");
	    } else {
	        content.showText("Mensagem: Faltou pouco! Tente novamente com foco e disciplina.");
	    }

	    content.endText();
	    content.close();

	    doc.save(caminho);
	    doc.close();

	    System.out.println("✅ Boletim em PDF gerado com sucesso: " + caminho);
	}

	/*//Gerando boletim em txt
	public void gerarBoletim(String cpf) throws IOException {

		Aluno aluno = encontrarAluno(cpf);

		if (aluno == null) {
			System.out.println("❌ Aluno não encontrado.");
			return;
		}

		String nomeArquivo = "boletim_" + aluno.getNome().replaceAll(" ", "_") + ".txt";
		String caminho = "C:\\workspace\\evolution\\cadastroAluno\\boletins\\" + nomeArquivo;

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
			writer.write("=== BOLETIM ESCOLAR ===\n");
			writer.write("Nome: " + aluno.getNome() + "\n");
			writer.write("CPF: " + aluno.getCpf() + "\n\\n");

			writer.write("Notas por Bimestre:\n");

			for (int b = 0; b < 4; b++) {
				writer.write("  Bimestre " + (b + 1) + ": ");
				writer.write(aluno.getNotas()[b][0] + " | " + aluno.getNotas()[b][1] + "\n");

			}

			writer.write("\nMédia Final: " + aluno.calcularMedia() + "\n");
			writer.write("Situação" + aluno.getSituacao() + "\n");

			if ("Aprovado".equalsIgnoreCase(aluno.getSituacao())) {
				writer.write("Mensagem: Parabéns! Você foi aprovado!\n");
			} else {
				writer.write("Mensagem: Faltou pouco! Tente novamente com foco e disciplina.\n");
			}

			System.out.println("✅ Boletim gerado com sucesso: " + caminho);

		} catch (IOException e) {
			System.out.println("❌ Erro ao gerar boletim: " + e.getMessage());
		}
	}
	*/
}
	