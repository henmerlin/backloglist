package model;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entidades.LogDefeito;
import entidades.LogDefeitoTv;
import entidades.UsuarioEfika;

@Stateless
public class LogServico {
	
	@PersistenceContext(unitName="vu")  
	private EntityManager entityManager;

	public LogServico() {
				
	}
	
	@SuppressWarnings("unchecked")
	public List<LogDefeito> listarLogDefeitoSS(String loginOUss, UsuarioEfika usuario) throws Exception {
		
		try {
			
			Query query = this.entityManager.createQuery("FROM LogDefeito l WHERE l.defeito.ss =:param1 OR l.usuario.login =:param1 ORDER BY l.horaAcao DESC");
			query.setParameter("param1", loginOUss);
			query.setMaxResults(70);
			return query.getResultList();			
			
		} catch (Exception e) {			
			
			throw new Exception("Colaborador/Defeito n�o encontrado ou o mesmo n�o est� abaixo de sua supervis�o!");
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<LogDefeitoTv> listarLogDefeitoSSTv(String loginOUss, UsuarioEfika usuario) throws Exception {
		
		try {
			
			Query query = this.entityManager.createQuery("FROM LogDefeitoTv l WHERE l.defeito.ss =:param1 OR l.usuario.login =:param1 ORDER BY l.horaAcao DESC");
			query.setParameter("param1", loginOUss);
			query.setMaxResults(50);
			return query.getResultList();			
			
		} catch (Exception e) {			
			
			throw new Exception("Colaborador/Defeito n�o encontrado ou o mesmo n�o est� abaixo de sua supervis�o!");
			
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<LogDefeito> listarLog(String LoginOuSs) {
		
		
		Query query = this.entityManager.createQuery("FROM LogDefeito l WHERE l.defeito.ss =:param1 OR l.usuario.login =:param1");
		query.setParameter("param1", LoginOuSs);
		return query.getResultList();
		
	}
	

}
