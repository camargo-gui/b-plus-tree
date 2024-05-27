package BPlusTree;
public class BPlusTree {
    private No raiz;

    public BPlusTree(){
        raiz = null;
    }

    public boolean buscar(int chave) {
        return buscarRecursivo(raiz, chave);
    }

    private boolean buscarRecursivo(No no, int chave) {
        if (no == null) {
            return false;
        }

        int pos = no.procurarPosicao(chave);

        if (pos < no.getTl() && no.getvInfo(pos) == chave) {
            return true;
        }

        return buscarRecursivo(no.getvLig(pos), chave);
    }

    private No navegarAteFolha(int info){
        int pos;
        No aux = raiz;
        while(!aux.ehFolha()){
            pos = aux.procurarPosicao(info);
            aux = aux.getvLig(pos);
        }
        return aux;
    }

    private No localizarPai(No folha){
        int pos;
        No atual = raiz, pai = atual;
        while(atual != folha){
            pai = atual;
            pos = atual.procurarPosicao(folha.getvInfo(0));
            atual = atual.getvLig(pos);
        }
        return pai;
    }

    private void split(No folha, No pai, boolean ehFolha){
        No cx1 = new No(), cx2 = new No();
        int divider = (int) (ehFolha ? Math.ceil((double) (No.N - 1) /2) : Math.ceil((double) No.N/2)-1);
        for(int i=0; i <divider; i++){
            cx1.setvInfo(i, folha.getvInfo(i));
            cx1.setvLig(i, folha.getvLig(i));
        }
        cx1.setTl(divider);
        cx1.setvLig(divider, folha.getvLig(divider));

        divider = folha.ehFolha() ? divider : divider + 1;

        for(int i=divider; i < folha.getTl(); i++){
            cx2.setvInfo(i - divider, folha.getvInfo(i));
            cx2.setvLig(i - divider, folha.getvLig(i));
        }
        cx2.setTl(folha.getTl() - divider);
        cx2.setvLig(cx2.getTl(), folha.getvLig(folha.getTl()));

        if(folha == pai){
            pai.setTl(1);
            pai.setvLig(0, cx1);
            pai.setvLig(1, cx2);
            pai.setvInfo(0, localizarSubD(pai, 1));
            raiz = pai;
        }
        else if(!folha.ehFolha()){
            int posPai = pai.procurarPosicao(cx2.getvInfo(0));
            pai.remanejar(posPai);
            pai.setvLig(posPai, cx1);
            pai.setvLig(posPai+1, cx2);
            pai.setvInfo(posPai, localizarSubD(pai, posPai+1));
            pai.setTl(pai.getTl()+1);
        }
        else {
            int pos = pai.procurarPosicao(folha.getvInfo(divider));
            pai.remanejar(pos);
            pai.setvInfo(pos, cx2.getvInfo(0));
            pai.setvLig(pos, cx1);
            pai.setvLig(pos+1, cx2);
            pai.setTl(pai.getTl()+1);
        }
        if (!pai.temEspaco()){
            folha = pai;
            pai = localizarPai(folha);
            split(folha, pai, false);
        }
        if(cx2.ehFolha()){
            linkEsq(cx1);
            linkDir(cx1);
            linkEsq(cx2);
            linkDir(cx2);
        }
    }

    private No localizarLigEsq(No folha) {
        No pai = localizarPai(folha);

        int pos = pai.procurarPosicao(folha.getvInfo(folha.getTl() - 1));

        if (pos > 0 && pai.getvLig(pos - 1) != null) {
            No ligacaoEsquerda = pai.getvLig(pos - 1);
            while (ligacaoEsquerda.getvLig(ligacaoEsquerda.getTl()) != null) {
                ligacaoEsquerda = ligacaoEsquerda.getvLig(ligacaoEsquerda.getTl());
            }
            return ligacaoEsquerda;
        } else {
            if(pai == raiz){
                return null;
            }
            return localizarLigEsq(pai);
        }
    }


    private No localizarLigDir(No folha){
        No pai = localizarPai(folha);
        int pos = pai.procurarPosicao(folha.getvInfo(0));

        if(pos < pai.getTl() && pai.getvLig(pos+1) != null){
            No ligacaoDireita = pai.getvLig(pos+1);
            while(ligacaoDireita.getvLig(0) != null){
                ligacaoDireita = ligacaoDireita.getvLig(0);
            }
            return ligacaoDireita;
        } else {
            if(pai == raiz){
                return null;
            }
            return localizarLigDir(pai);
        }
    }

    private void linkEsq(No folha){
        No ligEsq = localizarLigEsq(folha);
        if (ligEsq != null && ligEsq != folha){
            folha.setAnt(ligEsq);
            ligEsq.setProx(folha);
        }
    }

    private void linkDir(No folha){
        No ligDir = localizarLigDir(folha);
        if(ligDir != null && ligDir != folha){
            folha.setProx(ligDir);
            ligDir.setAnt(folha);
        }
    }

    public void inserir(int info){
        No folha,pai;
        if(raiz == null){
            raiz = new No(info);
        }
        else {
            folha = navegarAteFolha(info);
            int pos = folha.procurarPosicao(info);
            folha.remanejar(pos);
            folha.setvInfo(pos, info);
            folha.setTl(folha.getTl()+1);
            if (!folha.temEspaco()){
                pai = localizarPai(folha);
                split(folha, pai, true);
            }
        }
    }

    public void in_ordem () {
        No no = raiz;
        while(!no.ehFolha()){
            no = no.getvLig(0);
        }

        while(no != null){
            for(int i=0; i < no.getTl(); i++){
                System.out.println(no.getvInfo(i));
            }
            no = no.getProx();
        }
    }

