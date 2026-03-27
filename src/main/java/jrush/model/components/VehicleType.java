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
    YEL_CAR("YELCAR", 2, Color.YELLOW),
    GRN_CAR("GRNCAR", 2, Color.GREEN),
    ORA_CAR("ORACAR", 2, Color.ORANGE),
    BLU_CAR("BLUCAR", 2, Color.BLUE),
    PUR_CAR("PURCAR", 2, Color.PURPLE),
    PNK_CAR("PNKCAR", 2, Color.PINK),
    LGR_CAR("LGRCAR", 2, Color.LIGHTGREEN),
    LBL_CAR("LBLCAR", 2, Color.LIGHTBLUE),
    BRN_CAR("BRNCAR", 2, Color.BROWN),
    GRA_CAR("GRACAR", 2, Color.GRAY),
    MAR_CAR("MARCAR", 2, Color.MAROON),
    YEL_TRK("YELTRK", 3, Color.YELLOW),
    BLU_TRK("BLUTRK", 3, Color.BLUE),
    GRN_TRK("GRNTRK", 3, Color.GREEN),
    PUR_TRK("PURTRK", 3, Color.PURPLE);

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
