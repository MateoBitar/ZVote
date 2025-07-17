# ZVote

## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Getting Started](#getting-started)
5. [Prerequisites](#prerequisites)
6. [Installation](#installation)
7. [Usage](#usage)
8. [Front-End (JavaFX) Integration](#front-end-javafx-integration)

---

## Project Overview
**ZVote** is a pure Java application built with JavaFX, designed to facilitate electronic voting processes. The project provides a robust back-end for vote management and a modern JavaFX front-end for seamless user interaction. Users can create polls, vote, and view results in real time.

---

## Features
- User authentication and session management.
- Creation and management of voting polls.
- Casting votes securely for different poll options.
- Real-time results visualization.
- CRUD operations for polls and votes.
- JavaFX-based interactive user interface.
- Modular code structure for easy maintenance and extension.

---

## Architecture
ZVote follows a modular MVC (Model-View-Controller) structure:

- **Model**: Handles the core logic for polls, votes, and results.
- **View**: JavaFX scenes and UI components for user interaction.
- **Controller**: Connects the view with the model, handling user actions and updating the UI.

Typical components include:
- Poll and Vote models.
- Main JavaFX application launcher.
- Controllers for poll creation, voting, and result display.
- Utility classes for data persistence.

---

## Getting Started
Follow these instructions to set up and run the ZVote project locally.

### Prerequisites
- **Java JDK**: Version 11 or newer.
- **JavaFX SDK**: Compatible with your Java version.
- **Git**: To clone and manage the repository.
- (Optional) **IDE**: IntelliJ IDEA, Eclipse, or NetBeans for development.

---

## Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/MateoBitar/ZVote.git
    ```

2. **Import the project into your IDE**:
    - Open your IDE and select “Import Project.”
    - Choose the cloned folder.
    - If prompted, set up the JavaFX SDK as a library/module.

3. **Configure JavaFX SDK**:
    - Download JavaFX SDK from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/).
    - In your IDE, add the JavaFX library to your project/module settings.

4. **Build the project**:
    - Use your IDE’s build function, or compile from the command line:
      ```bash
      javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d out src/**/*.java
      ```

5. **Run the application**:
    - From your IDE, run the main class (often `Main.java`).
    - Or from the command line:
      ```bash
      java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out Main
      ```

---

## Usage

- **Poll Management**:
  - Create new polls via the JavaFX UI.
  - Edit or delete existing polls if supported.

- **Voting**:
  - Select active polls and cast your vote.
  - Ensure your vote is recorded (confirmation via UI).

- **Results Visualization**:
  - View real-time results for completed or ongoing polls.

- **Error Handling**:
  - User-friendly error messages are displayed via JavaFX dialogs or panes.

---

## Front-End (JavaFX) Integration

- **FXML Files**:
  - UI layouts are defined in `.fxml` files (typically in `/src/main/resources`).
  - Common views include:
    - `MainView.fxml`: Main dashboard.
    - `PollCreationView.fxml`: Create or edit polls.
    - `VoteView.fxml`: Cast votes.
    - `ResultsView.fxml`: View poll results.
    - `ErrorView.fxml`: Display errors.

- **Styling**:
  - CSS files for custom styling are usually in `/src/main/resources/css`.
    - Example: `main.css`, `poll.css`, `results.css`, `error.css`.

- **Controllers**:
  - Java classes handle UI logic, linked to FXML files.
  - Example: `MainController.java`, `PollController.java`, `VoteController.java`, `ResultsController.java`.

- **Static Assets**:
  - Images and icons for UI are stored in `/src/main/resources/assets`.

---

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. For major changes, open an issue first to discuss what you would like to change.

---

## License

This project is licensed under the MIT License.

---

For additional questions or issues, please refer to the documentation or contact the repository maintainer.
