
import java.io.*;

public class Menu {

    public static void menu() throws IOException {
        String BD = "dataset/capitulos.db";
        FileOutputStream arq = new FileOutputStream(BD);
        DataOutputStream dos = new DataOutputStream(arq);

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

            MyIO.print("Escolha uma opção: ");
            int opcao = MyIO.readInt();  // Mantendo o MyIO para entrada de dados

            switch (opcao) {
                case 1 ->
                    criarCapitulo(dos);
                case 2 ->
                    lerCapitulo(dis);
                case 3 ->
                    lerCapitulos(dis);
                case 4 ->
                    atualizarCapitulo();
                case 5 ->
                    deletarCapitulo();
                case 6 -> {
                    MyIO.println("Saindo...");
                    System.exit(0);
                }
                default ->
                    MyIO.println("Opção inválida. Tente novamente.");
            }
        }
    }

    //FEITO
    private static void criarCapitulo(DataOutputStream dos) throws IOException {
        MyIO.println("\n--- Criar Novo Capitulo ---");

        MyIO.print("(int) Numero do Capitulo: ");
        int numero = MyIO.readInt();

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

        String[] titulos = {tituloOriginal, tituloIngles};
        Capitulo capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);

        byte[] bytes = capitulo.toByteArray();
        dos.write(1);
        dos.write(bytes.length);
        dos.write(bytes);

        MyIO.println("Capítulo salvo com sucesso!");
    }

    //FAZER
    private static void lerCapitulo(DataInputStream dis) throws IOException {
        // Lógica para ler um capítulo
        MyIO.print("\nInforme o ID do capítulo: ");
        int idProcurado = MyIO.readInt();

        try {
            while (dis.available() > 0) {
                byte valido = dis.readByte(); // Lê o byte de validade
                int tamanhoVetor = dis.readInt(); // Lê o tamanho do vetor
                int id = dis.readInt(); // Lê o ID

                if (valido == 1 && id == idProcurado) {
                    byte[] bytes = new byte[tamanhoVetor];
                    dis.readFully(bytes); // Lê os bytes do capítulo

                    Capitulo capitulo = Capitulo.fromByteArray(bytes); // Converte os bytes para um objeto Capitulo
                   
                } else {
                    dis.skipBytes(tamanhoVetor); // Pula os bytes do capítulo
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyIO.println("Capítulo não encontrado!");
    }
    //FAZER
    private static void lerCapitulos(DataInputStream dis) throws IOException {
    }
    //FAZER
    private static void atualizarCapitulo() {
        // Lógica de atualização do capítulo
    }
    //FAZER
    private static void deletarCapitulo() {
        // Lógica de deleção do capítulo
    }


}
