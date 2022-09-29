package controller;

import java.sql.Connection;
import java.util.List;

import dao.ReservaDAO;
import factory.ConnectionFactory;
import modelo.Reserva;

public class ReservaController {

	private ReservaDAO reservaDAO;

	public ReservaController() {
		try {
			Connection connection = new ConnectionFactory().stringConexao();
			this.reservaDAO = new ReservaDAO(connection);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int inserirReserva(Reserva reserva) {
		return this.reservaDAO.inserirReserva(reserva);
	}

	public double valor(String dataEntrada, String dataSaida) {
		return this.reservaDAO.valor(dataEntrada, dataSaida);
	}

	public List<Reserva> listarReservas() {
		return this.reservaDAO.listaReservas();
	}

	public void editar(Reserva reserva) {
		this.reservaDAO.editar(reserva);
	}

	public void deletar(int id) {
		this.reservaDAO.deletar(id);
	}
}
