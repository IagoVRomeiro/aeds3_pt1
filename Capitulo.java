import java.io.*;

class Capitulo {
    
    protected Short NumeroCapitulo;
    protected Short Volume;
    protected String Nome;
    protected String[] Titulos; // Vetor para TituloOriginal e TituloIngles
    protected Short Paginas;
    protected String Data;
    protected String Episodio;

    public Capitulo(Short a, Short b, String c, String[] d, Short e, String f, String g) {
        NumeroCapitulo = a;
        Volume = b;
        Nome = c;  
        Titulos = d;
        Paginas = e;
        Data = f;
        Episodio = g;
    }

    public Capitulo() {
        NumeroCapitulo = -1;
        Volume = -1;
        Nome = "";  
        Titulos = new String[]{"", ""};
        Paginas = -1;
        Data = "";
        Episodio = "";
    }

    @Override
    public String toString() {
        return "\nNumeroCapitulo: " + NumeroCapitulo +
               "\nVolume: " + Volume +
               "\nNome: " + Nome +  
               "\nTituloOriginal: " + Titulos[0] +
               "\nTituloIngles: " + Titulos[1] +
               "\nPaginas: " + Paginas +
               "\nData: " + Data +
               "\nEpisodio: " + Episodio;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeShort(NumeroCapitulo);
        dos.writeShort(Volume);
        dos.writeUTF(Nome);
        dos.writeUTF(Titulos[0]);
        dos.writeUTF(Titulos[1]);
        dos.writeShort(Paginas);
        dos.writeUTF(Data);
        dos.writeUTF(Episodio);
        
        return baos.toByteArray();
    }
    
    public static Capitulo fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
    
        Short NumeroCapitulo = dis.readShort();
        Short Volume = dis.readShort();
        String Nome = dis.readUTF();
        String[] Titulos = {dis.readUTF(), dis.readUTF()};
        Short Paginas = dis.readShort();
        String Data = dis.readUTF();
        String Episodio = dis.readUTF();
    
        return new Capitulo(NumeroCapitulo, Volume, Nome, Titulos, Paginas, Data, Episodio);
    }

    public int getNumeroCapitulo() {
        return NumeroCapitulo;
    }

    public Short getVolume() {
        return Volume;
    }

    public String getNome() {
        return Nome;
    }

    public String[] getTitulos() {
        return Titulos;
    }

    public Short getPaginas() {
        return Paginas;
    }

    public String getData() {
        return Data;
    }

    public String getEpisodio() {
        return Episodio;
    }

    // Setters
    public void setNumeroCapitulo(Short numeroCapitulo) {
        this.NumeroCapitulo = numeroCapitulo;
    }

    public void setVolume(Short volume) {
        this.Volume = volume;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setTituloOriginal(String titulosOriginal) {
        this.Titulos[0] = titulosOriginal;
    }

    public void setTituloIngles(String titulosIngles) {
        this.Titulos[1] = titulosIngles;
    }

    public void setPaginas(Short paginas) {
        this.Paginas = paginas;
    }

    public void setData(String data) {
        this.Data = data;
    }

    public void setEpisodio(String episodio) {
        this.Episodio = episodio;
    }
}
