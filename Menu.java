
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
                    criarCapitulo(AuxFuncoes.CriarNovoCapitulo());
                case 2 ->
                    lerCapitulo(AuxFuncoes.qualID());
                case 3 ->
                    lerCapitulos(AuxFuncoes.PerguntaQTD_ID());
                case 4 ->
                    atualizarCapitulo(AuxFuncoes.qualID());
                case 5 ->
                    deletarCapitulo(AuxFuncoes.qualID());
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

        byte[] bytes = capitulo.toByteArray();

        AuxFuncoes.escreverCapitulo(bytes, raf.length());
        AuxFuncoes.IncrementaUltimoIdInserido();
        MyIO.println("Capítulo salvo com sucesso!");
        raf.close();
    }

    private static void lerCapitulo(int ID) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");
        boolean achou = false;

        raf.seek(4);

        while (raf.getFilePointer() < raf.length()) {
            byte valido = raf.readByte();
            int tamanhoVetor = raf.readInt();

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                raf.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                if (capitulo.getId() == ID) {
                    achou = true;
                    MyIO.println(capitulo.toString());
                    break;
                }
            } else {
                raf.skipBytes(tamanhoVetor);
            }
        }

        if (!achou) {
            MyIO.println("Não encontrado");
        }

        raf.close();
    }

    private static void lerCapitulos(int[] ids) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");

        raf.seek(4);

        while (raf.getFilePointer() < raf.length()) {
            byte valido = raf.readByte();
            int tamanhoVetor = raf.readInt();

            if (valido == 1) {
                byte[] byteArray = new byte[tamanhoVetor];
                raf.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                for (int id : ids) {
                    if (capitulo.getId() == id) {
                        MyIO.println(capitulo.toString());
                    }
                }
            } else {
                raf.skipBytes(tamanhoVetor);
            }
        }

        raf.close();
    }

    private static void atualizarCapitulo(int ID) throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");
        RAF.seek(4);

        while (RAF.getFilePointer() < RAF.length()) {
            long posicao = RAF.getFilePointer();
            byte valido = RAF.readByte();
            int tamanhoVetor = RAF.readInt();

            if (valido == 1) {

                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);
                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                if (capitulo.getId() == ID) {
                    Capitulo novoCapitulo = AuxFuncoes.CriarNovoCapitulo();
                    novoCapitulo.setId(ID);

                    byte[] novoByteArray = novoCapitulo.toByteArray();

                    if (novoByteArray.length <= tamanhoVetor) {
                        MyIO.println("Atualizacao coube no espaço reservado");
                        RAF.seek(posicao + 5);
                        RAF.write(novoByteArray);

                        RAF.write(new byte[tamanhoVetor - novoByteArray.length]);
                        break;
                    } else {
                        MyIO.println("Atualizacao nao coube no espaço reservado, inserido no fim do arquivo");

                        RAF.seek(posicao);
                        RAF.writeByte(0);
                        AuxFuncoes.escreverCapitulo(novoByteArray, RAF.length());
                        break;
                    }

                }

            } else {
                RAF.skipBytes(tamanhoVetor);
            }

        }
        RAF.close();
    }

    private static void deletarCapitulo(int ID) throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");

        RAF.seek(0);
        int UltimoId = RAF.readInt();
        byte valido = RAF.readByte();
        int tamanhoVetor = RAF.readInt();

        while (RAF.getFilePointer() < RAF.length()) {
            long ponteiro = RAF.getFilePointer();
            if (valido == 1) {

                byte[] byteArray = new byte[tamanhoVetor];
                RAF.readFully(byteArray);
                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray);

                if (capitulo.getId() == ID) {
                    RAF.seek(ponteiro);
                    RAF.writeByte(0);
                    MyIO.println("Capítulo " + ID + " deletado.");

                    if (ID == UltimoId) {
                        RAF.seek(0);
                        RAF.writeInt(UltimoId - 1);
                    }

                    RAF.close();

                }
            } else {
                RAF.skipBytes(tamanhoVetor);
            }
        }

        MyIO.println("Capítulo não encontrado.");
        RAF.close();
    }
}
