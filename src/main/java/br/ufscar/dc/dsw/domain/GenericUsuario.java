package br.ufscar.dc.dsw.domain;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;



@SuppressWarnings("serial")

@Entity
@Table(name = "GenericUsuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class GenericUsuario extends AbstractEntity<Long>{
	
	@NotBlank
    @Column(nullable = false, length = 20, unique = true)
    private String username;
    
	@NotBlank
    @Column(nullable = false, length = 64)
    private String password;
       
    @NotBlank
    @Column(nullable = false, length = 60)
    private String name;
    
    @NotBlank
    @Column(nullable = false, length = 15)
    private String role;
    
    @Column(nullable = true)
    private boolean enabled;
		
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
