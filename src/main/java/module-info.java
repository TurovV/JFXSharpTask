module com.sharp.jfxsharptask {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;


    opens com.sharp.jfxsharptask to javafx.fxml;
    exports com.sharp.jfxsharptask;
}