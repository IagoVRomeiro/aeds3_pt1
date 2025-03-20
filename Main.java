import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException, IOException {
        // Importa o arquivo CSV para Binario e chama o M
        ImportadorCSV.importarCSVParaBinario();
        Menu.menu();
    }

}
