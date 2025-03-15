import java.io.*;

public class ImportadorCSV {

    public static void importarCSVParaBinario() {
        String csv = "dataset/capitulos.csv";
        String binario = "dataset/capitulos.db";

        try (BufferedReader br = new BufferedReader(new FileReader(csv));
             FileOutputStream fos = new FileOutputStream(binario)) {

            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] infos = linha.split(",");

                if (infos.length < 8) {
                    System.err.println("Linha inválida: " + linha);
                    continue;
                }
        
                    int numeroCapitulo = Integer.parseInt(infos[0]);
                    int volume = Integer.parseInt(FormatarString(infos[1]));
                    String nome = FormatarString(infos[2]);
                    String[] titulos = {FormatarString(infos[3]), FormatarString(infos[4])};
                    int paginas = Integer.parseInt(FormatarString(infos[5]));
                    String data = infos[6];
                    String episodio = infos[7];

                    Capitulo capitulo = new Capitulo(numeroCapitulo, volume, nome, titulos, paginas, data, episodio);
                    byte[] dataBytes = capitulo.toByteArray();
                    fos.write(dataBytes);
                
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    public static String FormatarString(String linha) {
        if (linha == null) {
            return null;
        }
        return linha.replace(",", "").replace("\"", "");
    }
}

