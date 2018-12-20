package tp3.bd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.query.Query;
import tp3.bd.entities.User;
import tp3.bd.entities.User.EncryptedPassword;

import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@SuppressWarnings("unused")
public final class Users {
	private static final String ADMIN_USERNAME = "admin", ADMIN_PASSWORD = "admin";
	
	private static User admin;
	
	public static User getAdministrator() {
		if (admin == null) {
			admin = Collections.query(User.class)
					.field("username").equalIgnoreCase("admin").get();
			if (admin == null) admin = new User(ADMIN_USERNAME, ADMIN_PASSWORD);
			admin.getGroups().addAll(stream(Groups.values())
					                         .map(Groups::get)
					                         .collect(Collectors.toList()));
			admin.getPermissions().addAll(stream(Permissions.values())
					                              .map(Permissions::get)
					                              .collect(Collectors.toList()));
			Collections.save(admin);
		}
		return admin;
	}
	
	/**
	 * Permet de vérifier si un utilisateur existe.
	 *
	 * @param user L'utilisaeur
	 * @return L'ID de l'utilisateur ou null si inexistant
	 */
	@Nullable
	public static Long keyOf(User user) {
		return Collections.keyOf(user);
	}
	
	/**
	 * Crée une nouvelle requête permettant de récupérer un ou des utilisateur(s).
	 *
	 * @return Une nouvele requête
	 */
	@NotNull
	public static Query<User> query() {
		return Collections.query(User.class);
	}
	
	/**
	 * Récupère l'utilisateur avec le nom d'utilisateur spécifié.
	 *
	 * @param username Le nom d'utilisateur
	 * @return L'utilisateur ou null si inexistant
	 */
	@Nullable
	public static User find(String username) {
		return query().field("username").equalIgnoreCase(username).get();
	}
	
	/**
	 * Authentifier l'utilisateur et retourne l'utilisateur authentifié ou null.
	 * Retourne null si le nom d'utilisateur et/ou le mot de passe sont invalides.
	 *
	 * @param username Nom d'utiisateur
	 * @param password Mot de passe
	 * @return L'utilisateur authentifié ou null
	 */
	@Nullable
	public static User login(String username, String password) {
		return login(username, new EncryptedPassword(password));
	}
	
	/**
	 * Authentifier l'utilisateur et retourne l'utilisateur authentifié ou null.
	 * Retourne null si le nom d'utilisateur et/ou le mot de passe sont invalides.
	 *
	 * @param username Nom d'utiisateur
	 * @param password Mot de passe
	 * @return L'utilisateur authentifié ou null
	 */
	@Nullable
	public static User login(String username, EncryptedPassword password) {
		final User user = find(username);
		return user != null && user.getPassword().is(password) ? user : null;
	}
	
	/**
	 * Crée ou modifie l'utilisateur spécifié.
	 * Retourne null si un utilisateur avec le même nom d'utilisateur existe,
	 * mais que le mot de passe n'est pas le même.
	 * <p>
	 * NOTE: Ne modifie pas le mot de passe
	 *
	 * @param user L'utilisateur à créer/modifier
	 * @return L'utilisateur créé/modifié ou null
	 */
	@Nullable
	public static User save(final User user) {
		User saved = find(user.getUsername());
		if (saved != null) {
			saved = login(user.getUsername(), user.getPassword());
			if (saved != null) {
				saved.getGroups().clear();
				saved.getPermissions().clear();
				saved.getGroups().addAll(user.getGroups());
				saved.getPermissions().addAll(user.getPermissions());
			}
		} else saved = user;
		return saved == null ? null : Collections.save(saved);
	}
	
	/**
	 * Change le mot de passe d'un utilisateur.
	 * <p>
	 * Retourne <code>false</code> si le nom d'utilisateur et/ou
	 * le mot de passe sont invalides.
	 *
	 * @param username    Le nom de l'utilisateur
	 * @param oldPassword L'ancien mot de passe
	 * @param newPassword Le nouveau mot de passe
	 * @return Vrai si le mot de passe a été changé.
	 */
	public static boolean changePassword(@NotNull String username, @NotNull String oldPassword,
	                                     @NotNull String newPassword) {
		User user = login(username, oldPassword);
		if (user != null) {
			user.setPassword(newPassword);
			user = Collections.save(user);
		}
		return user != null;
	}
}
