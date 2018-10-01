package tp2.models.io;

import org.jetbrains.annotations.NotNull;

public interface MessageHandler {
	void handle(@NotNull Message message);
}
