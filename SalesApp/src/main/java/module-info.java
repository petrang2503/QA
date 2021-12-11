module com.mycompany.salesapp {
    requires javafx.controls;
    requires javafx.fxml;
        requires java.sql;
    requires java.base;

    opens com.mycompany.salesapp to javafx.fxml;
    exports com.mycompany.salesapp;
    exports com.mycompany.pojo;
    exports com.mycompany.services;
}
