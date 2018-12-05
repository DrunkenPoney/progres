package progres.tp4.api.dominospizzaapi.bo.embeddable;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.IValidated;
import progres.tp4.api.dominospizzaapi.bo.VolumeUnitBo;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.Min;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_VOLUME_MIN_ZERO;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;

@Embeddable
@SuppressWarnings("unused")
public class Volume implements IValidated {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_vun", nullable = false)
	private VolumeUnitBo unit;
	
	@Min(value = 0, message = MSG_VOLUME_MIN_ZERO)
	@Column(name = "volume", nullable = false, precision = 65)
	private double volume;
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	public VolumeUnitBo getUnit() {
		return unit;
	}
	
	public void setUnit(@NotNull VolumeUnitBo unit) {
		this.unit = unit;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		if (getUnit() == null)
			throw new RequestValidationException(msgRequiredAttr("unit", "volume"));
		if (getUnit().getId() == null)
			throw new RequestValidationException(msgRequiredAttr("id", "unit"));
	}
}
