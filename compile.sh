#!/bin/bash
FX="$HOME/javafx/javafx-sdk-21.0.11/lib"

echo "Compiling..."
find src -name "*.java" | xargs javac \
  --module-path "$FX" \
  --add-modules javafx.controls,javafx.fxml \
  -d out

if [ $? -eq 0 ]; then
  # Copy CSS and resources into output directory
  mkdir -p out/resources/styles
  cp src/resources/styles/dark-theme.css out/resources/styles/
  echo "✓ Build successful"
else
  echo "✗ Build failed"
fi