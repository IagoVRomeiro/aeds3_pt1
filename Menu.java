
import java.io.*;

public class Menu {

    public static final String BD = "dataset/capitulos.db";

    public static void menu() throws IOException {
        while (true) {
            MyIO.println("\n--- Menu CRUD Capitulo ---");
            MyIO.println("1. Criar Capitulo");
            MyIO.println("2. Ler Um Capitulo");
            MyIO.println("3. Ler Multiplos Capitulos");
            MyIO.println("4. Atualizar Capitulo");
            MyIO.println("5. Deletar Capitulo");
            MyIO.println("6. Sair");

            MyIO.print("Escolha uma opcao: ");
            int opcao = MyIO.readInt();

            switch (opcao) {
                case 1 ->
                    criarCapitulo(AuxFuncoes.CriarCapitulo());
                case 2 ->
                    lerCapitulo(AuxFuncoes.qualCapitulo());
                case 3 ->
                    lerCapitulos(AuxFuncoes.PerguntaQTD_ID());
                case 4 ->
                    atualizarCapitulo(AuxFuncoes.qualCapitulo());
                case 5 ->
                    deletarCapitulo(AuxFuncoes.qualCapitulo());
                case 6 -> {
                    MyIO.println("Saindo...");
                    System.exit(0);
                }
                default ->
                    MyIO.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void criarCapitulo(Capitulo capitulo) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");
        MyIO.println("\n--- Criar Novo Capitulo ---");

        byte[] bytes = capitulo.toByteArray();

        // Escrever o capítulo após sair do loop
        AuxFuncoes.escreverCapitulo_FIM(raf, bytes);
        AuxFuncoes.ReescreverUltimoIdInserido();
        MyIO.println("Capítulo salvo com sucesso!");
    }

    private static void lerCapitulo(int ID) throws IOException {
        FileInputStream arq2 = new FileInputStream(BD);
        DataInputStream dis = new DataInputStream(arq2);
        boolean achou = false;

        dis.readInt();
        while (dis.available() > 0) {

            byte valido = dis.readByte(); // Lê o byte de validade(lapide)
            int tamanhoVetor = dis.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                dis.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o capítulo com os dados
                if (capitulo.getNumCapitulo() == ID) {
                    achou = true;
                    MyIO.println(capitulo.toString()); // Exibe o conteúdo do capítulo
                }

            } else {
                // Se o vetor não for válido, pula os bytes correspondentes ao tamanho do vetor
                dis.skipBytes(tamanhoVetor);
            }
        }
        if (!achou) {
            MyIO.println("Nao encontrado");
        }
        dis.close();
    }

    private static void lerCapitulos(int[] ids) throws IOException {
        FileInputStream arq2 = new FileInputStream(BD);
        DataInputStream dis = new DataInputStream(arq2);
        boolean achou = false;
        dis.readInt();
        while (dis.available() > 0) {

            byte valido = dis.readByte(); // Lê o byte de validade(lapide)
            int tamanhoVetor = dis.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                dis.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o capítulo com os dados
                for (int i = 0; i < ids.length; i++) {
                    if (capitulo.getNumCapitulo() == ids[i]) {
                        achou = true;
                        MyIO.println(capitulo.toString()); // Exibe o conteúdo do capítulo
                    }

                }

            } else {
                // Se o vetor não for válido, pula os bytes correspondentes ao tamanho do vetor

                dis.skipBytes(tamanhoVetor);
            }
        }
        if (!achou) {
            MyIO.println("Nenhum encontrado");
        }
        dis.close();
    }

    private static void atualizarCapitulo(int ID) throws IOException {
        try (RandomAccessFile RAF = new RandomAccessFile(BD, "rw")) {
            MyIO.println("\n--- Atualizar Capitulo ---");

            // Faz com que o ponteiro volte ao começo do arquivo
            RAF.seek(0);
            RAF.readInt();

            long posicao = -1;
            int tamanhoVetor = 0;

            while (RAF.getFilePointer() < RAF.length()) {
                long ponteiro = RAF.getFilePointer();
                byte valido = RAF.readByte(); // Lê o byte de validade (lapide)
                tamanhoVetor = RAF.readInt(); // Lê o tamanho do vetor (4 bytes)

                if (valido == 1) {
                    byte[] byteArray = new byte[tamanhoVetor];
                    RAF.readFully(byteArray);

                    Capitulo capitulo = new Capitulo();
                    capitulo.fromByteArray(byteArray);
                    if (capitulo.getNumCapitulo() == ID) {
                        posicao = ponteiro;
                        break;
                    }
                } else {
                    RAF.skipBytes(tamanhoVetor); // Pula os bytes do capítulo inválido
                }
            }

            if (posicao == -1) {
                MyIO.println("Capítulo não encontrado.");
                return;
            }

            Capitulo novoCapitulo = AuxFuncoes.CriarCapitulo();

            byte[] novoByteArray = novoCapitulo.toByteArray();

            // Marca o novo capitulo como válido
            if (novoByteArray.length <= tamanhoVetor) {
                RAF.seek(posicao);
                RAF.writeByte(1);
                RAF.writeInt(tamanhoVetor);
                RAF.write(novoByteArray);

                // Preenche espaço restante com 0 caso o novo capítulo seja menor
                int bytesRestantes = tamanhoVetor - novoByteArray.length;
                if (bytesRestantes > 0) {
                    RAF.write(new byte[bytesRestantes]);
                }
            } else {
                AuxFuncoes.escreverCapitulo_FIM(RAF, novoByteArray);
            }

            MyIO.println("Capítulo atualizado com sucesso.");
        } catch (IOException e) {
            MyIO.println("Erro ao atualizar capítulo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deletarCapitulo(int ID) throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");
        MyIO.println("\n--- Deletar Capitulo ---");

        // Lê os primeiros 4 bytes do arquivo
        RAF.seek(0);
        int primeiroValor = RAF.readInt();

        // Se o registro for válido ele coloca a lapide e marca como invalido
        while (RAF.getFilePointer() < RAF.length()) {
            long ponteiro = RAF.getFilePointer();
            if (RAF.readByte() == 1) {
                int tamanhoVetor = RAF.readInt();
                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                if (capitulo.getNumCapitulo() == ID) {
                    RAF.seek(ponteiro);
                    RAF.writeByte(0); // Marca como inválido
                    MyIO.println("Capitulo " + ID + " deletado.");
                    // Se o ID deletado for igual aos primeiros 4 bytes, decrementa 1
                    if (ID == primeiroValor) {
                        RAF.seek(0);
                        RAF.writeInt(primeiroValor - 1);
                    }

                    RAF.close();
                    return;
                }
            } else {
                RAF.skipBytes(RAF.readInt()); // Pula o registro inválido
            }
        }

        MyIO.println("Capitulo nao encontrado.");
        RAF.close();
    }

}
