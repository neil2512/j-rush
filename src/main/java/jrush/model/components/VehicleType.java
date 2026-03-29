package jrush.model.components;

import javafx.scene.paint.Color;

/**
 * Un type de véhicule est défini par un identifiant, une taille et une couleur.
 * Il existe SEPT types de véhicules différents : QUATRE voitures de taille 2
 * (rouge, bleu, jaune et vert) et TROIS camions de taille 3 (bleu, jaune et
 * vert).
 */
public enum VehicleType {
    RED_CAR("REDCAR", 2, Color.RED),
    BLU_CAR("BLUCAR", 2, Color.BLUE),
    YEL_CAR("YELCAR", 2, Color.YELLOW),
    GRE_CAR("GRECAR", 2, Color.GREEN),
    ORA_CAR("ORACAR",2,Color.ORANGE),
    BLU_TCK("BLUTCK", 3, Color.BLUE),
    YEL_TCK("YELTCK", 3, Color.YELLOW),
    GRE_TCK("GRETCK", 3, Color.GREEN),
    PUR_TCK("PURTCK",3,Color.PURPLE);

    // ATTRIBUTS

    private final String id;
    private final int size;
    private final Color color;

    // CONSTRUCTEURS

    VehicleType(String id, int size, Color color) {
        this.id = id;
        this.size = size;
        this.color = color;
    }

    // REQUÊTES

    /**
     * Retourne l'identifiant du type de véhicule.
     *
     * @return l'identifiant du type de véhicule
     */
    public String getId() {
        return this.id;
    }

    /**
     * Retourne la taille du type de véhicule.
     *
     * @return la taille du type de véhicule
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Retourne la couleur du type de véhicule.
     *
     * @return la couleur du type de véhicule
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Retourne le type de véhicule correspondant à l'identifiant donné.
     *
     * @return le type de véhicule correspondant à l'identifiant donné
     */
    public static VehicleType fromId(String id) {
        for (VehicleType type : values()) {
            if (type.getId().equals(id)) return type;
        }
        throw new IllegalArgumentException("Illegal id : " + id);
    }
}
