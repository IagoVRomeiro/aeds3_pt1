
import java.io.*;

public class Menu {

    public static void menu() throws IOException {
        String BD = "dataset/capitulos.db";
        FileOutputStream arq = new FileOutputStream(BD, true);
        DataOutputStream dos = new DataOutputStream(arq);
        RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw");
        FileInputStream arq2 = new FileInputStream(BD);
        DataInputStream dis = new DataInputStream(arq2);

        while (true) {
            MyIO.println("\n--- Menu CRUD Capitulo ---");
            MyIO.println("1. Criar Capitulo");
            MyIO.println("2. Ler Um Capitulo");
            MyIO.println("3. Ler Multiplos Capitulos");
            MyIO.println("4. Atualizar Capitulo");
            MyIO.println("5. Deletar Capitulo");
            MyIO.println("6. Sair");

            MyIO.print("Escolha uma opcao: ");
            int opcao = MyIO.readInt(); // Mantendo o MyIO para entrada de dados

            switch (opcao) {
                case 1 ->
                    criarCapitulo(dos, dis);
                case 2 ->
                    lerCapitulo(dis);
                case 3 ->

                    lerCapitulos(dis, AuxFuncoes.PerguntaQTD());
                case 4 ->
                    atualizarCapitulo(dis);
                case 5 ->
                    deletarCapitulo(RAF);
                case 6 -> {
                    MyIO.println("Saindo...");
                    System.exit(0);
                }
                default ->
                    MyIO.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void criarCapitulo(DataOutputStream dos, DataInputStream dis) throws IOException {
        MyIO.println("\n--- Criar Novo Capitulo ---");

        MyIO.print("(int) Numero do Capitulo: ");
        int numero = MyIO.readInt();

        if (numero < dis.readInt()) {
            MyIO.println("Id já existe");
        } else {

            MyIO.print("(int) Volume: ");
            int volume = MyIO.readInt();

            MyIO.print("(String) Nome: ");
            String nome = MyIO.readLine();

            MyIO.print("(String) Titulo Original: ");
            String tituloOriginal = MyIO.readLine();

            MyIO.print("(String) Titulo Ingles: ");
            String tituloIngles = MyIO.readLine();

            MyIO.print("(int) Paginas: ");
            int paginas = MyIO.readInt();

            MyIO.print("(xx/xx/xxxx) Data: ");
            String data = MyIO.readLine();

            MyIO.print("(String) Episodio: ");
            String episodio = MyIO.readLine();

            String[] titulos = { tituloOriginal, tituloIngles };
            Capitulo capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);

            byte[] bytes = capitulo.toByteArray();

            dos.write(1);
            dos.write(bytes.length);
            dos.write(bytes);

            MyIO.println("Capítulo salvo com sucesso!");
        }
    }

    private static void lerCapitulo(DataInputStream dis) throws IOException {
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

    private static void lerCapitulos(DataInputStream dis, int[] ids) throws IOException {

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

    // FAZER
    private static void atualizarCapitulo(DataInputStream dis) throws IOException {
        System.out.println("\n--- Atualizar Capitulo ---");

        System.out.print("Numero do Capitulo para atualizar: ");
        int n = MyIO.readInt();

        dis.readInt();
        while (dis.available() > 0) {

            byte valido = dis.readByte(); // Lê o byte de validade
            int tamanhoVetor = dis.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                dis.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o capítulo com os dados
                if (capitulo.getNumCapitulo() == n) {

                    System.out.print("Novo volume: ");
                    capitulo.setVolume(MyIO.readInt());

                    System.out.print("Novo nome: ");
                    capitulo.setNome(MyIO.readLine());

                    System.out.print("Novos Títulos Inglês e Original (separados por vírgula): ");
                    String[] titulos = MyIO.readLine().split(",");
                    capitulo.setTitulos(titulos);

                    System.out.print("Novo número de páginas: ");
                    capitulo.setPaginas(MyIO.readInt());

                    System.out.print("Nova data: ");
                    capitulo.setData(MyIO.readLine());

                    System.out.print("Novo episódio: ");
                    capitulo.setEpisodio(MyIO.readLine());

                    // setar lapide para 0
                    // criar novo capitulo com os dados inseridos acima

                }

            } else {
                // Se o vetor não for válido, pula os bytes correspondentes ao tamanho do vetor
                dis.skipBytes(tamanhoVetor);
            }
        }

    }

    // FAZER
    private static void deletarCapitulo(RandomAccessFile RAF) throws IOException {
        System.out.println("\n--- Deletar Capítulo ---");

        System.out.print("Número do Capítulo para deletar: ");
        int n = MyIO.readInt();

        RAF.readInt();
            while (RAF.getFilePointer() < RAF.length()) {
            long ponteiro = RAF.getFilePointer();
            byte valido = RAF.readByte(); // Lê o byte de validade
            
            int tamanhoVetor = RAF.readInt(); // Lê o tamanho do vetor (4 bytes)
            

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o capítulo com os dados
                if (capitulo.getNumCapitulo() == n) {
                   RAF.seek(ponteiro);
                   RAF.write(0);
                }

            } else {
                // Se o vetor não for válido, pula os bytes correspondentes ao tamanho do vetor
                RAF.skipBytes(tamanhoVetor);
            }
        }
    }
}
