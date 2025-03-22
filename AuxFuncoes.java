import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuxFuncoes {

    // Separar campos do CSV
    public static String[] separarPorVirgula(String texto) {

        String regex = "\"([^\"]*)\"|([^,\"]*)";// Tratar campo string

        List<String> campos = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            // Verifica se o campo está entre aspas ou não
            String campo = null;
            if (matcher.group(1) != null) {
                campo = matcher.group(1); // Campo entre aspas
            } else if (matcher.group(2) != null) {
                campo = matcher.group(2); // Campo simples
            }

            // Adiciona o campo ao resultado apenas se não for vazio
            if (campo != null && !campo.isEmpty()) {
                campos.add(campo);
            }
        }

        return campos.toArray(new String[0]);
    }

    // Formatar data para dd/mm/aaaa
    public static String formatarData(String data) throws ParseException {

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatoEntrada.parse(data);
        return formatoSaida.format(date);

    }

    // Perguta Quantidade de Ids, em seguida preenche um vetor com os ids desejados
    public static int[] PerguntaQTD_ID() throws IOException {
        RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw");

        RAF.seek(0);
        int ultimoId = RAF.readInt();

        System.out.println("\nDigite a quantidade de capitulos que deseja pesquisar: ");
        int qtdIds = MyIO.readInt();
        int[] ids = new int[qtdIds];

        for (int i = 0; i < qtdIds; i++) {
            do {
                MyIO.println("Qual o ID do capitulo?");
                ids[i] = MyIO.readInt();

                if (ids[i] > ultimoId) {
                    MyIO.println("ID invalido. O ultimo ID registrado é " + ultimoId + ". Digite novamente.");
                }
            } while (ids[i] > ultimoId);
        }
        RAF.close();
        return ids;
    }

    // Reescreve ultimo id inserido
    public static void ReescreverUltimoIdInserido() throws IOException {
        RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw");

        RAF.seek(0);
        int ultimoID = RAF.readInt();

        RAF.seek(0);
        RAF.writeInt(ultimoID + 1);

        RAF.close();
    }

    // Coloca o array de bytes no db
    public static void escreverCapitulo(byte[] dataBytes, long lugar) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("dataset/capitulos.db", "rw");
        // Posiciona o ponteiro
        raf.seek(lugar);

        // Marca como válido
        raf.writeByte(1);

        // Escreve o tamanho do array
        
            raf.writeInt(dataBytes.length);
    
      
        // Escreve os dados binários do capítulo
        raf.write(dataBytes);
        raf.close();
    }

    // Pergunta qual ID
    public static int qualID() {

        MyIO.println("Qual o ID?");
        int i = MyIO.readInt();
        return i;
    }

    // Cria e retorna um objeto capitulo
    public static Capitulo CriarNovoCapitulo() throws IOException {
        try (RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw")) {
            int UltimoId = 0;

            RAF.seek(0);
            UltimoId = RAF.readInt();

            // Coleta dos dados do novo capítulo

            int id = UltimoId + 1;

            MyIO.print("(int) Capitulo: ");
            Short numCapitulo = (short) MyIO.readInt();

            MyIO.print("(int) Volume: ");
            Short volume = (short) MyIO.readInt();

            MyIO.print("(String) Nome: ");
            String nome = MyIO.readLine();

            MyIO.print("(String) Titulo Original: ");
            String tituloOriginal = MyIO.readLine();

            MyIO.print("(String) Titulo Ingles: ");
            String tituloIngles = MyIO.readLine();

            MyIO.print("(int) Paginas: ");
            Short paginas = (short) MyIO.readInt();

            MyIO.print("(xx/xx/xxxx) Data: ");
            String data = MyIO.readLine();

            MyIO.print("(String) Episodio: ");
            String episodio = MyIO.readLine();

            String[] titulos = { tituloOriginal, tituloIngles };

            return new Capitulo(id, numCapitulo, volume, nome, titulos, paginas, data, episodio);
        }
    }

}
