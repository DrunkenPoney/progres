package progres.tp4.api.dominospizzaapi.bo;

import javax.persistence.*;

@Entity
@Table(name = "pizza_size_psi")
public class PizzaSizeBo extends BaseBo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_psi", nullable = false)
	private Long id;
	
	@Column(name = "inches", nullable = false, unique = true)
	private int inches;
	
	@Column(name = "price_mod", nullable = false, precision = 65)
	private double priceMod;
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getInches() {
		return inches;
	}
	
	public void setInches(int inches) {
		this.inches = inches;
	}
	
	public double getPriceMod() {
		return priceMod;
	}
	
	public void setPriceMod(double priceMod) {
		this.priceMod = priceMod;
	}
	
	@Override
	public void validate() {}
}
