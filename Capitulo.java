
import java.io.*;

class Capitulo {

    protected int numCapitulo;
    protected int volume;
    protected String nome;
    protected String[] titulos;
    protected int qtdString;
    protected int paginas;
    protected String data;
    protected String episodio;

    public Capitulo(int numCapitulo, int volume, String nome, String[] titulos, int paginas, String data, String episodio) {;
        this.numCapitulo = numCapitulo;
        this.volume = volume;
        this.nome = nome;
        this.qtdString = titulos.length;
        this.titulos = titulos;
        this.paginas = paginas;
        this.data = data;
        this.episodio = episodio;
    }

    public Capitulo() {
        this.numCapitulo = -1;
        this.volume = -1;
        this.nome = "";
        this.titulos = new String[]{"", ""};
        this.qtdString = titulos.length;
        this.paginas = -1;
        this.data = "";
        this.episodio = "";
    }

    @Override
    public String toString() {
        return "NumeroCapitulo: " + numCapitulo
                + ", Volume: " + volume
                + ", Nome: " + nome
                + ", TituloOriginal: " + titulos[0]
                + ", TituloIngles: " + titulos[1]
                + ", QtdString: " + qtdString
                + ", Paginas: " + paginas
                + ", Data: " + data
                + ", Episodio: " + episodio;
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve os dados do registro
        dos.writeInt(numCapitulo);
        dos.writeInt(volume);
        dos.writeUTF(nome);
        dos.writeInt(qtdString);
        for (String titulo : titulos) {
            dos.writeUTF(titulo);
        }
        dos.writeInt(paginas);
        dos.writeUTF(data);
        dos.writeUTF(episodio);

        return baos.toByteArray();
    }

    public static Capitulo fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        byte valido = dis.readByte();
        int tamanhoVetor = dis.readInt();

        if (valido == 1) {
            MyIO.println("Tamanho do Vetor" + tamanhoVetor);
        }
        int numCapitulo = dis.readInt();
        int volume = dis.readInt();
        String nome = dis.readUTF();
        int qtdString = dis.readInt();
        String[] titulos = new String[qtdString];
        for (int i = 0; i < qtdString; i++) {
            titulos[i] = dis.readUTF();
        }
        int paginas = dis.readInt();
        String dataStr = dis.readUTF();
        String episodio = dis.readUTF();

        return new Capitulo(numCapitulo, volume, nome, titulos, paginas, dataStr, episodio);

    }

    public int getNumCapitulo() {
        return numCapitulo;
    }

    public int getVolume() {
        return volume;
    }

    public String getNome() {
        return nome;
    }

    public String[] getTitulos() {
        return titulos;
    }

    public int getQtdString() {
        return qtdString;
    }

    public int getPaginas() {
        return paginas;
    }

    public String getData() {
        return data;
    }

    public String getEpisodio() {
        return episodio;
    }

    public void setNumCapitulo(int numCapitulo) {
        this.numCapitulo = numCapitulo;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTitulos(String[] titulos) {
        this.titulos = titulos;
        this.qtdString = titulos.length;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }
}
