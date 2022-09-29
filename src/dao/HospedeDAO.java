package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Hospede;

public class HospedeDAO {
	private Connection connection;

	public HospedeDAO() {

	}

	public HospedeDAO(Connection connection) {
		this.connection = connection;
	}

	public void inserirHospede(Hospede hospede) {

		String querySql = "INSERT INTO hospedes(Nome,Sobrenome,DataNascimento,Nacionalidade,Telefone,idReserva) VALUES(?,?,?,?,?,?);";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setString(1, hospede.getNome());
			prst.setString(2, hospede.getSobrenome());
			prst.setString(3, hospede.getDataNascimento());
			prst.setString(4, hospede.getNacionalidade());
			prst.setString(5, hospede.getTelefone());
			prst.setInt(6, hospede.getIdReserva());
			prst.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Hospede> listarHospede() {

		List<Hospede> listaHospede = new ArrayList<Hospede>();
		String querySql = "SELECT * FROM hospedes;";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.execute();

			try (ResultSet rst = prst.getResultSet()) {
				while (rst.next()) {
					int id = rst.getInt(1);
					String nome = rst.getString(2);
					String sobrenome = rst.getString(3);
					String dataNascimento = rst.getString(4);
					String nacionalidade = rst.getString(5);
					String telefone = rst.getString(6);
					int idReserva = rst.getInt(7);

					Hospede hospede = new Hospede(id, nome, sobrenome, dataNascimento, nacionalidade, telefone,
							idReserva);

					listaHospede.add(hospede);
				}
			}

			return listaHospede;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Hospede> filtrar(String filtro) {

		List<Hospede> hospedes = new ArrayList<Hospede>();
		String querySql = "";
		if (filtro.matches("[a-z/A-Z]*")) {
			filtro = "%" + filtro + "%";
			querySql = "SELECT * FROM hospedes WHERE sobrenome LIKE ?;";
		} else {
			querySql = "SELECT * FROM hospedes WHERE IdReserva = ?";
		}

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setString(1, filtro);
			prst.execute();

			try (ResultSet rst = prst.getResultSet()) {
				while (rst.next()) {
					int id = rst.getInt(1);
					String nome = rst.getString(2);
					String sobrenome = rst.getString(3);
					String dataNascimento = rst.getString(4);
					String nacionalidade = rst.getString(5);
					String telefone = rst.getString(6);
					int idReserva = rst.getInt(7);

					Hospede hospede = new Hospede(id, nome, sobrenome, dataNascimento, nacionalidade, telefone,
							idReserva);

					hospedes.add(hospede);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return hospedes;

	}

	public void editar(Hospede hospede) {

		String querySql = "UPDATE hospedes H SET h.nome = ?, h.sobrenome = ?, h.dataNascimento = ?, h.nacionalidade = ?, h.telefone = ?, h.idReserva = ? WHERE id = ?";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setString(1, hospede.getNome());
			prst.setString(2, hospede.getSobrenome());
			prst.setString(3, hospede.getDataNascimento());
			prst.setString(4, hospede.getNacionalidade());
			prst.setString(5, hospede.getTelefone());
			prst.setInt(6, hospede.getIdReserva());
			prst.setInt(7, hospede.getId());
			prst.execute();

			JOptionPane.showMessageDialog(null, "Sucesso");

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void deletar(int id) {

		String querySql = "DELETE FROM hospedes WHERE id = ?";

		try (PreparedStatement prst = connection.prepareStatement(querySql)) {
			prst.setInt(1, id);
			prst.execute();
			JOptionPane.showMessageDialog(null, "Hospede Deletado com Sucesso!!");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
