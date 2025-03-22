import java.io.*;

public class ImportadorCSV {

    public static void importarCSVParaBinario() {
        String csv = "dataset/capitulos.csv";
        String binario = "capitulos.db";

        try (BufferedReader br = new BufferedReader(new FileReader(csv));
             FileOutputStream fos = new FileOutputStream(binario)) {

            String linha;
            
            while ((linha = br.readLine()) != null) {


                if (linha.trim().isEmpty()) {
                    continue;
                }

                
                String[] infos = linha.split(",");
            

                
                Short numeroCapitulo = Short.parseShort(infos[0]);
                Short volume = Short.parseShort(infos[1]);
                String nome = infos[2];
                String[] titulos = {infos[3], infos[4]};
                Short paginas = Short.parseShort(infos[5]);
                String data = infos[6];
                String episodio = infos[7];

                Capitulo capitulo = new Capitulo(numeroCapitulo, volume, nome, titulos, paginas, data, episodio);

                
                byte[] dataBytes = capitulo.toByteArray();
                fos.write(dataBytes);
            }

        } catch (IOException e) {
            e.getMessage();
        }
    }
}