import java.io.*;

public class Menu {

   
    private static final String Capitulos = "capitulos.db";
    public static void menu() {
        while (true) {
            MyIO.println("\n--- Menu CRUD Capitulo ---");
            MyIO.println("1. Criar Capitulo");
            MyIO.println("2. Ler Capitulos");
            MyIO.println("3. Atualizar Capitulo");
            MyIO.println("4. Deletar Capitulo");
            MyIO.println("5. Sair");

            MyIO.print("Escolha uma opção: ");
            int opcao = MyIO.readInt();
            

            try {
                switch (opcao) {
                    case 1 -> criarCapitulo();
                    case 2 -> Leitura.lerCapitulos();
                    case 3 -> atualizarCapitulo();
                    case 4 -> deletarCapitulo();
                    case 5 -> {
                        MyIO.println("Saindo...");
                        System.exit(0);
                    }
                    default -> MyIO.println("Opção inválida. Tente novamente.");
                }
            } catch (IOException e) {
                e.printStackTrace(); 
                MyIO.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void criarCapitulo() throws IOException {
        MyIO.println("\n--- Criar Novo Capitulo ---");
    
        MyIO.print("Numero do Capitulo: ");
        Short numero = Short.parseShort(MyIO.readString());
        

        if (capituloExiste(numero)) {
            MyIO.println("Erro: Capítulo " + numero + " já existe no banco de dados.");
            return;
        }
    
        MyIO.print("Volume: ");
        Short volume = Short.parseShort(MyIO.readString());
    
        MyIO.print("Nome: ");
        String nome = MyIO.readString();
    
        MyIO.print("Titulo Original: ");
        String tituloOriginal = MyIO.readString();
    
        MyIO.print("Titulo Ingles: ");
        String tituloIngles = MyIO.readString();
    
        MyIO.print("Paginas: ");
        Short paginas = Short.parseShort(MyIO.readString());
    
        MyIO.print("Data: ");
        String data = MyIO.readString();
    
        MyIO.print("Episodio: ");
        String episodio = MyIO.readString();
    
        String[] titulos = {tituloOriginal, tituloIngles};
        Capitulo capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);
    
        byte[] ba = capitulo.toByteArray();
    
        try (FileOutputStream arq = new FileOutputStream(Capitulos, true);
             DataOutputStream dos = new DataOutputStream(arq)) {
            dos.writeInt(ba.length);
            dos.write(ba);
        }
    
        MyIO.println("Capitulo salvo com sucesso!");
    }

    private static void atualizarCapitulo() throws IOException {
        MyIO.println("\n--- Atualizar Capitulo ---");

        MyIO.print("Numero do Capitulo para atualizar: ");
        int numero = MyIO.readInt();
        

        if (!capituloExiste(numero)) {
            MyIO.println("Erro: Capítulo " + numero + " não existe no banco de dados.");
            return;
        }

        FileInputStream arq2 = new FileInputStream(Capitulos);
        DataInputStream dis = new DataInputStream(arq2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        boolean encontrado = false;
        while (dis.available() > 0) {
            int len = dis.readInt();
            byte[] ba = new byte[len];
            dis.read(ba);

            Capitulo capitulo = Capitulo.fromByteArray(ba);
            if (capitulo.getNumeroCapitulo() == numero) {
                encontrado = true;
                MyIO.print("Novo nome: ");
                capitulo.setNome(MyIO.readString());

                MyIO.print("Novo Título Original: ");
                capitulo.setTituloOriginal(MyIO.readString());

                MyIO.print("Novo Título Ingles: ");
                capitulo.setTituloIngles(MyIO.readString());

                MyIO.print("Novo número de páginas: ");
                capitulo.setPaginas(Short.parseShort(MyIO.readString()));

                MyIO.print("Nova data: ");
                capitulo.setData(MyIO.readString());

                MyIO.print("Novo episódio: ");
                capitulo.setEpisodio(MyIO.readString());
            }

            byte[] updatedBa = capitulo.toByteArray();
            dos.writeInt(updatedBa.length);
            dos.write(updatedBa);
        }

        if (!encontrado) {
            MyIO.println("Capítulo não encontrado.");
        } else {
            try (FileOutputStream fos = new FileOutputStream(Capitulos);
                 DataOutputStream dosFinal = new DataOutputStream(fos)) {
                dosFinal.write(baos.toByteArray());
                MyIO.println("Capítulo atualizado com sucesso.");
            }
        }
    }

    private static void deletarCapitulo() throws IOException {
        MyIO.println("\n--- Deletar Capitulo ---");
    
        MyIO.print("Número do Capítulo para deletar: ");
        Short numero = Short.parseShort(MyIO.readString());
         
        if (!capituloExiste(numero)) {
            MyIO.println("Erro: Capítulo " + numero + " não existe no banco de dados.");
            return;
        }
    
        try (FileInputStream arq2 = new FileInputStream(Capitulos);
             DataInputStream dis = new DataInputStream(arq2);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {
    
            boolean encontrado = false;
    
            while (dis.available() > 0) {
                // Verifica se há dados suficientes para ler o tamanho do capítulo
                if (dis.available() < 4) {  // Se houver menos de 4 bytes restantes, não é possível ler um int
                    MyIO.println("ERRO: Dados insuficientes para ler o tamanho do capítulo.");
                    break;
                }
    
                int len = dis.readInt();
                MyIO.println("Lendo capítulo com tamanho: " + len);
    
                if (len > dis.available()) {
                    MyIO.println("ERRO: Tamanho do capítulo maior que os bytes disponíveis.");
                    break;
                }
    
                byte[] ba = new byte[len];
                dis.read(ba);
    
                Capitulo capitulo = Capitulo.fromByteArray(ba);
                if (capitulo.getNumeroCapitulo() != numero) {
                    // Se o capítulo não for o que queremos deletar, escreve ele no novo arquivo
                    byte[] updatedBa = capitulo.toByteArray();
                    dos.writeInt(updatedBa.length);
                    dos.write(updatedBa);
                } else {
                    encontrado = true;
                }
            }
    
            if (encontrado) {
                // Se o capítulo foi encontrado, escreve os capítulos restantes de volta no arquivo
                try (FileOutputStream fos = new FileOutputStream(Capitulos);
                     DataOutputStream dosFinal = new DataOutputStream(fos)) {
                    dosFinal.write(baos.toByteArray());
                    MyIO.println("Capítulo deletado com sucesso.");
                }
            } else {
                MyIO.println("Capítulo não encontrado.");
            }
        }
    }
    
    private static boolean capituloExiste(int numeroCapitulo) throws IOException {
        File arquivo = new File(Capitulos);
        if (!arquivo.exists()) {
            return false; // Se o arquivo não existe, o capítulo não pode existir
        }
    
        try (FileInputStream arq = new FileInputStream(arquivo);
             DataInputStream dis = new DataInputStream(arq)) {
            while (dis.available() > 0) {
                int tamanho = dis.readInt(); // Lê o tamanho do próximo capítulo
                byte[] ba = new byte[tamanho];
                dis.read(ba); // Lê os bytes do capítulo
    
                // Converter os bytes para um objeto Capitulo e verificar o número
                Capitulo capitulo = Capitulo.fromByteArray(ba);
                if (capitulo.getNumeroCapitulo() == numeroCapitulo) {
                    return true; // O capítulo já existe
                }
            }
        }
        return false; // Não encontrou o capítulo
    }

    public static void lerCapitulos() throws IOException {

        MyIO.println("\n--- Menu Leitura ---");
        MyIO.println("1. Imprimir pelo número do Capitulo");
        MyIO.println("2. Imprimir todos os Capitulos");

        MyIO.print("Escolha uma opção: ");
        int opcao = MyIO.readInt();

        if (opcao == 1) {
            MyIO.println("Escreva o número do Capitulo");
            int op = MyIO.readInt();
            lerCapituloPorCapitulo(op);
        } else if (opcao == 2) {
            imprimirTodosCapitulos();
        }
    }

public static void imprimirTodosCapitulos() throws IOException {

    File binario = new File("capitulos.db");

    try (FileInputStream arq2 = new FileInputStream(binario);
            DataInputStream dis = new DataInputStream(arq2)) {

        MyIO.println("\n--- Lista de Todos os Capitulos ---");

        // Percorre todos os capítulos no arquivo binário
        while (dis.available() > 0) {
            // Lê o tamanho do vetor (primeiro valor de cada registro)
            int len = dis.readInt();

            // Lê o vetor (capítulo) como um array de bytes com o tamanho informado por 'len'
            byte[] ba = new byte[len];
            dis.read(ba); // Preenche o array de bytes com os dados do capítulo

            // Converte os bytes lidos em um objeto Capítulo
            Capitulo capitulo = Capitulo.fromByteArray(ba);

            // Imprime as informações do capítulo
            MyIO.println("Capítulo encontrado: " + capitulo.toString());
        }

    } catch (IOException e) {
        MyIO.println("Erro ao ler o arquivo binário: " + e.getMessage());
    }
}

public static void lerCapituloPorCapitulo(int numeroRegistro) throws IOException {
    File binario = new File("capitulos.db");

    try (FileInputStream arq2 = new FileInputStream(binario);
            DataInputStream dis = new DataInputStream(arq2)) {

        MyIO.println("\n--- Leitura do Capítulo por ID ---");

        // Percorrendo os capítulos até o ID desejado
        while (dis.available() > 0) {
            // Lê o tamanho do vetor (primeiro valor do registro)
            int len = dis.readInt();

            // Lê o vetor como um array de bytes com o tamanho informado por 'len'
            byte[] ba = new byte[len];
            dis.read(ba); // Preenche o array de bytes com os dados do capítulo

            // Converte os bytes lidos em um objeto Capítulo
            Capitulo capitulo = Capitulo.fromByteArray(ba);

            // Verifica se o ID do capítulo corresponde ao número de registro
            if (capitulo.getNumeroCapitulo() == numeroRegistro) {
                MyIO.println("Capítulo encontrado: " + capitulo);
                return; // Encontrado, encerra a busca
            }
        }

        MyIO.println("Capítulo com ID " + numeroRegistro + " não encontrado.");
    }
}

}
