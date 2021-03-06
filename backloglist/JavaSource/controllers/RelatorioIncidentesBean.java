package controllers;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import entidades.Defeito;
import entidades.DefeitoTv;
import model.RelatorioIncidentesServico;
import util.JSFUtil;

@ManagedBean
@ViewScoped
public class RelatorioIncidentesBean {

	@EJB
	private RelatorioIncidentesServico relatorioIncidentesServico;

	private List<Defeito> defeitosNaoEncerrado;

	private List<DefeitoTv> defeitosNaoEncerradoTv;

	private List<Defeito> defeitosEncerrados;

	public RelatorioIncidentesBean() {

	}	

	public List<Defeito> listarDefeitosNaoEncerradosAdm() {

		defeitosNaoEncerrado = this.relatorioIncidentesServico.listaDefeitosStatusAdm(false);

		return defeitosNaoEncerrado;

	}

	public List<DefeitoTv> listarDefeitosNaoEncerradosAdmTv() {

		defeitosNaoEncerradoTv = this.relatorioIncidentesServico.listaDefeitosStatusAdmTv(false);

		return defeitosNaoEncerradoTv;

	}

	public List<Defeito> listarDefeitoEncerradosAdm() {

		defeitosEncerrados = this.relatorioIncidentesServico.listaDefeitosStatusAdm(true);

		return defeitosEncerrados;

	}

	public void atualizaDefeitoEncerrado(FileUploadEvent event) {

		UploadedFile file = event.getFile();

		try {

			this.relatorioIncidentesServico.importaDefeitosEncerradosDQTT(file);

			JSFUtil.addInfoMessage("Lote carregado com sucesso");

		} catch (Exception e) {

			JSFUtil.addErrorMessage(e.getMessage());

		}

	}

	public void atualizaDefeitoTvEncerrado(FileUploadEvent event) {

		UploadedFile file = event.getFile();

		try {

			this.relatorioIncidentesServico.importaDefeitosTvEncerradosDQTT(file);

			JSFUtil.addInfoMessage("Lote carregado com sucesso");

		} catch (Exception e) {

			JSFUtil.addErrorMessage(e.getMessage());

		}

	}

	public String calculaHoraEncerrado(Date date) {

		Date dataAtual = new Date();

		Long tempo = dataAtual.getTime() - date.getTime();

		tempo = tempo / 60000; // Converte o tempo para minutos

		int minutos = (int) (tempo % 60); // Retira os minutos da hora

		tempo = tempo / 60; // Deixa em tempo apenas as horas

		return String.format("%02d:%02d", tempo, minutos);

	}

}
