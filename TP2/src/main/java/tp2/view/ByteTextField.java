package tp2.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

public class ByteTextField extends ImprovedTextField {
	private final BooleanProperty binaryModeProperty;
	private final IntegerProperty valueProperty;
	
	public ByteTextField() {
		super();
		binaryModeProperty = new SimpleBooleanProperty(false);
		valueProperty = new SimpleIntegerProperty();
		initBindings();
		initListeners();
		initStyle(false);
	}
	
	private void initListeners() {
		addHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
				case LEFT:
				case BACK_SPACE:
					if (getCaretPosition() == 0 && getPrevious() != null) {
						getPrevious().requestFocus();
						getPrevious().positionCaret(getPrevious().getText().length());
					}
					break;
				case RIGHT:
					if (getCaretPosition() == getText().length() && getNext() != null) {
						getNext().requestFocus();
						getNext().positionCaret(0);
					}
					break;
			}
		});
	}
	
	private void initBindings() {
		textProperty().addListener((observable, oldValue, newValue) -> {
			if (isBinaryMode()) {
				if (!newValue.matches("^[01]{0,8}$")) {
					setValid(false);
					setText(oldValue);
				} else {
					setValid(true);
					if (newValue.length() == 8 && getNext() != null) {
						getNext().requestFocus();
						getNext().selectAll();
					}
				}
			} else {
				if (!newValue.codePoints().allMatch(Character::isDigit)) {
					final StringBuilder builder = new StringBuilder();
					newValue.codePoints().filter(Character::isDigit).forEach(builder::appendCodePoint);
					setValid(false);
					setText(builder.toString());
				} else if (newValue.length() > 3) {
					setText(newValue.substring(0, 3));
				} else {
					if (newValue.length() > 0 && Integer.parseInt(newValue) > 0xFF) {
						setValid(false);
					} else {
						setValid(true);
						if (newValue.length() == 3 && getNext() != null) {
							getNext().requestFocus();
							getNext().selectAll();
						}
					}
				}
			}
		});
		
		textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				valueProperty.set(isBinaryMode()
				                  ? Integer.parseUnsignedInt(newValue, 2)
				                  : Integer.parseInt(newValue));
			} catch (NumberFormatException ignored) {
			}
		});
		
		binaryModeProperty.addListener((observable, oldValue, newValue) -> initStyle(newValue));
	}
	
	public int getValue() {
		return valueProperty.get();
	}
	
	public void setValue(int value) {
		textProperty().set(String.valueOf(value));
	}
	
	protected void initStyle(boolean binaryMode) {
		setPrefColumnCount(binaryMode ? 8 : 3);
		// TODO
		// TODO binary input mode
	}
	
	// TODO byte getByte()
	// TODO int getValue()
	
	private ByteTextField[] getBytesInParent() {
		return this.getParent().getChildrenUnmodifiable()
				.stream()
				.filter(node -> node instanceof ByteTextField)
				.map(node -> (ByteTextField) node)
				.toArray(ByteTextField[]::new);
	}
	
	@Nullable
	public ByteTextField getPrevious() {
		int idx = getByteIndex() - 1;
		if (idx >= 0)
			return getBytesInParent()[idx];
		return null;
	}
	
	@Nullable
	public ByteTextField getNext() {
		int             idx   = getByteIndex() + 1;
		ByteTextField[] bytes = getBytesInParent();
		if (idx < bytes.length)
			return bytes[idx];
		return null;
	}
	
	public int getByteIndex() {
		return ArrayUtils.indexOf(getBytesInParent(), this);
	}
	
	public boolean isBinaryMode() {
		return this.binaryModeProperty.get();
	}
	
	public void setBinaryMode(boolean binaryMode) {
		this.binaryModeProperty.set(binaryMode);
	}
	
	public BooleanProperty getBinaryModeProperty() {
		return this.binaryModeProperty;
	}
}
