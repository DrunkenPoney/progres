package tp3.io;

import org.jetbrains.annotations.NotNull;
import tp3.io.data.TransmissionData;

public interface TransmissionDataHandler {
	void handle(@NotNull TransmissionData message);
}
