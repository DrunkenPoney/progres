package tp1.models.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ColoredText extends HBox {
    private final ObservableList<TextEntry> textEntries;
    
    public ColoredText() {
        textEntries = FXCollections.observableArrayList();
        textEntries.addListener((ListChangeListener<? super TextEntry>)
                                        change -> super.getChildren()
                                                       .setAll(change.getList()
                                                                     .stream()
                                                                     .map(entry -> entry.node)
                                                                     .toArray(Text[]::new)));
    }
    
    public ObservableList<TextEntry> getEntries() {
        return textEntries;
    }
    
    public ColoredText setText(String text, Paint color) {
        return setText(new TextEntry(text, color));
    }
    
    public ColoredText setText(TextEntry... text) {
        getChildren().clear();
        return appendText(text);
    }
    
    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildrenUnmodifiable();
    }
    
    public ColoredText appendText(TextEntry... text) {
        textEntries.addAll(text);
        return this;
    }
    
    public ColoredText appendText(String text) {
        return appendText(text, null);
    }
    
    public ColoredText appendText(String text, Paint color) {
        return appendText(new TextEntry(text, color));
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class TextEntry {
        protected final Text node;
        
        public TextEntry(String text) {
            this(text, null);
        }
        
        public TextEntry(String text, Paint color) {
            this.node = new Text(text);
            if (color != null) { node.setFill(color); }
        }
        
        public ObjectProperty<Paint> colorProperty() {
            return node.fillProperty();
        }
        
        public StringProperty textProperty() {
            return node.textProperty();
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TextEntry
                   && ((TextEntry) obj).node.getText().equals(this.getText())
                   && ((TextEntry) obj).node.getFill().equals(this.getColor())
                   && ((TextEntry) obj).getFont().equals(this.getFont());
        }
        
        public String getText() {
            return node.getText();
        }
        
        public void setText(String text) {
            node.setText(text);
        }
        
        public Paint getColor() {
            return node.getFill();
        }
        
        public void setColor(Paint color) {
            node.setFill(color);
        }
        
        public Font getFont() {
            return node.getFont();
        }
        
        public void setFont(Font font) {
            node.setFont(font);
        }
    }
}
