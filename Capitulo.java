
import java.io.*;

class Capitulo {
    protected int id;
    protected Short numCapitulo;
    protected Short volume;
    protected String nome;
    protected String[] titulos;
    protected byte qtdString;
    protected Short paginas;
    protected String data;
    protected String episodio;

    // Construtores, ToString, ToByteArray, FromByteArray, Gets, Sets.
    public Capitulo(int id, Short numCapitulo, Short volume, String nome, String[] titulos, Short paginas, String data,
            String episodio) throws IOException {
        this.id = id;
        this.numCapitulo = numCapitulo;
        this.volume = volume;
        this.nome = nome;
        this.qtdString = (byte)titulos.length;
        this.titulos = titulos;
        this.paginas = paginas;
        this.data = data;
        this.episodio = episodio;

    }

    public Capitulo() {
        this.id = -1;
        this.numCapitulo = -1;
        this.volume = -1;
        this.nome = "";
        this.titulos = new String[] { "", "" };
        this.qtdString = (byte)titulos.length;
        this.paginas = -1;
        this.data = "";
        this.episodio = "";
    }

    @Override
    public String toString() {
        return "ID: " + id 
                + "\n Numero do Capitulo: " + numCapitulo
                + "\n Volume: " + volume
                + "\n Nome: " + nome
                + "\n TituloOriginal: " + titulos[0]
                + "\n TituloIngles: " + titulos[1]
                + "\n Quantidade de Titulos: " + qtdString
                + "\n Paginas: " + paginas
                + "\n Data: " + data
                + "\n Episodio: " + episodio;
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve os dados do registro
        dos.writeInt(id);
        dos.writeShort(numCapitulo);
        dos.writeShort(volume);
        dos.writeUTF(nome);
        dos.writeByte(qtdString);
        for (String titulo : titulos) {
            dos.writeUTF(titulo);
        }
        dos.writeShort(paginas);
        dos.writeUTF(data);
        dos.writeUTF(episodio);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.numCapitulo = dis.readShort();
        this.volume = dis.readShort();
        this.nome = dis.readUTF();
        this.qtdString = dis.readByte();
        this.titulos = new String[qtdString];
        for (byte i = 0; i < qtdString; i++) {
            this.titulos[i] = dis.readUTF();
        }
        this.paginas = dis.readShort();
        this.data = dis.readUTF();
        this.episodio = dis.readUTF();

        bais.close();
        dis.close();
    }

    public int getId() {
        return id;
    }

    public Short getNumCapitulo() {
        return numCapitulo;
    }

    public Short getVolume() {
        return volume;
    }

    public String getNome() {
        return nome;
    }

    public String[] getTitulos() {
        return titulos;
    }

    public Byte getQtdString() {
        return qtdString;
    }

    public Short getPaginas() {
        return paginas;
    }

    public String getData() {
        return data;
    }

    public String getEpisodio() {
        return episodio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumCapitulo(Short numCapitulo) {
        this.numCapitulo = numCapitulo;

    }

    public void setVolume(Short volume) {
        this.volume = volume;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTitulos(String[] titulos) {
        this.titulos = titulos;
        this.qtdString = (byte) titulos.length;
    }

    public void setPaginas(Short paginas) {
        this.paginas = paginas;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }
}
