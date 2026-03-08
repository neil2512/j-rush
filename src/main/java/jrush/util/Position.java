package jrush.util;

public class Position {

  // ATTRIBUTS

  private int x;
  private int y;

  // CONSTRUCTEUR

  public Position(int x, int y) {
    // CONTRACT_TO_ADD
    this.x = x;
    this.y = y;
  }

  // REQUÊTES

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  // COMMANDES

  public void setX(int x) {
    // CONTRACT_TO_ADD
    this.x = x;
  }

  public void setY(int y) {
    // CONTRACT_TO_ADD
    this.y = y;
  }

  public void addX(int delta) {
    // CONTRACT_TO_ADD
    this.x += delta;
  }

  public void addY(int delta) {
    // CONTRACT_TO_ADD
    this.y += delta;
  }
}
