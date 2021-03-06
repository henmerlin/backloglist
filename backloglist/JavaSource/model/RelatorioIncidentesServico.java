package model;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.model.UploadedFile;

import com.opencsv.CSVReader;

import entidades.Defeito;
import entidades.DefeitoTv;
import entidades.TipoStatus;
import entidades.UsuarioEfika;
import util.JSFUtil;

@Stateless
public class RelatorioIncidentesServico {

	@PersistenceContext(unitName="vu")  
	private EntityManager entityManager;
	
	public RelatorioIncidentesServico() {

	}

	@SuppressWarnings("unchecked")
	public List<Defeito> listaDefeitosStatusAdm(Boolean dqttEncerrado) {

		try {

			Query query = this.entityManager.createQuery("FROM Defeito d WHERE d.encerradoAdm =:param1 AND d.status =:param2");
			query.setParameter("param1", dqttEncerrado);
			query.setParameter("param2", TipoStatus.ENCERRADO);
			return query.getResultList();

		} catch (Exception e) {

			return new ArrayList<Defeito>();

		}

	}

	@SuppressWarnings("unchecked")
	public List<DefeitoTv> listaDefeitosStatusAdmTv(Boolean dqttEncerrado) {

		try {

			Query query = this.entityManager.createQuery("FROM DefeitoTv d WHERE d.encerradoAdm =:param1 AND d.status =:param2");
			query.setParameter("param1", dqttEncerrado);
			query.setParameter("param2", TipoStatus.ENCERRADO);
			return query.getResultList();

		} catch (Exception e) {

			return new ArrayList<DefeitoTv>();

		}

	}
	
	public void mudaStatusIncidente(Defeito defeito) {

		defeito.setEncerradoAdm(true);

		this.entityManager.merge(defeito);

	}

	public void importaDefeitosEncerradosDQTT(UploadedFile file) throws Exception {

		try {

			byte[] conteudo = file.getContents();

			String nome = JSFUtil.gerarStringAleatoria(20);

			String fullname = "C:\\UploadedFiles\\" + nome + ".csv";

			FileOutputStream fos = new FileOutputStream(fullname);

			fos.write(conteudo);
			fos.close();

			this.importCSV(nome);

		} catch (ParseException e) {

			JSFUtil.addErrorMessage(e.getMessage());

		}

	}

	@SuppressWarnings("rawtypes")
	public void importCSV(String nomeArquivo) throws Exception {

		String[] row = null;
		String csvFilename = "C:\\UploadedFiles\\" + nomeArquivo + ".csv";

		CSVReader csvReader = new CSVReader(new FileReader(csvFilename), ';');
		List content = csvReader.readAll();


		for (Object object : content) {

			Defeito defeito = new Defeito();

			row = (String[]) object;

			String ss = row[0];
			
			String acao = row[1];

			try {

				if (!ss.isEmpty() && !acao.isEmpty()){

					defeito = this.buscaDefeitoEspecifico(ss);
					
					Date date = new Date();

					if (acao.trim().equalsIgnoreCase("true")){
						
						defeito.setStatus(TipoStatus.ENCERRADO);

						defeito.setEncerradoDQTT(true);
												
						defeito.setDataDQTT(date);

						defeito.setEncerradoAdm(true);

					}else if(acao.trim().equalsIgnoreCase("false")){
						
						defeito.setStatus(TipoStatus.ENCERRADO);

						defeito.setEncerradoDQTT(false);
						
						defeito.setDataDQTT(date);

						defeito.setEncerradoAdm(true);

					}else if (acao.trim().equalsIgnoreCase("sistema") && (defeito.getStatus().equals(TipoStatus.ABERTO) || defeito.getStatus().equals(TipoStatus.ENVIADOACAMPO))){
						
						UsuarioEfika userSis = this.buscaUsuarioSis();
						defeito.setUsuario(userSis);
						
						defeito.setStatus(TipoStatus.ENCERRADO);
						
						defeito.setDataEncerrado(date);
						
						defeito.setDataDQTT(date);

						defeito.setEncerradoAdm(true);
						
					}else if (acao.trim().equalsIgnoreCase("removido") && defeito.getStatus().equals(TipoStatus.ABERTO)){
						
						UsuarioEfika userSis = this.buscaUsuarioSis();
						defeito.setStatus(TipoStatus.REMOVIDO);						
						defeito.setUsuario(userSis);
						
						defeito.setDataEncerrado(date);
						
						defeito.setDataDQTT(date);

						defeito.setEncerradoAdm(true);
						
					}
					
					this.entityManager.merge(defeito);

				}else{					
					
					
				}
				
			} catch (Exception e) {

			}

		}

		csvReader.close();
	}
	
