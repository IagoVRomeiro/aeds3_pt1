import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Leitura {

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