package br.com.builders.controller.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "Resposta de erro")
public class RespostaErro implements Serializable { //http.csrf()
               /* http.disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new FiltroCustomizadoCors(), UsernamePasswordAuthenticationFilter.class);*/
      // super.configure(http);

       /* http.csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new FiltroCustomizadoCors(), UsernamePasswordAuthenticationFilter.class);*/
                //.addFilterBefore(new AuthenticationFilter(authenticationValidator), UsernamePasswordAuthenticationFilter.class);
        //super.configure(http);

    private static final long serialVersionUID = 1128857214535341816L;

    @ApiModelProperty(notes = "Codio de erro", name = "codigo", value = "200")
    private int codigo;

    @ApiModelProperty(notes = "Status", name = "status", value = "SUCCESS")
    private String status;

    @ApiModelProperty(notes = "Mensagem", name = "mensagem", value = "Campo incorreto")
    private String mensagem;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
