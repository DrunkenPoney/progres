package tp3;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import static java.lang.String.format;
import static javafx.beans.binding.Bindings.createDoubleBinding;

public class CloseButton extends Group {
	public static final char CLOSE_CHAR = 0x2297;
	
	private final Circle background;
	private final Text   symbol;
	private final DoubleProperty sizeProperty;
	
	public CloseButton() {
		this.symbol = new Text(String.valueOf(CLOSE_CHAR));
		this.symbol.setBoundsType(TextBoundsType.VISUAL);
		this.symbol.setTextOrigin(VPos.CENTER);
		this.symbol.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/RobotoMono-Regular.ttf"), 1));
		this.sizeProperty = new SimpleDoubleProperty();
		this.sizeProperty.addListener((obs, o, n) -> symbol.setStyle(format("-fx-font-size: %spx;", n)));
		this.background = new Circle();
		this.background.radiusProperty().bind(createDoubleBinding(() -> (this.getSize() / 2) - 5, this.sizeProperty));
		getChildren().addAll(this.background, this.symbol);
		layoutBoundsProperty().addListener((obs, o, n) -> {
			if (n.getWidth() - o.getWidth() > 0.01)
				this.symbol.setX(n.getWidth() / -2);
		});
		this.setCursor(Cursor.HAND);
		
		setOnMouseClicked(System.out::println);
		setSize(24);
		setBackgroundFill(Color.LIGHTSTEELBLUE);
		System.out.println(background.getRadius());
		setOnMouseEntered(e -> {
			setScaleX(1.2);
			setScaleY(1.2);
		});
		setOnMouseExited(e -> {
			setScaleX(1);
			setScaleY(1);
		});
	}
	
	public ObjectProperty<Paint> backgroundFillProperty() {
		return this.background.fillProperty();
	}
	
	public Paint getBackgroundFill() {
		return this.background.getFill();
	}
	
	public void setBackgroundFill(Paint color) {
		this.background.setFill(color);
	}
	
	public ObjectProperty<Paint> colorProperty() {
		return symbol.fillProperty();
	}
	
	public Paint getFill() {
		return this.symbol.getFill();
	}
	
	public void setFill(Paint color) {
		this.symbol.setFill(color);
	}
	
	public DoubleProperty sizeProperty() {
		return this.sizeProperty;
	}
	
	public double getSize() {
		return sizeProperty.get();
	}
	
	public void setSize(double size) {
		this.sizeProperty.set(size);
	}
}
