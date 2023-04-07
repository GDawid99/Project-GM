module com.project.program {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;


    opens com.project.files to javafx.fxml;
    exports com.project.files;
}