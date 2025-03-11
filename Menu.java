import java.io.*;
import java.util.Scanner;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String Capitulos = "capitulos.db";
    public static void menu() {
        while (true) {
            System.out.println("\n--- Menu CRUD Capitulo ---");
            System.out.println("1. Criar Capitulo");
            System.out.println("2. Ler Capitulos");
            System.out.println("3. Atualizar Capitulo");
            System.out.println("4. Deletar Capitulo");
            System.out.println("5. Sair");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

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
        int numero = scanner.nextInt();
        scanner.nextLine();

        if (capituloExiste(numero)) {
            System.out.println("Erro: Capítulo " + numero + " já existe no banco de dados.");
            return;
        }
    
        System.out.print("Volume: ");
        String volume = scanner.nextLine();
    
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
    
        System.out.print("Titulo Original: ");
        String tituloOriginal = scanner.nextLine();
    
        System.out.print("Titulo Ingles: ");
        String tituloIngles = scanner.nextLine();
    
        System.out.print("Paginas: ");
        String paginas = scanner.nextLine();
    
        System.out.print("Data: ");
        String data = scanner.nextLine();
    
        System.out.print("Episodio: ");
        String episodio = scanner.nextLine();
    
        String[] titulos = {tituloOriginal, tituloIngles};
        Capitulo capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);
    
        byte[] ba = capitulo.toByteArray();
    
        try (FileOutputStream arq = new FileOutputStream(Capitulos, true);
             DataOutputStream dos = new DataOutputStream(arq)) {
            dos.writeInt(ba.length);
            dos.write(ba);
        }
    
        System.out.println("Capitulo salvo com sucesso!");
    }
    

    private static void lerCapitulos() throws IOException {
        File file = new File("capitulos.db");
    
        if (!file.exists() || file.length() == 0) {
            System.out.println("\nNenhum capítulo encontrado.");
            return;
        }
    
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
        int numero = scanner.nextInt();
        scanner.nextLine();

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
                capitulo.setNome(scanner.nextLine());

                System.out.print("Novo Título Original: ");
                capitulo.setTituloOriginal(scanner.nextLine());

                System.out.print("Novo Título Ingles: ");
                capitulo.setTituloIngles(scanner.nextLine());

                System.out.print("Novo número de páginas: ");
                capitulo.setPaginas(scanner.nextLine());

                System.out.print("Nova data: ");
                capitulo.setData(scanner.nextLine());

                System.out.print("Novo episódio: ");
                capitulo.setEpisodio(scanner.nextLine());
            }

            byte[] updatedBa = capitulo.toByteArray();
            dos.writeInt(updatedBa.length);
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
        System.out.println("\n--- Deletar Capitulo ---");
    
        System.out.print("Número do Capítulo para deletar: ");
        int numero = scanner.nextInt();
        scanner.nextLine();  // Consome o caractere de nova linha após a leitura do número
    
        if (!capituloExiste(numero)) {
            System.out.println("Erro: Capítulo " + numero + " não existe no banco de dados.");
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
                    System.out.println("ERRO: Dados insuficientes para ler o tamanho do capítulo.");
                    break;
                }
    
                int len = dis.readInt();
                System.out.println("Lendo capítulo com tamanho: " + len);
    
                if (len > dis.available()) {
                    System.out.println("ERRO: Tamanho do capítulo maior que os bytes disponíveis.");
                    break;
                }
    
                byte[] ba = new byte[len];
                dis.readFully(ba);
    
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
                    System.out.println("Capítulo deletado com sucesso.");
                }
            } else {
                System.out.println("Capítulo não encontrado.");
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
                dis.readFully(ba); // Lê os bytes do capítulo
    
                // Converter os bytes para um objeto Capitulo e verificar o número
                Capitulo capitulo = Capitulo.fromByteArray(ba);
                if (capitulo.getNumeroCapitulo() == numeroCapitulo) {
                    return true; // O capítulo já existe
                }
            }
        }
        return false; // Não encontrou o capítulo
    }
}
