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
    public static String formatarData(String data) {
        if (data == null) {
            return null;
        }

        // Ajusta espaços extras e remove vírgulas desnecessárias
        data = data.trim().replace(",", "");

        // Regex corrigida para permitir vírgula opcional após o dia
        String regexData = "^[A-Za-z]+ \\d{1,2} \\d{4}$";
        if (!data.matches(regexData)) {
            return data;
        }

        try { // Formatar data de mm/dd/yyyy para dd/mm/aaaa
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatoEntrada.parse(data);
            return formatoSaida.format(date);
        } catch (ParseException e) {
            return data; // Retorna a entrada original se a conversão falhar
        }
    }

    // Perguta Quantidade de Ids, em seguida preenche um vetor com os ids desejados
    public static int[] PerguntaQTD_ID() {
        int ultimoId = 0;

        try (RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw")) {
            if (RAF.length() >= 4) {
                RAF.seek(0);
                ultimoId = RAF.readInt();
            }
        } catch (IOException e) {
            System.err.println("Erro ao acessar o arquivo: " + e.getMessage());
            return new int[0]; // Retorna um array vazio se houver erro
        }

        System.out.println("\nDigite a quantidade de capítulos que deseja pesquisar: ");
        int qtdIds = MyIO.readInt();
        int[] ids = new int[qtdIds];

        for (int i = 0; i < qtdIds; i++) {
            do {
                MyIO.println("Qual o ID do capítulo?");
                ids[i] = MyIO.readInt();

                if (ids[i] > ultimoId) {
                    MyIO.println("ID inválido. O último ID registrado é " + ultimoId + ". Digite novamente.");
                }
            } while (ids[i] > ultimoId);
        }

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

    // Inseri o array de bytes no final do arquivo .db
    public static void escreverCapitulo_FIM(RandomAccessFile raf, byte[] dataBytes) throws IOException {

        // Posiciona o ponteiro no final do arquivo para inserir o novo capítulo
        raf.seek(raf.length());

        // Marca como válido
        raf.writeByte(1);

        // Escreve o tamanho do array
        raf.writeInt(dataBytes.length);

        // Escreve os dados binários do capítulo
        raf.write(dataBytes);
    }

    // Pergunta qual ID
    public static int qualCapitulo() {

        MyIO.println("Qual o Capitulo?");
        int i = MyIO.readInt();
        return i;
    }

    // Cria e retorna um objeto capitulo
    public static Capitulo CriarCapitulo() throws IOException {
        try (RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw")) {
            int UltimoId = 0;

            RAF.seek(0);
            UltimoId = RAF.readInt();

            // Coleta dos dados do novo capítulo

            int id = UltimoId + 1;

            MyIO.print("(int) Capitulo: ");
            int numCapitulo = MyIO.readInt();

            MyIO.print("(int) Volume: ");
            int volume = MyIO.readInt();

            MyIO.print("(String) Nome: ");
            String nome = MyIO.readLine();

            MyIO.print("(String) Título Original: ");
            String tituloOriginal = MyIO.readLine();

            MyIO.print("(String) Título Inglês: ");
            String tituloIngles = MyIO.readLine();

            MyIO.print("(int) Páginas: ");
            int paginas = MyIO.readInt();

            MyIO.print("(xx/xx/xxxx) Data: ");
            String data = MyIO.readLine();

            MyIO.print("(String) Episódio: ");
            String episodio = MyIO.readLine();

            String[] titulos = { tituloOriginal, tituloIngles };

            return new Capitulo(id, numCapitulo, volume, nome, titulos, paginas, data, episodio);
        }
    }

}
