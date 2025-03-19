
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuxFuncoes {

    public static String[] separarPorVirgula(String texto) {
        // Regex para capturar campos entre aspas ou campos simples
        String regex = "\"([^\"]*)\"|([^,\"]*)";

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

        // Convertendo a lista para um array
        return campos.toArray(new String[0]);
    }

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

        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatoEntrada.parse(data);
            return formatoSaida.format(date);
        } catch (ParseException e) {
            return data; // Retorna a entrada original se a conversão falhar
        }
    }

    public static int[] PerguntaQTD() {

        System.out.println("\nDigite a quantidade de capitulos que deseja pesquisar: ");
        int qtdIds = MyIO.readInt();
        int[] ids = new int[qtdIds];
        for (int i = 0; i < qtdIds; i++) {
            MyIO.println("Qual o Id do capitulo?");
            ids[i] = MyIO.readInt();
        }

        return ids;
    }

    public static void ReescreverUltimoIdInserido(Capitulo capitulo) throws IOException {
        RandomAccessFile RAF = new RandomAccessFile("dataset/capitulos.db", "rw");

        RAF.seek(0);
        int ultimoId = RAF.readInt();

        // Se o novo ID for maior, reescreve
        if (capitulo.getNumCapitulo() > ultimoId) {
            RAF.seek(0); // Volta para o início para sobrescrever
            RAF.writeInt(capitulo.getNumCapitulo());
        }

        RAF.close(); // Garante que o arquivo será fechado
    }

    public static void escreverCapitulo(RandomAccessFile raf, byte[] dataBytes) throws IOException {
        raf.seek(raf.length()); // Posiciona no final do arquivo para inserir o novo capítulo
        // Marca como válido
        raf.writeByte(1);

        // Escreve o tamanho do array
        raf.writeInt(dataBytes.length);

        // Escreve os dados binários do capítulo
        raf.write(dataBytes);
    }

}
