package tp3.bd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.query.Query;
import tp3.bd.entities.Client;
import tp3.bd.entities.Reservation;

import java.sql.Date;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@SuppressWarnings("unused")
public final class Reservations {
	
	@Nullable
	public static Long keyOf(@NotNull Reservation reservation) {
		return Collections.keyOf(reservation);
	}
	
	@NotNull
	public static Query<Reservation> query() {
		return Collections.query(Reservation.class);
	}
	
	@NotNull
	public static List<Reservation> find(@NotNull Client client) {
		return query().field("client").equal(client).asList();
	}
	
	@NotNull
	public static List<Reservation> find(int room) {
		return query().field("room").equal(room).asList();
	}
	
	@NotNull
	public static List<Reservation> conflicts(@NotNull Reservation reservation) {
		Query<Reservation> query = intersectsQuery(reservation.getStartDate(),reservation.getEndDate());
		query.and(query.criteria("room").equal(reservation));
		return query.asList();
	}
	
	@NotNull
	public static List<Reservation> intersects(long startDate, long endDate) {
		return intersects(new Date(min(startDate, endDate)), new Date(max(startDate, endDate)));
	}
	
	@NotNull
	public static List<Reservation> intersects(@NotNull Date startDate, @NotNull Date endDate) {
		return intersectsQuery(startDate,endDate).asList();
	}
	
	@NotNull
	private static Query<Reservation> intersectsQuery(@NotNull Date startDate, @NotNull Date endDate) {
		if (startDate.toInstant().isAfter(endDate.toInstant())) {
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
		}
		Query<Reservation> query = query();
		query.or(
				query.criteria("endDate").greaterThanOrEq(startDate)
						.and(query.criteria("endDate").lessThanOrEq(endDate)),
				query.criteria("startDate").greaterThanOrEq(startDate)
						.and(query.criteria("startDate").lessThanOrEq(endDate)));
		return query;
	}
	
	@Nullable
	public static Reservation save(@NotNull Reservation reservation) {
		return conflicts(reservation).size() > 0 ? null : Collections.save(reservation);
	}
	
	public static boolean delete(@NotNull Reservation reservation) {
		return Collections.delete(reservation);
	}
}
