module apple.statistics.blogstatistics {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires java.base;
	requires java.xml;
	requires javafx.base;
	requires java.sql;
	requires java.desktop;

    opens apple.statistics.blogstatistics to javafx.fxml;
    exports apple.statistics.blogstatistics;
}
