import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.stage.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SimpleTest extends Application {
  final BufferedImage image;
  final BufferedImage imager;
  final String filename = "./images/street.png";

  Button NegaPosi;
  Canvas canvasi;
  GraphicsContext gci;
  TwoDimData data;

  public SimpleTest() {
    // a use example of JavaCV
    data = new TwoDimData(0,0);
    data.setResizeAndDefault(new File(filename));
    image = data.exportBufferedImage();
    imager =
        new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    for(int y=0;y<image.getHeight(); y++){
      for (int x = 0; x < image.getWidth(); x++){
        imager.setRGB(x, y, image.getRGB(x, y) ^ 0xffffff);
      }
    }
  }

  public void start(Stage stage) {

    canvasi = new Canvas(image.getWidth(), image.getHeight());
    gci = canvasi.getGraphicsContext2D();
    WritableImage imagei = new WritableImage(image.getWidth(), image.getHeight());
    gci.drawImage(imagei, 0, 0);
    SwingFXUtils.toFXImage(image, imagei);
    NegaPosi = new Button("NegaPosi");

    VBox imagebox = new VBox();
    imagebox.setAlignment(Pos.CENTER);
    imagebox.getChildren().addAll(canvasi, NegaPosi);

    Scene scene = new Scene(imagebox, image.getWidth(), image.getHeight()+30);

    NegaPosi.setOnMousePressed(e -> {
        SwingFXUtils.toFXImage(imager, imagei);
        gci.drawImage(imagei, 0, 0);
    });

    NegaPosi.setOnMouseReleased(e -> {
        SwingFXUtils.toFXImage(image, imagei);
        gci.drawImage(imagei, 0, 0);
     });

    stage.setTitle("JavaCV and JavaFX Use Test");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
