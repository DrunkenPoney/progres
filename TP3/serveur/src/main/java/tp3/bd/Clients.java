package tp3.bd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.query.CriteriaContainer;
import org.mongodb.morphia.query.Query;
import tp3.bd.entities.Client;

import java.util.List;

public final class Clients {
	
	@Nullable
	public static Long keyOf(@NotNull Client client) {
		return Collections.keyOf(client);
	}
	
	@NotNull
	public static Query<Client> query() {
		return Collections.query(Client.class);
	}
	
	public static Client save(@NotNull Client client) {
		Client saved = find(client.getEmail());
		if (saved != null) {
			saved.setTel(client.getTel());
			saved.setEmail(client.getEmail());
			saved.setLastName(client.getLastName());
			saved.setFirstName(client.getFirstName());
			saved.getReservations().clear();
			saved.getReservations().addAll(client.getReservations());
		} else saved = client;
		return Collections.save(saved);
	}
	
	@Nullable
	public static Client find(@NotNull String email) {
		return query().field("email").equalIgnoreCase(email).get();
	}
	
	public static List<Client> find(@Nullable String firstName, @Nullable String lastName) {
		Query<Client>     query     = query();
		CriteriaContainer criterias = query.and();
		if (firstName != null) criterias.add(query.criteria("first_name").equal(firstName));
		if (lastName != null) criterias.add(query.criteria("last_name").equal(lastName));
		return query.asList();
	}
	
	public static List<Client> findIgnoreCase(@Nullable String firstName, @Nullable String lastName) {
		Query<Client>     query     = query();
		CriteriaContainer criterias = query.and();
		if (firstName != null) criterias.add(query.criteria("first_name").equalIgnoreCase(firstName));
		if (lastName != null) criterias.add(query.criteria("last_name").equalIgnoreCase(lastName));
		return query.asList();
	}
}
