#!/bin/bash
FX="$HOME/javafx/javafx-sdk-21.0.11/lib"

java \
  --module-path "$FX" \
  --add-modules javafx.controls,javafx.fxml \
  -cp out \
  gui.MainApp