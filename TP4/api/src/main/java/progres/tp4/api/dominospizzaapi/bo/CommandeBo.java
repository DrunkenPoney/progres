package progres.tp4.api.dominospizzaapi.bo;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.embeddable.Pizza;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;

import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;

@SuppressWarnings("unused")
@Entity
@Table(name = "commande_cmd")
public class CommandeBo implements IBaseBo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_cmd", nullable = false)
	private Long id;
	
	@Embedded
	private Pizza pizza;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cli")
	private ClientBo client;
	
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Pizza getPizza() {
		return pizza;
	}
	
	public void setPizza(@NotNull Pizza pizza) {
		this.pizza = pizza;
	}
	
	public ClientBo getClient() {
		return client;
	}
	
	public void setClient(@NotNull ClientBo client) {
		this.client = client;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		if (getPizza() == null)
			throw new RequestValidationException(msgRequiredAttr("pizza"));
		getPizza().validate();
		
		if (getClient() == null)
			throw  new RequestValidationException(msgRequiredAttr("client"));
		if (getClient().getId() == null)
			throw new RequestValidationException(msgRequiredAttr("id", "client"));
	}
}
