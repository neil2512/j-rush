package jrush.app.util;

/**
 * Cette classe représente une position dans le jeu, définie par des coordonnées
 * x et y.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – int x
 *          – int y
 *      Postconditions :
 *          – getX() == x
 *          – getY() == y
 * </pre>
 */
public class Position {

    // ATTRIBUTS

    private int x;
    private int y;

    // CONSTRUCTEUR

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // REQUÊTES

    /**
     * Retourne la coordonnée x de cette position.
     *
     * @return la coordonnée x de cette position
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnée y de cette position.
     *
     * @return la coordonnée y de cette position
     */
    public int getY() {
        return y;
    }

    // COMMANDES

    /**
     * Ajoute un delta à la coordonnée x de cette position.
     *
     * @param delta le delta à ajouter à x
     */
    public void addX(int delta) {
        x += delta;
    }

    /**
     * Ajoute un delta à la coordonnée y de cette position.
     *
     * @param delta le delta à ajouter à y
     */
    public void addY(int delta) {
        y += delta;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Position that = (Position) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
