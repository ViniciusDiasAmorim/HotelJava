package controller;

import java.sql.Connection;
import java.util.List;

import dao.HospedeDAO;
import factory.ConnectionFactory;
import modelo.Hospede;

public class HospedeController {

	private HospedeDAO hospedeDAO;

	public HospedeController() {
		try {

			Connection connection = new ConnectionFactory().stringConexao();
			this.hospedeDAO = new HospedeDAO(connection);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void inserirHospede(Hospede hospede) {
		this.hospedeDAO.inserirHospede(hospede);
	}

	public List<Hospede> listarHospede() {
		return this.hospedeDAO.listarHospede();
	}

	public List<Hospede> filtrar(String filtro) {
		return this.hospedeDAO.filtrar(filtro);
	}

	public void editar(Hospede hospede) {
		this.hospedeDAO.editar(hospede);
	}

	public void deletar(int id) {
		this.hospedeDAO.deletar(id);
	}
}