    private No localizarNo(int info) {
        No aux = raiz;
        boolean achou = false;
        while (!achou) {
            if (aux.getvInfo(aux.procurarPosicao(info)) == info) {
                achou = true;
            } else {
                aux = aux.getvLig(aux.procurarPosicao(info));
            }
        }
        return aux;
    }

    private void excluirIndices(int info){
        No aux = raiz;
        while(aux != null && !aux.ehFolha()){
            int pos = aux.procurarPosicao(info);
            if(pos > 0 && aux.getvInfo(pos - 1 ) == info){
                excluirNaoFolha(aux, info);
            }
            aux = aux.getvLig(pos);
        }
    }


    private int localizarSubD(No no, int pos){
        no = no.getvLig(pos);
        while(!no.ehFolha()){
            no = no.getvLig(0);
        }
        return no.getvInfo(0);
    }


    private void excluirNaoFolha(No no, int info){
        int pos = no.procurarPosicao(info) - 1;
        no.setvInfo(pos, localizarSubD(no, pos + 1));
    }

    public void exclusao(int info){
        No folha = navegarAteFolha(info);
        int pos = folha.procurarPosicao(info) - 1;

        folha.remanejarExclusao(pos);
        folha.setTl(folha.getTl()-1);

        if(folha == raiz && folha.getTl() == 0){
            raiz = null;
        }
        else if (folha != raiz && folha.getTl() < No.min){
            rebalancear(folha);
        }

        excluirIndices(info);
    }

    private void rebalancear(No folha){
        No irmaoE = null, irmaoD = null;
        No pai = localizarPai(folha);
        int posPai = pai.procurarPosicao(folha.getvInfo(0));

        if (posPai-1 >= 0)
            irmaoE = pai.getvLig(posPai-1);
        if (posPai+1 <= pai.getTl())
            irmaoD = pai.getvLig(posPai+1);

        if(irmaoD != null && irmaoD.getTl() > No.min)
            redistribuirDireita(folha, irmaoD, pai, posPai);
        else if(irmaoE != null && irmaoE.getTl() > No.min)
            redistribuirEsquerda(folha, irmaoE, pai, posPai);
        else
            concatena(folha, irmaoE, irmaoD, pai, posPai);

        if(raiz == pai && pai.getTl() == 0){
            if (irmaoE != null){
                raiz = irmaoE;
            }
            else {
                raiz = irmaoD;
            }
        }
        else if(pai !=raiz && pai.getTl() < No.min){
            rebalancear(pai);
        }
    }

    private void redistribuirDireita(No no, No irmaoD, No pai, int posPai){
        no.setvInfo(no.getTl(), pai.getvInfo(posPai));
        no.setTl(no.getTl()+1);
        no.setvLig(no.getTl(), irmaoD.getvLig(0));

        irmaoD.remanejarExclusao(0);
        irmaoD.setTl(irmaoD.getTl()-1);

        if(no.ehFolha())
            pai.setvInfo(posPai, irmaoD.getvInfo(0));
    }

    private void redistribuirEsquerda(No no, No irmaoE, No pai, int posPai){
        if(no.ehFolha()){
            pai.setvInfo(posPai-1, irmaoE.getvInfo(irmaoE.getTl()-1));
        }

        no.remanejar(0);
        no.setvInfo(0, pai.getvInfo(posPai-1));
        no.setvLig(0, irmaoE.getvLig(irmaoE.getTl()));
        no.setTl(no.getTl()+1);

        if(!no.ehFolha()){
            pai.setvInfo(posPai-1, irmaoE.getvInfo(irmaoE.getTl()-1));
        }

        irmaoE.setTl(irmaoE.getTl()-1);
    }

    private void concatena(No no, No irmaoE, No irmaoD, No pai, int posPai){
        if(irmaoD != null)
            concatenaDireita(no, irmaoD, pai, posPai);
        else
            concatenaEsquerda(no, irmaoE, pai, posPai);
    }

    private void concatenaDireita(No no, No irmaoD, No pai, int posPai){
        if(!no.ehFolha()){
            irmaoD.remanejar(0);
            irmaoD.setvInfo(0, pai.getvInfo(posPai));
            irmaoD.setTl(irmaoD.getTl()+1);
            irmaoD.setvLig(0, no.getvLig(no.getTl()));
        }


        for (int i = no.getTl()-1; i >= 0; i--){
            irmaoD.remanejar(0);
            irmaoD.setvInfo(0, no.getvInfo(i));
            irmaoD.setvLig(0, no.getvLig(i));
            irmaoD.setTl(irmaoD.getTl()+1);
        }

        irmaoD.setAnt(no.getAnt());
        if(no.getAnt() != null)
            no.getAnt().setProx(irmaoD);

        pai.remanejarExclusao(posPai);
        pai.setTl(pai.getTl()-1);
    }

    private void concatenaEsquerda(No no, No irmaoE, No pai, int posPai){
        if(!no.ehFolha()){
            irmaoE.setvInfo(irmaoE.getTl(), pai.getvInfo(posPai-1));
            irmaoE.setTl(irmaoE.getTl()+1);
        }

        for(int i = 0; i < no.getTl(); i++){
            irmaoE.setvInfo(irmaoE.getTl(), no.getvInfo(i));
            irmaoE.setvLig(irmaoE.getTl(), no.getvLig(i));
            irmaoE.setTl(irmaoE.getTl()+1);
        }
        irmaoE.setvLig(irmaoE.getTl(), no.getvLig(no.getTl()));

        irmaoE.setProx(no.getProx());
        if(no.getProx()!=null){
            no.getProx().setAnt(irmaoE);
        }

        pai.remanejarExclusao(posPai-1);
        pai.setTl(pai.getTl()-1);
        pai.setvLig(posPai-1, irmaoE);
    }

}
