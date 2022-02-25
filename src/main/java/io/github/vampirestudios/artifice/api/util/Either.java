package io.github.vampirestudios.artifice.api.util;

public class Either<LEFT, RIGHT>{

    private final LEFT aa;
    private final RIGHT bb;
    public Either(LEFT a, RIGHT b){
        this.aa = a;
        this.bb = b;
    }
    public static <L,R> Either<L,R> left(L left){return new Either<>(left,null);}
    public static <L,R> Either<L,R> right(R right){return new Either<>(null,right);}

    public boolean isLeft(){
        return aa != null;
    }

    public LEFT getLeft() {return aa;}
    public RIGHT getRight() {return bb;}
}
