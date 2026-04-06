package jrush.app.model.components;

import javafx.scene.paint.Color;
import util.Contract;

/**
 * Un type de véhicule est défini par un identifiant, une taille et une couleur.
 * Il existe DOUZE types de voitures (taille 2) et QUATRE types de camions
 * (taille 3).
 */
public enum VehicleType {
    RED_CAR("REDCAR", 2, Color.RED),
    LIM_CAR("LIMCAR", 2, Color.LIME),
    ORA_CAR("ORACAR", 2, Color.ORANGE),
    LBL_CAR("LBLCAR", 2, Color.LIGHTBLUE),
    PIN_CAR("PINCAR", 2, Color.PINK),
    PUR_CAR("PURCAR", 2, Color.PURPLE),
    DGR_CAR("DGRCAR", 2, Color.DARKGREEN),
    GRA_CAR("GRACAR", 2, Color.GRAY),
    YEL_CAR("YELCAR", 2, Color.YELLOW),
    PGR_CAR("PGRCAR", 2, Color.PALEGREEN),
    BRO_CAR("BROCAR", 2, Color.BROWN),
    TAN_CAR("TANCAR", 2, Color.TAN),
    YEL_TRK("YELTRK", 3, Color.YELLOW),
    PUR_TRK("PURTRK", 3, Color.PURPLE),
    TEA_TRK("TEATRK", 3, Color.TEAL),
    DBL_TRK("DBLTRK", 3, Color.DARKBLUE);

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
