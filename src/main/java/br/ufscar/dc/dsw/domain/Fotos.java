package main.java.br.ufscar.dc.dsw.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

@SuppressWarnings("serial")
@Entity
@Table(name = "Fotos")
public class Fotos extends AbstractEntity<Long>{
    
    @NotNull(message = "{NotNull.fotos.fileName}")
    @Column(nullable = false)
    private String fileName;
    
    @NotNull(message = "{NotNull.fotos.data}")
    @Column(nullable = false)
    private byte[] data;

    @NotNull(message = "{NotNull.fotos.type}")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "{NotNull.fotos.pacote}")
    @ManyToOne
    @JoinColumn(name = "pacote_id", referecedToCollumnName = "id", foreignKey = @foreignKey(name = "id"))
    private Pacote pacote;

    public Fotos(){

    }

    public Fotos(String fileName, byte[] data, String type, Pacote pacote){
        this.fileName = fileName;
        this.data = data;
        this.type = type;
        this.pacote = pacote;
    }

    public String getFileName(){
        return this.fileName;
    }

    public byte[] getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public Pacote getPacote() {
        return pacote;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPacote(Pacote pacote) {
        this.pacote = pacote;
    }
}
