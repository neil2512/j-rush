package jrush.app.model.components;

import javafx.scene.paint.Color;

/**
 * Un type de véhicule est défini par un identifiant, une taille et une couleur.
 * Il existe DOUZE types de voitures (taille 2) et QUATRE types de camions
 * (taille 3).
 */
public enum VehicleType {
    RED_CAR("REDCAR", 2, Color.web("#D96C6C")),
    LIM_CAR("LIMCAR", 2, Color.web("#A8C66C")),
    ORA_CAR("ORACAR", 2, Color.web("#E0A060")),
    LBL_CAR("LBLCAR", 2, Color.web("#9FBFD1")),
    PIN_CAR("PINCAR", 2, Color.web("#E3A6B2")),
    PUR_CAR("PURCAR", 2, Color.web("#A287C2")),
    DGR_CAR("DGRCAR", 2, Color.web("#5E7F5E")),
    GRA_CAR("GRACAR", 2, Color.web("#A49B93")),
    YEL_CAR("YELCAR", 2, Color.web("#E6CF7A")),
    DKH_CAR("DKHCAR", 2, Color.web("#C6DEB4")),
    BRO_CAR("BROCAR", 2, Color.web("#9C6B4A")),
    TAN_CAR("TANCAR", 2, Color.web("#D8C3A5")),
    YEL_TRK("YELTRK", 3, Color.web("#D4B95F")),
    PUR_TRK("PURTRK", 3, Color.web("#8B6AB0")),
    TEA_TRK("TEATRK", 3, Color.web("#5FA8A8")),
    DBL_TRK("DBLTRK", 3, Color.web("#4F6F94"));

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
