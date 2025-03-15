import java.io.*;


public class Menu {
    public static File Capitulos = new File("capitulos.db");
    
    public static void menu() {
            while (true) {
                System.out.println("\n--- Menu CRUD Capitulo ---");
                System.out.println("1. Criar Capitulo");
                System.out.println("2. Ler Capitulos");
                System.out.println("3. Atualizar Capitulo");
                System.out.println("4. Deletar Capitulo");
                System.out.println("5. Sair");
    
                System.out.print("Escolha uma opção: ");
                int opcao = MyIO.readInt();
                MyIO.readLine();
    
                try {
                    switch (opcao) {
                        case 1 -> criarCapitulo();
                        case 2 -> lerCapitulos();
                        case 3 -> atualizarCapitulo();
                        case 4 -> deletarCapitulo();
                        case 5 -> {
                            System.out.println("Saindo...");
                            System.exit(0);
                        }
                        default -> System.out.println("Opção inválida. Tente novamente.");
                    }
                } catch (IOException e) {
                    e.printStackTrace(); 
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }


    private static void criarCapitulo() throws IOException {
            System.out.println("\n--- Criar Novo Capitulo ---");
        
            System.out.print("Numero do Capitulo: ");
            int numero = MyIO.readInt();
            MyIO.readInt();
           
            System.out.print("Volume: ");
            int volume = MyIO.readInt();
        
            System.out.print("Nome: ");
            String nome = MyIO.readLine();
        
            System.out.print("Titulo Original: ");
            String tituloOriginal = MyIO.readLine();
        
            System.out.print("Titulo Ingles: ");
            String tituloIngles = MyIO.readLine();
        
            System.out.print("Paginas: ");
            int paginas = MyIO.readInt();
        
            System.out.print("Data: ");
            String data = MyIO.readLine();
        
            System.out.print("Episodio: ");
            int episodio = MyIO.readInt();
        
            String[] titulos = {tituloOriginal, tituloIngles};
            Capitulo capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);
        
            byte[] ba = capitulo.toByteArray();
        
            try (FileOutputStream arq = new FileOutputStream(Capitulos, true);
             DataOutputStream dos = new DataOutputStream(arq)) {
            dos.writeint(ba.length);
            dos.write(ba);
        }
    
        System.out.println("Capítulo salvo com sucesso!");
    }
    
    

    private static void lerCapitulos() throws IOException {
        File file = new File("capitulos.db");
    
    
        try (FileInputStream arq2 = new FileInputStream(file);
             DataInputStream dis = new DataInputStream(arq2)) {
    
            System.out.println("\n--- Lista de Capitulos ---");
    
            while (dis.available() > 0) {
                System.out.println("Bytes disponíveis: " + dis.available());
    
                int len = dis.readInt();
                System.out.println("Lendo capítulo com tamanho: " + len);
    
                byte[] ba = new byte[len];
                dis.readFully(ba);
    
                try {
                    Capitulo capitulo = Capitulo.fromByteArray(ba);
                    System.out.println("Capítulo lido: " + capitulo);
                } catch (Exception e) {
                    System.out.println("Erro ao converter bytes para Capitulo: " + e.getMessage());
                }
            }
        }
    }
    

    private static void atualizarCapitulo() throws IOException {
        System.out.println("\n--- Atualizar Capitulo ---");

        System.out.print("Numero do Capitulo para atualizar: ");
        int numero = MyIO.readInt();
        MyIO.readLine();

        if (!capituloExiste(numero)) {
            System.out.println("Erro: Capítulo " + numero + " não existe no banco de dados.");
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
            dis.readFully(ba);
  
            Capitulo capitulo = Capitulo.fromByteArray(ba);
            if (capitulo.getNumeroCapitulo() == numero) {
                encontrado = true;
                System.out.print("Novo nome: ");
                capitulo.setNome(MyIO.readLine());

                System.out.print("Novo Título Original: ");
                capitulo.setTituloOriginal(MyIO.readLine());

                System.out.print("Novo Título Ingles: ");
                capitulo.setTituloIngles(MyIO.readLine());

                System.out.print("Novo número de páginas: ");
                capitulo.setPaginas(MyIO.readLine());

                System.out.print("Nova data: ");
                capitulo.setData(MyIO.readLine());

                System.out.print("Novo episódio: ");
                capitulo.setEpisodio(MyIO.readLine());
            }

            byte[] updatedBa = capitulo.toByteArray();
            dos.writeint(updatedBa.length);
            dos.write(updatedBa);
        }

        if (!encontrado) {
            System.out.println("Capítulo não encontrado.");
        } else {
            try (FileOutputStream fos = new FileOutputStream(Capitulos);
                 DataOutputStream dosFinal = new DataOutputStream(fos)) {
                dosFinal.write(baos.toByteArray());
                System.out.println("Capítulo atualizado com sucesso.");
            }
        }
    }

    private static void deletarCapitulo() throws IOException {


        System.out.println("\n--- Deletar Capítulo ---");
    
        System.out.print("Número do Capítulo para deletar: ");
        int numero = MyIO.readInt();
        MyIO.readLine();  // Consome o caractere de nova linha após a leitura do número
    
    
        try (FileInputStream arq2 = new FileInputStream(Capitulos);
             DataInputStream dis = new DataInputStream(arq2);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {
    
            boolean encontrado = false;
    
            while (dis.available() > 0) {
                int len = dis.readInt(); // Lê o tamanho do capítulo
                
                // Verificação básica para evitar erro de leitura
                if (len <= 0 || len > dis.available()) {
                    System.out.println("ERRO: Tamanho inválido detectado, ignorando entrada corrompida.");
                    break;
                }
    
                byte[] ba = new byte[len];
                dis.readFully(ba);
    
                Capitulo capitulo = Capitulo.fromByteArray(ba);
                if (capitulo.getNumeroCapitulo() != numero) {
                    // Se o capítulo não for o que queremos deletar, escreve ele no novo arquivo
                    dos.writeint(len);
                    dos.write(ba);
                } else {
                    encontrado = true;
                }
            }
    
            if (encontrado) {
                // Se o buffer estiver vazio, significa que todos os capítulos foram deletados
                if (baos.size() == 0) {
                    if (arquivo.delete()) {
                        System.out.println("Capítulo deletado e banco de dados esvaziado.");
                    } else {
                        System.out.println("Erro ao esvaziar o banco de dados.");
                    }
                } else {
                    // Caso contrário, sobrescreve o arquivo com os capítulos restantes
                    try (FileOutputStream fos = new FileOutputStream(arquivo);
                         DataOutputStream dosFinal = new DataOutputStream(fos)) {
                        dosFinal.write(baos.toByteArray());
                        System.out.println("Capítulo deletado com sucesso.");
                    }
                }
            } else {
                System.out.println("Capítulo não encontrado.");
            }
        }
    }
    
   
}
