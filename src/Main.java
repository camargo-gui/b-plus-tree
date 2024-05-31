import BPlusTree.BPlusTree;

public class Main {
    public static void main(String[] args) {
        BPlusTree b = new BPlusTree();

        // Teste inserindo 10 mil elementos
        // e excluindo todos os pares

        for (int i = 10000; i > 0; i--) {
            b.inserir(i);
        }

        for (int i = 10000; i > 0; i--) {
            if(i % 2 == 0)
                b.exclusao(i);
        }

        b.in_ordem();
    }
}