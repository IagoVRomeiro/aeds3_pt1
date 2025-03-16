
import java.io.*;

public class ImportadorCSV {

    public static void importarCSVParaBinario() throws IOException {
        String csv = "dataset/capitulos.csv";
        String binario = "dataset/capitulos.db";

        try (BufferedReader br = new BufferedReader(new FileReader(csv)); FileOutputStream fos = new FileOutputStream(binario)) {

            String linha;

            while ((linha = br.readLine()) != null) {
             

                // Usando a função de formatação para processar a linha
                String[] campos = AuxFuncoes.separarPorVirgula(linha);

      
                int numeroCapitulo = Integer.parseInt(campos[0]);
                int volume = Integer.parseInt(campos[1]);
                String nome = campos[2];
                String[] titulos = {campos[3], campos[4]};
                int paginas = Integer.parseInt(campos[5]);
                String data = AuxFuncoes.formatarData(campos[6]);
                String episodio = campos[7];


                Capitulo capitulo = new Capitulo(numeroCapitulo, volume, nome, titulos, paginas, data, episodio);
                byte[] dataBytes = capitulo.toByteArray();
                fos.write(dataBytes);

            }

        }

    }
}
