package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Reserva;

public class ReservaDAO {

	private Connection connection;

	public ReservaDAO() {

	}

	public ReservaDAO(Connection connection) {
		this.connection = connection;
	}

	public int inserirReserva(Reserva reserva) {

		String querySql = "INSERT INTO reservas(DataEntrada,DataSaida,Valor,FormaPagamento) VALUES(?,?,?,?);";
		try (PreparedStatement prst = connection.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {
			prst.setString(1, reserva.getDataEntrada());
			prst.setString(2, reserva.getDataEntrada());
			prst.setBigDecimal(3, reserva.getValor());
			prst.setString(4, reserva.getFormaPagamento());
			prst.execute();

			try (ResultSet rst = prst.getGeneratedKeys()) {
				while (rst.next()) {
					return rst.getInt(1);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return 0;
	}

	public double valor(String dataEntrada, String dataSaida) {

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		LocalDate dataEntradaFormatada = LocalDate.parse(dataEntrada, formato);
		LocalDate dataSaidaFormatada = LocalDate.parse(dataSaida, formato);

		int dias = 0;

		while (!dataEntradaFormatada.equals(dataSaidaFormatada)) {
			dataEntradaFormatada = dataEntradaFormatada.plusDays(1);
			dias++;
		}

		double valor = dias * 20;

		return valor;
	}

	public List<Reserva> listaReservas() {

		List<Reserva> listaReserva = new ArrayList<Reserva>();
		String querySql = "SELECT * FROM RESERVAS;";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.execute();

			try (ResultSet rst = prst.getResultSet()) {
				while (rst.next()) {
					int id = rst.getInt(1);
					String dataEntrada = rst.getString(2);
					String dataSaida = rst.getString(3);
					BigDecimal valor = rst.getBigDecimal(4);
					String formaPagamento = rst.getString(5);

					Reserva reserva = new Reserva(id, dataEntrada, dataSaida, valor, formaPagamento);

					listaReserva.add(reserva);
				}
				return listaReserva;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void editar(Reserva reserva) {

		String querySql = "UPDATE reservas R SET R.DataEntrada = ?, R.DataSaida = ?, R.Valor = ?, R.FormaPagamento = ? WHERE id = ?";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setString(1, reserva.getDataEntrada());
			prst.setString(2, reserva.getDataSaida());
			prst.setDouble(3, this.valor(reserva.getDataEntrada(), reserva.getDataSaida()));
			prst.setString(4, reserva.getFormaPagamento());
			prst.setInt(5, reserva.getId());
			prst.execute();

			JOptionPane.showMessageDialog(null, "Sucesso");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void deletar(int idReserva) {
		String querySql = "DELETE FROM reservas WHERE id = ?";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setInt(1, idReserva);
			prst.execute();
			JOptionPane.showMessageDialog(null, "Reserva Deletada com Sucesso !!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ops ocorreu um problema, pode ser que tenha um hospede nessa reserva !!");
			throw new RuntimeException(e);
		}
	}
}
