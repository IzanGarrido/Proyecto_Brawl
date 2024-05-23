module izangq {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires org.controlsfx.controls;

    opens izangq to javafx.fxml;
    exports izangq;
}
