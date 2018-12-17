package progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.VolumeUnit.convert;

public class Volume {
	private List<VolumeListener> listeners;
	private VolumeUnit unit;
	private double volume;
	
	public Volume(@NotNull VolumeUnit unit, double volume) {
		listeners = new ArrayList<>();
		this.unit = unit;
		this.volume = volume;
	}
	
	public double getVolume(@NotNull VolumeUnit unit) {
		return convert(volume, this.unit, unit);
	}
	
	public Volume add(@NotNull VolumeUnit unit, double volume) {
		this.volume += convert(volume, unit, this.unit);
		return this;
	}
	
	public Volume add(@NotNull Volume volume) {
		this.add(volume.unit, volume.volume);
		return this;
	}
	
	public Volume addListener(@NotNull VolumeListener listener) {
		this.listeners.add(listener);
		return this;
	}
	
	public boolean removeListener(@NotNull VolumeListener listener) {
		return listeners.remove(listener);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Volume
			       && ((Volume) obj).getVolume(unit) == this.volume;
	}
	
	public Volume clone(boolean includeListeners) {
		Volume vol = new Volume(this.unit, this.volume);
		if (includeListeners) vol.listeners = this.listeners;
		return vol;
	}
	
	public interface VolumeListener extends Consumer<Volume> {}
}
