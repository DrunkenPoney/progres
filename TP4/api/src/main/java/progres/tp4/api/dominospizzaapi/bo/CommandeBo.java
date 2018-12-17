package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.embeddable.Pizza;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.Min;

import java.sql.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;

@SuppressWarnings("unused")
@Entity
@Table(name = "commande_cmd")
@JsonRootName("commande")
public class CommandeBo implements IBaseBo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_cmd", nullable = false)
	private Long id;
	
	@Embedded
	private Pizza pizza;
	
	@Min(0)
	@Column(name = "total")
	private double total;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cli")
	private ClientBo client;
	
	@JsonProperty(access = READ_ONLY)
	@Column(name = "date_created", nullable = false)
	private Date dateCreated;
	
	@JsonProperty(access = READ_ONLY)
	@Column(name = "date_updated")
	private Date dateUpdated;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
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
	
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(@NotNull Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Date getDateUpdated() {
		return dateUpdated;
	}
	
	public void setDateUpdated(@NotNull Date dateUpdated) {
		this.dateUpdated = dateUpdated;
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
