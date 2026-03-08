package jrush.model;

import jrush.util.Position;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public interface Vehicle {

  // CONSTANTES

  char WIN_CAR_ID = 'R';
  String PROP_POSITION = "position";

  // REQUÊTES

  char getId();

  int getSize();

  boolean isHorizontal();

  Position getPosition();

  VetoableChangeListener[] getVetoableChangeListeners();

  PropertyChangeListener[] getPropertyChangeListeners();

  // COMMANDES

  void move(int delta) throws PropertyVetoException;

  void addVetoableChangeListener(VetoableChangeListener listener);

  void removeVetoableChangeListener(VetoableChangeListener listener);

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);

}
