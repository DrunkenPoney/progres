package tp2.models.io;

import org.jetbrains.annotations.NotNull;

public interface TransmissionDataHandler {
	void handle(@NotNull TransmissionData message);
}
