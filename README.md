# Doodler

This repository contains the source code for the Doodler app, a drawing and doodling application for Android. The app allows users to create and modify drawings using a variety of tools, including a pen, eraser, color picker, stroke width adjustment, and opacity control. The app also features a clean user interface with customizable options.

## How to Run

To run this app, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/h-sehee/Doodler.git
2. **Open the project in Android Studio:**

   - If you haven't already, download and install Android Studio from the [official website](https://developer.android.com/studio)
   - Open Android Studio and choose "Open an existing Android Studio project."
   - Navigate to the cloned repository folder and select it.

3. **Build and run the project:**

   - In Android Studio, click on "Run" (the green triangle) or use the keyboard shortcut `Shift + F10` to build and run the app on either an emulator or a physical device.

## Features
- **Drawing Tools:** Pen, Eraser
- **Customaization Options:** Color Picker, Stroke Width, Opacity
- **User Interface:** Simple, clean design with easy-to-use controls
- **Undo/Redo Functionality:** Users can undo and redo their drawing actions. The `undo` and `redo` buttons allow users to reverse or reapply their last drawing or eraser actions. The button states dynamically change to disabled (gray) when no further undo or redo actions can be performed.

## Libraries and Resources Used
- [AmbilWarna](https://github.com/yukuku/ambilwarna) - A color picker library used for selecting colors in the app.