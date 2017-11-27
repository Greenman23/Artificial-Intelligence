package pacman;

public class Coordinate {
    private int x;
    private int y;

    void setCoordinates(int x ,int y){
        this. x = x;
        this.y = y;
    }

    int getX(){return this.x;}
    int getY(){return  this.y;}

    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
}
