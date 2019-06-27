package senac.cardapio.models;

public class Cardapio {

    private String nome;
    private double preco;
    private int foto;

    public Cardapio(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

}
