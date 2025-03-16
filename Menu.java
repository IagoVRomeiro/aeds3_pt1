import java.io.*;


public class Menu {
    public static File Capitulos = new File("dataset/capitulos.db");
    
    public static void menu() throws IOException {
            while (true) {
                System.out.println("\n--- Menu CRUD Capitulo ---");
                System.out.println("1. Criar Capitulo");
                System.out.println("2. Ler Capitulos");
                System.out.println("3. Atualizar Capitulo");
                System.out.println("4. Deletar Capitulo");
                System.out.println("5. Sair");
    
                System.out.print("Escolha uma opção: ");
                int opcao = MyIO.readInt();

              
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
                }
            }  
              

        private static void criarCapitulo() throws IOException {
            System.out.println("\n--- Criar Novo Capitulo ---");
    
            System.out.print("(int) Numero do Capitulo: ");
            int numero = MyIO.readInt();
            
            System.out.print("(int) Volume: ");
            int volume = MyIO.readInt();
    
            System.out.print("(String) Nome: ");
            String nome = MyIO.readLine();
    
            System.out.print("(String) Titulo Original: ");
            String tituloOriginal = MyIO.readLine();
    
            System.out.print("(String) Titulo Ingles: ");
            String tituloIngles = MyIO.readLine();
    
            System.out.print("(int) Paginas: ");
            int paginas = MyIO.readInt();
    
            System.out.print("(String) Data: ");
            String data = MyIO.readLine();
    
            System.out.print("(String) Episodio: ");
            String episodio = MyIO.readLine();
    
            String[] titulos = {tituloOriginal, tituloIngles};
            Capitulo Capitulo = new Capitulo(numero, volume, nome, titulos, paginas, data, episodio);
    
            byte[] bytes = Capitulo.toByteArray();
    
            FileOutputStream arq = new FileOutputStream(Capitulos, true);
            DataOutputStream dos = new DataOutputStream(arq);
   
            dos.write(bytes);
            
    
            System.out.println("Capítulo salvo com sucesso!");
        }
    
    

    private static void lerCapitulos() throws IOException {
        

    }
    
    private static void atualizarCapitulo() throws IOException {
       
    }

    private static void deletarCapitulo() throws IOException {

    }
    
   
}
