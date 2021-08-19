package br.com.builders.controller.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@ApiModel(description = "ClienteDTO")
public class ClienteDTO implements Serializable {

    private static final long serialVersionUID = 5627353735438801507L;

    @ApiModelProperty(notes = "id do cliente", name = "id")
    private Long id;

    @ApiModelProperty(notes = "nome do cliente", name = "nome", required = true)
    private String nome;

    @ApiModelProperty(notes = "data nascimento do cliente", name = "data nascimento", required = true)
    private LocalDate dataNascimento;

    @ApiModelProperty(notes = "sexo do cliente", name = "sexo")
    private String sexo;

    @ApiModelProperty(notes = "Idade do cliente", name = "idade")
    private long idade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public long getIdade() {
        this.idade = ChronoUnit.YEARS.between(this.getDataNascimento(), LocalDate.now());
        return idade;
    }

}
