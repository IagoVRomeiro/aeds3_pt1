
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
            MyIO.println("6. Listar Capitulos Deletados"); // Nova opção
            MyIO.println("7. Sair");

            MyIO.print("Escolha uma opcao: ");
            int opcao = MyIO.readInt();

            switch (opcao) {
                case 1 ->
                    criarCapitulo();
                case 2 ->
                    lerCapitulo();
                case 3 ->
                    lerCapitulos(AuxFuncoes.PerguntaQTD());
                case 4 ->
                    atualizarCapitulo();
                case 5 ->
                    deletarCapitulo();
                case 6 ->
                    listarDeletados(); // Chama a função de listar deletados
                case 7 -> {
                    MyIO.println("Saindo...");
                    System.exit(0);
                }
                default ->
                    MyIO.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void criarCapitulo() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(BD, "rw")) {
            MyIO.println("\n--- Criar Novo Capitulo ---");

            MyIO.print("(int) Numero do Capitulo: ");
            int numero = MyIO.readInt();

            // Verificar se o ID já existe percorrendo o arquivo
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();
                int tamanho = raf.readInt();
                if (id == numero) {
                    MyIO.println("ID já existe!");
                    return;
                }
                raf.skipBytes(tamanho);
            }

            // Captura dos dados
            MyIO.print("(int) Volume: ");
            int volume = MyIO.readInt();

            MyIO.print("(String) Nome: ");
            String nome = MyIO.readLine();

            MyIO.print("(String) Título Original: ");
            String tituloOriginal = MyIO.readLine();

            MyIO.print("(String) Título Inglês: ");
            String tituloIngles = MyIO.readLine();

            MyIO.print("(int) Páginas: ");
            int paginas = MyIO.readInt();

            MyIO.print("(xx/xx/xxxx) Data: ");
            String data = MyIO.readLine();

            MyIO.print("(String) Episódio: ");
            String episodio = MyIO.readLine();

            String[] titulos = {tituloOriginal, tituloIngles};
            Capitulo capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);

            byte[] bytes = capitulo.toByteArray();

            // Posiciona no final do arquivo e grava
            raf.seek(raf.length());

            AuxFuncoes.escreverCapitulo(raf, bytes);

            MyIO.println("Capítulo salvo com sucesso!");
        }
    }

    private static void lerCapitulo() throws IOException {
        FileInputStream arq2 = new FileInputStream(BD);
        DataInputStream dis = new DataInputStream(arq2);
        MyIO.print("(int) Qual o Capitulo/Id? ");
        int op = MyIO.readInt();

        dis.readInt();
        while (dis.available() > 0) {

            byte valido = dis.readByte(); // Lê o byte de validade
            int tamanhoVetor = dis.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                dis.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o capítulo com os dados
                if (capitulo.getNumCapitulo() == op) {
                    System.out.println(capitulo.toString()); // Exibe o conteúdo do capítulo
                }

            } else {
                // Se o vetor não for válido, pula os bytes correspondentes ao tamanho do vetor
                dis.skipBytes(tamanhoVetor);
            }
        }
    }

    private static void lerCapitulos(int[] ids) throws IOException {
        FileInputStream arq2 = new FileInputStream(BD);
        DataInputStream dis = new DataInputStream(arq2);

        dis.readInt();
        while (dis.available() > 0) {

            byte valido = dis.readByte(); // Lê o byte de validade
            int tamanhoVetor = dis.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                dis.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o capítulo com os dados
                for (int i = 0; i < ids.length; i++) {
                    if (capitulo.getNumCapitulo() == ids[i]) {
                        System.out.println(capitulo.toString()); // Exibe o conteúdo do capítulo
                    }
                }

            } else {
                // Se o vetor não for válido, pula os bytes correspondentes ao tamanho do vetor
                dis.skipBytes(tamanhoVetor);
            }
        }
    }

    private static void atualizarCapitulo() throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");
        System.out.println("\n--- Atualizar Capitulo ---");

        System.out.print("Numero do Capitulo para atualizar: ");
        int n = MyIO.readInt();

        RAF.seek(0);
        int primeiroValor = RAF.readInt();
        long posicao = -1;

        while (RAF.getFilePointer() < RAF.length()) {
            long ponteiro = RAF.getFilePointer();
            byte valido = RAF.readByte(); // Lê o byte de validade
            int tamanhoVetor = RAF.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);
                if (capitulo.getNumCapitulo() == n) {
                    posicao = ponteiro;
                    break;
                }
            } else {
                RAF.skipBytes(tamanhoVetor); // Pula os bytes do capítulo inválido
            }
        }

        if (posicao == -1) {
            System.out.println("Capítulo não encontrado.");
        } else {
            RAF.seek(posicao);
            RAF.writeByte(0); // Marca como inválido

            System.out.print("Novo volume: ");
            int novoVolume = MyIO.readInt();

            System.out.print("Novo nome: ");
            String novoNome = MyIO.readLine();

            System.out.print("Novos Títulos Inglês e Original (separados por vírgula): ");
            String[] novosTitulos = MyIO.readLine().split(",");

            System.out.print("Novo número de páginas: ");
            int novasPaginas = MyIO.readInt();

            System.out.print("Nova data: ");
            String novaData = MyIO.readLine();

            System.out.print("Novo episódio: ");
            String novoEpisodio = MyIO.readLine();

            Capitulo novoCapitulo = new Capitulo(n, novoVolume, novoNome, novosTitulos, novasPaginas, novaData, novoEpisodio);
            byte[] novoByteArray = novoCapitulo.toByteArray();

        
            AuxFuncoes.escreverCapitulo(RAF, novoByteArray);

            System.out.println("Capítulo atualizado com sucesso.");
        }

        RAF.close();
    }

    private static void deletarCapitulo() throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");
        System.out.println("\n--- Deletar Capítulo ---");

        System.out.print("Número do Capítulo para deletar: ");
        int numCapitulo = MyIO.readInt();

        // Lê os primeiros 4 bytes do arquivo
        RAF.seek(0);
        int primeiroValor = RAF.readInt();

        while (RAF.getFilePointer() < RAF.length()) {
            long ponteiro = RAF.getFilePointer();
            if (RAF.readByte() == 1) { // Se o registro for válido
                int tamanhoVetor = RAF.readInt();
                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                if (capitulo.getNumCapitulo() == numCapitulo) {
                    RAF.seek(ponteiro);
                    RAF.writeByte(0); // Marca como inválido
                    System.out.println("Capítulo " + numCapitulo + " deletado.");

                    // Se o ID deletado for igual aos primeiros 4 bytes, decrementa 1
                    if (numCapitulo == primeiroValor) {
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

        System.out.println("Capítulo não encontrado.");
        RAF.close();
    }

    private static void listarDeletados() throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");
        System.out.println("\n--- Listar Capitulos Deletados ---");

        RAF.seek(0); // Posiciona no início do arquivo
        int primeiroValor = RAF.readInt();

        boolean encontrouDeletados = false;

        while (RAF.getFilePointer() < RAF.length()) {
            long ponteiro = RAF.getFilePointer();
            byte valido = RAF.readByte(); // Lê o byte de validade
            int tamanhoVetor = RAF.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 0) { // Capítulo deletado (inválido)
                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                // Exibe os capítulos deletados
                System.out.println("Capítulo Deletado: " + capitulo.getNumCapitulo());
                encontrouDeletados = true;
            } else {
                RAF.skipBytes(tamanhoVetor); // Pula os capítulos válidos
            }
        }

        if (!encontrouDeletados) {
            System.out.println("Nenhum capítulo deletado encontrado.");
        }

        RAF.close();
    }

}
