import java.io.*;

class Capitulo {
    
    protected int NumeroCapitulo;
    protected String Volume;
    protected String Nome;
    protected String[] Titulos; // Vetor para TituloOriginal e TituloIngles
    protected String Paginas;
    protected String Data;
    protected String Episodio;

    public Capitulo(int a, String b, String c, String[] d, String e, String f, String g) {
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
        Volume = "";
        Nome = "";  
        Titulos = new String[]{"", ""};
        Paginas = "";
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
        
        dos.writeInt(NumeroCapitulo);
        dos.writeUTF(Volume);
        dos.writeUTF(Nome);
        dos.writeUTF(Titulos[0]);
        dos.writeUTF(Titulos[1]);
        dos.writeUTF(Paginas);
        dos.writeUTF(Data);
        dos.writeUTF(Episodio);
        
        return baos.toByteArray();
    }
    
    public static Capitulo fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
    
        int NumeroCapitulo = dis.readInt();
        String Volume = dis.readUTF();
        String Nome = dis.readUTF();
        String[] Titulos = {dis.readUTF(), dis.readUTF()};
        String Paginas = dis.readUTF();
        String Data = dis.readUTF();
        String Episodio = dis.readUTF();
    
        return new Capitulo(NumeroCapitulo, Volume, Nome, Titulos, Paginas, Data, Episodio);
    }

    public int getNumeroCapitulo() {
        return NumeroCapitulo;
    }

    public String getVolume() {
        return Volume;
    }

    public String getNome() {
        return Nome;
    }

    public String[] getTitulos() {
        return Titulos;
    }

    public String getPaginas() {
        return Paginas;
    }

    public String getData() {
        return Data;
    }

    public String getEpisodio() {
        return Episodio;
    }

    // Setters
    public void setNumeroCapitulo(int numeroCapitulo) {
        this.NumeroCapitulo = numeroCapitulo;
    }

    public void setVolume(String volume) {
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

    public void setPaginas(String paginas) {
        this.Paginas = paginas;
    }

    public void setData(String data) {
        this.Data = data;
    }

    public void setEpisodio(String episodio) {
        this.Episodio = episodio;
    }
}
