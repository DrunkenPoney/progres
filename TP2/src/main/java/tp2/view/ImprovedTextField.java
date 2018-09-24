package tp2.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.TextField;

import java.util.concurrent.LinkedTransferQueue;

public class ImprovedTextField extends TextField {
	public static final String INVALID_STYLE_CLASS = "invalid";
	
	private final LinkedTransferQueue<InternalHandler> handlers;
	private final BooleanProperty                   valid;
	
	public ImprovedTextField() {
		handlers = new LinkedTransferQueue<>();
		initHandlers();
		valid = new SimpleBooleanProperty(true);
		this.valid.addListener(((observable, oldValue, newValue) -> {
			if (newValue)
				getStyleClass().removeAll(INVALID_STYLE_CLASS);
			else if (!getStyleClass().contains(INVALID_STYLE_CLASS))
				getStyleClass().add(INVALID_STYLE_CLASS);
		}));
	}
	
	@SuppressWarnings("unchecked")
	private void initHandlers() {
		final EventHandler commonHandler = event -> handlers.iterator().forEachRemaining(eventHandler -> eventHandler.handle(event));
		setOnAction(commonHandler);
		setOnContextMenuRequested(commonHandler);
		setOnDragDetected(commonHandler);
		setOnDragDone(commonHandler);
		setOnDragDropped(commonHandler);
		setOnDragEntered(commonHandler);
		setOnDragExited(commonHandler);
		setOnDragOver(commonHandler);
		setOnInputMethodTextChanged(commonHandler);
		setOnKeyPressed(commonHandler);
		setOnKeyReleased(commonHandler);
		setOnKeyTyped(commonHandler);
		setOnMouseClicked(commonHandler);
		setOnMouseDragEntered(commonHandler);
		setOnMouseDragExited(commonHandler);
		setOnMouseDragged(commonHandler);
		setOnMouseDragOver(commonHandler);
		setOnMouseDragReleased(commonHandler);
		setOnMouseEntered(commonHandler);
		setOnMouseExited(commonHandler);
		setOnMouseMoved(commonHandler);
		setOnMousePressed(commonHandler);
		setOnMouseReleased(commonHandler);
		setOnRotate(commonHandler);
		setOnRotationFinished(commonHandler);
		setOnRotationStarted(commonHandler);
		setOnScroll(commonHandler);
		setOnScrollFinished(commonHandler);
		setOnScrollStarted(commonHandler);
		setOnSwipeDown(commonHandler);
		setOnSwipeLeft(commonHandler);
		setOnSwipeRight(commonHandler);
		setOnSwipeUp(commonHandler);
		setOnTouchMoved(commonHandler);
		setOnTouchPressed(commonHandler);
		setOnTouchReleased(commonHandler);
		setOnTouchStationary(commonHandler);
		setOnZoom(commonHandler);
		setOnZoomFinished(commonHandler);
		setOnZoomStarted(commonHandler);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Event, T extends EventType<E>> void addHandler(final T eventType, final EventHandler<E> handler) {
		handlers.add(event -> {
			if (event.getEventType().getName().equals(eventType.getName()))
				handler.handle((E) event);
		});
	}
	
	public BooleanProperty getValidProperty() {
		return this.valid;
	}
	
	public boolean isValid() {
		return this.valid.get();
	}
	
	public void setValid(boolean valid) {
		this.valid.set(valid);
	}
	
	private interface InternalHandler {
		void handle(Event event);
	}
	
}
