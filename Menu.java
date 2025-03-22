
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
                    MyIO.println("Opçao invalida. Tente novamente.");
            }
        }
    }

    private static void criarCapitulo(Capitulo capitulo) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");

        byte[] bytes = capitulo.toByteArray();

        // Escrever o capítulo após sair do loop
        AuxFuncoes.escreverCapitulo(bytes, raf.length());
        AuxFuncoes.ReescreverUltimoIdInserido();
        MyIO.println("Capitulo salvo com sucesso!");
        raf.close();
    }

    private static void lerCapitulo(int ID) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");
        boolean achou = false;

        raf.seek(4); // Garante que a leitura começa do início

        while (raf.getFilePointer() < raf.length()) {
            byte valido = raf.readByte(); // Lê a lápide (byte de validade)
            int tamanhoVetor = raf.readInt(); // Lê o tamanho do vetor de bytes

            if (valido == 1) { // Se o registro for válido
                byte[] byteArray = new byte[tamanhoVetor];
                raf.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Converte os bytes para objeto

                if (capitulo.getId() == ID) {
                    achou = true;
                    MyIO.println(capitulo.toString()); // Exibe o capítulo encontrado
                    break;
                }
            } else {
                // Se o registro for inválido, pula os bytes correspondentes
                raf.skipBytes(tamanhoVetor);
            }
        }

        if (!achou) {
            MyIO.println("Nao encontrado");
        }

        raf.close(); // Fecha o arquivo após a leitura
    }

    private static void lerCapitulos(int[] ids) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");


        raf.seek(4); // Garante que começamos do início do arquivo

        while (raf.getFilePointer() < raf.length()) { // Continua enquanto houver dados no arquivo

            byte valido = raf.readByte(); // Lê o byte de validade (lápide)
            int tamanhoVetor = raf.readInt(); // Lê o tamanho do vetor (4 bytes)

            if (valido == 1) { // Se o registro for válido
                byte[] byteArray = new byte[tamanhoVetor];
                raf.readFully(byteArray);

                Capitulo capitulo = new Capitulo();
                capitulo.fromByteArray(byteArray); // Preenche o objeto Capitulo

                for (int id : ids) {
                    if (capitulo.getId() == id) {
                        MyIO.println(capitulo.toString()); // Exibe o capítulo encontrado
                    }
                }

            } else {
                // Pula os bytes do vetor caso seja inválido
                raf.skipBytes(tamanhoVetor);
            }
            
        }



        raf.close();
    }

    private static void atualizarCapitulo(int ID) throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");
        RAF.seek(4); // Pula o cabeçalho
    
        while (RAF.getFilePointer() < RAF.length()) {
            long posicao = RAF.getFilePointer();
            byte valido = RAF.readByte(); // Lê o byte de validade
    
            int tamanhoVetor = RAF.readInt(); // Lê o tamanho do vetor
    
            if (valido != 1) { // Se for um capítulo inválido, apenas pula os bytes do registro
                RAF.skipBytes(tamanhoVetor);
                continue;
            }
    
            byte[] byteArray = new byte[tamanhoVetor];
            RAF.readFully(byteArray);
    
            Capitulo capitulo = new Capitulo();
            capitulo.fromByteArray(byteArray);
    
            if (capitulo.getId() == ID) {
                Capitulo novoCapitulo = AuxFuncoes.CriarNovoCapitulo();
               novoCapitulo.setId(ID);

                byte[] novoByteArray = novoCapitulo.toByteArray();
    
                if (novoByteArray.length <= tamanhoVetor) {
                    MyIO.println("Atualização coube no espaço reservado");
                
                   
                    RAF.seek(posicao + 5); // Pula o byte de validade e o int de tamanho
                    RAF.write(novoByteArray); // Escreve os novos dados
    
                    RAF.write(new byte[tamanhoVetor - novoByteArray.length]);
                } else {
                    MyIO.println("Atualizaçao nao coube no espaco reservado, inserido no fim do arquivo");

                    RAF.seek(posicao);
                    RAF.writeByte(0);
                    AuxFuncoes.escreverCapitulo(novoByteArray, RAF.length());
                }
    
                MyIO.println("Capitulo atualizado com sucesso.");
                RAF.close();
                return;
            }
        }
    
        MyIO.println("Capitulo nao encontrado.");
        RAF.close();
    }
    
    

    private static void deletarCapitulo(int ID) throws IOException {
        RandomAccessFile RAF = new RandomAccessFile(BD, "rw");

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

                if (capitulo.getId() == ID) {
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