	public void importaDefeitosTvEncerradosDQTT(UploadedFile file) throws Exception {

		try {

			byte[] conteudo = file.getContents();

			String nome = JSFUtil.gerarStringAleatoria(20);

			String fullname = "C:\\UploadedFiles\\" + nome + ".csv";

			FileOutputStream fos = new FileOutputStream(fullname);

			fos.write(conteudo);
			fos.close();

			this.importCSVTv(nome);

		} catch (ParseException e) {

			JSFUtil.addErrorMessage(e.getMessage());

		}

	}
	
	@SuppressWarnings("rawtypes")
	public void importCSVTv(String nomeArquivo) throws Exception {

		String[] row = null;
		String csvFilename = "C:\\UploadedFiles\\" + nomeArquivo + ".csv";

		CSVReader csvReader = new CSVReader(new FileReader(csvFilename), ';');
		List content = csvReader.readAll();


		for (Object object : content) {

			DefeitoTv defeito = new DefeitoTv();

			row = (String[]) object;

			String ss = row[0];
			
			String acao = row[1];

			try {

				if (!ss.isEmpty() && !acao.isEmpty()){					

					defeito = this.buscaDefeitoTvEspecifico(ss);
					
					Date date = new Date();

					if (acao.trim().equalsIgnoreCase("true")){
						
						defeito.setStatus(TipoStatus.ENCERRADO);

						defeito.setEncerradoDQTT(true);						
						
						defeito.setDataDQTT(date);			

						defeito.setEncerradoAdm(true);

					}else if(acao.trim().equalsIgnoreCase("false")){
						
						defeito.setStatus(TipoStatus.ENCERRADO);

						defeito.setEncerradoDQTT(false);
						
						defeito.setDataDQTT(date);			

						defeito.setEncerradoAdm(true);

					}else if (acao.trim().equalsIgnoreCase("sistema") && (defeito.getStatus().equals(TipoStatus.ABERTO) || defeito.getStatus().equals(TipoStatus.ENVIADOACAMPO))){
						
						UsuarioEfika userSis = this.buscaUsuarioSis();												
						defeito.setUsuario(userSis);
						
						defeito.setDataEncerrado(date);
						
						defeito.setDataDQTT(date);

						defeito.setEncerradoAdm(true);
						
					}else if (acao.trim().equalsIgnoreCase("removido") && defeito.getStatus().equals(TipoStatus.ABERTO)){
						
						UsuarioEfika userSis = this.buscaUsuarioSis();
						defeito.setStatus(TipoStatus.REMOVIDO);						
						defeito.setUsuario(userSis);
						
						defeito.setDataEncerrado(date);
						
						defeito.setDataDQTT(date);

						defeito.setEncerradoAdm(true);
						
					}
					
					this.entityManager.merge(defeito);

				}else{					
					
					
				}
				
			} catch (Exception e) {

			}

		}

		csvReader.close();
	}

	public Defeito buscaDefeitoEspecifico(String ss) throws Exception {

		try {

			Query query = this.entityManager.createQuery("FROM Defeito d WHERE d.ss =:param1");
			query.setParameter("param1", ss);
			return (Defeito) query.getSingleResult();

		} catch (Exception e) {

			throw new Exception("Defeito n�o encontrado");

		}

	}
	
	public DefeitoTv buscaDefeitoTvEspecifico(String ss) throws Exception {

		try {

			Query query = this.entityManager.createQuery("FROM DefeitoTv d WHERE d.ss =:param1");
			query.setParameter("param1", ss);
			return (DefeitoTv) query.getSingleResult();

		} catch (Exception e) {

			throw new Exception("Defeito n�o encontrado");

		}

	}
	
	public UsuarioEfika buscaUsuarioSis() {
		
		Query query = this.entityManager.createQuery("FROM UsuarioEfika u WHERE u.login=:param1");
		query.setParameter("param1", "backlog_sistema");
		return (UsuarioEfika) query.getSingleResult();
		
	}

}
