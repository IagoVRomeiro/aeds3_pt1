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

    public Capitulo(int numCapitulo, int volume, String nome, String[] titulos, int paginas, String data, String episodio) {
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
        return "NumeroCapitulo: " + numCapitulo +
               ", Volume: " + volume +
               ", Nome: " + nome +  
               ", TituloOriginal: " + titulos[0] +
               ", TituloIngles: " + titulos[1] +
               ", QtdString: " + qtdString +
               ", Paginas: " + paginas +
               ", Data: " + data +
               ", Episodio: " + episodio;
    }

    public byte[] toByteArray() throws IOException {
        // Cria o fluxo para escrever os dados binários
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve a Lápide (1 byte) - 1 para válido, 0 para excluído
        dos.writeByte(1);

        // Prepara o vetor de bytes dos dados (dados do registro)
        ByteArrayOutputStream recordStream = new ByteArrayOutputStream();
        DataOutputStream recordDos = new DataOutputStream(recordStream);

        // Escreve os dados do registro
        recordDos.writeInt(numCapitulo);
        recordDos.writeInt(volume);
        recordDos.writeUTF(nome);
        recordDos.writeInt(qtdString);
        for (String titulo : titulos) {
            recordDos.writeUTF(titulo);
        }
        recordDos.writeInt(paginas);
        recordDos.writeUTF(data);
        recordDos.writeUTF(episodio);

        // Grava o tamanho do vetor de bytes (indicador de tamanho do registro)
        byte[] recordBytes = recordStream.toByteArray();
        dos.writeInt(recordBytes.length); // Tamanho do vetor de bytes

        // Grava o vetor de bytes do registro
        dos.write(recordBytes);

        return baos.toByteArray();
    }
    
    public static Capitulo fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
    
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
