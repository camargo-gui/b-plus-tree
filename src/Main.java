import BPlusTree.BPlusTree;

public class Main {
    public static void main(String[] args) {
        BPlusTree b = new BPlusTree();

//        for (int i = 10000000; i > 0; i--) {
//            b.inserir(i);
//        }
//
//        for (int i = 10000000; i > 0; i--) {
//            if(i%2 == 1)
//                b.exclusao(i);
//        }

        for(int i = 0; i < 11; i++){
            b.inserir(i);
        }

        for (int i = 5; i < 11; i++ ){
            b.exclusao(i);
        }

        b.in_ordem();
    }
}