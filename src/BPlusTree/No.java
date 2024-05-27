package BPlusTree;

public class No {

    public static final int N = 4;

    public static final int min = (int) (Math.ceil((double)N/2)-1);
    private int vInfo[];
    private No vLig[];
    private No ant;
    private No prox;
    private int TL;

    public No(){
        vInfo = new int[N];
        vLig = new No[N+1];
        TL = 0;
    }

    public No(int info){
        this();
        vInfo[0] = info;
        TL = 1;
    }

    public void remanejar(int pos){
        int j = TL;
        vLig[j+1] = vLig[j];
        while(j > pos){
            vInfo[j] = vInfo[j-1];
            vLig[j] = vLig[j-1];
            j--;
        }
    }

    public void remanejarExclusao(int pos){
        for(int i=pos; i<TL-1; i++){
            vInfo[i] = vInfo[i+1];
            vLig[i] = vLig[i+1];
        }
        vLig[TL-1] = vLig[TL];
    }


    public boolean ehFolha(){
        return getvLig(0) == null;
    }

    public boolean temEspaco(){ return TL <= N-1; }

    public int procurarPosicao(int info){
        int pos = 0;
        while(pos < TL && vInfo[pos] <= info)
            pos++;
        return pos;
    }

    public int getvInfo(int p) {
        return vInfo[p];
    }

    public void setvInfo(int p, int info) {
        vInfo[p] = info;
    }

    public No getvLig(int p) {
        return vLig[p];
    }

    public void setvLig(int p, No lig) {
        vLig[p] = lig;
    }

    public int getTl() {
        return TL;
    }

    public void setTl(int TL) {
        this.TL = TL;
    }

    public No getAnt() {
        return ant;
    }

    public void setAnt(No ant) {
        this.ant = ant;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }
}
