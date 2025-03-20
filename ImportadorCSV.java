
import java.io.*;

public class ImportadorCSV {

    public static void importarCSVParaBinario() throws IOException {
        String csv = "dataset/capitulos.csv";
        String binario = "dataset/capitulos.db";

        // Abre o arquivo CSV para leitura e o arquivo binário para escrita
        BufferedReader br = new BufferedReader(new FileReader(csv));
        RandomAccessFile raf = new RandomAccessFile(binario, "rw");

        String linha;

        // Reserva 4 bytes para o último ID inserido (inicializa com 0)
        raf.writeInt(0);

        while ((linha = br.readLine()) != null) {
            String[] campos = AuxFuncoes.separarPorVirgula(linha);

            // Preenche os campos com os dados separados por ','

            int numeroCapitulo = Integer.parseInt(campos[0]);
            int id = numeroCapitulo;
            int volume = Integer.parseInt(campos[1]);
            String nome = campos[2];
            String[] titulos = { campos[3], campos[4] };
            int paginas = Integer.parseInt(campos[5]);
            String data = AuxFuncoes.formatarData(campos[6]);
            String episodio = campos[7];

            // Criação do objeto Capitulo
            Capitulo capitulo = new Capitulo(id, numeroCapitulo, volume, nome, titulos, paginas, data, episodio);
            byte[] dataBytes = capitulo.toByteArray();

            // Chama a função para escrever no arquivo binário
            AuxFuncoes.escreverCapitulo_FIM(raf, dataBytes);
            AuxFuncoes.ReescreverUltimoIdInserido();
        }

        // Fecha os recursos abertos
        br.close();
        raf.close();
    }
}
