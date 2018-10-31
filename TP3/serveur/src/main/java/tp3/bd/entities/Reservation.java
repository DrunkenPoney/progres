package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;

import java.sql.Date;
import java.time.Instant;
import java.time.Period;

@Entity("reservations")
@Indexes(@Index(fields = {
		@Field("client"),
		@Field("room"),
		@Field("startDate")
}, options = @IndexOptions(unique = true)))
@SuppressWarnings("unused")
public class Reservation extends BaseEntity {
	@Reference
	private final Client client;
	@Reference
	private final int    room;
	private       Date   startDate, endDate;
	
	public Reservation(int room, @NotNull Client client, @NotNull Instant startDate,
	                   @NotNull Instant endDate) throws InvalidPeriodException {
		super();
		this.client = client;
		this.room = room;
		setPeriod(startDate, endDate);
	}
	
	private Reservation() {
		super();
		this.client = null;
		this.room = -1;
		this.startDate = null;
		this.endDate = null;
	}
	
	public Client getClient() {
		return client;
	}
	
	public int getRoom() {
		return room;
	}
	
	public Instant getStartDate() {
		return this.startDate.toInstant();
	}
	
	public Instant getEndDate() {
		return this.endDate.toInstant();
	}
	
	public Period getPeriod() {
		return Period.between(this.startDate.toLocalDate(), this.endDate.toLocalDate());
	}
	
	public void setPeriod(@NotNull Instant startDate, @NotNull Instant endDate) throws InvalidPeriodException {
		if (!startDate.isBefore(endDate))
			throw new InvalidPeriodException("La date de début doit être antérieur à la date de fin.");
		this.startDate = new Date(startDate.toEpochMilli());
		this.endDate = new Date(endDate.toEpochMilli());
	}
	
	public static class InvalidPeriodException extends Exception {
		private static final long serialVersionUID = -6418459563737152819L;
		
		public InvalidPeriodException(String message) {
			super(message);
		}
	}
}
