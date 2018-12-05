package progres.tp4.api.dominospizzaapi.bo;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.apache.commons.lang3.StringUtils.*;
import static progres.tp4.api.dominospizzaapi.util.Constants.ENTITY_KEY_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_BLANK_KEY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_INVALID_KEY;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "pizza_state_pst")
public class PizzaStateBo extends BaseBo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_pst", nullable = false)
	private Long id;
	
	@NotBlank(message = MSG_BLANK_KEY)
	@Pattern(regexp = ENTITY_KEY_PATTERN, message = MSG_INVALID_KEY)
	@Column(name = "key_pst", nullable = false, unique = true, updatable = false)
	private String key;
	
	@NotBlank
	@Column(name = "title", nullable = false)
	private String title;
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(@NotNull String key) {
		this.key = key;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(@NotNull String title) {
		this.title = title;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setKey(trim(normalizeSpaces(getKey())));
		setTitle(trim(normalizeSpaces(getTitle())));
		
		if (isBlank(getKey()))
			throw new RequestValidationException(msgRequiredAttr("key"));
		if (isBlank(getTitle()))
			throw new RequestValidationException(msgRequiredAttr("title"));
	}
}
