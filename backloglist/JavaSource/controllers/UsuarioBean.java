package controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

import entidades.Supervisor;
import entidades.Usuario;
import model.UsuarioServico;
import util.JSFUtil;

@ManagedBean
@RequestScoped
public class UsuarioBean {
	
	private Usuario usuario;
	
	private Supervisor supervisor;
		
	@EJB
	private UsuarioServico usuarioServico;

	public UsuarioBean() {
		
		this.usuario = new Usuario();
		this.supervisor = new Supervisor();
	}
	
	public void cadastrarOperador() {
		
		try {
			this.usuario.setSenha("12345gvt");
			this.usuarioServico.cadastrarOperador(this.usuario, this.supervisor);
			JSFUtil.addInfoMessage("Usu�rio criado com sucesso, senha padr�o: 12345gvt");
			
			this.usuario = new Usuario();
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e.getMessage());
		}
		
	}
	
	public List<Usuario> listarUsuarios() {
		
		return this.usuarioServico.listarUsuarios();
		
	}
	
	public List<Supervisor> listarSupervisor() {
		
		return this.usuarioServico.listarSupervisor();
		
	}	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}
	
	

}
