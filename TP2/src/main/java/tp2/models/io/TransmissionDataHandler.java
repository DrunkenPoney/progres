package tp2.models.io;

import org.jetbrains.annotations.NotNull;
import tp2.models.io.data.TransmissionData;

public interface TransmissionDataHandler {
	void handle(@NotNull TransmissionData message);
}
